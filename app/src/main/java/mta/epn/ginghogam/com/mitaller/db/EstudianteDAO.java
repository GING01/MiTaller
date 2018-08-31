package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;

public class EstudianteDAO extends SQLiteDB {
    public EstudianteDAO(Context context) {
        super(context);
    }
    public void create(Estudiante estudiante){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE_ESTUDIANTE, estudiante.getNombreEstudiate());
        values.put(ConstanteDB.COLUMN_APELLIDO_ESTUDIANTE, estudiante.getApellidoEstudiante());
        values.put(ConstanteDB.COLUMN_EDAD_ESTUDIANTE, estudiante.getEdadEstudiante());
        values.put(ConstanteDB.COLUMN_FOTO_ESTUDIANTE, estudiante.getFotoEstudiante());
        values.put(ConstanteDB.COLUMN_GENERO_ESTUDIANTE, estudiante.getGeneroEstudiante());
        values.put(ConstanteDB.COLUMN_PERFIL_ESTUDIANTE, estudiante.getPerfilEstudiante());
        values.put(ConstanteDB.COLUMN_ID_TUTOR_PK, estudiante.getIdTutor());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_ESTUDIANTE,
                null,
                values);
    }

    public Cursor retrieve(long id){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ConstanteDB.TABLE_ESTUDIANTE + " WHERE "
                + ConstanteDB.COLUMN_ID_TUTOR_PK + " = " + id + " ORDER BY " + ConstanteDB.COLUMN_NOMBRE_ESTUDIANTE;

        Cursor c = db.rawQuery(selectQuery, null);

        return c;
    }

    public void update(Estudiante estudiante){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE_ESTUDIANTE, estudiante.getNombreEstudiate());
        values.put(ConstanteDB.COLUMN_APELLIDO_ESTUDIANTE, estudiante.getApellidoEstudiante());
        values.put(ConstanteDB.COLUMN_FOTO_ESTUDIANTE, estudiante.getFotoEstudiante());
        values.put(ConstanteDB.COLUMN_EDAD_ESTUDIANTE, estudiante.getEdadEstudiante());
        values.put(ConstanteDB.COLUMN_PERFIL_ESTUDIANTE, estudiante.getFotoEstudiante());
        values.put(ConstanteDB.COLUMN_GENERO_ESTUDIANTE, estudiante.getFotoEstudiante());

        // Which row to update, based on the ID
        String selection = ConstanteDB.COLUMN_ID_ESTUDIANTE + " LIKE ?";
        String[] selectionArgs = { String.valueOf(estudiante.getIdEstudiante()) };

        int count = db.update(
                ConstanteDB.TABLE_ESTUDIANTE,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

        // Define 'where' part of query.
        String selection = ConstanteDB.COLUMN_ID_ESTUDIANTE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(ConstanteDB.TABLE_ESTUDIANTE, selection, selectionArgs);
    }
}
