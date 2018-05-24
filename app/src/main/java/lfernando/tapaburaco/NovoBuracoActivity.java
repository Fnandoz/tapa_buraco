package lfernando.tapaburaco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class NovoBuracoActivity extends AppCompatActivity {

    ImageView fotoImageView;
    ImageButton novaFotoButton;
    EditText tituloTextView;
    SeekBar indiceSeekBar;
    MapView mapView;
    Button enviarButton;

    Uri imageUri;
    Bitmap selectedImage;
    InputStream imageStream;

    LatLng latLngFinal;

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_LIBRARY = 2;

    int contadorDePontosNoMapa = 0;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    Marker mMarker;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_buraco);

        fotoImageView = findViewById(R.id.fotoImageView);
        fotoImageView.setDrawingCacheEnabled(true);
        fotoImageView.buildDrawingCache(true);
        fotoImageView.setDrawingCacheEnabled(false);


        novaFotoButton = findViewById(R.id.novaImagemButton);
        tituloTextView = findViewById(R.id.tituloEditText);
        indiceSeekBar = findViewById(R.id.impactoSeekBar);
        enviarButton = findViewById(R.id.salvarButton);
        mapView = findViewById(R.id.mapView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        novaFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto();
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                LatLng belem = new LatLng(-1.385851, -48.4614937);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belem, 11));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (contadorDePontosNoMapa==0) {
                            latLngFinal = latLng;
                            mMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Novo buraco"));
                            contadorDePontosNoMapa++;
                        }else{
                            googleMap.clear();
                            contadorDePontosNoMapa--;
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mMarker.remove();
                        contadorDePontosNoMapa--;
                        return false;
                    }
                });
            }
        });

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoCarregamento();
                salva();
            }
        });
    }

    public void getPhoto(){
        final CharSequence[] acoes = {"Abrir câmera", "Abrir galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar foto");
        builder.setItems(acoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (acoes[i].equals("Abrir câmera")){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }else if(acoes[i].equals("Abrir galeria")){
                    Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_LIBRARY);
                    }
                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoImageView.setImageBitmap(imageBitmap);
        }else if(requestCode == REQUEST_IMAGE_LIBRARY && resultCode == RESULT_OK){
            try {

                imageUri = data.getData();
                imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                fotoImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(NovoBuracoActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    public void salva(){
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");


        final String d = UUID.randomUUID().toString();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) fotoImageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        StorageReference foto = mStorageRef.child(d);
        foto.putBytes(imageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                HashMap<String, String> dados = new HashMap<>();
                dados.put("imagem", d);
                dados.put("descricao", tituloTextView.getText().toString());
                dados.put("impacto", String.valueOf(indiceSeekBar.getProgress()));
                dados.put("lat", String.valueOf(latLngFinal.latitude));
                dados.put("lon", String.valueOf(latLngFinal.longitude));
                dados.put("data", mdformat.format(calendar.getTime()));

                mDatabase.child("buracos").push().setValue(dados).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        limpaCampos();
                        dialog.cancel();
                    }
                });
            }
        });
    }


    public void limpaCampos(){
        fotoImageView.setImageResource(android.R.color.darker_gray);
        tituloTextView.setText("");
        indiceSeekBar.setProgress(0);
        mMarker.remove();

    }

    public void dialogoCarregamento(){

        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.loading_view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }
}
