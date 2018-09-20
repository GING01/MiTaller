package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import mta.epn.ginghogam.com.mitaller.adaptadores.TallerListAdapter;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class TallerActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private RecyclerView recyclerTaller;


    private TallerListAdapter tallerListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private SQLiteDB sqLiteDB;
    private TallerDAO tallerDAO;
    private Button aceptar,cancelar;
    public boolean visible=false;
    ArrayList<Taller> seleccion= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taller);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        recyclerTaller = (RecyclerView) findViewById(R.id.recyclerTalleres);


        linearLayoutManager = new LinearLayoutManager(this);
        tallerListAdapter = new TallerListAdapter(this);
        tallerListAdapter.setOnItemClickListener(this);

        recyclerTaller.setLayoutManager(linearLayoutManager);
        recyclerTaller.setAdapter(tallerListAdapter);
        aceptar=findViewById(R.id.aceptar);
        cancelar =findViewById(R.id.cancelar);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){
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
            }while (cursor.moveToNext());
        }

        tallerListAdapter.clear();
        tallerListAdapter.addAll(tallerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_taller, menu);
        getSupportActionBar().setCustomView(R.layout.menu_talleres_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nuevoTaller) {
            EdicionTallerActivity.start(TallerActivity.this);
        return true;
    }
        if (id==R.id.borrar){
            visible=true;
            tallerListAdapter.notifyDataSetChanged();
            aceptar.setVisibility(View.VISIBLE);
            cancelar.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position, View view) {
        EdicionTallerActivity.start(this, tallerListAdapter.getItem(position));
    }

    public void aceptar(View view) {
        borrarElementos();
        recreate();
    }

    public void cancelar(View view) {
        visible=false;
        tallerListAdapter.notifyDataSetChanged();
        aceptar.setVisibility(View.GONE);
        cancelar.setVisibility(View.GONE);
    }

    public void prepararSeleccion(View view, int position){
        if(((CheckBox)view).isChecked()){
            seleccion.add(tallerListAdapter.getItem(position));
        }
        else{
            seleccion.remove(tallerListAdapter.getItem(position));
        }

    }
    public void borrarElementos(){
        sqLiteDB = new SQLiteDB(this);
        tallerDAO = new TallerDAO(this);
        for(int i=0; i<seleccion.size();i++){
            tallerDAO.delete((Integer)seleccion.get(i).getIdTaller());
        }


    }
    public void llamarmenu(View view){
        finish();
    }

}