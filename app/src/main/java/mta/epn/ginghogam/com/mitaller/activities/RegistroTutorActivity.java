package mta.epn.ginghogam.com.mitaller.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.TutorDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.utilidades.ValidadorCedula;

public class RegistroTutorActivity extends AppCompatActivity {
    LoginActivity registro= new LoginActivity();
    TextView titulo, nombre, apellido, contraseña, contraseña2, usuario;
    public Boolean tutorEditar=false;
    EditText ci;
    Tutor tutors;
    Cursor cursor;
    Button aceptar;
    Tutor tutor;
    private TutorDAO tutorDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tutor);
        titulo=findViewById(R.id.Registro);
        ci=findViewById(R.id.txtCI);
        nombre=findViewById(R.id.txtNombre);
        apellido=findViewById(R.id.txtApellido);
        usuario=findViewById(R.id.txtUsuario);
        contraseña=findViewById(R.id.txtcontraseña);
        contraseña2=findViewById(R.id.txtcontraseña2);
        tutorEditar=(boolean)getIntent().getExtras().getBoolean("editar");
        aceptar=findViewById(R.id.btnAceptar);
        if(tutorEditar==true){
            nombre.setEnabled(false);
            apellido.setEnabled(false);
            usuario.setEnabled(false);
            contraseña.setEnabled(false);
            contraseña2.setEnabled(false);
            aceptar.setEnabled(false);

            titulo.setText("Cambiar Contraseña");
            ci=findViewById(R.id.txtCI);
            ci.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        Toast.makeText(RegistroTutorActivity.this, "Buscando", Toast.LENGTH_LONG).show();
                         buscarcedula();
                        if(buscarcedula())
                            llenarDatosTutor(tutor);
                        else
                            Toast.makeText(RegistroTutorActivity.this, "Cedula no valida", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(RegistroTutorActivity.this, "Ingrese su cedula para buscar", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else{
            titulo=findViewById(R.id.Registro);
            titulo.setText("Unete a nuestros talleres");
        }
    }

    private void llenarDatosTutor(Tutor tutor) {
        nombre.setEnabled(true);
        apellido.setEnabled(true);
        usuario.setEnabled(true);
        contraseña.setEnabled(true);
        contraseña2.setEnabled(true);
        nombre.setText(tutor.getNombreTutor());
        apellido.setText(tutor.getApellidoTutor());
        usuario.setText(tutor.getUsuarioTutor());
        contraseña.setText("");
        contraseña2.setText("");
        aceptar.setEnabled(true);

    }

    private boolean buscarcedula() {
        ValidadorCedula validadorCedula =new ValidadorCedula();
        if(validadorCedula.validadorDeCedula(ci.getText().toString())){
            tutorDAO =new TutorDAO(this);
            List<Tutor> tutorList = new ArrayList<>();
            try {
                String cedula = ci.getText().toString().trim();
                Cursor cursor = tutorDAO.retrieveCedula(cedula);
                cursor.moveToFirst();
                tutor = new Tutor();
                tutor.setNombreTutor(cursor.getString(1));
                tutor.setApellidoTutor(cursor.getString(2));
                tutor.setUsuarioTutor(cursor.getString(4));
                return true;
            }
            catch (Exception e){
                Toast.makeText(RegistroTutorActivity.this, "No se encontro la cedula", Toast.LENGTH_LONG).show();

                return false;
            }

        }
        else{
            Toast.makeText(RegistroTutorActivity.this, "Cedula no valida", Toast.LENGTH_LONG).show();
            return false;
        }


    }

    public void registrar(View view) {
        if(!tutorEditar){
            tutorDAO =new TutorDAO(this);
            if (buscarUsuario()){

                Toast.makeText(RegistroTutorActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
            }
            else {
                if(buscarcedula()){
                    Toast.makeText(RegistroTutorActivity.this, "Esta Ci ya existe", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        tutor = new Tutor();
                        tutor.setNombreTutor(nombre.getText().toString());
                        tutor.setApellidoTutor(apellido.getText().toString());
                        tutor.setCiTutor(ci.getText().toString());
                        tutor.setUsuarioTutor(usuario.getText().toString());
                        tutor.setContraseñaTutor(contraseña.getText().toString());
                        if (contraseña.getText().toString().equals(contraseña2.getText().toString())){

                            if(!nombre.getText().toString().equals("") && !apellido.getText().toString().equals("") && !ci.getText().toString().equals("") &&
                                    !usuario.getText().toString().equals("") &&
                                    !contraseña.getText().toString().equals("")){
                                tutorDAO.create(tutor);
                                finish();
                            }
                            else{
                                Toast.makeText(RegistroTutorActivity.this, "Los campos no estan completos revisa porfavor", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(RegistroTutorActivity.this, "Las contraseñas tienen que ser iguales", Toast.LENGTH_SHORT).show();

                        }


                    }
                    catch (Exception e)
                    {
                        Toast.makeText(RegistroTutorActivity.this, "No se pudo registrar", Toast.LENGTH_LONG).show();

                    }

                }
            }

        }
        else{
            tutorDAO =new TutorDAO(this);
            if (buscarUsuario()){
                Toast.makeText(RegistroTutorActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
            }

            else {

                try {
                    tutor = new Tutor();
                    tutor.setNombreTutor(nombre.getText().toString());
                    tutor.setApellidoTutor(apellido.getText().toString());
                    tutor.setCiTutor(ci.getText().toString());
                    tutor.setUsuarioTutor(usuario.getText().toString());
                    tutor.setContraseñaTutor(contraseña.getText().toString());
                    if (contraseña.getText().toString().equals(contraseña2.getText().toString())){

                        if(!nombre.getText().toString().equals("") && !apellido.getText().toString().equals("") && !ci.getText().toString().equals("") &&
                                !usuario.getText().toString().equals("") &&
                                !contraseña.getText().toString().equals("")){
                            tutorDAO.update(tutor);
                            finish();
                        }
                        else{
                            Toast.makeText(RegistroTutorActivity.this, "Los campos no estan completos revisa porfavor", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(RegistroTutorActivity.this, "Las contraseñas tienen que ser iguales", Toast.LENGTH_SHORT).show();

                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(RegistroTutorActivity.this, "No se pudo actualizar", Toast.LENGTH_LONG).show();

                }

            }
        }


    }

    public void cancelar(View view) {
        finish();
    }
    public boolean buscarUsuario(){

        String usr= "'"+usuario.getText().toString().trim()+"'";
        tutorDAO =new TutorDAO(this);

        try {
            cursor=tutorDAO.retrieveUsuario(usr);
            cursor.moveToFirst();
            cursor.getString(0);
            return true;
        }
        catch (Exception e){
            Toast.makeText(RegistroTutorActivity.this, "No se encontro el usuario", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportActionBar().setCustomView(R.layout.titulo_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

        return true;
    }
}
