package lfernando.tapaburaco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button novo, listar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        novo = findViewById(R.id.novoRegistroButton);
        listar = findViewById(R.id.listarRegistrosButton);

        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NovoBuracoActivity.class);
                startActivity(intent);
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ListarBuracosActivity.class);
                startActivity(intent);
            }
        });
    }
}
