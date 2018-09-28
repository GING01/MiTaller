package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

import static android.graphics.Color.rgb;

public class BienvenidaTallerActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private Estudiante estudiante;
    private Tutor tutor;
    private String dificultadSeleccionada;
    private Taller taller;
    TextView nombreTaller;
    ImageView imagenTaller;
    TextView descripcionTaller;
    private TextToSpeech TtS;
    private Handler mHandler;
    private Runnable mRunnable;
    private ImageView guia;
    private TextView lectura;
    private int j;
    int i=0;
    String[] s = {};
    TextView changingText;
    String manyDifferentStrings;
    String[] resul;
    List myList;
    ArrayList<String[]> l;
    String msj;

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
        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        msj = taller.getDescripcionTaller();
        lectura.setText(msj);
        lectura.setTextColor(rgb(255,192,0));
        TtS = new TextToSpeech(this, this);

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
                i++;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setCustomView(R.layout.menu_bienvenida_taller_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private void hablar() {
        ArrayList<String> texto = new ArrayList<>();

        Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher reMatcher = re.matcher(msj);
        int h =0;
        while (reMatcher.find()) {
            texto.add(reMatcher.group());
            h++;

        }
        l = new ArrayList<>();
        for(int i = 0; i<texto.size();i++){
            resul =  texto.get(i).split("\\s");
            s = resul;
            myList = Arrays.asList(s);
            l.add(s);
        }

        lectura.setText("");
        j= 0;
        final Handler mHandler = new Handler();
        if(i<l.size()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    lectura.append(l.get(i-1)[j]+" ");
                    lectura.setTextColor(rgb(255, 192, 0));

                    j++;
                    if(j < l.get(i-1).length) {
                        mHandler.postDelayed(this, 400);
                    }else{

                    }
                }
            });
            TtS.speak(texto.get(i), TextToSpeech.QUEUE_FLUSH, null);
        }else{
            i = 0;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    lectura.append(l.get(0)[j]+" ");
                    lectura.setTextColor(rgb(255, 192, 0));

                    j++;
                    if(j < l.get(0).length) {
                        mHandler.postDelayed(this, 400);
                    }else{

                    }
                }
            });
            TtS.speak(texto.get(0), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int text) {
        if (text == TextToSpeech.SUCCESS) {
            int lenguaje = TtS.isLanguageAvailable(new Locale("spa", "ESP"));
            if (lenguaje == TextToSpeech.LANG_MISSING_DATA || lenguaje == TextToSpeech.LANG_NOT_SUPPORTED) {
                //hablar();
            } else {
            }
        } else {
        }
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
