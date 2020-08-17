package com.example.lenovo.grandwordremember;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by lenovo on 2020/6/8.
 */

public class WordDb {
    private ContentResolver resolver;

    WordDb(Resolver resolver) {
        this.resolver = resolver.getResolver();
    }

    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String sortOrder) {
        return resolver.query(uri, projection, where, whereArgs, sortOrder);
    }

    public Uri insert(Uri uri, ContentValues cv) {
        return resolver.insert(uri, cv);
    }

    public int delete(Uri uri, String where, String[] whereArgs) {
        return resolver.delete(uri, where, whereArgs);
    }

    public int update(Uri uri, ContentValues cv, String where, String[] whereArgs) {
        return resolver.update(uri, cv, where, whereArgs);
    }
}
