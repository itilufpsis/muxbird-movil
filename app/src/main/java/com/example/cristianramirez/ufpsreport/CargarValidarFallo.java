package com.example.cristianramirez.ufpsreport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Util.ConvertidorGson;
import Util.Sala;

public class CargarValidarFallo extends AppCompatActivity implements View.OnClickListener{
TextView tex1,tex2,tex3,tex4,tex5,tex6,tex7,tex8,
text1,text2,text3,text4,text5,text6,text7,text8,
 textr1,textr2,textr3,textr4,textr5,textr6,textr7,textr8;
Button bton1,bton2,bton3,bton4,bton5,bton6,bton7,bton8;
    private SharedPreferences session;
    String idSala,idPro,nombreProfesor;
    int reportes;

    List<Sala> salas;
    String resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_validar_fallo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = getSharedPreferences("Session",0);

        tex1 = (TextView) findViewById(R.id.texto1);
        tex2 = (TextView) findViewById(R.id.texto2);
        tex3 = (TextView) findViewById(R.id.texto3);
        tex4 = (TextView) findViewById(R.id.texto4);
        tex5 = (TextView) findViewById(R.id.texto5);
        tex6 = (TextView) findViewById(R.id.texto6);
        tex7 = (TextView) findViewById(R.id.texto7);
        tex8 = (TextView) findViewById(R.id.texto8);

        text1 = (TextView) findViewById(R.id.textot1);
        text2 = (TextView) findViewById(R.id.textot2);
        text3 = (TextView) findViewById(R.id.textot3);
        text4 = (TextView) findViewById(R.id.textot4);
        text5 = (TextView) findViewById(R.id.textot5);
        text6 = (TextView) findViewById(R.id.textot6);
        text7 = (TextView) findViewById(R.id.textot7);
        text8 = (TextView) findViewById(R.id.textot8);

        textr1 = (TextView) findViewById(R.id.textotr1);
        textr2 = (TextView) findViewById(R.id.textotr2);
        textr3 = (TextView) findViewById(R.id.textotr3);
        textr4 = (TextView) findViewById(R.id.textotr4);
        textr5 = (TextView) findViewById(R.id.textotr5);
        textr6 = (TextView) findViewById(R.id.textotr6);
        textr7 = (TextView) findViewById(R.id.textotr7);
        textr8 = (TextView) findViewById(R.id.textotr8);

        bton1 = (Button) findViewById(R.id.btnir1);
        bton2 = (Button) findViewById(R.id.btnir2);
        bton3 = (Button) findViewById(R.id.btnir3);
        bton4 = (Button) findViewById(R.id.btnir4);
        bton5 = (Button) findViewById(R.id.btnir5);
        bton6 = (Button) findViewById(R.id.btnir6);
        bton7 = (Button) findViewById(R.id.btnir7);
        bton8 = (Button) findViewById(R.id.btnir8);
        bton1.setOnClickListener(this);
        bton2.setOnClickListener(this);
        bton3.setOnClickListener(this);
        bton4.setOnClickListener(this);
        bton5.setOnClickListener(this);
        bton6.setOnClickListener(this);
        bton7.setOnClickListener(this);
        bton8.setOnClickListener(this);
        Intent aux = getIntent();
        idPro=aux.getStringExtra("Id");
        nombreProfesor=aux.getStringExtra("nombre");
        ordenar2();
        reportes=0;
        AccesoRemoto a = new AccesoRemoto();
        a.execute();
        Toast.makeText(getApplicationContext(), "Cargando Datos ", Toast.LENGTH_LONG).show();

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
            Intent a = new Intent(getApplicationContext(),IniciarSesion.class);
            session.edit().remove("Session").commit();
            startActivity(a);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent validar = new Intent(getApplicationContext(),ValidarFallo.class);
        validar.putExtra("Id",idPro);

    if(view.getId()==R.id.btnir1){
    idSala=tex1.getText().toString();

    }
    if(view.getId()==R.id.btnir2){
        idSala=tex2.getText().toString();
    }
    if(view.getId()==R.id.btnir3){
            idSala=tex3.getText().toString();
    }
    if(view.getId()==R.id.btnir4){
            idSala=tex4.getText().toString();
    }
    if(view.getId()==R.id.btnir5){
            idSala=tex5.getText().toString();
    }if(view.getId()==R.id.btnir6){
            idSala=tex6.getText().toString();
    }
    if(view.getId()==R.id.btnir7){
            idSala=tex7.getText().toString();
    }
    if(view.getId()==R.id.btnir8){
            idSala=tex8.getText().toString();
    }
    for(int j=0;j<salas.size();j++){
        if(salas.get(j).getSalonId()==Integer.parseInt(idSala)){
            reportes=salas.get(j).getNumeroReportes();
        }
    }

    validar.putExtra("idSala",idSala);
    validar.putExtra("reportes",reportes);
        validar.putExtra("nombre",nombreProfesor);
        startActivity(validar);
    }
    public void ordenar2(){
        bton1.setVisibility(View.GONE);
        bton2.setVisibility(View.GONE);
        bton3.setVisibility(View.GONE);
        bton4.setVisibility(View.GONE);
        bton5.setVisibility(View.GONE);
        bton6.setVisibility(View.GONE);
        bton7.setVisibility(View.GONE);
        bton8.setVisibility(View.GONE);

        tex1.setVisibility(View.GONE);
        tex2.setVisibility(View.GONE);
        tex3.setVisibility(View.GONE);
        tex4.setVisibility(View.GONE);
        tex5.setVisibility(View.GONE);
        tex6.setVisibility(View.GONE);
        tex7.setVisibility(View.GONE);
        tex8.setVisibility(View.GONE);

        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);
        text4.setVisibility(View.GONE);
        text5.setVisibility(View.GONE);
        text6.setVisibility(View.GONE);
        text7.setVisibility(View.GONE);
        text8.setVisibility(View.GONE);

        textr1.setVisibility(View.GONE);
        textr2.setVisibility(View.GONE);
        textr3.setVisibility(View.GONE);
        textr4.setVisibility(View.GONE);
        textr5.setVisibility(View.GONE);
        textr6.setVisibility(View.GONE);
        textr7.setVisibility(View.GONE);
        textr8.setVisibility(View.GONE);
    }
    public void ordenarListado(){
        int con=1;
        if(salas.size()>0) {
            for (Sala a : salas) {
                if (con == 1) {
                    bton1.setVisibility(View.VISIBLE);
                    tex1.setVisibility(View.VISIBLE);
                    tex1.setText(a.getSalonId()+"");
                    text1.setVisibility(View.VISIBLE);
                    text1.setText(a.getNumeroReportes() + "");
                    textr1.setVisibility(View.VISIBLE);

                }
                if (con == 2) {
                    bton2.setVisibility(View.VISIBLE);
                    tex2.setVisibility(View.VISIBLE);
                    tex2.setText(a.getSalonId()+"");
                    text2.setVisibility(View.VISIBLE);
                    text2.setText(a.getNumeroReportes() + "");
                    textr2.setVisibility(View.VISIBLE);
                }
                if (con == 3) {
                    bton3.setVisibility(View.VISIBLE);
                    tex3.setVisibility(View.VISIBLE);
                    tex3.setText(a.getSalonId()+"");
                    text3.setVisibility(View.VISIBLE);
                    text3.setText(a.getNumeroReportes() + "");
                    textr3.setVisibility(View.VISIBLE);
                }
                if (con == 4) {
                    bton4.setVisibility(View.VISIBLE);
                    tex4.setVisibility(View.VISIBLE);
                    tex4.setText(a.getSalonId()+"");
                    text4.setVisibility(View.VISIBLE);
                    text4.setText(a.getNumeroReportes() + "");
                    textr4.setVisibility(View.VISIBLE);
                }
                if (con == 5) {
                    bton5.setVisibility(View.VISIBLE);
                    tex5.setVisibility(View.VISIBLE);
                    tex5.setText(a.getSalonId()+"");
                    text5.setVisibility(View.VISIBLE);
                    text5.setText(a.getNumeroReportes() + "");
                    textr5.setVisibility(View.VISIBLE);
                }
                if (con == 6) {
                    bton6.setVisibility(View.VISIBLE);
                    tex6.setVisibility(View.VISIBLE);
                    tex6.setText(a.getSalonId()+"");
                    text6.setVisibility(View.VISIBLE);
                    text6.setText(a.getNumeroReportes() + "");
                    textr6.setVisibility(View.VISIBLE);
                }
                if (con == 7) {
                    bton7.setVisibility(View.VISIBLE);
                    tex7.setVisibility(View.VISIBLE);
                    tex7.setText(a.getSalonId()+"");
                    text7.setVisibility(View.VISIBLE);
                    text7.setText(a.getNumeroReportes() + "");
                    textr7.setVisibility(View.VISIBLE);
                }
                if (con == 8) {
                    bton8.setVisibility(View.VISIBLE);
                    tex8.setVisibility(View.VISIBLE);
                    tex8.setText(a.getSalonId()+"");
                    text8.setVisibility(View.VISIBLE);
                    text8.setText(a.getNumeroReportes() + "");
                    textr8.setVisibility(View.VISIBLE);
                }
                con++;
            }
        }else{

            Toast.makeText(getApplicationContext(), "No Hay Reportes Disponibles", Toast.LENGTH_LONG).show();
        }
    }
    private class AccesoRemoto extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;

            try {
                Log.e("Codigo",idPro+"");

                url = new URL("http://gidis.ufps.edu.co:8088/servicios_arch/reporte/selectBySala");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");

                String data = "{\n" +
                        "        \"profesorId\": \""+idPro+"\"\n" +
                        "}";
                URLEncoder.encode(data,"UTF-8");
                conection.setDoOutput(true);
                conection.setFixedLengthStreamingMode(data.getBytes().length);
                conection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                OutputStream out = new BufferedOutputStream(conection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();
                respuesta=conection.getResponseCode();
                result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((linea = reader.readLine()) != null)
                        result.append(linea);
                }
            }catch (Exception a ){

            }
        return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultado=s;
            Gson miGson = new Gson();
            salas = ConvertidorGson.getList( resultado ,Sala[].class);
            ordenarListado();
        }
    }
}
