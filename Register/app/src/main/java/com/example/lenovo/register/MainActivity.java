package com.example.lenovo.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String str = "", tmp = "";
    String[] college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //自定义spinner字体大小及颜色
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        college = getResources().getStringArray(R.array.计算机学院);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this,R.layout.spinner_item,college);
        arrayAdapter.setDropDownViewResource(R.layout.dropdown);  //下拉风格，可以不写
        spinner.setAdapter(arrayAdapter);

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View vw) {
                str = "";
                //editText1
                EditText editText1 = (EditText) findViewById(R.id.editText1);
                str = "用户名: " + editText1.getText().toString() + "\n";
                //editText2
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                str += "密码: " + editText2.getText().toString() + "\n";
                tmp = "";
                //checkBox1
                CheckBox chk = (CheckBox) findViewById(R.id.checkBox1);
                if (chk.isChecked())
                    tmp = chk.getText().toString();
                //checkBox2
                chk = (CheckBox) findViewById(R.id.checkBox2);
                if (chk.isChecked()) {
                    if (!tmp.equals(""))
                        tmp += "," + chk.getText().toString();
                    else
                        tmp += chk.getText().toString();
                }
                //checkBox3
                chk = (CheckBox) findViewById(R.id.checkBox3);
                if (chk.isChecked()) {
                    if (!tmp.equals(""))
                        tmp += "," + chk.getText().toString();
                    else
                        tmp += chk.getText().toString();
                }

                if (!tmp.equals(""))
                    str += "爱好: " + tmp + "\n";
                else
                    str += "爱好: 无\n";
                //radiobutton
                RadioGroup rp = (RadioGroup) findViewById(R.id.radiogroup1);
                if (rp.getCheckedRadioButtonId() == -1)
                    str += "年级: \n";
                else {
                    RadioButton rb = (RadioButton) findViewById(rp.getCheckedRadioButtonId());
                    str += "年级: " + rb.getText().toString() + "\n";
                }

                //spinner
                /*Spinner spinner = (Spinner) findViewById(R.id.spinner1);
                str += "学院: " + spinner.getSelectedItem().toString() + "\n";*/
                str += "学院: " + spinner.getSelectedItem().toString() + "\n";

                //switch
                Switch switch1 = (Switch) findViewById(R.id.switch1);
                if (switch1.isChecked())
                    str += "全日制: 是\n";
                else
                    str += "全日制: 否\n";

                //display
                if (editText1.getText().toString().length() > 10 || editText1.getText().toString().length() < 2
                        || editText2.getText().toString().length() > 20 || editText2.getText().toString().length() < 6)
                    DisplayToast("用户名或密码格式有误！！");
                else
                    DisplayToast(str);
            }
        });
    }

    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 220);
        toast.show();
    }
}
