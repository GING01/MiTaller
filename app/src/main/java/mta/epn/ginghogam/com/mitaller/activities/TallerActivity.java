package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

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
    private Button aceptar, cancelar;
    public boolean visible = false;
    ArrayList<Taller> seleccion = new ArrayList<>();
    ImageView fondosinregistro;

    //menu
    MenuItem menuItem;
    MenuItem cancelarMenu;
    MenuItem ok;
    MenuItem nuevo;
    MenuItem borrar;

    boolean isClicked = false;


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
        fondosinregistro=findViewById(R.id.sinregistros);


//        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);

        tallerListAdapter = new TallerListAdapter(this);
        tallerListAdapter.setOnItemClickListener(this);

        recyclerTaller.setLayoutManager(linearLayoutManager);
        recyclerTaller.setAdapter(tallerListAdapter);
        aceptar = findViewById(R.id.aceptar);
        cancelar = findViewById(R.id.cancelar);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData() {
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
            } while (cursor.moveToNext());
            fondosinregistro.setVisibility(View.GONE);
        }
        else {
            fondosinregistro.setVisibility(View.VISIBLE);
        }

        cursor.close();
        tallerDAO.close();

        tallerListAdapter.clear();
        tallerListAdapter.addAll(tallerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_taller, menu);
        getSupportActionBar().setCustomView(R.layout.menu_talleres_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        cancelarMenu = menu.findItem(R.id.accion_cancelar);
        ok = menu.findItem(R.id.accion_ok);
        nuevo = menu.findItem(R.id.nuevoTaller);
        borrar = menu.findItem(R.id.borrar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nuevoTaller) {
            EdicionTallerActivity.start(TallerActivity.this);
            return true;
        }
        if (id == R.id.borrar) {
            visible = true;
            tallerListAdapter.notifyDataSetChanged();
            nuevo.setVisible(false);
            borrar.setVisible(false);
            ok.setVisible(true);
            cancelarMenu.setVisible(true);
            isClicked = true;
        }
        if (id == R.id.accion_ok) {
            aceptar(null);

        }
        if (id == R.id.accion_cancelar) {
            cancelar(null);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position, View view) {
        if (!isClicked) {
            EdicionTallerActivity.start(this, tallerListAdapter.getItem(position));
        }
    }

    private void menu() {
        nuevo.setVisible(true);
        borrar.setVisible(true);
        ok.setVisible(false);
        cancelarMenu.setVisible(false);
    }

    public void aceptar(View view) {
        borrarElementos();
        loadData();
        menu();
        isClicked = false;
        visible = false;
    }

    public void cancelar(View view) {
        visible = false;
        isClicked = false;
        tallerListAdapter.notifyDataSetChanged();
        menu();

    }

    public void prepararSeleccion(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            seleccion.add(tallerListAdapter.getItem(position));
        } else {
            seleccion.remove(tallerListAdapter.getItem(position));
        }

    }

    public void borrarElementos() {
        sqLiteDB = new SQLiteDB(this);
        tallerDAO = new TallerDAO(this);
        for (int i = 0; i < seleccion.size(); i++) {
            tallerDAO.delete((Integer) seleccion.get(i).getIdTaller());
        }


    }

    public void llamarmenu(View view) {
        finish();
    }

}