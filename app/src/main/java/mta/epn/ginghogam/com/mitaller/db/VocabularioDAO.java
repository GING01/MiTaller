package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;

public class VocabularioDAO extends SQLiteDB {
    public VocabularioDAO(Context context) {
        super(context);
    }
    public void create(Vocabulario vocabulario){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_PALABRA, vocabulario.getPalabra());
        values.put(ConstanteDB.COLUMN_IMAGEN_PALABRA, vocabulario.getImagenPalabra());
        values.put(ConstanteDB.COLUMN_SONIDO_PALABRA, vocabulario.getSonidoPalabra());
        values.put(ConstanteDB.COLUMN_TIPO_PALABRA, vocabulario.getTipoPalabra());
        values.put(ConstanteDB.COLUMN_ID_TALLER_PK_VOCABULARIO, vocabulario.getIdTaller());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_VOCABULARIO,
                null,
                values);
    }

    public Cursor retrieve(long id){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ConstanteDB.TABLE_VOCABULARIO + " WHERE "
                + ConstanteDB.COLUMN_ID_TALLER_PK_VOCABULARIO + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        return c;
    }

    public void update(Vocabulario vocabulario){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_PALABRA, vocabulario.getPalabra());
        values.put(ConstanteDB.COLUMN_IMAGEN_PALABRA, vocabulario.getImagenPalabra());
        values.put(ConstanteDB.COLUMN_SONIDO_PALABRA, vocabulario.getSonidoPalabra());
        values.put(ConstanteDB.COLUMN_TIPO_PALABRA, vocabulario.getTipoPalabra());

        // Which row to update, based on the ID
        String selection = ConstanteDB.COLUMN_ID_PALABRA + " LIKE ?";
        String[] selectionArgs = { String.valueOf(vocabulario.getIdpalabra()) };

        int count = db.update(
                ConstanteDB.TABLE_VOCABULARIO,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

    }
}
