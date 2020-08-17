package com.example.lenovo.musicplay;

/**
 * Created by lenovo on 2020/6/28.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int position;
        if(bundle == null){
            position = 2;
        }
        else{
            position = bundle.getInt("position", 0);
        }

        if(position != -1){
            Intent intent1 = new Intent();
            intent1.putExtra("position", position);
            intent1.setAction("android.intent.action.FIRST_SERVICE");
            intent1.setPackage(context.getPackageName());
            context.startService(intent1);
        }
    }
}

