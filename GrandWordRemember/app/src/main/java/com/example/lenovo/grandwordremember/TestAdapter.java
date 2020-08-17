package com.example.lenovo.grandwordremember;

/**
 * Created by lenovo on 2020/6/7.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAdapter extends BaseAdapter {
    private ArrayList<String> list;//单词
    private ArrayList<String> ex_list;//解释
    private ArrayList<View> views;
    private Context mContext;
    private ArrayList<Integer> color;//
    private ArrayList<Integer> selected;
    LayoutInflater inflater;
    public Map<Integer, String> map;
    private String word_color;

    public TestAdapter(ArrayList<String> list, ArrayList<String> ex_list, Context mContext, ArrayList<Integer> color, Map<Integer, String> map, String word_color) {
        super();
        this.list = list;
        this.ex_list = ex_list;
        this.mContext = mContext;
        this.color = color;
        this.map = map;
        this.word_color = word_color;
        views = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
        System.out.println(list);
    }


    public class ViewHolder {
        RadioGroup rg;
        TextView tv;
        RadioButton rb1;
        RadioButton rb2;
        RadioButton rb3;
        RadioButton rb4;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {//仅仅设置了单词
        ViewHolder holder = null;
        if (position == views.size()) {
            convertView = inflater.inflate(R.layout.select_word, null);
            views.add(convertView);
        }
        convertView = views.get(position);
        holder = new ViewHolder();
        holder.rg = (RadioGroup) convertView.findViewById(R.id.radioGroup1);
        holder.tv = (TextView) convertView.findViewById(R.id.word);
        holder.rb1 = (RadioButton) convertView.findViewById(R.id.answer1);
        holder.rb2 = (RadioButton) convertView.findViewById(R.id.answer2);
        holder.rb3 = (RadioButton) convertView.findViewById(R.id.answer3);
        holder.rb4 = (RadioButton) convertView.findViewById(R.id.answer4);

        //设置显示
        holder.tv.setText(" " + (list.get(position)));
        holder.rb1.setText(ex_list.get(position * 4));
        holder.rb2.setText(ex_list.get(position * 4 + 1));
        holder.rb3.setText(ex_list.get(position * 4 + 2));
        holder.rb4.setText(ex_list.get(position * 4 + 3));

        final RadioGroup rgBtn = holder.rg;

        if (word_color.equals("Red")) {
            holder.tv.setTextColor(Color.parseColor("#FF0000"));
        } else if (word_color.equals("Blue")) {
            holder.tv.setTextColor(Color.parseColor("#0099FF"));
        } else if (word_color.equals("Green")) {
            holder.tv.setTextColor(Color.parseColor("#8FBC8F"));
        } else {
            holder.tv.setTextColor(Color.parseColor("#000000"));
        }

        if (this.color.size() != 0) {
            int colored_position = this.color.get(position);
            switch (colored_position) {
                case 0:
                    holder.rb1.setBackgroundColor(Color.parseColor("#F0F0F0"));
                    break;
                case 1:
                    holder.rb2.setBackgroundColor(Color.parseColor("#F0F0F0"));
                    break;
                case 2:
                    holder.rb3.setBackgroundColor(Color.parseColor("#F0F0F0"));
                    break;
                case 3:
                    holder.rb4.setBackgroundColor(Color.parseColor("#F0F0F0"));
                    break;
                default:
                    break;
            }
        }


        if (map.containsKey(position)) {
            String choice_ = map.get(position);
            if (choice_ == holder.rb1.getText().toString()) {
                holder.rb1.setChecked(true);
            } else if (choice_ == holder.rb2.getText().toString()) {
                holder.rb2.setChecked(true);
            } else if (choice_ == holder.rb3.getText().toString()) {
                holder.rb3.setChecked(true);
            } else {
                holder.rb4.setChecked(true);
            }
        }


        rgBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbtn = (RadioButton) group.findViewById(checkedId);
                rbtn.setChecked(true);
                map.put(position, rbtn.getText().toString()); //用map记录点击的button
                Toast.makeText(mContext, rbtn.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
