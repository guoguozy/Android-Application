package com.example.lenovo.grandwordremember;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2020/6/4.
 */


public class DlgHandleWord {
    private static AlertDialog addWordDlg;
    private static AlertDialog seacrhWordDlg;
    private static AlertDialog searchtestDlg;

    static public void SeatchWordDlg(final MainActivity mContext, final Resolver resolver) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(mContext);
        seacrhWordDlg = dlgBuilder
                // 设置图标
                .setIcon(R.drawable.dict)
                // 设置对话框标题
                .setTitle("查找单词")
                // 设置对话框显示的View对象add_word.xml
                .setView(R.layout.search_dict)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<WordRec> words = new ArrayList<WordRec>();
                        int bookid = 0;
                        EditText new_word = (EditText) seacrhWordDlg.findViewById(R.id.searched_word);
                        String cur_word = new_word.getText().toString();
                        String target_word = "%" + cur_word + "%";
                        String[] cur_words = new String[]{target_word};
                        Cursor cursor = resolver.getResolver().query(resolver.getUri(), null, "word like ?", cur_words, null);
                        while (cursor.moveToNext()) {
                            String word = cursor.getString(cursor.getColumnIndex("word"));
                            String explanation = cursor.getString(cursor.getColumnIndex("explanation"));
                            String level = cursor.getString(cursor.getColumnIndex("level"));
                            WordRec a_word = new WordRec(bookid, word, explanation, Integer.valueOf(level));
                            bookid++;
                            words.add(a_word);
                        }
                        ArrayList list = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < words.size(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("word", words.get(i).getWord());
                            list.add(map);
                        }
                        System.out.println(list);
                        ListView search_view = (ListView) mContext.findViewById(R.id.search_list);
                        search_view.setVisibility(View.VISIBLE);
                        SimpleAdapter adapter = new SimpleAdapter(mContext, list,
                                R.layout.search_item, new String[]{"word"},
                                new int[]{R.id.word});
                        search_view.setAdapter(adapter);
                    }
                }).create();
        seacrhWordDlg.show();
    }

    static public void AddWordDlg(final Context mContext, final Resolver resolver) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(mContext);
        addWordDlg = dlgBuilder.setTitle("增加单词")
                .setIcon(R.drawable.dict)
                .setView(R.layout.add_dict)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText new_word = (EditText) addWordDlg.findViewById(R.id.new_word);
                        long time = System.currentTimeMillis() / 1000;
                        EditText new_explanation = (EditText) addWordDlg.findViewById(R.id.new_explanation);
                        EditText new_level = (EditText) addWordDlg.findViewById(R.id.new_level);
                        CheckBox cover = (CheckBox) addWordDlg.findViewById(R.id.cover);
                        String cur_word = new_word.getText().toString();
                        if (!cur_word.equals("")) {
                            String cur_ex = new_explanation.getText().toString();
                            Integer cur_level = Integer.valueOf(new_level.getText().toString());
                            ContentValues cv = new ContentValues();
                            cv.put("word", cur_word);
                            cv.put("explanation", cur_ex);
                            cv.put("level", cur_level);
                            cv.put("modified_time", time);
                            String[] cur_words = new String[]{cur_word};
                            Cursor cursor = resolver.getResolver().query(resolver.getUri(), null, "word=?", cur_words, null);
                            if (cursor.getCount() == 0 || (cursor.getCount() != 0 && cover.isChecked())) {
                                resolver.getResolver().insert(resolver.getUri(), cv);
                                Toast.makeText(mContext, "成功添加", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "单词已存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        addWordDlg.show();
    }

    static public void SeatchtestDlg(final StatActivity mContext) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(mContext);
        searchtestDlg = dlgBuilder
                // 设置图标
                .setIcon(R.drawable.dict)
                // 设置对话框标题
                .setTitle("请输入首字母搜索")
                // 设置对话框显示的View对象add_word.xml
                .setView(R.layout.search_dict)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText new_word = (EditText) searchtestDlg.findViewById(R.id.searched_word);
                        mContext.getData(new_word.getText().toString());
                        mContext.showData();
                    }
                }).create();
        searchtestDlg.show();
    }
}