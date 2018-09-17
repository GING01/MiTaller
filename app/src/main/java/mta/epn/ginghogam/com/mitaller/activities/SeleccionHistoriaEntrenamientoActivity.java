package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformer;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformerHistoria;

import static android.graphics.Color.rgb;

public class SeleccionHistoriaEntrenamientoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private ViewPager mViewPager;
    private CardPagerAdapterHistoria mCardAdapter;
    private ShadowTransformerHistoria mCardShadowTransformer;

    Context context;

    private HistoriaDAO historiaDAO;

    private Taller taller;
    private Estudiante estudiante;
    private Tutor tutor;

    private TextView lectura;
    private ImageView guia;

    private TextToSpeech TtS;


    private Handler mHandler;
    private Runnable mRunnable;
    private int i = 0;

    String dificultadSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_historia_entrenamiento);
        TtS = new TextToSpeech(this, this);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        taller = extras.getParcelable("taller");
        dificultadSeleccionada = extras.getString("dificultad");

//        Toast.makeText(this, "Dificultad: " +dificultadSeleccionada, Toast.LENGTH_LONG).show();

        context = this;

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapterHistoria();

        historiaDAO = new HistoriaDAO(this);

        List<Historia> historiaList = new ArrayList<>();

        long params = taller.getIdTaller();

        Cursor cursor = historiaDAO.retrieveWithDificult(params, "'"+dificultadSeleccionada.toString().trim()+"'");
//        Cursor cursor = historiaDAO.retrieve(params);

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


                Toast.makeText(this, "Historia: " +historia.getDificultad(), Toast.LENGTH_LONG).show();

                mCardAdapter.addCardItemS(historia, estudiante, tutor,taller);
            } while (cursor.moveToNext());
        }




        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Selecciona la historia";
        lectura.setText(msj);
        lectura.setTextColor(rgb(255, 192, 0));

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
        lectura.setText("");
        i = 0;
        final Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                lectura.append(palabraResaltada[i] + " ");
                lectura.setTextColor(rgb(255, 192, 0));
                lectura.setMovementMethod(new ScrollingMovementMethod());


                i++;
                if (i < palabraResaltada.length) {
                    mHandler.postDelayed(this, 300);

                }
            }
        });
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
}