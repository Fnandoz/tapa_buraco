package lfernando.tapaburaco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NovoBuracoActivity extends AppCompatActivity {

    ImageView fotoImageView;
    ImageButton novaFotoButton;
    EditText tituloTextView;
    SeekBar indiceSeekBar;
    MapView mapView;
    Button enviarButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_LIBRARY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_buraco);

        fotoImageView = findViewById(R.id.fotoImageView);
        novaFotoButton = findViewById(R.id.novaImagemButton);
        tituloTextView = findViewById(R.id.tituloEditText);
        indiceSeekBar = findViewById(R.id.impactoSeekBar);
        enviarButton = findViewById(R.id.salvarButton);
        mapView = findViewById(R.id.mapView);

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
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Novo buraco"));
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.remove();
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
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                fotoImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
