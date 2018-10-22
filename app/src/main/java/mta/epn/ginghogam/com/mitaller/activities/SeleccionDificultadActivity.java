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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    int contador = 0;


    Handler handlerbuho;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_dificultad);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TtS = new TextToSpeech(this, this);

        seekBarDificultad = findViewById(R.id.seekbardificultad);
        seekBarDificultad.setMax(2);
        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarDificultad.setMin(1);
        }
        seekBarDificultad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    seekbarvalue = progress;
                    dificultadSeleccionada = "facil";
                }
                if (progress == 1) {

                    dificultadSeleccionada = "medio";
                }
                if (progress == 2) {

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
        lectura.setTextColor(rgb(0,0,0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contador++;

                Toast.makeText(getApplicationContext(), "" + contador, Toast.LENGTH_SHORT).show();
                handlerbuho = new Handler();
                handlerbuho.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (contador == 1) {
                            hablar();
                        }
                        if (contador == 2) {
                        }
                        if (contador >= 3) {
                            contador = 0;
                        }

                    }
                }, 5);
            }
        });

    }

    private void hablar() {
        String msj = "Porfavor selecciona la dificultad para el ejercicio";
        final String texto = msj.toString();

        final String[] palabraResaltada = texto.split("\\s+");
        lectura.setText(msj);
//        i = 0;
//        final Handler mHandler = new Handler();
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (contador == 1) {
//                    lectura.append(palabraResaltada[i] + " ");
//                    lectura.setTextColor(rgb(0,0,0));
//                    lectura.setMovementMethod(new ScrollingMovementMethod());
//                    i++;
//                    if (i < palabraResaltada.length) {
//                        mHandler.postDelayed(this, 450);
//                    } else {
//                        i = 0;
//                        handlerbuho.removeCallbacksAndMessages(null);
//                    }
//                }
//            }
//        });
        TtS.speak(texto, TextToSpeech.QUEUE_FLUSH, null);


    }

    public void pasar(View view) {
        Intent intent = new Intent(SeleccionDificultadActivity.this, SeleccionTallerEntrenamientoActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("dificultad", dificultadSeleccionada);
        startActivity(intent);
    }


    @Override
    public void onInit(int text) {
        if (text == TextToSpeech.SUCCESS) {
            int lenguaje = TtS.isLanguageAvailable(new Locale("spa", "ESP"));
            if (lenguaje == TextToSpeech.LANG_MISSING_DATA || lenguaje == TextToSpeech.LANG_NOT_SUPPORTED) {
                guia.setSaveEnabled(true);
                hablar();

            } else {
            }
        } else {
        }
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

    public void llamarmenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
