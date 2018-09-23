package mta.epn.ginghogam.com.mitaller.adaptadores.viewpager;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import mta.epn.ginghogam.com.mitaller.activities.EntrenamientoVocabularioActivity;
import mta.epn.ginghogam.com.mitaller.activities.JuegoActivity;
import mta.epn.ginghogam.com.mitaller.activities.SeleccionHistoriaEntrenamientoActivity;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
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
    int i=0;
    public static boolean changePage;

    String[] s = {};
    String[] resul;
    List myList;
    ArrayList<String[]> l;
    Handler mHandler = new Handler();


    int contador = 0;
    boolean isStopped;



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
        return (view == (View)object);
    }


    View view = null;


    public Object instantiateItem(final ViewGroup collection, final int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View localView = null;
        view = LayoutInflater.from(collection.getContext()).inflate(R.layout.adapter_historia, collection, false);
        collection.addView(view);


        final String manyDifferentStrings = mData.get(position).getDescripcionHistoria();

        final Button plusmaxhp =  view.findViewById(R.id.play);

        final TextView maxhpdisplay = (TextView) view.findViewById(R.id.tvDescripcionHistoria);

        final TextView nombreHistoria = (TextView) view.findViewById(R.id.tvNombreHistoria);


        maxhpdisplay.setText(mData.get(position).getDescripcionHistoria());
        localView = view;

        TtS = new TextToSpeech(collection.getContext().getApplicationContext(), this);


        ImageView image = (ImageView) view.findViewById(R.id.ivHistoria);
        image.setImageBitmap(BitmapFactory.decodeFile(mData.get(position).getImagenHistoria()));

        Button irJuego = (Button) view.findViewById(R.id.irJuego);



        irJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityJuego(view.getContext(), mData.get(position), estudiante, tutor, taller);

            }
        });




        switch (position){
            default:
                final View finalLocalView = localView;
                plusmaxhp.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {

                        if(!changePage){
                            i++;
                            nombreHistoria.setText(mData.get(position).getNombreHistoria());
                            hablar(manyDifferentStrings, finalLocalView, i, position);
                        }if(changePage){
                            TtS.speak("Por favor, vuelve a presionar el botón play!", TextToSpeech.QUEUE_FLUSH, null);

                            changePage = false;
                            i = 0;
                            j = 0;
                            isStopped = true;
//                            TtS.stop();

                        }


                    }
                });
                break;

        }



        ((ViewPager) collection).addView(localView, position);



        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);







        return localView;

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

    private void hablar(String  manyDifferentStrings, View v, final int p, int position) {


        final TextView maxhpdisplay = (TextView) v.findViewById(R.id.tvDescripcionHistoria);
        final Button plusmaxhp =  v.findViewById(R.id.play);


        switch (position){

            default:


                if(!isStopped){


                }else{
                    isStopped = false;
                    mHandler.removeCallbacksAndMessages(null);
                }


                ArrayList<String> texto = new ArrayList<>();
                Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
                Matcher reMatcher = re.matcher(manyDifferentStrings);
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


                maxhpdisplay.setText("");
                j= 0;
                mHandler = new Handler();


                if(p<=l.size()) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(!changePage){
                                maxhpdisplay.append(l.get(p-1)[j]+" ");
                                maxhpdisplay.setTextColor(rgb(255, 192, 0));
                                plusmaxhp.setVisibility(View.GONE);
                                j++;
                                if(j < l.get(p-1).length) {
                                    mHandler.postDelayed(this, 400);
                                }else{
                                    plusmaxhp.setVisibility(View.VISIBLE);
                                }
                            }if(changePage){

                                l = new ArrayList<>();
                                j = 0;
                                plusmaxhp.setVisibility(View.VISIBLE);


                            }


                        }
                    });

                    TtS.speak(texto.get(p-1), TextToSpeech.QUEUE_FLUSH, null);
                }
                else{

                    i = 0;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(!changePage){
                                maxhpdisplay.append(l.get(0)[j]+" ");
                                maxhpdisplay.setTextColor(rgb(255, 192, 0));
                                plusmaxhp.setVisibility(View.GONE);
                                j++;
                                if(j < l.get(0).length) {
                                    mHandler.postDelayed(this, 400);
                                }else{
                                    plusmaxhp.setVisibility(View.VISIBLE);
                                }
                            }if(changePage){

                                l = new ArrayList<>();
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
