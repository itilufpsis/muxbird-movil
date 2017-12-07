package com.example.cristianramirez.ufpsreport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import Util.AsyncResponseLogin;

public class RegistrarUsuario extends AppCompatActivity implements View.OnClickListener {
    EditText nombre, correo, repcorreo, contraseña, repcontraseña, codigo;
    Spinner tipo;
    Button btnRegistrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombre = (EditText) findViewById(R.id.textNombre);
        codigo = (EditText) findViewById(R.id.textCodigo);
        correo = (EditText) findViewById(R.id.textcorreo);
        repcorreo = (EditText) findViewById(R.id.textrepcorreo);
        contraseña = (EditText) findViewById(R.id.textcontraseña);
        repcontraseña = (EditText) findViewById(R.id.textrepetircontraseña);
        tipo = (Spinner) findViewById(R.id.spinner2);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarU);
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Estudiante");
        adapter.add("Docente");
        tipo.setAdapter(adapter);
        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String tipoU = tipo.getSelectedItem().toString();
        String codigos = codigo.getText().toString();
        String nombres = nombre.getText().toString();

        String email = correo.getText().toString();
        String emailrep = repcorreo.getText().toString();
        String contra = contraseña.getText().toString();
        String repContra = repcontraseña.getText().toString();

        if (!tipoU.isEmpty() && !codigos.isEmpty() && !emailrep.isEmpty() && !nombres.isEmpty() && !contra.isEmpty() && !repContra.isEmpty() && !email.isEmpty()
                ) {
            if (codigos.length() <= 7) {
                if (email.contains("@ufps.edu.co")) {
                    if (email.equals(emailrep)) {
                        if (contra.length() >= 6) {
                            if (contra.equals(repContra)) {
                            Toast.makeText(this, "Registrando..", Toast.LENGTH_SHORT).show();
                            AccesoRemoto a = new AccesoRemoto();
                            a.execute();

                            } else {
                                Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "La contraseña debe contener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Los correos deben coincidir", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "El correo debe ser institucional", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "El codigo puede tener maximo 7 caracteres", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Datos incorrectos, por favor llene todos los campos ", Toast.LENGTH_SHORT).show();
        }
    }

    private class AccesoRemoto extends AsyncTask<Void, Void, String> {


        protected String doInBackground(Void... argumentos) {
            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;
            JSONObject jsonSesion;

            Log.e("doInBackground: ","entro");

            try {

                url = new URL("http://35.227.122.71/servicioApp/index.php/usuario/registrar");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");



                jsonSesion = new JSONObject();
                try{
                    // Construimos el JSON.


                    jsonSesion.accumulate("correo",correo.getText().toString());
                    jsonSesion.accumulate("contrasena",contraseña.getText().toString());
                    jsonSesion.accumulate("nombre",nombre.getText().toString());
                    jsonSesion.accumulate("tipo",tipo.getSelectedItem().toString());
                    jsonSesion.accumulate("codigo",codigo.getText().toString());


                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"a" , Toast.LENGTH_LONG).show();
                }

                Log.e("data:",jsonSesion.toString());
                URLEncoder.encode(jsonSesion.toString(), "UTF-8");

                conection.setDoOutput(true);
                conection.setFixedLengthStreamingMode(jsonSesion.toString().getBytes().length);
                conection.setRequestProperty("Content-Type", "application/json");

                OutputStream out = new BufferedOutputStream(conection.getOutputStream());
                out.write(jsonSesion.toString().getBytes());
                out.flush();
                out.close();


                respuesta = conection.getResponseCode();
                Log.e("data:",respuesta+"");
                result = new StringBuilder();
                if (respuesta == 200) {
                    InputStream in = new BufferedInputStream(conection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((linea = reader.readLine()) != null)
                        result.append(linea);
                }
            } catch (Exception a) {

            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            Intent i;

            if (!s.equals("")) {
                try{
                    if(s.toString().trim().equals("{\"success\":\"exito\"}")){
                        Toast.makeText(getApplicationContext(), "Registro de "+tipo.getSelectedItem().toString()+ " exitoso", Toast.LENGTH_SHORT).show();
                    }else if(s.toString().trim().equals("{\"err\":\"Usuario existente\"}")){
                        Toast.makeText(getApplicationContext(), "El usuario ya esta registrado", Toast.LENGTH_SHORT).show();
                    }else if(s.toString().trim().equals("{\"err\":\"No se pudo registrar\"}")){
                        Toast.makeText(getApplicationContext(), "Ocurrio un error por favor vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }


                    i = new Intent(getApplicationContext(), IniciarSesion.class);
                    startActivity(i);
                }catch (Exception e){}
            }else{
                Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}










