package com.example.lenovo.grandwordremember;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by lenovo on 2020/6/7.
 */

public class TestActivity extends AppCompatActivity {
    private Context mContext = TestActivity.this;
    private ArrayList<WordRec> words;
    private ArrayList<String> words_list;
    private ArrayList<String> words_explanation;
    private ArrayList<String> choice;
    public int test_num = 10;
    public int choice_num = 4;
    private Map<Integer, String> map;
    private String word_color = "Red";
    private TestAdapter adapter;
    private ListView listView;
    private Button commit;
    public TestDb testDb;
    public WordDb wordDb;
    public Resolver resolver;
    public ContentValues cv;
    public ArrayList<ContentValues> cvlist;
    public boolean saved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("学霸背单词");
        actionBar.setSubtitle("单词测验");

        listView = (ListView) findViewById(R.id.list);
        commit = (Button) findViewById(R.id.commit);
        testDb = new TestDb(mContext);
        resolver = new Resolver(mContext);
        wordDb = new WordDb(resolver);

        Intent intent = getIntent();
        word_color = intent.getStringExtra("word_color");
        test_num = intent.getIntExtra("test_num",10);
        saved = intent.getBooleanExtra("saved", true);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saved) {
                    int i = 0;
                    for (i = 0; i < cvlist.size(); ++i)
                        testDb.insert(cvlist.get(i));
                    update_correct_count();
                    commit.setVisibility(View.INVISIBLE);
                }
                show_answers();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.test:
                Toast.makeText(this, "开始测验", Toast.LENGTH_SHORT).show();
                Cursor cursor = get_test_cursor();
                cursor_to_choice(cursor);
                show_choices();
                commit.setVisibility(View.VISIBLE);
                break;
            case R.id.menu1:
                Toast.makeText(this, "开始复习", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu2:
                Toast.makeText(this, "清除数据", Toast.LENGTH_SHORT).show();
                testDb.delete(null, null);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public Cursor get_test_cursor() {
        Cursor cursor_test, cursor_db;
        System.out.println(test_num);
        String[] columns = new String[]{"word", "explanation", "level"};
        if (testDb.getCount() == 0) {
            cursor_test = wordDb.query(resolver.getUri(), columns, null, null, "word COLLATE NOCASE limit " + String.valueOf(test_num));
            cursor_test.moveToFirst();
        } else {
            cursor_db = testDb.query(new String[]{"word"}, null, null, "word COLLATE NOCASE desc");
            cursor_db.moveToFirst();
            String last_one = cursor_db.getString(cursor_db.getColumnIndex("word"));
            cursor_test = wordDb.query(resolver.getUri(), columns, "word >= ?", new String[]{last_one}, "word COLLATE NOCASE limit " + String.valueOf(test_num + 1));
            cursor_test.moveToFirst();
            cursor_test.moveToNext();
        }
        return cursor_test;
    }

    public void cursor_to_choice(Cursor cursor) {
        int bookid = 0;
        words_list = new ArrayList<>();
        words_explanation = new ArrayList<>();
        words = new ArrayList<WordRec>();
        cvlist = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String explanation = cursor.getString(cursor.getColumnIndex("explanation"));
            String level = cursor.getString(cursor.getColumnIndex("level"));
            long time = System.currentTimeMillis() / 1000;
            System.out.println(word);
            cv = new ContentValues();
            cv.put("word", word);
            cv.put("level", level);
            cv.put("last_test_time", time);
            cv.put("test_count", 1);
            cvlist.add(cv);
            WordRec a_word = new WordRec(bookid, word, explanation, Integer.valueOf(level));
            bookid++;
            words.add(a_word);
            cursor.moveToNext();
        }
        int i, j;
        for (i = 0; i < test_num; i++) {
            String ex = words.get(i).getExplanation();
            int position = ex.indexOf("]");
            String mid = words.get(i).getExplanation().substring(position + 1);
            words_list.add(words.get(i).getWord());
            words_explanation.add(mid);
        }
        System.out.println(words_list);
        System.out.println(words_explanation);

        choice = new ArrayList<>();
        Random random = new Random();
        for (i = 0; i < test_num; i++) {
            ArrayList<Integer> choice_no = new ArrayList<>();
            choice_no.add(i);
            while (choice_no.size() < choice_num) {
                int randomInt = random.nextInt(test_num);
                if (!choice_no.contains(randomInt))
                    choice_no.add(randomInt);
            }
            Collections.shuffle(choice_no); //打乱选项顺序
            Iterator<Integer> iterator = choice_no.iterator();

            while (iterator.hasNext()) {
                choice.add(words_explanation.get(iterator.next()));
            }
        }
        System.out.println(choice);
    }

    public void show_choices() {
        ArrayList<Integer> color = new ArrayList<>();
        map = new HashMap<Integer, String>();
        adapter = new TestAdapter(words_list, choice, mContext, color, map, word_color);
        listView.setAdapter(adapter);
    }

    public void update_correct_count() {
        map = adapter.map;
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            int position = entry.getKey();
            String choosed_ex = entry.getValue();
            String Right = words_explanation.get(position);
            if (Right.equals(choosed_ex)) {
                String cur_word = words_list.get(position);
                Cursor cursor2 = testDb.query(null, "word=?", new String[]{cur_word}, null);
                cursor2.moveToFirst();
                int times = cursor2.getInt(cursor2.getColumnIndex("correct_count"));
                times++;
                ContentValues cv = new ContentValues();
                cv.put("correct_count", times);
                testDb.update(cv, "word=?", new String[]{cur_word});//修改答对总次数
            }
        }
    }

    public void show_answers() {
        ArrayList<Integer> color = new ArrayList();//需要变色的选项
        for (int i = 0; i < test_num; i++) {
            String right = words_explanation.get(i);
            for (int j = i * 4; j < (i + 1) * 4; j++) {
                String cur_choice = choice.get(j);
                if (right.equals(cur_choice)) {
                    color.add(j - i * 4);
                }
            }
        }
        adapter = new TestAdapter(words_list, choice, mContext, color, map, word_color);
        listView.setAdapter(adapter);
    }

}
