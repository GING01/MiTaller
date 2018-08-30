package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.SecuenciaListAdapter;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class SecuenciaActivity extends AppCompatActivity implements RecyclerItemClickListener {


    private Historia historia;
    private Taller taller;
    private RecyclerView recyclerHistoria;


    private SecuenciaListAdapter historiaListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private SQLiteDB sqLiteDB;
    private HistoriaDAO historiaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuencia);

        recyclerHistoria = (RecyclerView) findViewById(R.id.recyclerSecuencias);

        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");

        linearLayoutManager = new LinearLayoutManager(this);
        historiaListAdapter = new SecuenciaListAdapter(this);
        historiaListAdapter.setOnItemClickListener(this);

        recyclerHistoria.setLayoutManager(linearLayoutManager);
        recyclerHistoria.setAdapter(historiaListAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){
        sqLiteDB = new SQLiteDB(this);
        historiaDAO = new HistoriaDAO(this);

        List<Historia> histariaList = new ArrayList<>();

        if(taller != null){
            long  params = taller.getIdTaller();
            Cursor cursor = historiaDAO.retrieve(params);
            if (cursor.moveToFirst()) {
                do {

                    historia = new Historia();

                    historia.setIdHistoria(cursor.getInt(0));
                    historia.setNombreHistoria(cursor.getString(1));
                    historia.setDescripcionHistoria(cursor.getString(2));
                    historia.setImagenHistoria(cursor.getString(3));
                    historia.setNumeroLaminas(cursor.getString(4));
                    historia.setDificultad(cursor.getString(5));
                    historia.setIdTaller(cursor.getInt(6));
                    histariaList.add(historia);
                }while (cursor.moveToNext());
            }
        }else{
            Toast.makeText(this,"No hay registros realizados", Toast.LENGTH_LONG).show();
        }


        Historia historia;



        historiaListAdapter.clear();
        historiaListAdapter.addAll(histariaList);
    }
    
    @Override
    public void onItemClick(int position, View view) {
        EdicionSecuenciaActivity.startSecuen(this, historiaListAdapter.getItem(position));
      //  Toast.makeText(this,""+historiaListAdapter.getItem(position).getIdHistoria(),Toast.LENGTH_LONG).show();

    }
}



