package com.example.lenovo.granddictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by lenovo on 2020/6/4.
 */


public class DlgHandleWord {
    private static AlertDialog addWordDlg;
    private static AlertDialog seacrhWordDlg;
    private static AlertDialog modifyWordDlg;
    private static AlertDialog deleteWordDlg;

    static public void SeatchWordDlg(final MainActivity mContext, final DictDb dictDb) {
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
                        EditText new_word = (EditText) seacrhWordDlg.findViewById(R.id.searched_word);
                        String cur_word = new_word.getText().toString();
                        String target_word = "%" + cur_word;
                        mContext.resettvColor();
                        mContext.getwords(target_word);
                        mContext.handler.sendEmptyMessage(0x001);
                    }
                }).create();
        seacrhWordDlg.show();
    }

    static public void AddWordDlg(final MainActivity mContext, final DictDb dictDb) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(mContext);
        addWordDlg = dlgBuilder.setTitle("增加单词")
                .setIcon(R.drawable.dict)
                .setView(R.layout.add_dict)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText new_word = (EditText) addWordDlg.findViewById(R.id.new_word);
                        EditText new_explanation = (EditText) addWordDlg.findViewById(R.id.new_explanation);
                        EditText new_level = (EditText) addWordDlg.findViewById(R.id.new_level);
                        CheckBox cover = (CheckBox) addWordDlg.findViewById(R.id.cover);
                        String cur_word = new_word.getText().toString();
                        if (!cur_word.equals("")) {
                            String cur_ex = new_explanation.getText().toString();
                            Integer cur_level = Integer.valueOf(new_level.getText().toString());
                            long time = System.currentTimeMillis() / 1000;
                            ContentValues cv = new ContentValues();
                            cv.put("word", cur_word);
                            cv.put("explanation", cur_ex);
                            cv.put("level", cur_level);
                            cv.put("modified_time", time);
                            String[] cur_words = new String[]{cur_word};
                            Cursor cursor = dictDb.query(null, "word=?", cur_words, null);
                            if (cursor.getCount() == 0 || (cursor.getCount() != 0 && cover.isChecked())) {
                                dictDb.insert(cv);
                                Toast.makeText(mContext, "成功添加", Toast.LENGTH_SHORT).show();
                                mContext.resettvColor();
                                mContext.getwords("");
                                mContext.handler.sendEmptyMessage(0x001);
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

    static public void ModifyWordDlg(final MainActivity mContext, final DictDb dictDb) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(mContext);
        modifyWordDlg = dlgBuilder.setTitle("修改单词")
                .setIcon(R.drawable.dict)
                .setView(R.layout.modify_dict)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText new_word = (EditText) modifyWordDlg.findViewById(R.id.new_word);
                        EditText new_explanation = (EditText) modifyWordDlg.findViewById(R.id.new_explanation);
                        EditText new_level = (EditText) modifyWordDlg.findViewById(R.id.new_level);
                        String cur_word = new_word.getText().toString();
                        String[] cur_words = new String[]{cur_word};
                        Cursor cursor = dictDb.query(null, "word=?", cur_words, null);
                        if (cursor.getCount() == 0)
                            Toast.makeText(mContext, "单词不存在", Toast.LENGTH_SHORT).show();
                        else {
                            if (!cur_word.equals("")) {
                                String cur_ex = new_explanation.getText().toString();
                                Integer cur_level = Integer.valueOf(new_level.getText().toString());
                                ContentValues cv = new ContentValues();
                                cv.put("word", cur_word);
                                cv.put("explanation", cur_ex);
                                cv.put("level", cur_level);
                                dictDb.update(cv, "word=?", cur_words);
                                mContext.getwords("");
                                mContext.resettvColor();
                                mContext.handler.sendEmptyMessage(0x001);
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        modifyWordDlg.show();
    }

    static public void DeleteWordDlg(final MainActivity mContext, final DictDb dictDb, final String deleted_word) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_dict, null);
        dlgBuilder.setView(dialogView);
        dlgBuilder
                // 设置图标
                .setIcon(R.drawable.dict)
                // 设置对话框标题
                .setTitle("删除单词");
                // 设置对话框显示的View对象add_word.xml

        TextView show = (TextView) dialogView.findViewById(R.id.deleted_infor);
        show.setText(show.getText().toString() + deleted_word + "?");
        dlgBuilder
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor cursor = dictDb.query(null, "word=?", new String[]{deleted_word}, null);

                        if (cursor.getCount() == 0)
                            Toast.makeText(mContext, "单词不存在", Toast.LENGTH_SHORT).show();
                        else {
                            if (!deleted_word.equals("")) {
                                dictDb.delete("word=?", new String[]{deleted_word});
                                mContext.getwords("");
                                mContext.resettvColor();
                                mContext.handler.sendEmptyMessage(0x001);
                            }
                        }
                    }
                }).create();
        deleteWordDlg = dlgBuilder.create();
        deleteWordDlg.show();
    }
}
