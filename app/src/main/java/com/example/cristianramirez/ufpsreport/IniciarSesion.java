package com.example.cristianramirez.ufpsreport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener,AsyncResponseLogin {

    TextInputEditText txtcodigo, txtpassword;
    TextView registrar;
    Button btnlogin;
    private SharedPreferences session;
    private String datosSesion;
    LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Guardar la sesion del profesor o estudiante.
        session = getApplicationContext().getSharedPreferences("Session",0);

        txtcodigo = (TextInputEditText) findViewById(R.id.codigo);
        txtpassword = (TextInputEditText) findViewById(R.id.password);
        registrar=(TextView) findViewById(R.id.registrar);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        registrar.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        /*Usando un AsyncTask para el logueo.*/
        if(view.getId()==R.id.btnlogin){
            if(txtcodigo.getText().toString().trim().equals("")|| txtpassword.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();

            }else{
                if(txtcodigo.getText().toString().trim().contains("@ufps.edu.co")){
                    loginTask = new LoginTask(txtcodigo.getText().toString(),txtpassword.getText().toString());
                    //Manda la instancia actual a la interfaz.
                    loginTask.delegate = this;
                    loginTask.execute();
                }else{
                    loginTask = new LoginTask(txtcodigo.getText().toString()+"@ufps.edu.co",txtpassword.getText().toString());
                        //Manda la instancia actual a la interfaz.
                        loginTask.delegate = this;
                        loginTask.execute();
                }


            }
        }
        if(view.getId()==R.id.registrar){
            Intent i = new Intent(getApplicationContext(),RegistrarUsuario.class);
            startActivity(i);
        }


    }

    @Override
    public void processFinish(String datosSesion) {
        this.datosSesion = datosSesion;
        //Log.i("AQUI REGRESO --->",this.datosSesion);

        Intent i;

        if (!this.datosSesion.equals(""))
        {
            String codigo2= "",nombre2="",  rol2= "";
            //Log.i("VAMO A PASEARTE JSON", this.datosSesion);
            try{
                Log.e("processFinish:s ", datosSesion );
                JSONArray pruebas = new JSONArray(this.datosSesion);
                JSONObject prueba2 = pruebas.getJSONObject(0);

                codigo2 = prueba2.getString("id");

                nombre2= prueba2.getString("name");

                rol2 =prueba2.getString("tipo");

            }catch (Exception e){
                Log.e( "processFinish: ",e.getMessage());
            }

            Log.e("---------------JSON CODIGO------------", codigo2);

            Log.e("---------------JSON ROL------------", rol2);


            SharedPreferences.Editor edit = session.edit();
            //Colocamos los valores en sesión.
            edit.putString("Id",codigo2);
            edit.putString("nombre",nombre2);
            edit.putString("tipoRol",rol2);

            edit.commit(); //Guardar cambios.
            //Log.d("RESULTADO", prueba);

            // Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

            switch (rol2){
                case "Estudiante":
                    i = new Intent(getApplicationContext(), MenuEstudiante.class);
                    startActivity(i);
                    break;
                case "Docente":
                    i = new Intent(getApplicationContext(),MenuProfesor.class);

                    startActivity(i);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Correo o contraseña no válida", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else
        {

            Toast.makeText(getApplicationContext(), "No se puede acceder", Toast.LENGTH_SHORT).show();
        }

    }

    private class LoginTask extends AsyncTask<Void, Void,String>{

        private String codigo, password;
        public AsyncResponseLogin delegate = null;

        public LoginTask(String cod, String pass)
        {
            codigo = cod;
            password = pass;
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;
            JSONObject jsonSesion;

            try {

                url = new URL("http://35.227.122.71/servicioApp/index.php/validar?correo="+codigo+"&contrasena="+password);
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");

                jsonSesion = new JSONObject();
                try{
                    // Construimos el JSON.

                    jsonSesion.accumulate("correo",codigo);
                    jsonSesion.accumulate("contrasena",password);


                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage() , Toast.LENGTH_LONG).show();
                }

               Log.d("---------JSON------",jsonSesion.toString());
                URLEncoder.encode(jsonSesion.toString(),"UTF-8");
                conection.setDoInput(true);
                conection.setDoOutput(true);
                conection.setFixedLengthStreamingMode(jsonSesion.toString().getBytes().length);
                conection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                OutputStream out = new BufferedOutputStream(conection.getOutputStream());
                out.write(jsonSesion.toString().getBytes());
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

                    return result.toString();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No hay conexión a Internet", Toast.LENGTH_LONG).show();
                    return "";
                }
            }catch (Exception e )
            {

                return "";
            }
        }

        @Override
        protected void onPostExecute(String dataSesion) {
            super.onPostExecute(dataSesion);
            Log.i("AQUI VIENE POSTEXECUTE",dataSesion);
            delegate.processFinish(dataSesion);

        }
    }
}
