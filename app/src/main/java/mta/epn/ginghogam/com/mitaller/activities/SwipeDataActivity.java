package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;

import mta.epn.ginghogam.com.mitaller.adaptadores.viewpager.CardPagerAdapterS;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.utilidades.ShadowTransformer;

public class SwipeDataActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private CardPagerAdapterS mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;


    Context context;
    Button button;

    private SQLiteDB sqLiteDB;
    private TallerDAO tallerDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_data);


        context = this;

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapterS();

      /*  mCardAdapter.addCardItemS(new CardItemString( "Panaderia", "Aqui se hace pan"));
        mCardAdapter.addCardItemS(new CardItemString( "Tarjeteria", "Aqui se hace pan"));
        mCardAdapter.addCardItemS(new CardItemString( "Carpinteria", "Aqui se hace pan"));
        mCardAdapter.addCardItemS(new CardItemString( "Mecanica", "Aqui se hace pan"));
        mCardAdapter.addCardItemS(new CardItemString( "Panaderia", "Aqui se hace pan"));

*/

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
                mCardAdapter.addCardItemS(taller);
            }while (cursor.moveToNext());
        }





        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

    }
}
