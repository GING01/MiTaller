package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Secuencia;

public class SecuenciaDAO extends SQLiteDB {
    public SecuenciaDAO(Context context) {
        super(context);
    }

    public void create(Secuencia secuencia){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_IMAGEN_SECUENCIA, secuencia.getImagenSecuencia());
        values.put(ConstanteDB.COLUMN_IMAGEN_ORDEN, secuencia.getOrdenImagenSecuencia());
        values.put(ConstanteDB.COLUMN_ID_HISTORIA_PK, secuencia.getIdHistoria());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_SECUENCIA,
                null,
                values);
    }

    public Cursor retrieve(long id){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ConstanteDB.TABLE_SECUENCIA + " WHERE "
                + ConstanteDB.COLUMN_ID_HISTORIA_PK + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        return c;
    }

    public void update(Secuencia secuencia){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_IMAGEN_SECUENCIA, secuencia.getImagenSecuencia());
        values.put(ConstanteDB.COLUMN_IMAGEN_ORDEN, secuencia.getOrdenImagenSecuencia());
        // Which row to update, based on the ID
        String selection = ConstanteDB.COLUMN_ID_SECUENCIA + " LIKE ?";
        String[] selectionArgs = { String.valueOf(secuencia.getIdSecuencia()) };

        int count = db.update(
                ConstanteDB.TABLE_SECUENCIA,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

        // Define 'where' part of query.
        String selection = ConstanteDB.COLUMN_ID_SECUENCIA + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(ConstanteDB.TABLE_SECUENCIA, selection, selectionArgs);
    }




}
