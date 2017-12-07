package com.example.cristianramirez.ufpsreport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Util.ConvertidorGson;
import Util.Reporte;
import Util.Sala;

import static android.view.View.GONE;

public class ValidarFallo extends AppCompatActivity implements View.OnClickListener {
    String nombre, idprofe, cadena, Idsala;
    TextView titulo;
    CheckBox c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, ct;
    Button btnEl, btnLis, btnApro;
    int num = 0;
    String resultado;
    List<Reporte> reportes;
    List<Reporte> repEl = new ArrayList<>();
    List<Reporte> repApro = new ArrayList<>();
    private SharedPreferences session;
    String Ur = "http://35.227.122.71/servicioApp/index.php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_fallo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = getSharedPreferences("Session", 0);
        Intent validar = getIntent();
        nombre = validar.getExtras().getString("nombre");
        titulo = (TextView) findViewById(R.id.texttituloListar);
        cadena = titulo.getText().toString();

        idprofe = validar.getExtras().getString("Id");
        Idsala = validar.getExtras().getString("idSala");
        titulo.setText(cadena + " salas");
        num = validar.getExtras().getInt("reportes");
        reportes=new ArrayList<>();
        c1 = (CheckBox) findViewById(R.id.checkBox1);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
        c4 = (CheckBox) findViewById(R.id.checkBox4);
        c5 = (CheckBox) findViewById(R.id.checkBox5);
        c6 = (CheckBox) findViewById(R.id.checkBox6);
        c7 = (CheckBox) findViewById(R.id.checkBox7);
        c8 = (CheckBox) findViewById(R.id.checkBox8);
        c9 = (CheckBox) findViewById(R.id.checkBox9);
        c10 = (CheckBox) findViewById(R.id.checkBox10);
        c11 = (CheckBox) findViewById(R.id.checkBox11);
        c12 = (CheckBox) findViewById(R.id.checkBox12);
        c13 = (CheckBox) findViewById(R.id.checkBox13);
        c14 = (CheckBox) findViewById(R.id.checkBox14);
        c15 = (CheckBox) findViewById(R.id.checkBox15);
        c16 = (CheckBox) findViewById(R.id.checkBox16);
        c17 = (CheckBox) findViewById(R.id.checkBox17);
        c18 = (CheckBox) findViewById(R.id.checkBox18);
        c19 = (CheckBox) findViewById(R.id.checkBox19);
        c20 = (CheckBox) findViewById(R.id.checkBox20);


        btnApro = (Button) findViewById(R.id.btnReportar);
        btnEl = (Button) findViewById(R.id.btnEliminar);
        btnLis = (Button) findViewById(R.id.btnListar);
        btnLis.setOnClickListener(this);
        btnEl.setOnClickListener(this);
        btnApro.setOnClickListener(this);

        lim();

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
            Intent a = new Intent(getApplicationContext(), IniciarSesion.class);
            session.edit().remove("Session").commit();
            startActivity(a);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnReportar) {
            AccesoRemotoAprobar a = new AccesoRemotoAprobar();
            a.execute();

            AccesoRemoto b = new AccesoRemoto();
            b.execute();
            Toast.makeText(getApplicationContext(), "Cargando Datos ", Toast.LENGTH_LONG).show();
        }
        if (view.getId() == R.id.btnListar) {

            AccesoRemoto a = new AccesoRemoto();
            a.execute();
            Toast.makeText(getApplicationContext(), "Cargando Datos ", Toast.LENGTH_LONG).show();
        }
        if (view.getId() == R.id.btnEliminar) {

            AccesoRemotoEliminar a = new AccesoRemotoEliminar();
            a.execute();


            AccesoRemoto b = new AccesoRemoto();
            b.execute();
            Toast.makeText(getApplicationContext(), "Cargando Datos ", Toast.LENGTH_LONG).show();
        }
    }

    public void lim() {
        c1.setChecked(false);
        c2.setChecked(false);
        c3.setChecked(false);
        c4.setChecked(false);
        c5.setChecked(false);
        c6.setChecked(false);
        c7.setChecked(false);
        c8.setChecked(false);
        c9.setChecked(false);
        c10.setChecked(false);
        c11.setChecked(false);
        c12.setChecked(false);
        c13.setChecked(false);
        c14.setChecked(false);
        c15.setChecked(false);
        c16.setChecked(false);
        c17.setChecked(false);
        c18.setChecked(false);
        c19.setChecked(false);
        c20.setChecked(false);
        c1.setVisibility(View.GONE);
        c2.setVisibility(View.GONE);
        c3.setVisibility(View.GONE);
        c4.setVisibility(View.GONE);
        c5.setVisibility(View.GONE);
        c6.setVisibility(View.GONE);
        c7.setVisibility(View.GONE);
        c8.setVisibility(View.GONE);
        c9.setVisibility(View.GONE);
        c10.setVisibility(View.GONE);
        c11.setVisibility(View.GONE);
        c12.setVisibility(View.GONE);
        c13.setVisibility(View.GONE);
        c14.setVisibility(View.GONE);
        c15.setVisibility(View.GONE);
        c16.setVisibility(View.GONE);
        c17.setVisibility(View.GONE);
        c18.setVisibility(View.GONE);
        c19.setVisibility(View.GONE);
        c20.setVisibility(View.GONE);
    }
    public void llenarareglo(){
        int contar=0;
        for(Reporte aux:reportes){
        if(contar==0){
            if(c1.isChecked()){
                repApro.add(reportes.get(contar));
            }
        }
            if(contar==1){
                if(c2.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==2){
                if(c3.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==3){
                if(c4.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==4){
                if(c5.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==5){
                if(c6.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==6){
                if(c7.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==7){
                if(c8.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==8){
                if(c9.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==9){
                if(c10.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==10){
                if(c11.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==11){
                if(c12.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==12){
                if(c13.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==13){
                if(c14.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==14){
                if(c15.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==15){
                if(c16.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==16){
                if(c17.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==17){
                if(c18.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==18){
                if(c19.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            if(contar==19){
                if(c20.isChecked()){
                    repApro.add(reportes.get(contar));
                }
            }
            contar++;
        }
        if(repApro.size()==0){
            Toast.makeText(getApplicationContext(), "No selecciono reportes ", Toast.LENGTH_LONG).show();
        }
    }
    public void llenararegloE(){
        int contar=0;
        for(Reporte aux:reportes){
            if(contar==0){
                if(c1.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==1){
                if(c2.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==2){
                if(c3.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==3){
                if(c4.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==4){
                if(c5.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==5){
                if(c6.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==6){
                if(c7.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==7){
                if(c8.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==8){
                if(c9.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==9){
                if(c10.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==10){
                if(c11.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==11){
                if(c12.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==12){
                if(c13.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==13){
                if(c14.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==14){
                if(c15.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==15){
                if(c16.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==16){
                if(c17.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==17){
                if(c18.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==18){
                if(c19.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            if(contar==19){
                if(c20.isChecked()){
                    repEl.add(reportes.get(contar));
                }
            }
            contar++;
        }
        if(repEl.size()==0){
            Toast.makeText(getApplicationContext(), "No selecciono reportes ", Toast.LENGTH_LONG).show();
        }
    }
    public void activarCheck() {
        int a = 0;

        for (Reporte reporte : reportes) {
            if (a == 0) {
                Log.e("entro en el for", num + "");
                c1.setVisibility(View.VISIBLE);
                c1.setText(reporte.toString());
            }
            if (a == 1) {
                c2.setVisibility(View.VISIBLE);
                c2.setText(reporte.toString());
            }
            if (a == 2) {
                c3.setVisibility(View.VISIBLE);
                c3.setText(reporte.toString());
            }
            if (a == 3) {
                c4.setVisibility(View.VISIBLE);
                c4.setText(reporte.toString());
            }
            if (a == 4) {
                c5.setVisibility(View.VISIBLE);
                c5.setText(reporte.toString());
            }
            if (a == 5) {
                c6.setVisibility(View.VISIBLE);
                c6.setText(reporte.toString());
            }
            if (a == 6) {
                c7.setVisibility(View.VISIBLE);
                c7.setText(reporte.toString());
            }
            if (a == 7) {
                c8.setVisibility(View.VISIBLE);
                c8.setText(reporte.toString());
            }
            if (a == 8) {
                c9.setVisibility(View.VISIBLE);
                c9.setText(reporte.toString());
            }
            if (a == 9) {
                c10.setVisibility(View.VISIBLE);
                c10.setText(reporte.toString());
            }
            if (a == 10) {
                c11.setVisibility(View.VISIBLE);
                c11.setText(reporte.toString());
            }
            if (a == 11) {
                c12.setVisibility(View.VISIBLE);
                c12.setText(reporte.toString());
            }
            if (a == 12) {
                c13.setVisibility(View.VISIBLE);
                c13.setText(reporte.toString());
            }
            if (a == 13) {
                c14.setVisibility(View.VISIBLE);
                c14.setText(reporte.toString());
            }
            if (a == 14) {
                c15.setVisibility(View.VISIBLE);
                c15.setText(reporte.toString());
            }
            if (a == 15) {
                c16.setVisibility(View.VISIBLE);
                c16.setText(reporte.toString());
            }
            if (a == 16) {
                c17.setVisibility(View.VISIBLE);
                c17.setText(reporte.toString());
            }
            if (a == 17) {
                c18.setVisibility(View.VISIBLE);
                c18.setText(reporte.toString());
            }
            if (a == 18) {
                c19.setVisibility(View.VISIBLE);
                c19.setText(reporte.toString());
            }
            if (a == 19) {
                c20.setVisibility(View.VISIBLE);
                c20.setText(reporte.toString());
            }
            a++;
        }
    }

    private class AccesoRemoto extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;
            try {
                URL mUrl = new URL(Ur+"reporte/listar_docente?docente="+idprofe);
                HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setRequestProperty("Content-length", "0");
                httpConnection.setUseCaches(false);
                httpConnection.setAllowUserInteraction(false);
                httpConnection.setConnectTimeout(100000);
                httpConnection.setReadTimeout(100000);
                httpConnection.connect();
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.substring(11,s.length()-2);
            resultado= s;
            reportes = ConvertidorGson.getList(resultado,Reporte[].class);
            if(reportes.size()==0){
                Toast.makeText(getApplicationContext(), "NO Hay reprotes disponibles ", Toast.LENGTH_LONG).show();
            }else{
                lim();
                activarCheck();

            }
        }
    }

    private class AccesoRemotoEliminar extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            llenararegloE();
            for(Reporte aux:repEl) {
                Log.e("heyy",aux.toString());
                try {
                    URL mUrl = new URL(Ur + "reporte/eliminar?id="+aux.getId()+"&usuario="+idprofe);
                    HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                    httpConnection.setRequestMethod("GET");
                    httpConnection.setRequestProperty("Content-length", "0");
                    httpConnection.setUseCaches(false);
                    httpConnection.setAllowUserInteraction(false);
                    httpConnection.setConnectTimeout(100000);
                    httpConnection.setReadTimeout(100000);
                    httpConnection.connect();
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        Log.e("---->",sb.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return "Ha";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultado=s;
            lim();
        }

         }


    private class AccesoRemotoAprobar extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;
            llenarareglo();
            for(Reporte aux:repApro) {

                try {
                    URL mUrl = new URL(Ur + "reporte/validar?id="+aux.getId()+"&usuario="+idprofe);
                    HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                    httpConnection.setRequestMethod("GET");
                    httpConnection.setRequestProperty("Content-length", "0");
                    httpConnection.setUseCaches(false);
                    httpConnection.setAllowUserInteraction(false);
                    httpConnection.setConnectTimeout(100000);
                    httpConnection.setReadTimeout(100000);
                    httpConnection.connect();
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        Log.e("---->",sb.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return "Ha";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultado=s;
            lim();
        }



    }
}