package cn.rejiejay.application.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * 通过 SQLiteOpenHelper 自行管理数据库表目录和结构
 *
 * 生成 helper 对象，可以打开数据库文件。文件名可以是相对路径或绝对路径
 * DBOpenHelper dataAccessObject = new DataAccessObject(this, "test.db", null, 1);
 *
 * 用读写的方式打开数据库文件
 * SQLiteDatabase database = dbHelper.getWritableDatabase();
 *
 * https://juejin.im/post/5c52a5b36fb9a049a5715424#heading-5
 */
public class DataAccessObject extends SQLiteOpenHelper {
    // 相当于 SQLiteDatabase openDatabase(String, CursorFactory)
    public DataAccessObject(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

//    // 相当于 SQLiteDatabase openDatabase(String, CursorFactory, DatabaseErrorHandler)
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public DataAccessObject(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

//    // 相当于 SQLiteDatabase openDatabase(String , OpenParams);
//    @TargetApi(Build.VERSION_CODES.P)
//    public DataAccessObject(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
//        super(context, name, version, openParams);
//    }

    // 创建数据文件时调用，此时适合创建新表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO 创建数据库后，对数据库的操作
    }

    // 更新数据库版本时调用，适合更新表结构或创建新表
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO 更改数据库版本的操作
    }
}
