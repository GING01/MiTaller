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
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
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

    private SQLiteDB sqLiteDB;
    private TallerDAO tallerDAO;

    private Estudiante estudiante;
    private Tutor tutor;

    private TextView lectura;
    private ImageView guia;

    private TextToSpeech TtS;


    private Handler mHandler;
    private Runnable mRunnable;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_historia_entrenamiento);
        TtS = new TextToSpeech(this, this);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");

        Toast.makeText(this, "Estudiante: " + estudiante.getNombreEstudiate() + " - Tutor: " + tutor.getNombreTutor(), Toast.LENGTH_LONG).show();


        context = this;

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapterHistoria();

        sqLiteDB = new SQLiteDB(this);
        tallerDAO = new TallerDAO(this);

        List<Taller> tallerList = new ArrayList<>();

        Cursor cursor = tallerDAO.retrieve();
        Taller taller;

        if (cursor.moveToFirst()) {
            do {

                taller = new Taller();

                taller.setIdTaller(cursor.getInt(0));
                taller.setNombreTaller(cursor.getString(1));
                taller.setDescripcionTaller(cursor.getString(2));
                taller.setImagenTaller(cursor.getString(3));
                tallerList.add(taller);
                mCardAdapter.addCardItemS(taller, estudiante, tutor);
            } while (cursor.moveToNext());
        }


        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Variación de una magnitud en función de la distancia, " +
                "a partir de la línea en que esta variación es máxima en las magnitudes cuyo valor es distinto en los diversos puntos de una región del espacio.";
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
        String msj = "Un gradiente es la variación de una magnitud en función de la distancia, " +
                "a partir de la línea en que esta variación es máxima en las magnitudes cuyo valor es distinto en los diversos puntos de una región del espacio." +
                " Variación de una magnitud en función de la distancia, \" +\n" +
                "                \"a partir de la línea en que esta variación es máxima en las magnitudes cuyo valor es distinto en los diversos puntos de una región del espacio.";
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