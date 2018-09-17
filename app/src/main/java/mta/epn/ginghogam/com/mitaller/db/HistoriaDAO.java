package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;

public class HistoriaDAO extends SQLiteDB  {
    public HistoriaDAO(Context context) {
        super(context);
    }
    public void create(Historia historia){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE_HISTORIA, historia.getNombreHistoria());
        values.put(ConstanteDB.COLUMN_DESCRIPCION_HISTORIA, historia.getDescripcionHistoria());
        values.put(ConstanteDB.COLUMN_IMAGEN_HISTORIA, historia.getImagenHistoria());
        values.put(ConstanteDB.COLUMN_NUM_LAMINAS, historia.getNumeroLaminas());
        values.put(ConstanteDB.COLUMN_DIFICULTAD, historia.getDificultad());
        values.put(ConstanteDB.COLUMN_ID_TALLER, historia.getIdTaller());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_HISTORIA,
                null,
                values);
    }

    public Cursor retrieve(long id){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ConstanteDB.TABLE_HISTORIA + " WHERE "
                + ConstanteDB.COLUMN_ID_TALLER + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        return c;
    }

    public Cursor retrieveWithDificult(long id, String dificultad){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ConstanteDB.TABLE_HISTORIA + " WHERE "
                + ConstanteDB.COLUMN_ID_TALLER + " = " + id +" AND " + ConstanteDB.COLUMN_DIFICULTAD +
                " = " + dificultad;

        Cursor c = db.rawQuery(selectQuery, null);

        return c;
    }


    public void update(Historia historia){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE_HISTORIA, historia.getNombreHistoria());
        values.put(ConstanteDB.COLUMN_DESCRIPCION_HISTORIA, historia.getDescripcionHistoria());
        values.put(ConstanteDB.COLUMN_IMAGEN_HISTORIA, historia.getImagenHistoria());
        values.put(ConstanteDB.COLUMN_NUM_LAMINAS, historia.getNumeroLaminas());
        values.put(ConstanteDB.COLUMN_DIFICULTAD, historia.getDificultad());

        // Which row to update, based on the ID
        String selection = ConstanteDB.COLUMN_ID_HISTORIA + " LIKE ?";
        String[] selectionArgs = { String.valueOf(historia.getIdHistoria()) };

        int count = db.update(
                ConstanteDB.TABLE_HISTORIA,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

        // Define 'where' part of query.
        String selection = ConstanteDB.COLUMN_ID_HISTORIA + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(ConstanteDB.TABLE_HISTORIA, selection, selectionArgs);
    }

}

