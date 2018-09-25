package mta.epn.ginghogam.com.mitaller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class ResultadosActivity extends AppCompatActivity {
    private Tutor tutor;
    private Estudiante estudiante;
    private Historia historia;
    private Taller taller;
    int correctas = 0;
    int inCorrectas = 0;
    private Integer tiempo=1;
    private Boolean logro =false;
    private TextView nombreTaller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        historia = extras.getParcelable("historia");
        taller = extras.getParcelable("taller");
        correctas = extras.getInt("correctas");
        inCorrectas = extras.getInt("incorrectas");
        tiempo = extras.getInt("tiempo");
        logro = extras.getBoolean("logro");

        Toast.makeText(this,"nombreTaller:"+ taller.getNombreTaller(),Toast.LENGTH_SHORT).show();

       nombreTaller =(TextView)findViewById(R.id.nombretaller);
        TextView nombrehistoria =findViewById(R.id.nombrehistoriaResultado);
        TextView logro =findViewById(R.id.resultado);
        TextView aciertos =findViewById(R.id.aciertos);
        TextView fallos =findViewById(R.id.fallos);
        ImageButton guardar= findViewById(R.id.guardar);
        EditText observacion =findViewById(R.id.observaciones);

    nombreTaller.setText(taller.getNombreTaller().toString());
//        nombrehistoria.setText(historia.getNombreHistoria().toString());


    }
}
