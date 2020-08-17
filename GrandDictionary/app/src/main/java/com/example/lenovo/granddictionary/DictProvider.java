package com.example.lenovo.granddictionary;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
/**
 * Created by lenovo on 2020/6/4.
 */

public class DictProvider extends ContentProvider {
    private static final UriMatcher sUrimatcher;
    public static final String AUTHORITY = "com.example.lenovo.granddictionary.DictProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY );
    static {
        sUrimatcher = new UriMatcher((UriMatcher.NO_MATCH));
        sUrimatcher.addURI(AUTHORITY, "/dict", 1);
    }
    private DBOpenHandler dbOpenHandler;

    @Override
    public boolean onCreate()  {
        System.out.println("===onCreate is called===");
        dbOpenHandler = new DBOpenHandler(getContext(), "dbDict.db3", null, 1);
        return true;
    }

    @Override
    public String getType(Uri uri){    // 返回本ContentProvider所提供数据的MIME类型
        System.out.println("~~getType is called~~"+sh(uri));
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
        switch (sUrimatcher.match(uri)){
            case 1:
                queryBuilder.setTables("dict");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, where, whereArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv){
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        switch (sUrimatcher.match(uri)){
            case 1:{
                long rowID = db.insert("dict", null, cv);
                if(rowID > 0){
                    Uri retUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    return retUri;
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs){
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        int count = 0;
        switch (sUrimatcher.match(uri)){
            case 1:
                count = db.delete("dict", where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues cv, String where,String[] whereArgs){
        SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
        int count = 0;
        switch (sUrimatcher.match(uri)){
            case 1:
                count = db.update("dict", cv, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        System.out.println("===update is called==="+sh(uri));
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String sh(Uri uri){
        return "\r\n-----"+uri+"-----\r\n";
    }
}
