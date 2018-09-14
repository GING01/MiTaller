package mta.epn.ginghogam.com.mitaller.adaptadores.viewpager;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.activities.EntrenamientoVocabularioActivity;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;


public class CardPagerAdapterHistoria extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Taller> mData;
    private float mBaseElevation;

    private Tutor tutor;
    private Estudiante estudiante;


    public CardPagerAdapterHistoria() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(Taller item, Estudiante estudiante, Tutor tutor) {
        mViews.add(null);
        mData.add(item);

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
        bind(mData.get(position),estudiante, tutor, view);
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

    private void bind(final Taller item, final Estudiante estudiante, final Tutor tutor, final View view) {

        ImageView image = (ImageView) view.findViewById(R.id.imgPageTaller);
        Button taller = view.findViewById(R.id.irTaller);
        taller.setText(item.getNombreTaller());
        image.setImageBitmap(BitmapFactory.decodeFile(item.getImagenTaller()));


        Button fab = (Button) view.findViewById(R.id.irTaller);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(view.getContext(), item, estudiante, tutor);
            }
        });


    }

    public static void startActivity(Context context, Taller taller, Estudiante estudiante, Tutor tutor) {
        Intent intent = new Intent(context, EntrenamientoVocabularioActivity.class);
        intent.putExtra("taller", taller);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("tutor", tutor);
        context.startActivity(intent);
    }



}

