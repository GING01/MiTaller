package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.viewpager.CardPagerAdapterHistoria;
import mta.epn.ginghogam.com.mitaller.adaptadores.viewpager.CardPagerAdapterS;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.SecuenciaDAO;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Secuencia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformer;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformerHistoria;

import static android.graphics.Color.rgb;

public class SeleccionHistoriaEntrenamientoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private ViewPager mViewPager;
    private CardPagerAdapterHistoria mCardAdapter;
    public ShadowTransformerHistoria mCardShadowTransformer;

    public int valor;


    Context context;

    private HistoriaDAO historiaDAO;
    private SecuenciaDAO secuenciaDAO;

    private Taller taller;
    private Estudiante estudiante;
    private Tutor tutor;
    private Secuencia secuencia;

    private TextView lectura;
    private ImageView guia;

    private TextToSpeech TtS;


    private Handler mHandler;
    private Runnable mRunnable;
    private int i = 0;

    String dificultadSeleccionada;
    ImageView fondosinregistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_historia_entrenamiento);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TtS = new TextToSpeech(this, this);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        taller = extras.getParcelable("taller");
        dificultadSeleccionada = extras.getString("dificultad");


        context = this;

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        fondosinregistro=findViewById(R.id.sinregistros);

        mCardAdapter = new CardPagerAdapterHistoria();

        historiaDAO = new HistoriaDAO(this);
        secuenciaDAO = new SecuenciaDAO(this);

        List<Historia> historiaList = new ArrayList<>();
        List<Secuencia> secuenciaList = new ArrayList<>();


        long params = taller.getIdTaller();
        Cursor cursor = historiaDAO.retrieveWithDificult(params, "'" + dificultadSeleccionada.toString().trim() + "'");
        Historia historia;




        if (cursor.moveToFirst()) {
            do {

                historia = new Historia();

                historia.setIdHistoria(cursor.getInt(0));
                historia.setNombreHistoria(cursor.getString(1));
                historia.setDescripcionHistoria(cursor.getString(2));
                historia.setImagenHistoria(cursor.getString(3));
                historia.setNumeroLaminas(cursor.getString(4));
                historia.setDificultad(cursor.getString(5));
                historia.setIdTaller(cursor.getInt(6));
                historiaList.add(historia);





                mCardAdapter.addCardItemS(historia, estudiante, tutor, taller);
            } while (cursor.moveToNext());
            fondosinregistro.setVisibility(View.GONE);
        }
        else {
            fondosinregistro.setVisibility(View.VISIBLE);
        }

        cursor.close();
        historiaDAO.close();
        secuenciaDAO.close();


        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Selecciona la historia";
        lectura.setText(msj);
        lectura.setTextColor(rgb(0, 0, 0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });

        mCardShadowTransformer = new ShadowTransformerHistoria(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

    }

    private void hablar() {
        String msj = "Selecciona la historia";

        final String texto = msj.toString();

        final String[] palabraResaltada = texto.split("\\s+");
        lectura.setText(msj);
        i = 0;
//        final Handler mHandler = new Handler();
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                lectura.append(palabraResaltada[i] + " ");
//                lectura.setTextColor(rgb(0,0,0));
//                lectura.setMovementMethod(new ScrollingMovementMethod());
//
//
//                i++;
//                if (i < palabraResaltada.length) {
//                    mHandler.postDelayed(this, 300);
//
//                }
//            }
//        });
        TtS.speak(texto, TextToSpeech.QUEUE_FLUSH, null);


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
        getSupportActionBar().setCustomView(R.layout.menu_seleccion_historia_titulo);
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