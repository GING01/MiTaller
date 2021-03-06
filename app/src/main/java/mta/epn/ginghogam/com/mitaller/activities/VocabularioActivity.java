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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.PalabraListAdapter;
import mta.epn.ginghogam.com.mitaller.adaptadores.TallerListAdapter;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.db.VocabularioDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class VocabularioActivity extends AppCompatActivity implements RecyclerItemClickListener {


    private RecyclerView recyclerPalabra;


    private PalabraListAdapter palabraListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private SQLiteDB sqLiteDB;
    private VocabularioDAO vocabularioDAO;
    private Taller taller;
    public boolean visible = false;
    ArrayList<Vocabulario> seleccion = new ArrayList<>();
    ImageView fondosinregistro;


    //Menu
    MenuItem cancelarMenu;
    MenuItem ok;
    MenuItem nuevo;
    MenuItem borrar;
    boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");

        recyclerPalabra = (RecyclerView) findViewById(R.id.recyclerVocabulario);
        fondosinregistro=findViewById(R.id.sinregistros);


//        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);

        palabraListAdapter = new PalabraListAdapter(this);
        palabraListAdapter.setOnItemClickListener(this);


        recyclerPalabra.setLayoutManager(linearLayoutManager);
        recyclerPalabra.setAdapter(palabraListAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData() {

        vocabularioDAO = new VocabularioDAO(this);

        List<Vocabulario> vocabularioList = new ArrayList<>();
        long params = taller.getIdTaller();
        Cursor cursor = vocabularioDAO.retrieve(params);

        Vocabulario vocabulario;

        if (cursor.moveToFirst()) {
            do {

                vocabulario = new Vocabulario();

                vocabulario.setIdpalabra(cursor.getInt(0));
                vocabulario.setPalabra(cursor.getString(1));
                vocabulario.setImagenPalabra(cursor.getString(2));
                vocabulario.setSonidoPalabra(cursor.getString(3));
                vocabulario.setTipoPalabra(cursor.getString(4));
                vocabulario.setIdTaller(cursor.getInt(5));
                vocabularioList.add(vocabulario);


            } while (cursor.moveToNext());
            fondosinregistro.setVisibility(View.GONE);
        }
        else {
            fondosinregistro.setVisibility(View.VISIBLE);
        }

        cursor.close();
        vocabularioDAO.close();

        palabraListAdapter.clear();
        palabraListAdapter.addAll(vocabularioList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vocabulario, menu);
        getSupportActionBar().setCustomView(R.layout.menu_vocabulario_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cancelarMenu = menu.findItem(R.id.accion_cancelar);
        ok = menu.findItem(R.id.accion_ok);
        nuevo = menu.findItem(R.id.nuevaPalabra);
        borrar = menu.findItem(R.id.borrar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nuevaPalabra) {
            EdicionPalabraActivity.startP(VocabularioActivity.this, taller);
            return true;
        }
        if (id == R.id.borrar) {
            visible = true;
            palabraListAdapter.notifyDataSetChanged();
            nuevo.setVisible(false);
            borrar.setVisible(false);
            ok.setVisible(true);
            cancelarMenu.setVisible(true);

            isClicked = true;

        }
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        if (taller != null && !isClicked) {
            EdicionPalabraActivity.startP(this, palabraListAdapter.getItem(position), taller);
        }
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
        palabraListAdapter.notifyDataSetChanged();
        menu();
        visible = false;
        isClicked = false;
    }

    private void menu() {
        nuevo.setVisible(true);
        borrar.setVisible(true);
        ok.setVisible(false);
        cancelarMenu.setVisible(false);
    }

    public void prepararSeleccion(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            seleccion.add(palabraListAdapter.getItem(position));
        } else {
            seleccion.remove(palabraListAdapter.getItem(position));
        }

    }

    public void borrarElementos() {
        sqLiteDB = new SQLiteDB(this);
        vocabularioDAO = new VocabularioDAO(this);
        for (int i = 0; i < seleccion.size(); i++) {
            vocabularioDAO.delete((Integer) seleccion.get(i).getIdpalabra());
        }


    }

    public void llamarmenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}