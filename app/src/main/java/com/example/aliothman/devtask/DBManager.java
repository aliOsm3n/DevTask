package com.example.aliothman.devtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class DBManager {
    private SQLiteDatabase sqldb;

    static final String DBName = "REPO";
    static final String TableName = "Reposotries";
    static final String Colownername = "Ownername";
    static final String Coldescreption = "Descreption";
    static final String ColReponame = "Reponame";
    static final String Colownerhtml = "Ownerhtml";
    static final String Colrepohtml = "Repohtml";
    static final String Colfork = "Fork";

    static final String ColID = "ID";
    static final int DBVersion = 4;


    static final String CreateTable =
            "CREATE TABLE IF NOT EXISTS " + TableName +
                    "(ID integer PRIMARY KEY AUTOINCREMENT ," +
                    Colownername + " TEXT ," +
                    Coldescreption + "TEXT ," +
                    ColReponame + "TEXT ," +
                    Colownerhtml + "TEXT ," +
                    Colrepohtml + "TEXT ," +
                    Colfork + " TEXT)";


    static class DATABASEHelperUser extends SQLiteOpenHelper {

        Context context;

        public DATABASEHelperUser(Context context) {
            super(context, TableName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
            Toast.makeText(context, "table created", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
            db.execSQL("DROP TABLE IF EXISTS " + TableName);
            onCreate(db);
        }
    }

    public DBManager(Context context) {
        DATABASEHelperUser databaseHelperUser = new DATABASEHelperUser(context);
        sqldb = databaseHelperUser.getWritableDatabase();

    }

    public long insert(ContentValues values) {
        long id = sqldb.insert(TableName, "", values);
        return id;
    }

    public Cursor query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TableName);
        Cursor cursor = queryBuilder.query(sqldb, Projection, Selection, SelectionArgs, null, null, SortOrder);
        return cursor;
    }


    public void ClearAll() {
        // sqldb.execSQL("delete from "+ TableName);
        sqldb.execSQL("DROP TABLE IF EXISTS " + TableName);
        sqldb.execSQL(CreateTable);
    }


}
