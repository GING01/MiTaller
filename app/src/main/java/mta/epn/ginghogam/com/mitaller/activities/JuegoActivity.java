package mta.epn.ginghogam.com.mitaller.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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


    private LinearLayout target, btnTarget, tg;
    Button bt1;
    ImageView bt2;
    private TextToSpeech TtS;
    private int i = 0;
    long tiempaso;


    private TextView lectura;
    private ImageView guia;

    private static final int ITEM_ID = 4500;

    ArrayList<Secuencia> lista;

    int correctas = 0;
    int inCorrectas = 0;


    private LinearLayout rootLayout, rootLayout2;

    private Tutor tutor;
    private Estudiante estudiante;
    private Historia historia;
    private Taller taller;
    private SecuenciaDAO secuenciaDAO;
    private Integer tiempo = 1;
    private TextView ttiempo;
    long start;
    long end;
    private Boolean logro = false;
    AlertDialog dialog;
    Handler handler1;
    private String titulo;
    MediaPlayer well, wrong, hend, bend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        TtS = new TextToSpeech(this, this);

        target =  findViewById(R.id.target1);
        btnTarget =  findViewById(R.id.btnTarget);
        rootLayout =  findViewById(R.id.layout_root);
        rootLayout2 =  findViewById(R.id.layout_root2);

        wrong = MediaPlayer.create(this, R.raw.wrongmove);
        hend = MediaPlayer.create(this, R.raw.welldone);
        bend = MediaPlayer.create(this, R.raw.tryagain);
        well = MediaPlayer.create(this, R.raw.rigth);


        secuenciaDAO = new SecuenciaDAO(this);

        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");
        estudiante = extras.getParcelable("estudiante");
        historia = extras.getParcelable("historia");
        taller = extras.getParcelable("taller");


        long id = historia.getIdHistoria();
        Cursor cursor = secuenciaDAO.retrieve(id);
        lista = new ArrayList<Secuencia>();
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

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


        cursor.close();
        secuenciaDAO.close();

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                for (int i = 0; i < lista.size(); i++) {

                    LinearLayout itemLayout = new LinearLayout(JuegoActivity.this);
                    itemLayout.setId(ITEM_ID + i);
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    params.setMargins(10, 10, 10, 10);

                    itemLayout.setLayoutParams(params);
                    tg = new LinearLayout(getApplicationContext());
                    bt1 = new Button(getApplicationContext());
                    bt1.setText("" + (i + 1));
                    bt1.setLayoutParams(params);
                    bt1.setId(lista.get(i).getOrdenImagenSecuencia());
                    bt1.setBackgroundColor(Color.DKGRAY);
                    bt1.setTextColor(Color.WHITE);
                    bt1.setWidth(400);
                    bt1.setHeight(400);
                    bt1.setTextSize(75);
                    bt1.setOnDragListener(dragListener);
                    tg.addView(bt1);
                    target.addView(tg);
                    rootLayout.addView(itemLayout);
                }
                ArrayList<Secuencia> listaRandom = new ArrayList<Secuencia>();
                listaRandom = lista;
                Random rndm = new Random();
                Collections.shuffle(listaRandom, rndm);
                for (int i = 0; i < lista.size(); i++) {
                    LinearLayout itemLayout = new LinearLayout(JuegoActivity.this);
                    itemLayout.setId(ITEM_ID + i);
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    params.setMargins(10, 10, 10, 10);

                    itemLayout.setLayoutParams(params);


                    Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(listaRandom.get(i).getImagenSecuencia()), 400,
                            400, true);


                    bt2 = new ImageView(getApplicationContext());
                    bt2.setId(listaRandom.get(i).getOrdenImagenSecuencia());
                    bt2.setLayoutParams(params);
                    bt2.setBackgroundColor(Color.BLACK);
                    bt2.setImageBitmap(newBitmap);

                    bt2.setOnLongClickListener(longClickListener);
                    btnTarget.addView(bt2);
                    rootLayout2.addView(itemLayout);
                }

                break;
                default:
                    for (int i = 0; i < lista.size(); i++) {

                        LinearLayout itemLayout = new LinearLayout(JuegoActivity.this);
                        itemLayout.setId(ITEM_ID + i);
                        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL;
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
                    ArrayList<Secuencia> listaRandom2 = new ArrayList<Secuencia>();
                    listaRandom2 = lista;
                    Random rndm2 = new Random();
                    Collections.shuffle(listaRandom2, rndm2);
                    for (int i = 0; i < lista.size(); i++) {
                        LinearLayout itemLayout = new LinearLayout(JuegoActivity.this);
                        itemLayout.setId(ITEM_ID + i);
                        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL;
                        params.setMargins(10, 10, 10, 10);

                        itemLayout.setLayoutParams(params);


                        Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(listaRandom2.get(i).getImagenSecuencia()), 190,
                                190, true);


                        bt2 = new ImageView(getApplicationContext());
                        bt2.setId(listaRandom2.get(i).getOrdenImagenSecuencia());
                        bt2.setLayoutParams(params);
                        bt2.setBackgroundColor(Color.BLACK);
                        bt2.setImageBitmap(newBitmap);

                        bt2.setOnLongClickListener(longClickListener);
                        btnTarget.addView(bt2);
                        rootLayout2.addView(itemLayout);
                    }



        }
        //Toast.makeText(this, "" + lista.get(0).getImagenSecuencia(), Toast.LENGTH_LONG).show();




        lectura = findViewById(R.id.texto);
        ttiempo = (TextView) findViewById(R.id.tiempoedt);
        guia = findViewById(R.id.guia);

        String msj = "Selecciona una imagen y arrastrala hacia los cuadros superiores";
        lectura.setText(msj);
        lectura.setTextColor(rgb(0, 0, 0));

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });


    }

    @Override
    protected void onStart() {
        tiempo();
        super.onStart();
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
                        LinearLayout oldparent = (LinearLayout) view.getParent();
                        oldparent.removeView(view);
                        Button newParent = (Button) v;
                        LinearLayout ly = (LinearLayout) newParent.getParent();
                        v.setVisibility(View.GONE);
                        ly.addView(view);
                        well.start();

                        correctas++;
                    } else {
                        wrong.start();
                        inCorrectas++;
                    }
                    if (correctas == lista.size() && inCorrectas == 0) {

                    }
                    if (correctas == lista.size()) {
                        end = System.currentTimeMillis();
                        logro = true;
                        tiempaso = (end - start) / 1000;
                        welldone();
                    }
                    break;
            }

            return true;
        }
    };

    public void cerraTiempo(final Integer i) {
        handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(JuegoActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_resutlado_negativo, null);
                final ImageButton imageButton = mView.findViewById(R.id.baddoneico);
                bend.start();
                bend.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ResultadosActivity.class);
                        intent.putExtra("historia", historia);
                        intent.putExtra("estudiante", estudiante);
                        intent.putExtra("tutor", tutor);
                        intent.putExtra("taller", taller);
                        intent.putExtra("tiempo", (long) tiempo * 60);
                        intent.putExtra("logro", false);
                        intent.putExtra("correctas", correctas);
                        intent.putExtra("incorrectas", inCorrectas);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();


                    }
                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.setCancelable(false);
                dialog.show();

            }
        }, i);
    }

    private void hablar() {
        String msj = "Selecciona una imagen y arrastrala hacia los cuadros superiores";

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
//                lectura.setTextColor(rgb(0, 0, 0));
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

    public void tiempo() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(JuegoActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_tiempo, null);
        final SeekBar seekBar = mView.findViewById(R.id.seekBartiempo);
        final TextView txttiempo = mView.findViewById(R.id.txttiempo);
        seekBar.setMax(9);
        txttiempo.setText((seekBar.getProgress() + 1) + " min");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txttiempo.setText((progress + 1) + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBuilder.setView(mView);
        mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                tiempo = seekBar.getProgress() + 1;
                ttiempo.setText(tiempo + "");
                cerraTiempo(tiempo * 60000);
                start = System.currentTimeMillis();
                dialog.dismiss();


            }
        });
        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void welldone() {

        handler1.removeCallbacksAndMessages(null);


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(JuegoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_resutlado, null);
        ImageButton imageButton = mView.findViewById(R.id.welldoneico);
        hend.start();
        hend.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultadosActivity.class);
                intent.putExtra("historia", historia);
                intent.putExtra("estudiante", estudiante);
                intent.putExtra("tutor", tutor);
                intent.putExtra("taller", taller);
                intent.putExtra("tiempo", tiempaso);
                intent.putExtra("logro", logro);
                intent.putExtra("correctas", correctas);
                intent.putExtra("incorrectas", inCorrectas);
                startActivity(intent);
                dialog.dismiss();
                finish();

            }
        });
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_secuencia, menu);
        //getSupportActionBar().setCustomView(R.layout.menu_juego_taller_titulo);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.menu_juego_taller_titulo);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        historia = extras.getParcelable("historia");
        getSupportActionBar().setTitle(historia.getNombreHistoria().toUpperCase());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            handler1.removeCallbacksAndMessages(null);
            new AlertDialog.Builder(this)
                    .setMessage("¿Estás seguro que deseas salir?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            handler1.removeCallbacksAndMessages(null);
                            JuegoActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(this)
                .setMessage("¿Estás seguro que deseas salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        handler1.removeCallbacksAndMessages(null);
                        JuegoActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

}