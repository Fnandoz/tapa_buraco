
package lfernando.tapaburaco;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import lfernando.tapaburaco.Fragments.ListarBuracosFragment;
import lfernando.tapaburaco.Fragments.MapaFragment;

public class ListarBuracosActivity extends AppCompatActivity {




    ListarBuracosFragment listaFragment;
    MapaFragment mapaFragment;
    android.support.v4.app.FragmentTransaction transaction;
    FloatingActionButton fab;


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


        listaFragment = new ListarBuracosFragment();
        mapaFragment = new MapaFragment();
        fab = findViewById(R.id.floatingActionButton);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentoLayout, mapaFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Adicionar um novo", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(ListarBuracosActivity.this, NovoBuracoActivity.class);
                startActivity(intent);
            }
        });
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
}
