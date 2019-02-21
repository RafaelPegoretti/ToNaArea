package br.com.hbsis.tonaarea.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.hbsis.tonaarea.util.Constants;

public class DataOpenHelper extends SQLiteOpenHelper {

    public DataOpenHelper(Context context){
        super(context, Constants.DATABASE.DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptHelper.createTableClient());
        db.execSQL(ScriptHelper.createTableProduct());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
