package mta.epn.ginghogam.com.mitaller.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.SecuenciaDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Secuencia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

import static android.graphics.Color.rgb;

public class JuegoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private LinearLayout target, btnTarget, tg, tg2;
    Button bt1;
    ImageView bt2;
    private TextToSpeech TtS;
    private int i = 0;


    private TextView lectura;
    private ImageView guia;

    private static final int ITEM_ID = 4500;

    ArrayList<Secuencia> lista;

    int correctas = 0;
    int inCorrectas = 0;


    private LinearLayout rootLayout, rootLayout2;

    private Secuencia secuencia;
    private Tutor tutor;
    private Estudiante estudiante;
    private Historia historia;
    private Taller taller;
    private SecuenciaDAO secuenciaDAO;
    private Integer tiempo=0 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tiempo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        if(tiempo == 11){
            finish();
        }

        TtS = new TextToSpeech(this, this);

        target = (LinearLayout) findViewById(R.id.target1);
        btnTarget = (LinearLayout) findViewById(R.id.btnTarget);
        rootLayout = (LinearLayout) findViewById(R.id.layout_root);
        rootLayout2 = (LinearLayout) findViewById(R.id.layout_root2);


        secuenciaDAO = new SecuenciaDAO(this);
        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        historia = extras.getParcelable("historia");
        taller = extras.getParcelable("taller");

        long id = historia.getIdHistoria();
        Cursor cursor = secuenciaDAO.retrieve(id);
        lista = new ArrayList<Secuencia>();


        Secuencia secuencia;

        if (cursor.moveToFirst()) {
            do {

                secuencia = new Secuencia();
                secuencia.setIdSecuencia(cursor.getInt(0));
                secuencia.setImagenSecuencia(cursor.getString(1));
                secuencia.setOrdenImagenSecuencia(cursor.getInt(2));
                secuencia.setIdHistoria(cursor.getInt(3));
                lista.add(secuencia);
            } while (cursor.moveToNext());
        }

        Toast.makeText(this, "" + lista.get(0).getImagenSecuencia(), Toast.LENGTH_LONG).show();


        for (int i = 0; i < lista.size(); i++) {

            LinearLayout itemLayout = new LinearLayout(JuegoActivity.this);
            itemLayout.setId(ITEM_ID + i);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.setMargins(10, 10, 10, 10);

            itemLayout.setLayoutParams(params);


            tg = new LinearLayout(getApplicationContext());


            bt1 = new Button(getApplicationContext());
            bt1.setText("" + (i + 1));
            bt1.setLayoutParams(params);
            bt1.setId(lista.get(i).getOrdenImagenSecuencia());
            bt1.setBackgroundColor(Color.DKGRAY);
            bt1.setTextColor(Color.WHITE);
            bt1.setWidth(100);
            bt1.setHeight(150);
            bt1.setTextSize(75);
            bt1.setOnDragListener(dragListener);
            tg.addView(bt1);
            target.addView(tg);

            rootLayout.addView(itemLayout);

        }

        ArrayList<Secuencia> listaRandom = new ArrayList<Secuencia>();
        listaRandom = lista;

        Random rndm = new Random();
        //rndm.setSeed(1); esto no sirve
        Collections.shuffle(listaRandom, rndm);

        for (int i = 0; i < lista.size(); i++) {

            LinearLayout itemLayout = new LinearLayout(JuegoActivity.this);
            itemLayout.setId(ITEM_ID + i);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);

//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(190, 190);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.gravity = Gravity.CENTER;
            params.setMargins(10, 10, 10, 10);

            itemLayout.setLayoutParams(params);


            Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(listaRandom.get(i).getImagenSecuencia()), 190,
                    190, true);


            bt2 = new ImageView(getApplicationContext());
            bt2.setId(listaRandom.get(i).getOrdenImagenSecuencia());
            bt2.setLayoutParams(params);
            bt2.setBackgroundColor(Color.BLACK);
            bt2.setImageBitmap(newBitmap);

            bt2.setOnLongClickListener(longClickListener);
            btnTarget.addView(bt2);
            rootLayout2.addView(itemLayout);
        }


        lectura = findViewById(R.id.texto);
        guia = findViewById(R.id.guia);

        String msj = "Por favor, ordena las imagenes!";
        lectura.setText(msj);
        lectura.setTextColor(rgb(255, 192, 0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });


    }



    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, myShadowBuilder, view, 0);

            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:

                    if (view.getId() == v.getId()) {
                        Toast.makeText(getApplicationContext(), "Dropped", Toast.LENGTH_SHORT).show();

                        LinearLayout oldparent = (LinearLayout) view.getParent();
                        oldparent.removeView(view);
                        Button newParent = (Button) v;
                        LinearLayout hola = (LinearLayout) newParent.getParent();
                        v.setVisibility(View.GONE);
                        hola.addView(view);

                        correctas++;
                    } else {


                        inCorrectas++;
                        Toast.makeText(getApplicationContext(), "Incorrecto" + " incorrecto" + inCorrectas, Toast.LENGTH_SHORT).show();
                    }
                    if (correctas == lista.size()) {
                        Toast.makeText(getApplicationContext(), "FELICIDADES LOS HAS LOGRADO" + " correctas" + correctas + " incorrectas: " + inCorrectas, Toast.LENGTH_SHORT).show();

                    }
                    break;
            }

            return true;
        }
    };

    private void hablar() {
        String msj = "Por favor, ordena las imagenes!";

        final String texto = msj.toString();

        final String[] palabraResaltada = texto.split("\\s+");
        lectura.setText("");
        i = 0;
        final Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                lectura.append(palabraResaltada[i] + " ");
//                lectura.setTextColor(rgb(255, 192, 0));
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

    public void tiempo(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(JuegoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_tiempo, null);
        Button boButton =mView.findViewById(R.id.button2);
        boButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              cancelar();
              finish();

            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();

    }

    private void cancelar() {
        tiempo =11;
    }
}