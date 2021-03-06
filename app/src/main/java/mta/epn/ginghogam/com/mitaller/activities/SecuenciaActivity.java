package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        recyclerHistoria = (RecyclerView) findViewById(R.id.recyclerSecuencias);

        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");

//        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);

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

    void loadData() {
        sqLiteDB = new SQLiteDB(this);
        historiaDAO = new HistoriaDAO(this);

        List<Historia> histariaList = new ArrayList<>();

        if (taller != null) {
            long params = taller.getIdTaller();
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
                } while (cursor.moveToNext());
            }

            cursor.close();

            historiaDAO.close();
        } else {
            Toast.makeText(this, "No hay registros realizados", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_secuencia, menu);
        getSupportActionBar().setCustomView(R.layout.menu_secuencia_titulo);
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
    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}



