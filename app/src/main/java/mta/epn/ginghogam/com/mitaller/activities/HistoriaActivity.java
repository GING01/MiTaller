package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.HistoriaListAdapter;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;


public class HistoriaActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private Taller taller;
    private RecyclerView recyclerHistoria;


    private HistoriaListAdapter historiaListAdapter;
    private LinearLayoutManager linearLayoutManager;


    private SQLiteDB sqLiteDB;
    private HistoriaDAO historiaDAO;
    private Button aceptar,cancelar;
    public boolean visible=false;
    ArrayList<Historia> seleccion= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");

        recyclerHistoria = (RecyclerView) findViewById(R.id.recyclerHistoria);


//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);

        historiaListAdapter = new HistoriaListAdapter(this);
        historiaListAdapter.setOnItemClickListener(this);
        aceptar=findViewById(R.id.aceptar);
        cancelar =findViewById(R.id.cancelar);

        recyclerHistoria.setLayoutManager(linearLayoutManager);
        recyclerHistoria.setAdapter(historiaListAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){

        historiaDAO = new HistoriaDAO(this);

        List<Historia> histariaList = new ArrayList<>();



            long params = taller.getIdTaller();

            Cursor cursor = historiaDAO.retrieve(params);
            Historia historia;

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


        historiaListAdapter.clear();
        historiaListAdapter.addAll(histariaList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historia, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nuevaHistoria) {
            EdicionHistoriaActivity.startH(HistoriaActivity.this, taller);
            return true;
        }
        if (id==R.id.borrar){
            visible=true;
            historiaListAdapter.notifyDataSetChanged();
            aceptar.setVisibility(View.VISIBLE);
            cancelar.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position, View view) {

        if(taller != null) {

            EdicionHistoriaActivity.startH(this, historiaListAdapter.getItem(position), taller);
        }
    }

    public void aceptar(View view) {
        borrarElementos();
        recreate();
    }

    public void cancelar(View view) {
        visible=false;
        historiaListAdapter.notifyDataSetChanged();
        aceptar.setVisibility(View.GONE);
        cancelar.setVisibility(View.GONE);
    }

    public void prepararSeleccion(View view, int position){
        if(((CheckBox)view).isChecked()){
            seleccion.add(historiaListAdapter.getItem(position));
        }
        else{
            seleccion.remove(historiaListAdapter.getItem(position));
        }

    }
    public void borrarElementos(){
        sqLiteDB = new SQLiteDB(this);
        historiaDAO = new HistoriaDAO(this);
        for(int i=0; i<seleccion.size();i++){
            historiaDAO.delete((Integer)seleccion.get(i).getIdHistoria());
        }


    }
}

