package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mta.epn.ginghogam.com.mitaller.R;

import mta.epn.ginghogam.com.mitaller.adaptadores.viewpager.CardPagerAdapterS;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformer;

import static android.graphics.Color.rgb;

public class SeleccionTallerEntrenamientoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ViewPager mViewPager;
    private CardPagerAdapterS mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

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
    private String dificultadSeleccionada;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_data);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TtS=new TextToSpeech(this,this);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        dificultadSeleccionada=extras.getString("dificultad");

       // Toast.makeText(this, "Dificultad "+dificultadSeleccionada, Toast.LENGTH_LONG).show();


        context = this;

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapterS();

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
                mCardAdapter.addCardItemS(taller, estudiante, tutor, dificultadSeleccionada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        tallerDAO.close();

        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Selecciona el taller";
        lectura.setText(msj);
        lectura.setTextColor(rgb(0,0,0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

    }

    private void hablar() {
        String msj = "Selecciona el taller";

        final String texto= msj.toString();

        final String [] palabraResaltada = texto.split("\\s+");
        lectura.setText(msj);
        i=0;
//        final Handler mHandler = new Handler();

//        mHandler.post(new Runnable(){
//            @Override
//            public void run() {
//                lectura.append(palabraResaltada[i]+" ");
//                lectura.setTextColor(rgb(0,0,0));
//                lectura.setMovementMethod(new ScrollingMovementMethod());
//
//
//                i++;
//                if(i < palabraResaltada.length) {
//                    mHandler.postDelayed(this, 300);
//
//                }
//            }
//        });
        TtS.speak(texto, TextToSpeech.QUEUE_FLUSH,null);


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
        getSupportActionBar().setCustomView(R.layout.menu_seleccion_taller_titulo);
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

//    public void pasar(View view) {
//        Intent intent = new Intent(SeleccionTallerEntrenamientoActivity.this, BienvenidaTallerActivity.class);
//        intent.putExtra("dificultad",dificultadSeleccionada);
//        intent.putExtra("tutor", tutor);
//        intent.putExtra("estudiante", estudiante);
//        startActivity(intent);
//    }

    public void llamarmenu(View view){
    Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("EXIT", true);
    startActivity(intent);
    finish();

    }

}