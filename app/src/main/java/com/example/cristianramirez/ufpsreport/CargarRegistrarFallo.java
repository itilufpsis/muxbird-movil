package com.example.cristianramirez.ufpsreport;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class CargarRegistrarFallo extends AppCompatActivity implements View.OnClickListener{

    ImageButton imgMouse, imgTeclado, imgCPU, imgMonitor;
    TextInputEditText descripcion;
    private SharedPreferences session;
    String dispositivoId, salonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_registrar_fallo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        dispositivoId = i.getExtras().getString("dispositivoId");
        salonId = i.getExtras().getString("salonId");

        imgCPU = (ImageButton) findViewById(R.id.imgCPU);
        imgTeclado = (ImageButton) findViewById(R.id.imgTeclado);
        imgMonitor = (ImageButton) findViewById(R.id.imgMonitor);
        imgMouse = (ImageButton) findViewById(R.id.imgMouse);
        descripcion = (TextInputEditText) findViewById(R.id.txtDescripcion);

        imgCPU.setOnClickListener(this);
        imgMonitor.setOnClickListener(this);
        imgMouse.setOnClickListener(this);
        imgTeclado.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_iniciar_sesion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent a = new Intent(getApplicationContext(), IniciarSesion.class);
            //Traemos la sesion.
            session = getSharedPreferences("Session", 0);
            session.edit().remove("Session").commit();
            startActivity(a);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent i;
        switch (v.getId())
        {
            case R.id.imgCPU:

                i = new Intent(getApplicationContext(),RegistrarFallo.class);
                i.putExtra("tipoDispositivo","CPU");
                i.putExtra("dispositivoId",dispositivoId);
                i.putExtra("salonId",salonId);
                startActivity(i);
                break;

            case R.id.imgMonitor:
                i = new Intent(getApplicationContext(),RegistrarFallo.class);
                i.putExtra("tipoDispositivo","Monitor");
                i.putExtra("dispositivoId",dispositivoId);
                i.putExtra("salonId",salonId);
                startActivity(i);
                break;
            case R.id.imgMouse:
                i = new Intent(getApplicationContext(),RegistrarFallo.class);
                i.putExtra("tipoDispositivo","Mouse");
                i.putExtra("dispositivoId",dispositivoId);
                i.putExtra("salonId",salonId);
                startActivity(i);
                break;
            case R.id.imgTeclado:
                i = new Intent(getApplicationContext(),RegistrarFallo.class);
                i.putExtra("tipoDispositivo","Teclado");
                i.putExtra("dispositivoId",dispositivoId);
                i.putExtra("salonId",salonId);
                startActivity(i);
                break;
        }
    }
}
