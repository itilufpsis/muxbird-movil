package com.example.cristianramirez.ufpsreport;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class MenuProfesor extends AppCompatActivity implements View.OnClickListener{
Button btnValida,btnEscanea;
    String textoQr;
    String nombreProfesor;
    String idProfesor;
    TextView texto;
    private SharedPreferences session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profesor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnValida = (Button) findViewById(R.id.btnValidard);
        btnEscanea = (Button) findViewById(R.id.btnEscanear);
        texto = (TextView) findViewById(R.id.textotitulo) ;
        btnValida.setOnClickListener(this);
        btnEscanea.setOnClickListener(this);

        session = getSharedPreferences("Session",0);
        idProfesor=session.getString("Id","Error");
        nombreProfesor=session.getString("nombre","Error");
        texto.setText("Bienvenido Ingenier@ " +nombreProfesor);
        SharedPreferences.Editor edit = session.edit();
        //Colocamos los valores en sesión.
        edit.putString("pimpollo","docente");
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
            Intent a = new Intent(getApplicationContext(),IniciarSesion.class);
            session.edit().remove("Session").commit();
            startActivity(a);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnValidard){
            Intent validar = new Intent(getApplicationContext(),ValidarFallo.class);
            validar.putExtra("nombre",nombreProfesor);
            validar.putExtra("Id",idProfesor);
            startActivity(validar);

        }if(view.getId()==R.id.btnEscanear){
            final Activity act = this;
            IntentIntegrator integrator = new IntentIntegrator(act);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Escanear código QR de la computadora");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
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
                // separado[0] = PC-01
                // separado[1] = 404
                separado = textoQr.split("_");


                //dispositivo[0] = PC.
                //dispositivo[1] = 01

                //Recogemos el numero del salon y el id del dispositivo.
                salonId = Integer.parseInt(separado[1]);
                dispositivoId = Integer.parseInt(separado[1]);

                Log.e("DISPOSITIVO",separado[0]);

                //Checamos si es PC o Videobeam.
                if(separado[0].equalsIgnoreCase("PC"))
                {

                    i = new Intent(getApplicationContext(),CargarRegistrarFallo.class);
                    i.putExtra("dispositivoId",separado[1]);
                    i.putExtra("salonId",separado[1]);
                    startActivity(i);
                    //Toast.makeText(this, "PC", Toast.LENGTH_SHORT).show();
                }
                else if(separado[0].equalsIgnoreCase("VB"))
                {
                    i = new Intent(getApplicationContext(),RegistrarFallo.class);
                    i.putExtra("dispositivoId",separado[1]);
                    i.putExtra("salonId",separado[1]);
                    i.putExtra("tipoDispositivo","VB");
                    startActivity(i);
                    //Toast.makeText(this, "VB", Toast.LENGTH_SHORT).show();
                }
                else if(separado[0].equalsIgnoreCase("MN"))
                {
                    i = new Intent(getApplicationContext(),RegistrarFallo.class);
                    i.putExtra("dispositivoId",separado[1]);
                    i.putExtra("salonId",separado[1]);
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
