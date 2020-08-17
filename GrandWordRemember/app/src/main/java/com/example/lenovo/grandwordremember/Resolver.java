package com.example.lenovo.grandwordremember;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by lenovo on 2020/6/7.
 */

public class Resolver {
    private ContentResolver resolver;
    private Uri uri = Uri.parse("content://com.example.lenovo.granddictionary.DictProvider/dict");
    private Context mContext;

    Resolver(Context mContext) {
        resolver = mContext.getContentResolver();
        this.mContext = mContext;
    }

    public ContentResolver getResolver() {
        return resolver;
    }
    public Uri getUri(){
        return uri;
    }

}
