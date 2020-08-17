package com.example.lenovo.granddictionary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final static int MAX_PROGRESS = 100;
    ProgressDialog pd;
    int progressStatus = 0;  // 记录进度对话框的完成百分比
    private Toolbar mToolbar;
    private MainActivity mContext = this;
    private DictDb dictDb;
    private ListView listView;
    private ArrayList<WordRec> words;
    ArrayList<Map<String, Object>> list;
    private SimpleAdapter adapter;
    private TextView display;
    private boolean show_meaning = false;
    private String deleted_word;
    private boolean is_download = false;
    private DlgHandleWord dlgHandleWord;
    private AlertDialog alertDialog = null;
    private AlertDialog.Builder dialogBuilder = null;
    private boolean displayornot = true;
    public int has_data = 0;
    private int[] data = new int[50];
    public int has_word = 0;
    public String result = "";
    public int word_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //We have to tell the activity where the toolbar is
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        //Display home with the "up" arrow indicator
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("简明英文词典");
        actionBar.setSubtitle("中山大学");

        display = (TextView) findViewById(R.id.display);
        display.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!show_meaning) {
                    display.setVisibility(View.VISIBLE);
                    Cursor cursor = dictDb.query(null, "word=?", new String[]{list.get(position).get("word").toString()}, null);
                    cursor.moveToFirst();
                    if (cursor.getCount() == 0) {
                        display.setText("error");
                    } else {
                        String cur_ex = cursor.getString(cursor.getColumnIndex("explanation"));
                        String display_thing = (String) list.get(position).get("word") + "\n" + cur_ex;
                        if (!display_thing.equals(display.getText())) {
                            display.setText((String) list.get(position).get("word") + "\n" + cur_ex);
                            displayornot = true;
                        } else if (displayornot) {
                            display.setVisibility(View.GONE);
                            displayornot = false;
                        } else {
                            display.setVisibility(View.VISIBLE);
                            displayornot = true;
                        }
                    }
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                deleted_word = (String) list.get(position).get("word");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                DlgHandleWord.DeleteWordDlg(MainActivity.this, dictDb, deleted_word);
                                break;
                            case R.id.modify:
                                DlgHandleWord.ModifyWordDlg(MainActivity.this, dictDb);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;  //true: 只执行长按事件(ShortClick事件失效)
            }
        });


        LinearLayout ll = (LinearLayout) findViewById(R.id.letter);
        for (int i = 0; i < 27; i++) {
            final TextView myTextView = new TextView(MainActivity.this);
            myTextView.setId(i);
            if (i != 0)
                myTextView.setText(Character.toString((char) (i + 96)));
            else
                myTextView.setText(" ");
            myTextView.setTextSize(20);
            myTextView.setBackgroundColor(Color.argb(255, 200, 200, 255));
            myTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cur_id = myTextView.getId();
                    for (int i = 0; i < 27; i++) {
                        if (cur_id != i) {
                            TextView a_TextView = (TextView) findViewById(i);
                            a_TextView.setBackgroundColor(Color.argb(255, 200, 200, 255));
                        }
                    }
                    myTextView.setBackgroundColor(Color.parseColor("#79CDCD"));
                    String cur_head = myTextView.getText().toString();
                    if (cur_head.equals(" ")) cur_head = "";
                    if (is_download) {
                        getwords(cur_head);
                        handler.sendEmptyMessage(0x001);
                    }
                }
            });
            LinearLayout.LayoutParams textViewLP = new LinearLayout.LayoutParams(
                    50, LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewLP.setMargins(20, 0, 0, 0);
            myTextView.setGravity(Gravity.CENTER);
            ll.addView(myTextView, textViewLP);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    //定义一个Handler用来更新页面：
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    showWords();
                    break;
                case 0x002:
                    pd.setProgress(progressStatus);
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action1:
                DlgHandleWord.SeatchWordDlg(this, dictDb);
                break;
            case R.id.action2:
                DlgHandleWord.AddWordDlg(this, dictDb);
                break;
            case R.id.download:
                showProgress(MainActivity.this.getWindow().getDecorView());
                is_download = true;
                dictDb = new DictDb(mContext);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DownloadDict downloadDict = new DownloadDict(mContext, dictDb);
                            downloadDict.start("");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.display:
                resettvColor();
                if (item.isChecked()) {
                    item.setChecked(false);
                    show_meaning = false;
                    display.setVisibility(View.VISIBLE);
                    getwords("");
                    handler.sendEmptyMessage(0x001);
                } else {
                    item.setChecked((true));
                    show_meaning = true;
                    display.setVisibility(View.GONE);
                    getwords("");
                    handler.sendEmptyMessage(0x001);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void getwords(String filter) {
        try {
            words = new ArrayList<WordRec>();
            int bookid = 0;
            Cursor cursor = dictDb.query(null, "word like ?", new String[]{filter + "%"}, "word COLLATE NOCASE");
            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String explanation = cursor.getString(cursor.getColumnIndex("explanation"));
                String level = cursor.getString(cursor.getColumnIndex("level"));
                WordRec a_word = new WordRec(bookid, word, explanation, Integer.valueOf(level));
                bookid++;
                words.add(a_word);
            }
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < words.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (show_meaning) {
                    map.put("word", words.get(i).getWord());
                    map.put("explanation", words.get(i).getExplanation());
                } else
                    map.put("word", words.get(i).getWord());
                list.add(map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showWords() {
        try {
            if (show_meaning) {
                adapter = new SimpleAdapter(MainActivity.this, list,
                        R.layout.list_item1, new String[]{"word", "explanation"},
                        new int[]{R.id.word1, R.id.explanation});
                listView.setAdapter(adapter);
            } else {
                adapter = new SimpleAdapter(MainActivity.this, list,
                        R.layout.list_item, new String[]{"word"},
                        new int[]{R.id.word});
                listView.setAdapter(adapter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void resettvColor() {
        for (int i = 0; i < 27; i++) {
            TextView myTextView = (TextView) findViewById(i);
            myTextView.setBackgroundColor(Color.argb(255, 200, 200, 255));
        }
    }


    public void showProgress(View source) {
        progressStatus = 0;   // 将进度条的完成进度重设为0
        has_data = 0;   // 重新开始填充数组
        pd = new ProgressDialog(MainActivity.this);
        pd.setMax(MAX_PROGRESS);
        pd.setTitle("下载词典");   // 设置对话框的标题
        //pd.setMessage("耗时任务的完成百分比");  // 设置对话框显示的内容
        pd.setCancelable(false);   // 设置对话框不能用“取消”按钮关闭
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(false);          // 设置对话框的进度条是否显示进度
        pd.show(); // ③

        new Thread() {
            public void run() {
                while (has_word<8583) {
                    progressStatus = has_word *MAX_PROGRESS/ 8583;   // 获取耗时操作的完成百分比
                    handler.sendEmptyMessage(0x002); // 发送空消息到Handler
                }
                //if (progressStatus >= MAX_PROGRESS) {  // 如果任务已经完成
                pd.dismiss();   // 关闭对话框
                //}
            }
        }.start();
    }
}
