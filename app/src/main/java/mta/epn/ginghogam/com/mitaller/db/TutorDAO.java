package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class TutorDAO extends SQLiteDB {
    public TutorDAO(Context context) {
        super(context);
    }
    public void create(Tutor tutor){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE_TUTOR, tutor.getNombreTutor());
        values.put(ConstanteDB.COLUMN_APELLIDO_TUTOR, tutor.getApellidoTutor());
        values.put(ConstanteDB.COLUMN_CI_TUTOR, tutor.getCiTutor());
        values.put(ConstanteDB.COLUMN_USUARIO_TUTOR, tutor.getUsuarioTutor());
        values.put(ConstanteDB.COLUMN_CONTRASEÑA_TUTOR, tutor.getContraseñaTutor());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_TUTOR,
                null,
                values);
    }

    public Cursor retrieve(String usuario, String contraseña){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String selectQuery ="SELECT * FROM "+ConstanteDB.TABLE_TUTOR+" WHERE "
                + ConstanteDB.COLUMN_USUARIO_TUTOR+ " = " + usuario + " AND " + ConstanteDB.COLUMN_CONTRASEÑA_TUTOR+" = "+contraseña;
        Cursor c = db.rawQuery(selectQuery,null);
        return c;
    }

    public Cursor retrieveUsuario(String usuario){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String selectQuery ="SELECT * FROM "+ConstanteDB.TABLE_TUTOR+" WHERE "
                + ConstanteDB.COLUMN_USUARIO_TUTOR+ " = " + usuario;
        Cursor c = db.rawQuery(selectQuery,null);
        return c;
    }

    public Cursor retrieveCedula(String cedula){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery ="SELECT * FROM "+ConstanteDB.TABLE_TUTOR+" WHERE "
                + ConstanteDB.COLUMN_CI_TUTOR+ " = " + cedula;
        Cursor c = db.rawQuery(selectQuery,null);
        return c;
    }

    public void update(Tutor tutor){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE_TUTOR, tutor.getNombreTutor());
        values.put(ConstanteDB.COLUMN_APELLIDO_TUTOR, tutor.getApellidoTutor());
        values.put(ConstanteDB.COLUMN_CI_TUTOR, tutor.getCiTutor());
        values.put(ConstanteDB.COLUMN_USUARIO_TUTOR, tutor.getUsuarioTutor());
        values.put(ConstanteDB.COLUMN_CONTRASEÑA_TUTOR, tutor.getContraseñaTutor());

        // Which row to update, based on the ID
        String selection = ConstanteDB.COLUMN_CI_TUTOR + " LIKE ?";
        String[] selectionArgs = { String.valueOf(tutor.getCiTutor()) };

        int count = db.update(
                ConstanteDB.TABLE_TUTOR,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

        // Define 'where' part of query.
        String selection = ConstanteDB.COLUMN_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(ConstanteDB.TABLE_TUTOR, selection, selectionArgs);
    }
}
