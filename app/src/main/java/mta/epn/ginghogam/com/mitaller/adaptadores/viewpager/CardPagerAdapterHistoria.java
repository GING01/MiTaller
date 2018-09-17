package mta.epn.ginghogam.com.mitaller.adaptadores.viewpager;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
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
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

import static android.graphics.Color.rgb;


public class CardPagerAdapterHistoria extends PagerAdapter implements CardAdapter, TextToSpeech.OnInitListener {

    private List<CardView> mViews;
    private List<Historia> mData;
    private float mBaseElevation;

    private Taller taller;
    private Tutor tutor;
    private Estudiante estudiante;



    //variable karaoke
    private TextToSpeech TtS;
    private int j;
    int i=0;
    String[] s = {};
    String manyDifferentStrings;
    String[] resul;
    List myList;
    ArrayList<String[]> l;


    Button play;
    TextView descripcionHistoria;
    ImageView siguiente;


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
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter_historia, container, false);
        container.addView(view);
        bind(mData.get(position),estudiante, tutor, taller, view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(final Historia item, final Estudiante estudiante, final Tutor tutor, final Taller taller, final View view) {

        TtS = new TextToSpeech(view.getContext(), this);

        manyDifferentStrings = item.getDescripcionHistoria();

        descripcionHistoria =  (TextView) view.findViewById(R.id.tvDescripcionHistoria);
        descripcionHistoria.setText(item.getDescripcionHistoria());
        final TextView nombreHistoria = (TextView) view.findViewById(R.id.tvNombreHistoria);
        nombreHistoria.setText(item.getNombreHistoria());


        ImageView image = (ImageView) view.findViewById(R.id.ivHistoria);
        image.setImageBitmap(BitmapFactory.decodeFile(item.getImagenHistoria()));

        Button irJuego = (Button) view.findViewById(R.id.irJuego);
        irJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(view.getContext(), item, estudiante, tutor, taller);
            }
        });

        play = (Button) view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),"Aqui va el karaoke", Toast.LENGTH_LONG).show();
            }
        });
        siguiente = (ImageView) view.findViewById(R.id.siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hablar();
                i++;

            }
        });

    }

    public static void startActivity(Context context, Historia historia, Estudiante estudiante, Tutor tutor, Taller taller) {
        Intent intent = new Intent(context, EntrenamientoVocabularioActivity.class);
        intent.putExtra("historia", historia);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("tutor", tutor);
        intent.putExtra("taller", taller);
        context.startActivity(intent);
    }

    private void hablar() {

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

        descripcionHistoria.setText("");
        j= 0;
        final Handler mHandler = new Handler();
        if(i<l.size()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    descripcionHistoria.append(l.get(i-1)[j]+" ");
                    descripcionHistoria.setTextColor(rgb(255, 192, 0));
                    siguiente.setVisibility(View.GONE);
                    j++;
                    if(j < l.get(i-1).length) {
                        mHandler.postDelayed(this, 400);
                    }else{
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            });
            TtS.speak(texto.get(i), TextToSpeech.QUEUE_FLUSH, null);
        }else{
            i = 0;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    descripcionHistoria.append(l.get(0)[j]+" ");
                    descripcionHistoria.setTextColor(rgb(255, 192, 0));
                    siguiente.setVisibility(View.GONE);
                    j++;
                    if(j < l.get(0).length) {
                        mHandler.postDelayed(this, 400);
                    }else{
                        siguiente.setVisibility(View.VISIBLE);
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
                hablar();
            } else {
            }
        } else {
        }
    }

}

