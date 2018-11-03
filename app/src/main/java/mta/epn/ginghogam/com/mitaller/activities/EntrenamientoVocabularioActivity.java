package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.PalabraEntrenamientoListAdapter;
import mta.epn.ginghogam.com.mitaller.adaptadores.PalabraListAdapter;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.VocabularioDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

import static android.graphics.Color.rgb;

public class EntrenamientoVocabularioActivity extends AppCompatActivity implements RecyclerItemClickListener, TextToSpeech.OnInitListener {

    private RecyclerView recyclerPalabra;


    private PalabraEntrenamientoListAdapter palabraEntrenamientoListAdapter;
    private LinearLayoutManager linearLayoutManager;


    private VocabularioDAO vocabularioDAO;

    private Taller taller;
    private Tutor tutor;
    private Estudiante estudiante;
    private List<Vocabulario> vocabularioList;
    String dificultadSeleccionada;
    ImageView fondosinregistro;


    private ImageView guia;
    private TextView lectura;

    private TextToSpeech TtS;
    int contador = 0;
    private int i = 0;


    Handler handlerbuho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_vocabulario);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);




        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");
        estudiante = extras.getParcelable("estudiante");
        tutor = extras.getParcelable("tutor");
        dificultadSeleccionada = extras.getString("dificultad");

        Toast.makeText(this, dificultadSeleccionada, Toast.LENGTH_LONG).show();

        recyclerPalabra = (RecyclerView) findViewById(R.id.recyclerPalabraEntrenamiento);
        fondosinregistro=findViewById(R.id.sinregistros);


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        palabraEntrenamientoListAdapter = new PalabraEntrenamientoListAdapter(this);
        palabraEntrenamientoListAdapter.setOnItemClickListener(this);

        recyclerPalabra.setLayoutManager(linearLayoutManager);
        recyclerPalabra.setAdapter(palabraEntrenamientoListAdapter);

        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Antes de continuar es importante que conozcas algunas palabras. " +
                "Ten en cuenta que la pabras con un recuadro azul son insumos. Los recuadros rojos son cosas peligrosas." +
                "Los recuadros verdes son comida";
        lectura.setText(msj);
        lectura.setTextColor(rgb(0,0,0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });


        TtS = new TextToSpeech(this, this);



    }

    private void hablar() {
        String msj = "Antes de continuar es importante que conozcas algunas palabras. Ten en cuenta que la palabras con un recuadro azul son insumos. Los recuadros rojos son cosas peligrosas. "  +
                "Los recuadros verdes son comida";
        final List<String> texto =new ArrayList<>();
        Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher reMatcher = re.matcher(msj);
        int h =0;
        while (reMatcher.find()) {
            texto.add(reMatcher.group());
        }
        final Handler mHandler = new Handler();
        if(i>=0 && i<texto.size()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    lectura.setText(texto.get(i-1));
                }
            });
            TtS.speak(texto.get(i), TextToSpeech.QUEUE_FLUSH, null);
            // mHandler.removeCallbacksAndMessages(this);
            i++;
        }else
        if(i>texto.size() ||i==texto.size()){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    lectura.setText(texto.get(0));
                }
            });

            TtS.speak(texto.get(0), TextToSpeech.QUEUE_FLUSH, null);
            i = 1;

            // mHandler.removeCallbacksAndMessages(this);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData() {

        vocabularioDAO = new VocabularioDAO(this);

        vocabularioList = new ArrayList<>();
        long params = taller.getIdTaller();
        Cursor cursor = vocabularioDAO.retrieve(params);

        Vocabulario vocabulario;

        if (cursor.moveToFirst()) {
            do {

                vocabulario = new Vocabulario();

                vocabulario.setIdpalabra(cursor.getInt(0));
                vocabulario.setPalabra(cursor.getString(1));
                vocabulario.setImagenPalabra(cursor.getString(2));
                vocabulario.setSonidoPalabra(cursor.getString(3));
                vocabulario.setTipoPalabra(cursor.getString(4));
                vocabulario.setIdTaller(cursor.getInt(5));
                vocabularioList.add(vocabulario);


            } while (cursor.moveToNext());
            fondosinregistro.setVisibility(View.GONE);
        }
        else {
            fondosinregistro.setVisibility(View.VISIBLE);
        }

        cursor.close();
        vocabularioDAO.close();

        palabraEntrenamientoListAdapter.clear();
        palabraEntrenamientoListAdapter.addAll(vocabularioList);
    }


    @Override
    public void onItemClick(int position, View view) {

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setCustomView(R.layout.menu_vocabulario_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void pasar(View view) {
        Intent intent = new Intent(EntrenamientoVocabularioActivity.this, SeleccionHistoriaEntrenamientoActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("dificultad", dificultadSeleccionada);
        intent.putExtra("taller", taller);
        TtS.stop();
        startActivity(intent);
    }

    public void llamarmenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();

    }

    @Override
    public void onInit(int text) {
        if (text == TextToSpeech.SUCCESS) {
            int lenguaje = TtS.isLanguageAvailable(new Locale("spa", "ESP"));
            if (lenguaje == TextToSpeech.LANG_MISSING_DATA || lenguaje == TextToSpeech.LANG_NOT_SUPPORTED) {
                hablar();
            } else {
            }
        } else {
        }
    }
}
