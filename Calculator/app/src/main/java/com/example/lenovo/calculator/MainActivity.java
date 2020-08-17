package com.example.lenovo.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn1; // 数字1
    Button btn2; // 数字2
    Button btn3; // 数字3
    Button btn4; // 数字4
    Button btn5; // 数字5
    Button btn6; // 数字6
    Button btn7; // 数字7
    Button btn8; // 数字8
    Button btn9; // 数字9
    Button btn0; // 数字0
    Button addsub; //正负号
    Button clearall; //C
    Button del; //d el号
    Button add; // +号
    Button sub; // -号
    Button mul; // *号
    Button div; // 除号
    Button dot; // 小数点
    Button equ; // =号
    Button clear; //C
    TextView result; // 显示文本

    double num1 = 0, num2 = 0; //计算的数字
    boolean isnum1 = false, isnum2 = false, nextnum = false;//数字1,2是否赋值，是否是下一数字
    boolean getop = false;//是否获取操作符
    double Result = 0;//计算结果
    int op = -1;//判断操作符
    String opd = " ";//显示操作符

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取页面上的控件
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btn0 = (Button) findViewById(R.id.btn0);
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        mul = (Button) findViewById(R.id.mul);
        div = (Button) findViewById(R.id.div);
        equ = (Button) findViewById(R.id.equ);
        dot = (Button) findViewById(R.id.dot);
        clear = (Button) findViewById(R.id.clear);
        clearall = (Button) findViewById(R.id.clearall);
        del = (Button) findViewById(R.id.del);
        addsub = (Button) findViewById(R.id.addsub);
        result = (TextView) findViewById(R.id.result);

        // 按钮的单击事件
        btn1.setOnClickListener(new Click());
        btn2.setOnClickListener(new Click());
        btn3.setOnClickListener(new Click());
        btn4.setOnClickListener(new Click());
        btn5.setOnClickListener(new Click());
        btn6.setOnClickListener(new Click());
        btn7.setOnClickListener(new Click());
        btn8.setOnClickListener(new Click());
        btn9.setOnClickListener(new Click());
        btn0.setOnClickListener(new Click());
        add.setOnClickListener(new Click());
        sub.setOnClickListener(new Click());
        mul.setOnClickListener(new Click());
        div.setOnClickListener(new Click());
        equ.setOnClickListener(new Click());
        dot.setOnClickListener(new Click());
        clear.setOnClickListener(new Click());
        clearall.setOnClickListener(new Click());
        del.setOnClickListener(new Click());
        addsub.setOnClickListener(new Click());
    }

    // 设置按钮点击后的监听
    class Click implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {                //switch循环获取点击按钮后的值
                case R.id.btn0:                //获取，0-9、小数点，并在编辑框显示
                    if (!nextnum) {
                        String myString = result.getText().toString();
                        if (!myString.equals("0")) {
                            myString += "0";
                            result.setText(myString);
                        }
                    } else {
                        result.setText("0");
                        nextnum = false;
                    }
                    break;
                case R.id.btn1:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "1";
                            result.setText(myString1);
                        } else
                            result.setText("1");
                    } else {
                        result.setText("1");
                        nextnum = false;
                    }
                    break;
                case R.id.btn2:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "2";
                            result.setText(myString1);
                        } else
                            result.setText("2");
                    } else {
                        result.setText("2");
                        nextnum = false;
                    }
                    break;
                case R.id.btn3:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "3";
                            result.setText(myString1);
                        } else
                            result.setText("3");
                    } else {
                        result.setText("3");
                        nextnum = false;
                    }
                    break;
                case R.id.btn4:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "4";
                            result.setText(myString1);
                        } else
                            result.setText("4");
                    } else {
                        result.setText("4");
                        nextnum = false;
                    }
                    break;
                case R.id.btn5:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "5";
                            result.setText(myString1);
                        } else
                            result.setText("5");
                    } else {
                        result.setText("5");
                        nextnum = false;
                    }
                    break;
                case R.id.btn6:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "6";
                            result.setText(myString1);
                        } else
                            result.setText("6");
                    } else {
                        result.setText("6");
                        nextnum = false;
                    }
                    break;
                case R.id.btn7:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "7";
                            result.setText(myString1);
                        } else
                            result.setText("7");
                    } else {
                        result.setText("7");
                        nextnum = false;
                    }
                    break;
                case R.id.btn8:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "8";
                            result.setText(myString1);
                        } else
                            result.setText("8");
                    } else {
                        result.setText("8");
                        nextnum = false;
                    }
                    break;
                case R.id.btn9:
                    if (!nextnum) {
                        String myString1 = result.getText().toString();
                        if (!myString1.equals("0")) {
                            myString1 += "9";
                            result.setText(myString1);
                        } else
                            result.setText("9");
                    } else {
                        result.setText("9");
                        nextnum = false;
                    }
                    break;
                case R.id.dot:
                    String myStringDot = result.getText().toString();
                    if (!myStringDot.contains(".")) {
                        myStringDot += ".";
                        result.setText(myStringDot);
                    } else
                        DisplayToast("warning：不可重复输入小数点");
                    break;
                case R.id.add:             //判断，使用加减乘除的操作符
                    getop = true;
                    nextnum = true;
                    String myStringAdd = result.getText().toString();
                    if (myStringAdd.equals(null))
                        return;
                    if (!isnum1) {
                        num1 = Double.valueOf(myStringAdd);
                        isnum1 = true;
                        op = 1;
                    } else if (!isnum2) {
                        num2 = Double.valueOf(myStringAdd);
                        isnum2 = true;
                    }
                    if (isnum2 && isnum1 && getop) {
                        isnum2 = false;
                        double ans = calculate_ans();
                        op = 1;
                        num1 = ans;
                        result.setText(Double.toString(ans));
                    }
                    break;
                case R.id.sub:
                    getop = true;
                    nextnum = true;
                    String myStringSub = result.getText().toString();
                    if (myStringSub.equals(null))
                        return;
                    if (!isnum1) {
                        num1 = Double.valueOf(myStringSub);
                        isnum1 = true;
                        op = 2;
                    } else if (!isnum2) {
                        num2 = Double.valueOf(myStringSub);
                        isnum2 = true;
                    }
                    if (isnum2 && isnum1 && getop) {
                        isnum2 = false;
                        double ans = calculate_ans();
                        op = 2;
                        num1 = ans;
                        result.setText(Double.toString(ans));
                    }
                    break;
                case R.id.mul:
                    getop = true;
                    nextnum = true;
                    String myStringMul = result.getText().toString();
                    if (myStringMul.equals(null))
                        return;
                    if (!isnum1) {
                        num1 = Double.valueOf(myStringMul);
                        isnum1 = true;
                        op = 3;
                    } else if (!isnum2) {
                        num2 = Double.valueOf(myStringMul);
                        isnum2 = true;
                    }
                    if (isnum2 && isnum1 && getop) {
                        isnum2 = false;
                        double ans = calculate_ans();
                        op = 3;
                        num1 = ans;
                        result.setText(Double.toString(ans));
                    }
                    break;
                case R.id.div:
                    getop = true;
                    nextnum = true;
                    String myStringDiv = result.getText().toString();
                    if (myStringDiv.equals(null))
                        return;
                    if (!isnum1) {
                        num1 = Double.valueOf(myStringDiv);
                        isnum1 = true;
                        op = 4;
                    } else if (!isnum2) {
                        num2 = Double.valueOf(myStringDiv);
                        isnum2 = true;
                    }
                    if (isnum2 && isnum1 && getop) {
                        isnum2 = false;
                        double ans = calculate_ans();
                        op = 4;
                        num1 = ans;
                        result.setText(Double.toString(ans));
                    }
                    break;
                case R.id.clear:                 //清除，将编辑框文本显示为空
                    result.setText("0");
                    break;
                case R.id.clearall:
                    result.setText("0");
                    isnum1 = false;
                    isnum2 = false;
                    break;
                case R.id.addsub:
                    String myStringaddsub = result.getText().toString();
                    if (myStringaddsub.equals("")) {
                        result.setText("-");
                    }
                    else if (myStringaddsub.equals("-")) {
                        result.setText("");
                    } else {
                        if (Double.valueOf(myStringaddsub) > 0)
                            result.setText("-" + myStringaddsub);
                        else if(Double.valueOf(myStringaddsub) < 0)
                            result.setText(myStringaddsub.substring(1, myStringaddsub.length()));
                        else
                        {
                            if(myStringaddsub.startsWith("-"))
                                result.setText(myStringaddsub.substring(1, myStringaddsub.length()));
                            else
                                result.setText("-0");
                        }
                    }
                    break;
                case R.id.equ:
                    nextnum = true;
                    String myStringequ = result.getText().toString();
                    if (isnum1 && getop) {
                        num2 = Double.valueOf(myStringequ);
                        double ans = calculate_ans();
                        num1 = ans;
                        isnum1 = false;
                        isnum2 = false;
                        result.setText(Double.toString(ans));
                        getop = false;
                    }
                    break;
                case R.id.del:
                    String myStringdel = result.getText().toString();
                    if (myStringdel.length() >= 1)
                        result.setText(myStringdel.substring(0, myStringdel.length() - 1));
                    break;
                default:
                    break;
            }
        }

    }

    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 220);
        toast.show();
    }

    public double calculate_ans() {
        switch (op) {
            case 1:
                return num1 + num2;
            case 2:
                return num1 - num2;
            case 3:
                return num1 * num2;
            case 4:
                if (num2 == 0) {
                    isnum1 = false;
                    isnum2 = false;
                    DisplayToast("除数不可为零！");
                }
                return num1 / num2;
            default:
                return 0;
        }
    }
}
