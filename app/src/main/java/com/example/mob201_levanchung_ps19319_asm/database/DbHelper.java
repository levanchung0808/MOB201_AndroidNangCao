package com.example.mob201_levanchung_ps19319_asm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mob201_levanchung_ps19319_asm.model.KhoaHoc;
import com.example.mob201_levanchung_ps19319_asm.model.SinhVien;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    SQLiteDatabase db = getReadableDatabase();

    public DbHelper(Context context) {
        super(context, "PS19319_DB_HoTroHocTap", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SINHVIEN
        String sql = "CREATE TABLE SINHVIEN (username text PRIMARY KEY, password text)";
        db.execSQL(sql);
        sql = "INSERT INTO SINHVIEN VALUES ('sv01','111')";
        db.execSQL(sql);
        sql = "INSERT INTO SINHVIEN VALUES ('sv02','222')";
        db.execSQL(sql);
        sql = "INSERT INTO SINHVIEN VALUES ('sv03','333')";
        db.execSQL(sql);

        //KHOAHOC
        String sql1 = "CREATE TABLE KHOAHOC (maKH integer PRIMARY KEY AUTOINCREMENT, tenKH text, lichHoc text, lichThi text)";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('Lập trình Java 1','Ca 2 - Thứ 3,5,7','22-02-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('Lập trình Java 2','Ca 4 - Thứ 2,4,6','14-04-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('Android cơ bản','Ca 5 - Thứ 7,CN','15-05-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('Android nâng cao','Ca 2 - Thứ 3,5','20-05-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('CTDL & GT','Ca 6 - Thứ 2,3','16-05-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('HTML & CSS3','Ca 3 - Thứ 6,7','17-05-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('Tin học cơ bản','Ca 5 - Thứ 5','22-05-2022')";
        db.execSQL(sql1);
        sql1 = "INSERT INTO KHOAHOC ('tenKH','lichHoc','lichThi') VALUES ('AV 2.2','Ca 3 - Thứ 7,CN','26-05-2022')";
        db.execSQL(sql1);

        //THONGTINDANGKY
        String sql2 = "CREATE TABLE THONGTINDANGKY (id integer PRIMARY KEY AUTOINCREMENT, username text references SINHVIEN(username), maKH integer references KHOAHOC(maKH))";
        db.execSQL(sql2);
        sql2 = "INSERT INTO THONGTINDANGKY('username','maKH') VALUES ('sv01',1)";
        db.execSQL(sql2);
        sql2 = "INSERT INTO THONGTINDANGKY('username','maKH') VALUES ('sv02',2)";
        db.execSQL(sql2);
        sql2 = "INSERT INTO THONGTINDANGKY('username','maKH') VALUES ('sv03',3)";
        db.execSQL(sql2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS SINHVIEN";
        db.execSQL(sql);
        String sql1 = "DROP TABLE IF EXISTS KHOAHOC";
        db.execSQL(sql1);
        String sql2 = "DROP TABLE IF EXISTS THONGTINDANGKY";
        db.execSQL(sql2);
        onCreate(db);
    }

    //lấy danh sách khóa học
    public ArrayList<KhoaHoc> getAllCourse(){
        Cursor cs = db.rawQuery("SELECT * FROM KHOAHOC",null);
        ArrayList<KhoaHoc> arr = new ArrayList<>();
        if(cs.getCount() != 0){
            cs.moveToFirst();
            do{
                arr.add(new KhoaHoc(cs.getInt(0),cs.getString(1),cs.getString(2),cs.getString(3)));
            }while (cs.moveToNext());
        }
        return arr;
    }

    //Đăng ký khóa học mới
    public void registerCourse(String username, int maKH) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("maKH", maKH);
        db.insert("THONGTINDANGKY", null, values);
    }

    //Hủy khóa học đã đăng ký
    public void unRegisterCourse(String maSV, int maKH){
        db.delete("THONGTINDANGKY", "username=? AND maKH=?",new String[]{maSV,String.valueOf(maKH)});
    }

    //xem danh sách khóa học & lịch thi đã đăng ký
    public ArrayList<KhoaHoc> getAllCourseRegister(String username){
        Cursor cs = db.rawQuery("SELECT * FROM KHOAHOC kh,THONGTINDANGKY tt WHERE kh.maKH = tt.maKH AND tt.username LIKE '" + username + "'",null);
        ArrayList<KhoaHoc> arr = new ArrayList<>();
        if(cs.getCount() != 0){
            cs.moveToFirst();
            do{
                arr.add(new KhoaHoc(cs.getInt(0),cs.getString(1),cs.getString(2),cs.getString(3)));
            }while (cs.moveToNext());
        }
        return arr;
    }

    //check login
    public boolean checkLogin(SinhVien sv){
        String sql = "SELECT * FROM SINHVIEN WHERE username=? AND password=?";
        Cursor cs = db.rawQuery(sql,new String[]{sv.getUsername(), sv.getPassword()});
        return (cs.getCount() > 0);
    }

    public boolean insert(SinhVien sv) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",sv.getUsername());
        values.put("password",sv.getPassword());

        long row = db.insert("SINHVIEN",null,values);
        return (row>0);
    }
}
