package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

import static android.graphics.Color.rgb;


public class SeleccionDificultadActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private SeekBar seekBarDificultad;
    private TextView dificultad;
    private int seekbarvalue;
    private Estudiante estudiante;
    private Tutor tutor;
    private TextToSpeech TtS;
    private Handler mHandler;
    private Runnable mRunnable;
    private int i = 0;
    private ImageView guia;
    private TextView lectura;

    String dificultadSeleccionada = "facil";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_dificultad);
        TtS=new TextToSpeech(this,this);

        seekBarDificultad=findViewById(R.id.seekbardificultad);
        dificultad=findViewById(R.id.dificultad);
        seekBarDificultad.setMax(9);
        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarDificultad.setMin(1);
        }
        seekBarDificultad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>=1 && progress <=3){
                    seekbarvalue=progress;
                    dificultad.setText("FACIL: "+progress+ " laminas");
                    dificultadSeleccionada = "facil";
                }
                if(progress>3 && progress <=6){
                    dificultad.setText("MEDIO: "+progress+ " laminas");
                    dificultadSeleccionada = "medio";
                }
                if(progress>6 && progress <=9){
                    dificultad.setText("DIFICIL: "+progress+ " laminas");
                    dificultadSeleccionada = "dificil";
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Porfavor selecciona la dificultad para el ejercicio";
        lectura.setText(msj);
        lectura.setTextColor(rgb(255,192,0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });

    }

    private void hablar() {
        String msj = "Porfavor selecciona la dificultad para el ejercicio";
        final String texto= msj.toString();

        final String [] palabraResaltada = texto.split("\\s+");
        lectura.setText("");
        i=0;
        final Handler mHandler = new Handler();

        mHandler.post(new Runnable(){
            @Override
            public void run() {
                lectura.append(palabraResaltada[i]+" ");
                lectura.setTextColor(rgb(255,192,0));
                lectura.setMovementMethod(new ScrollingMovementMethod());


                i++;
                if(i < palabraResaltada.length) {
                    mHandler.postDelayed(this, 300);

                }
            }
        });
        TtS.speak(texto, TextToSpeech.QUEUE_FLUSH,null);


    }
    public void pasar(View view) {
        Intent intent = new Intent(SeleccionDificultadActivity.this, SeleccionTallerEntrenamientoActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("dificultad",dificultadSeleccionada);
        startActivity(intent);
    }



    @Override
    public void onInit(int text) {
        if(text==TextToSpeech.SUCCESS){
            int lenguaje= TtS.isLanguageAvailable(new Locale("spa", "ESP"));
            if(lenguaje == TextToSpeech.LANG_MISSING_DATA || lenguaje==TextToSpeech.LANG_NOT_SUPPORTED){
                guia.setSaveEnabled(true);
                hablar();

            }
            else {
            }
        }
        else{}
    }

    public boolean onCreateOptionsMenu(Menu menu) {
            getSupportActionBar().setCustomView(R.layout.menu_seleccion_dificultad_titulo);
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
    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
