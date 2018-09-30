package mta.epn.ginghogam.com.mitaller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

        Toast.makeText(this,historia.getNombreHistoria().toString(),Toast.LENGTH_SHORT).show();

       nombreTaller =(TextView)findViewById(R.id.nombretaller);
        TextView nombrehistoria =findViewById(R.id.nombrehistoriaResultado);
        final TextView logros =findViewById(R.id.resultado);
        TextView aciertos =findViewById(R.id.aciertos);
        TextView fallos =findViewById(R.id.fallos);
        ImageButton guardar= findViewById(R.id.guardar);
        final EditText observacion =findViewById(R.id.observaciones);

    nombreTaller.setText(taller.getNombreTaller().toString());
    nombrehistoria.setText(Long.toString(tiempo));
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
            sesion.setNombrehistoria(historia.getNombreHistoria());
            sesion.setLogro(logros.getText().toString());
            sesion.setNombreEstudiate(estudiante.getNombreEstudiate()+ " "+ estudiante.getApellidoEstudiante());
            sesion.setNombretutor(tutor.getNombreTutor()+" "+ tutor.getApellidoTutor());
            sesion.setTiempo(tiempo);
            sesion.setObservacion(observacion.getText().toString());
            sesion.setIdEstudiante(estudiante.getIdEstudiante());
            sesionDAO.create(sesion);
            finish();

        }
    });


    }
}
