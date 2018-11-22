package mta.epn.ginghogam.com.mitaller.adaptadores.viewpager;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.activities.EntrenamientoVocabularioActivity;
import mta.epn.ginghogam.com.mitaller.activities.JuegoActivity;
import mta.epn.ginghogam.com.mitaller.activities.ResultadosActivity;
import mta.epn.ginghogam.com.mitaller.activities.SeleccionHistoriaEntrenamientoActivity;
import mta.epn.ginghogam.com.mitaller.db.SecuenciaDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Secuencia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformerHistoria;

import static android.graphics.Color.rgb;


public class CardPagerAdapterHistoria extends PagerAdapter
        implements CardAdapter, TextToSpeech.OnInitListener {

    private List<CardView> mViews;
    private List<Historia> mData;
    private float mBaseElevation;


    private Taller taller;
    private Tutor tutor;
    private Estudiante estudiante;

    //variables karaoke
    private TextToSpeech TtS;
    int j;
    int i = 0;
    public static boolean changePage;

    String[] s = {};
    String[] resul;
    List myList;
    ArrayList<String[]> l;
    Handler mHandler = new Handler();

    AlertDialog dialog;

    int contador = 0;
    boolean isStopped;

    private SecuenciaDAO secuenciaDAO;
    private Secuencia secuencia;


    public CardPagerAdapterHistoria() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(Historia item, Estudiante estudiante, Tutor tutor, Taller taller) {
        mViews.add(null);

        mData.add(item);

        this.taller = taller;
        this.estudiante = estudiante;
        this.tutor = tutor;


    }


    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (View) object);
    }


    View view = null;


    public Object instantiateItem(final ViewGroup collection, final int position) {

        secuenciaDAO = new SecuenciaDAO(collection.getContext());

        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View localView = null;
        view = LayoutInflater.from(collection.getContext()).inflate(R.layout.adapter_historia, collection, false);
        collection.addView(view);


        final String manyDifferentStrings = mData.get(position).getDescripcionHistoria();
        final ImageView plusmaxhp = view.findViewById(R.id.play);
        final TextView maxhpdisplay = (TextView) view.findViewById(R.id.tvDescripcionHistoria);
        final TextView nombreHistoria = (TextView) view.findViewById(R.id.tvNombreHistoria);

        nombreHistoria.setText(mData.get(position).getNombreHistoria());
        maxhpdisplay.setText(mData.get(position).getDescripcionHistoria());
        localView = view;

        TtS = new TextToSpeech(collection.getContext().getApplicationContext(), this);

        ImageView image = (ImageView) view.findViewById(R.id.ivHistoria);
        Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position).getImagenHistoria());
        //image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 250, 220, true));
        ImageView irJuego = (ImageView) view.findViewById(R.id.irJuego);
        irJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityJuego(view.getContext(), mData.get(position), estudiante, tutor, taller);

                entrenamiento(collection.getContext(), position);

            }
        });
        switch (position) {
            default:
                final View finalLocalView = localView;
                plusmaxhp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!changePage) {
                            i++;
                            hablar(manyDifferentStrings, finalLocalView, i, position);
                        }
                        if (changePage) {
                            TtS.speak("Por favor, vuelve a presionar el botón!", TextToSpeech.QUEUE_FLUSH, null);
                            changePage = false;
                            i = 0;
                            j = 0;
                            isStopped = true;
                        }
                    }
                });
                break;
        }
        if (mViews != null) {
            ((ViewPager) collection).addView(localView, position);

        }
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return localView;

    }

    int contadorSecuencia = 0;



    private void entrenamiento(final Context context, final int position) {

        long id = mData.get(position).getIdHistoria();
        Cursor c = secuenciaDAO.retrieve(id);
        final ArrayList<Secuencia> secuenciaList = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                secuencia = new Secuencia();
                secuencia.setIdSecuencia(c.getInt(0));
                secuencia.setImagenSecuencia(c.getString(1));
                secuencia.setOrdenImagenSecuencia(c.getInt(2));
                secuencia.setDescripcionImagenSecuencia(c.getString(3));
                secuencia.setIdHistoria(c.getInt(4));
                secuenciaList.add(secuencia);
            } while (c.moveToNext());

        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.entrenamiento_secuencia_dialog, null);

        final ImageView imageView = mView.findViewById(R.id.welldoneico);
        Button jugar = mView.findViewById(R.id.empezar);
        Button cerrar = mView.findViewById(R.id.iconcerrar);
        final TextView texto = mView.findViewById(R.id.textolamina);


        final TextView numLaminas = mView.findViewById(R.id.numLaminas);
        TtS.speak("Conoce los pasos que debes seguir", TextToSpeech.QUEUE_FLUSH, null);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (secuenciaList.size() == 0) {
                    TtS.speak("Perdon no existe contenido", TextToSpeech.QUEUE_FLUSH, null);

                } else if (contadorSecuencia < secuenciaList.size()) {

                    numLaminas.setText((contadorSecuencia + 1) + "/" + secuenciaList.size());
                    texto.setText(secuenciaList.get(contadorSecuencia).getDescripcionImagenSecuencia().toString());

                    File fileImagen = new File(secuenciaList.get(contadorSecuencia).getImagenSecuencia());
                    Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(fileImagen.getPath()), 450,
                            350, true);


                    imageView.setImageBitmap(newBitmap);
                    TtS.speak(secuenciaList.get(contadorSecuencia).getDescripcionImagenSecuencia(), TextToSpeech.QUEUE_FLUSH, null);
                    TtS.speak("Paso" + (contadorSecuencia + 1) + "  :" + secuenciaList.get(contadorSecuencia).getDescripcionImagenSecuencia(), TextToSpeech.QUEUE_FLUSH, null);

                } else if (contadorSecuencia == secuenciaList.size()) {
                    contadorSecuencia = 0;
                    numLaminas.setText((1) + "/" + secuenciaList.size());
                    texto.setText(secuenciaList.get(contadorSecuencia).getDescripcionImagenSecuencia().toString());
                    File fileImagen = new File(secuenciaList.get(contadorSecuencia).getImagenSecuencia());
                    Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(fileImagen.getPath()), 450,
                            350, true);

                    imageView.setImageBitmap(newBitmap);
                    TtS.speak("Paso" + (contadorSecuencia + 1) + "  :" + secuenciaList.get(contadorSecuencia).getDescripcionImagenSecuencia(), TextToSpeech.QUEUE_FLUSH, null);
                }


                contadorSecuencia++;


            }
        });


        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secuenciaList.size() == 0) {
                    TtS.speak("Perdon no existe contenido", TextToSpeech.QUEUE_FLUSH, null);
                    dialog.dismiss();

                } else {
                    startActivityJuego(view.getContext(), mData.get(position), estudiante, tutor, taller);
                    contadorSecuencia=0;
                    dialog.dismiss();
                }


            }
        });
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    public static void startActivityJuego(Context context, Historia historia, Estudiante estudiante, Tutor tutor, Taller taller) {
        Intent intent = new Intent(context, JuegoActivity.class);
        intent.putExtra("historia", historia);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("tutor", tutor);
        intent.putExtra("taller", taller);
        context.startActivity(intent);
    }

    private void hablar(String manyDifferentStrings, View v, final int p, int position) {


        final TextView maxhpdisplay = (TextView) v.findViewById(R.id.tvDescripcionHistoria);
        final ImageView plusmaxhp = v.findViewById(R.id.play);

        final ImageView image = (ImageView) view.findViewById(R.id.ivHistoria);


        switch (position) {

            default:
                if (!isStopped) {
                } else {
                    isStopped = false;
                    mHandler.removeCallbacksAndMessages(null);
                }
                final ArrayList<String> texto = new ArrayList<>();
                Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
                Matcher reMatcher = re.matcher(manyDifferentStrings);
                int h = 0;
                while (reMatcher.find()) {
                    texto.add(reMatcher.group());
                    h++;
                }
                maxhpdisplay.setText("");
                j = 0;
                mHandler = new Handler();
                if (p <= texto.size()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!changePage) {
                                maxhpdisplay.append(texto.get(p - 1) + " ");

                                maxhpdisplay.setTextColor(rgb(0, 0, 0));
                                j++;

                            }
                            if (changePage) {
                                j = 0;
                                plusmaxhp.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    TtS.speak(texto.get(p - 1), TextToSpeech.QUEUE_FLUSH, null);
                } else {

                    i = 0;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (!changePage) {
                                maxhpdisplay.append(texto.get(0) + " ");
                                maxhpdisplay.setTextColor(rgb(0, 0, 0));
                                j++;

                            }
                            if (changePage) {
                                j = 0;
                                plusmaxhp.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    TtS.speak(texto.get(0), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
        }
    }

    @Override
    public float getPageWidth(int position) {
        return (0.90f);
    }

    @Override
    public void onInit(int text) {
        if (text == TextToSpeech.SUCCESS) {
            int lenguaje = TtS.isLanguageAvailable(new Locale("spa", "ESP"));
            if (lenguaje == TextToSpeech.LANG_MISSING_DATA || lenguaje == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {
        }
    }
}