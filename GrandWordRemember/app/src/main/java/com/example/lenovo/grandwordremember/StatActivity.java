package com.example.lenovo.grandwordremember;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2020/6/8.
 */

public class StatActivity extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<String> words;
    private ArrayList<Integer> levels;
    private ArrayList<Integer> test_nums;
    private ArrayList<Integer> correct_nums;
    ArrayList<Map<String, Object>> list;
    private DBOpenHandler dbOpenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("学霸背单词");
        actionBar.setSubtitle("测验统计");
        listView = (ListView) findViewById(R.id.listView);
        dbOpenHandler = new DBOpenHandler(StatActivity.this, "dbWords.db3", null, 1);
        getData("");
        showData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_stat:
                Toast.makeText(StatActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                DlgHandleWord.SeatchtestDlg(StatActivity.this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void getData(String filter) {
        try {
            words = new ArrayList<>();
            levels = new ArrayList<>();
            test_nums = new ArrayList<>();
            correct_nums = new ArrayList<>();

            SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
            Cursor cursor = db.query("words", null, "word like ?", new String[]{filter + "%"}, null, null, "word COLLATE NOCASE");
            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                Integer level = cursor.getInt(cursor.getColumnIndex("level"));
                Integer test_num = cursor.getInt(cursor.getColumnIndex("test_count"));
                Integer correct_num = cursor.getInt(cursor.getColumnIndex("correct_count"));
                words.add(word);
                levels.add(level);
                test_nums.add(test_num);
                correct_nums.add(correct_num);
            }
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < words.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("word", words.get(i));
                map.put("level", levels.get(i));
                map.put("test_num", test_nums.get(i));
                map.put("correct_num", correct_nums.get(i));
                list.add(map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showData() {
        try {
            adapter = new SimpleAdapter(StatActivity.this, list,
                    R.layout.stat_item, new String[]{"word", "level", "test_num", "correct_num"},
                    new int[]{R.id.word, R.id.level, R.id.test_num, R.id.correct_num});
            listView.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
