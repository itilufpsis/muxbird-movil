package com.example.cristianramirez.ufpsreport;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MenuEstudiante extends AppCompatActivity implements View.OnClickListener {

    TextView txtbienvenida;
    private SharedPreferences session;
    Button btnEscanear;
    String textoQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_estudiante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Traemos la sesion del usuario.
        session = getSharedPreferences("Session",0);

        btnEscanear = (Button) findViewById(R.id.btnEscanearAlumno);
        txtbienvenida = (TextView) findViewById(R.id.txtBienvenida);

        txtbienvenida.setText("Bienvenido " + session.getString("nombre",null) );
        btnEscanear.setOnClickListener(this);
        SharedPreferences.Editor edit = session.edit();
        //Colocamos los valores en sesión.
        edit.putString("pimpollo","estudiante");
        edit.commit();
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
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnEscanearAlumno:
                final Activity act = this;
                IntentIntegrator integrator = new IntentIntegrator(act);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Escanear código QR de la computadora");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        String[] separado, dispositivo;
        int dispositivoId, salonId;

        if(result != null)
        {
            if(result.getContents() == null)
            {
                Toast.makeText(this, "Se canceló el escaneo", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent i;
                textoQr = result.getContents();

                //Formato del QR: PCNumero_SalonId
                //Ejemplo: PC-01_404

                //El separado quedará así:
                // separado[0] = PC
                // separado[1] = 01
                // separado[2] = 404
                separado = textoQr.split("_");

                //Recogemos el numero del salon y el id del dispositivo.
                salonId = Integer.parseInt(separado[1]);
                dispositivoId = Integer.parseInt(separado[1]);

                Log.e("DISPOSITIVO",separado[0]);
                Log.e("DISPOSITIVO",separado[1]);

                //Checamos si es PC o Videobeam.
                if(separado[0].equalsIgnoreCase("pc"))
                {

                    i = new Intent(getApplicationContext(),CargarRegistrarFallo.class);
                    i.putExtra("dispositivoId",separado[1]);
                    startActivity(i);
                    //Toast.makeText(this, "PC", Toast.LENGTH_SHORT).show();
                }
                else if(separado[0].equalsIgnoreCase("vb"))
                {
                    i = new Intent(getApplicationContext(),RegistrarFallo.class);
                    i.putExtra("dispositivoId",separado[1]);
                    i.putExtra("tipoDispositivo","VB");
                    startActivity(i);
                    //Toast.makeText(this, "VB", Toast.LENGTH_SHORT).show();
                }
                else if(separado[0].equalsIgnoreCase("mn"))
                {
                    i = new Intent(getApplicationContext(),RegistrarFallo.class);
                    i.putExtra("dispositivoId",separado[1]);
                    i.putExtra("tipoDispositivo","MN");
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(this, "Código QR no válido", Toast.LENGTH_SHORT).show();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
