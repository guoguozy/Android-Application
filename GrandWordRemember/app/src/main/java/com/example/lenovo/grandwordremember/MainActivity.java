package com.example.lenovo.grandwordremember;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public Context mContext = this;
    private SharedPreferences settings;
    public String word_color;
    public int test_num;
    public boolean saved;
    public ListView search_view;
    public Button search_result;
    ArrayList<Map<String, Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("学霸背单词");
        actionBar.setSubtitle("- 快速记忆法");

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        word_color = settings.getString("list_key", "Red");
        test_num = Integer.valueOf(settings.getString("edittext_key", "10"));
        saved = settings.getBoolean("checkbox_key", true);
        list = new ArrayList<>();
        search_view = (ListView) findViewById(R.id.search_list);
        search_view.setVisibility(View.INVISIBLE);
        search_result = (Button) findViewById(R.id.button_search);
        search_result.setVisibility(View.INVISIBLE);
        search_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_result.setVisibility(View.INVISIBLE);
                search_view.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.test:
                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra("word_color", word_color);
                intent.putExtra("test_num", test_num);
                intent.putExtra("saved", saved);
                startActivity(intent);
                break;
            case R.id.menu1:
                Toast.makeText(this, "新增单词", Toast.LENGTH_SHORT).show();
                add_new_word();
                break;
            case R.id.menu2:
                Toast.makeText(this, "学习统计", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, StatActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu3:
                Toast.makeText(this, "查找单词", Toast.LENGTH_SHORT).show();
                search_word();
                break;
            case R.id.menu4:
                Toast.makeText(this, "系统设置", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, PrefActivity.class);
                startActivity(intent2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    void add_new_word() {
        Resolver resolver = new Resolver(mContext);
        DlgHandleWord.AddWordDlg(mContext, resolver);
    }

    void search_word() {
        search_result.setVisibility(View.VISIBLE);
        Resolver resolver1 = new Resolver(mContext);
        DlgHandleWord.SeatchWordDlg(MainActivity.this, resolver1);
    }
}
