package com.example.lenovo.granddictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2020/6/3.
 */

public class DownloadDict {
    private DictDb dictDb;
    public int word_count = 100, has_word = 0;
    public MainActivity mContext;

    DownloadDict(MainActivity context, DictDb dictDb) {
        this.dictDb = dictDb;
        this.mContext=context;
    }

    public void start(String tmp) {
        try {
            String result = getJson();
            System.out.println(result);
            parserJSONtoDB(result);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public String getInputStreamText(InputStream is) throws Exception {
        InputStreamReader isr = new InputStreamReader(is, "utf8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public String getJson() throws Exception {
        String apiUrl = String.format("http://103.26.79.35:8080/dict/");
        URL url = new URL(apiUrl);
        URLConnection open = url.openConnection();
        open.connect();
        InputStream inputStream = open.getInputStream();
        return getInputStreamText(inputStream);
    }

    public void parserJSONtoDB(String str) {
        JSONObject wordObj = null;
        JSONArray wordArray = null;
        try {
            wordArray = new JSONArray(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int i;
        word_count = wordArray.length();
        for (i = 0; i < wordArray.length(); i++) {
            try {
                wordObj = wordArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //对象转存到数据库
            long time = System.currentTimeMillis() / 1000;
            ContentValues cv = new ContentValues();
            String[] cur_word = {wordObj.optString("word")};
            cv.put("word", wordObj.optString("word"));
            cv.put("explanation", wordObj.optString("explanation"));
            cv.put("level", wordObj.optInt("level"));
            cv.put("modified_time", time);
            if (dictDb.getCount() == 0) {
                dictDb.insert(cv);
            } else {
                Cursor cursor = dictDb.query(null, "word=?", cur_word, null);
                if (cursor.getCount() != 0)
                    dictDb.update(cv, "word=?", cur_word);
                else
                    dictDb.insert(cv);
            }
            has_word++;
            mContext.has_word=has_word;
        }
    }
}
