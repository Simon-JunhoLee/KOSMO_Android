package com.example.ex03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressHelper extends SQLiteOpenHelper {
    public AddressHelper(@Nullable Context context) {
        super(context, "address.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table address(_id integer primary key autoincrement,name text,phone text,juso text,photo text)");
        db.execSQL("insert into address values(null, '홍길동', '010-1010-1010', '인천 서구 서곶로120 루원시티 포레나', '')");
        db.execSQL("insert into address values(null, '강감찬', '010-2020-2020', '서울 강남구 압구정동 현대아파트', '')");
        db.execSQL("insert into address values(null, '심청이', '010-3030-3030', '인천 부평구 계산동 자이아파트', '')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}