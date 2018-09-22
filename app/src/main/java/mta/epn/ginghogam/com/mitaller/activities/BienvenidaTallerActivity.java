package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private String dificultadSeleccionada;
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
        taller = extras.getParcelable("taller");
        dificultadSeleccionada = extras.getString("dificultad");

        Toast.makeText(this, "dificultad: " + dificultadSeleccionada, Toast.LENGTH_SHORT).show();

        nombreTaller=(TextView) findViewById(R.id.nTaller);
        imagenTaller=findViewById(R.id.imagenTaller);
        descripcionTaller=(TextView) findViewById(R.id.texto);

        nombreTaller.setText(taller.getNombreTaller());
        imagenTaller.setImageBitmap(BitmapFactory.decodeFile(taller.getImagenTaller()));
        descripcionTaller.setText(taller.getDescripcionTaller());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setCustomView(R.layout.menu_bienvenida_taller_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void pasar(View view) {
        Intent intent = new Intent(BienvenidaTallerActivity.this,EntrenamientoVocabularioActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("dificultad",dificultadSeleccionada);
        intent.putExtra("taller",taller);
        startActivity(intent);
    }

    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();

    }
}
