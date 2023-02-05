package com.example.comov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMap(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        EditText name = (EditText) findViewById(R.id.nombre_mapa);
        intent.putExtra("nombreMapa", name.getText().toString());
        startActivity(intent);
    }

}