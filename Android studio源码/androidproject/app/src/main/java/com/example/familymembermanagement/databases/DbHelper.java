package com.example.familymembermanagement.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 创建数据库
 */
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "family_member.db", null, 1);
    }

    /**
     * 创建表
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE data(id VARCHAR(20) NOT NULL PRIMARY KEY, ip VARCHAR(20) NOT NULL,port VARCHAR(10) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
