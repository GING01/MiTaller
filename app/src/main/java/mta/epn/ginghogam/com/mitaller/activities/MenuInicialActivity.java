package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class MenuInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button pagerAct;
    private Tutor tutor;
    private static final String preference = "mitaller.iniciosesion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pagerAct = (Button) findViewById(R.id.pager_act);


        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");

        SharedPreferences preferences = this.getSharedPreferences("mitaller.iniciosesion", Context.MODE_PRIVATE);

        tutor = new Tutor();


        tutor.setIdTutor(preferences.getInt("ID", 0));

        tutor.setNombreTutor(preferences.getString("nombre", null));
        tutor.setUsuarioTutor(preferences.getString("usuario", null));
        tutor.setContraseñaTutor(preferences.getString("contraseña", null));
        tutor.setCiTutor(preferences.getString("ci", null));




        if (tutor == null) {
            Toast.makeText(this, " PATO", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "a: " + tutor.getCiTutor(), Toast.LENGTH_LONG).show();
        }

        GradientDrawable border = new GradientDrawable();

        border.setColor(Color.RED); //white background
        border.setStroke(3, Color.BLACK); //black border with full opacity
        border.setCornerRadius(15);

        pagerAct.setBackground(border);

        pagerAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MenuInicialActivity.this, EleccionEstudianteEntrenamientoActivity.class);
                intent.putExtra("tutor", tutor);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicial, menu);
        getSupportActionBar().setCustomView(R.layout.menu_inicial_itulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.talleres) {
            Intent i = new Intent(MenuInicialActivity.this, TallerActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.estudiantes) {
            Intent i = new Intent(MenuInicialActivity.this, EstudiantesActivity.class);
            i.putExtra("tutor", tutor);
            startActivity(i);

        } else if (id == R.id.evaluacion) {
            Intent i = new Intent(MenuInicialActivity.this, EleccionEstudianteEstadisticaActivity.class);
            i.putExtra("tutor", tutor);
            startActivity(i);

        } else if (id == R.id.salir) {
            SharedPreferences preferences = this.getSharedPreferences("mitaller.iniciosesion", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            startActivity(new Intent(MenuInicialActivity.this, LoginActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
