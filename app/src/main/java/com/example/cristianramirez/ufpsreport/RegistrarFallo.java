package com.example.cristianramirez.ufpsreport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.List;
import java.util.concurrent.ExecutionException;

import Util.AsyncResponseLogin;
import Util.AsyncResponseRegistroFallos;
import Util.ConvertidorGson;
import Util.Reporte;

import static com.example.cristianramirez.ufpsreport.R.id.codigo;

public class RegistrarFallo extends AppCompatActivity implements View.OnClickListener, AsyncResponseRegistroFallos{

    private SharedPreferences session;
    String URL = "http://35.227.122.71/servicioApp/index.php/";
    JSONObject jsonRegistro;
    ImageView imageDispositivo;
    TextInputEditText txtDescripcion;
    String tipoDispositivo,dispositivoId, salonId, descripcion, docente, perisferico, autor;
    Button btnRegistrarFallo;
    RegistroFallosTask registroFallosTask;
    Intent i;

    Spinner spinner;

    Context contexto = this;

    String resultado;
    List<Docente> listaDocentes;
    String pimpollo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_fallo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Traemos la sesion del usuario.
        session = getSharedPreferences("Session",0);

        imageDispositivo = (ImageView) findViewById(R.id.imageDispositivo);
        btnRegistrarFallo = (Button) findViewById(R.id.btnRegistrarFallo);
        txtDescripcion = (TextInputEditText) findViewById(R.id.txtDescripcion);

        i = getIntent();

        tipoDispositivo = i.getExtras().getString("tipoDispositivo");
        dispositivoId = i.getExtras().getString("dispositivoId");
        salonId = i.getExtras().getString("salonId");
        jsonRegistro = new JSONObject();

        try{
            pimpollo = session.getString("pimpollo",null);
            autor = session.getString("Id",null);
            jsonRegistro.accumulate("autor", autor);
            jsonRegistro.accumulate("dispositivo", dispositivoId);
            jsonRegistro.accumulate("descripcion", descripcion);
        }catch (Exception e){}

        switch (tipoDispositivo)
        {
            case "MN":
                imageDispositivo.setImageResource(R.drawable.microcomponente);
                break;
            case "VB":
                imageDispositivo.setImageResource(R.drawable.videobeam);
                break;
            case "Mouse":
                imageDispositivo.setImageResource(R.drawable.mouse);
                try{
                    perisferico = "mouse";
                    jsonRegistro.accumulate("perisferico", "mouse");
                }catch (Exception e){}

                break;
            case "Monitor":
                imageDispositivo.setImageResource(R.drawable.monitor);
                try{
                    perisferico = "pantalla";
                    jsonRegistro.accumulate("perisferico", "pantalla");
                }catch (Exception e){}

                break;
            case "Teclado":
                imageDispositivo.setImageResource(R.drawable.teclado);
                try{
                    perisferico = "teclado";
                    jsonRegistro.accumulate("perisferico", "teclado");
                }catch (Exception e){}

                break;
            case "CPU":
                imageDispositivo.setImageResource(R.drawable.cpu);
                try{
                    perisferico = "torre";
                    jsonRegistro.accumulate("perisferico", "torre");
                }catch (Exception e){}

                break;
        }

        AccesoRemoto a = new AccesoRemoto();
        try {
            a.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        btnRegistrarFallo.setOnClickListener(this);
    }

    private class AccesoRemoto extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;
            Log.e("--->","-->"+docente+"<--");
            try {
                URL mUrl = new URL(URL + "usuario/listar_docentes");
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
            resultado=s;
            listaDocentes = ConvertidorGson.getList(resultado, Docente[].class);
            Log.e("aca",listaDocentes.toString());

            spinner = (Spinner) findViewById(R.id.comboDocentes);
            spinner.setAdapter(new ArrayAdapter<Docente>(contexto, android.R.layout.simple_spinner_item, listaDocentes));
        }
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
    public void onClick(View view) {

        descripcion = txtDescripcion.getText().toString();

        try{
            if(!jsonRegistro.has("descripcion"))
            {
                jsonRegistro.accumulate("descripcion", descripcion);
            }
            else
            {
                jsonRegistro.put("descripcion",descripcion);
            }
        }catch (Exception e){}

        Log.i("VALOR JSON INSERT", jsonRegistro.toString());

        Docente selected = (Docente) spinner.getSelectedItem();
        docente = selected.getId() + "";
        try {
            jsonRegistro.accumulate("docente", docente);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*Usando un AsyncTask para el registro de Reportes.*/
        registroFallosTask = new RegistroFallosTask(jsonRegistro.toString());
        //Manda la instancia actual a la interfaz.
        registroFallosTask.delegate = this;
        registroFallosTask.execute();

    }

    @Override
    public void processFinish(String respuestaInsert) {

        Log.i("VALOR INSERTADO",respuestaInsert);


        try{
            JSONObject jsonMensaje = new JSONObject(respuestaInsert);
            String mensaje = jsonMensaje.getString("msgTitle");

            Log.i("MENSAJE",mensaje);


            if (mensaje.equals("exito"))
            {
                Intent i = new Intent(getApplicationContext(),MenuEstudiante.class);
                Toast.makeText(this,jsonMensaje.getString("msgDescription"),Toast.LENGTH_LONG).show();
                startActivity(i);
            }
            else
            {

                Toast.makeText(this,jsonMensaje.getString("msgDescription"),Toast.LENGTH_LONG).show();
            }

        }catch(Exception e)
        {

        }
    }

    // ASYNCTASK REGISTROFALLOS
    private class RegistroFallosTask extends AsyncTask<Void, Void,String> {

        private String jsonInsert;
        public AsyncResponseRegistroFallos delegate = null;

        public RegistroFallosTask(String jsonInsert)
        {
            this.jsonInsert = jsonInsert;
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;

            try {

                url = new URL(URL + "reporte/crear");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");

                URLEncoder.encode(jsonInsert,"UTF-8");
                conection.setDoInput(true);
                conection.setDoOutput(true);
                conection.setFixedLengthStreamingMode(jsonInsert.getBytes().length);
                conection.setRequestProperty("Content-Type","application/json");
                OutputStream out = new BufferedOutputStream(conection.getOutputStream());
                out.write(jsonInsert.getBytes());
                out.flush();
                out.close();
                //Conseguimos la respuesta
                respuesta=conection.getResponseCode();

                result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((linea = reader.readLine()) != null)
                        result.append(linea);
                }
            }catch (Exception a ) {

            }

            String descValidada = descripcion.replaceAll(" ", "%20");

            Log.e("RESULTADO", URL + "reporte/crear?docente=" + docente + "&" +
                    "dispositivo=" + dispositivoId + "&" +
                    "perisferico=" + perisferico  + "&" +
                    "descripcion=" + descValidada + "&" +
                    "autor=" + autor);
            Log.e("RESULTADO", jsonInsert);
            Log.e("RESULTADO", result.toString());

                if(pimpollo.equalsIgnoreCase("docente")) {
                    i = new Intent(getApplicationContext(),MenuProfesor.class);
                    startActivity(i);
                } else if(pimpollo.equalsIgnoreCase("estudiante")) {
                    i = new Intent(getApplicationContext(), MenuEstudiante.class);
                    startActivity(i);
                }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String respuestaInsert) {
            super.onPostExecute(respuestaInsert);
            Log.i("AQUI VIENE POST-EXECUTE",respuestaInsert);
            delegate.processFinish(respuestaInsert);
        }
    }

    private class Docente {
        private int id;
        private String nombre;
        private String correo;
        private String codigo;

        public Docente() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

}
