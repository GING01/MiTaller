package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class BienvenidaTallerActivity extends AppCompatActivity {
    private Estudiante estudiante;
    private Tutor tutor;
    private String dificultad;
    private Taller taller;
    TextView nombreTaller;
    ImageView imagenTaller;
    TextView descripcionTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida_taller);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        dificultad=extras.getParcelable("dificultad");
        taller=extras.getParcelable("taller");

        Toast.makeText(this, "dificultad: " + dificultad + " - Tutor: " + tutor.getNombreTutor(), Toast.LENGTH_SHORT).show();
        nombreTaller=(TextView) findViewById(R.id.nTaller);
        imagenTaller=findViewById(R.id.imagenTaller);
        descripcionTaller=(TextView) findViewById(R.id.texto);

       nombreTaller.setText(taller.getNombreTaller());
        imagenTaller.setImageBitmap(BitmapFactory.decodeFile(taller.getImagenTaller()));
        descripcionTaller.setText(taller.getDescripcionTaller());

    }



    public void pasar(View view) {
        Intent intent = new Intent(BienvenidaTallerActivity.this,EntrenamientoVocabularioActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("dificultad",dificultad);
        intent.putExtra("taller",taller);
        startActivity(intent);
    }
}
