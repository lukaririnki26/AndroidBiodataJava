package net.fahmi.biodata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static  final String db_name = "db_biodata";
    public static  final String tb_name = "tb_biodata";

    public static  final String row_id = "_id";
    public static  final String row_nomor = "nomor";
    public static  final String row_nama = "nama";
    public static  final String row_jk= "jk";
    public static  final String row_tempat = "tempat";
    public static  final String row_tgl = "tgl";
    public static  final String row_alamat = "alamat";
    public static  final String row_img = "foto";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, db_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+tb_name+" ("+row_id+" integer primary key autoincrement," +
                row_nomor+" varchar,"+row_nama+" varchar,"+row_jk+" varchar,"+row_tempat+" varchar," +
                row_tgl+" varchar,"+row_alamat+" varchar, "+row_img+" varchar)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+tb_name);
    }
    public Cursor allData(){
        Cursor c = db.rawQuery("select * from "+tb_name,null);
        return  c;
    }
    public  Cursor oneData(long id){
        Cursor c = db.rawQuery("select * from "+tb_name+" where "+row_id+"="+id,null);
        return  c;
    }
    public void insertData(ContentValues values){
        db.insert(tb_name,null,values);
    }
    public void updateData(ContentValues values,long id){
        db.update(tb_name,values,row_id+"="+id,null);
    }
    public void deleteData(long id){
        db.delete(tb_name,row_id+"="+id,null);
    }
}
