
package lfernando.tapaburaco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarBuracosActivity extends AppCompatActivity {

    List<Buraco> buracoList;
    BuracoAdapter adapter;
    ListView listaView;
    private DatabaseReference mDatabase;


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
        setContentView(R.layout.activity_listar_buracos);

        buracoList = new ArrayList<>();
        listaView = findViewById(R.id.buracoListView);
        adapter = new BuracoAdapter(buracoList, this);
        listaView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getData();
    }

    public void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(ListarBuracosActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    public void getData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LIST", dataSnapshot.child("buracos").getValue().toString());
                HashMap<Object, Object> buracos = (HashMap<Object, Object>) dataSnapshot.child("buracos").getValue();
                Log.d("LIST", ""+buracos.size());
                for (Map.Entry<Object, Object> buraco:buracos.entrySet()) {
                    String key = buraco.getKey().toString();
                    HashMap<String, String> valores = (HashMap<String, String>) buraco.getValue();



                    //Log.d("LIST", key);
                    //Log.d("LIST", valores.get("lon"));
                    Buraco b = new Buraco(key, valores.get("descricao"), valores.get("imagem"), Integer.parseInt(valores.get("impacto")),
                            Float.parseFloat(valores.get("lat")), Float.parseFloat(valores.get("lon")));

                    buracoList.add(b);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
