package mta.epn.ginghogam.com.mitaller.activities;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.SesionDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Sesion;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class ResultadosActivity extends AppCompatActivity {
    private Tutor tutor;
    private Estudiante estudiante;
    private Historia historia;
    private Taller taller;
    private Sesion sesion;
    int correctas = 0;
    int inCorrectas = 0;
    private Long tiempo;
    private Boolean logro =false;
    private TextView nombreTaller;
    private SesionDAO sesionDAO;
    Date c;
    private SQLiteDB sqLiteDB;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        historia = extras.getParcelable("historia");
        taller = extras.getParcelable("taller");
        correctas = extras.getInt("correctas");
        inCorrectas = extras.getInt("incorrectas");
        tiempo = extras.getLong("tiempo");
        logro = extras.getBoolean("logro");
        c = Calendar.getInstance().getTime();
        SimpleDateFormat src = new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
        final String fecha_format=src.format(c);
        sqLiteDB = new SQLiteDB(this);
        sesionDAO = new SesionDAO(this);


       nombreTaller =(TextView)findViewById(R.id.nombretaller);
        TextView nombrehistoria =findViewById(R.id.nombrehistoriaResultado);
        final TextView logros =findViewById(R.id.resultado);
        TextView aciertos =findViewById(R.id.aciertos);
        TextView fallos =findViewById(R.id.fallos);
        ImageView guardar= findViewById(R.id.guardar);
        final EditText observacion =findViewById(R.id.observaciones);

    nombreTaller.setText("Taller de "+taller.getNombreTaller().toString()+":");
    nombrehistoria.setText(historia.getNombreHistoria());
    aciertos.setText(correctas+"" );
    fallos.setText(inCorrectas+"");
    if(logro){
        logros.setText("si");
    }
    else logros.setText("no");

    guardar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            sesion= new Sesion();

            sesion.setFechaSesion(fecha_format);
            sesion.setAciertos(correctas);
            sesion.setFallos(inCorrectas);
            sesion.setNombretaller(taller.getNombreTaller());
            sesion.setIdTaller(taller.getIdTaller());
            sesion.setNombrehistoria(historia.getNombreHistoria());
            sesion.setIdHistoria(historia.getIdHistoria());
            sesion.setLogro(logros.getText().toString());
            sesion.setNombreEstudiate(estudiante.getNombreEstudiate()+ " "+ estudiante.getApellidoEstudiante());
            sesion.setNombretutor(tutor.getNombreTutor()+" "+ tutor.getApellidoTutor());
            sesion.setTiempo(tiempo);
            sesion.setObservacion(observacion.getText().toString());
            sesion.setIdEstudiante(estudiante.getIdEstudiante());
            sesionDAO.create(sesion);

            sesionDAO.close();
            finish();

        }
    });


    }
    boolean doubleBackToExitPressedOnce = false;

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setCustomView(R.layout.menu_entrenamiento_resultado_preliminar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Si sales ahora no se guardaran los datos", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
