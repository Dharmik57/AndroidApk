package com.example.firebaseauthentication.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

public class MyReceiver  extends  BroadcastReceiver {

    private  TextView  internetmessage;


    @Override
    public void onReceive(Context context, Intent intent) {


        String status = NetworkUtil.getConnectivityStatusString(context);
        String message;
        int color,backgroundcolor;
        if(status.isEmpty()) {
            status="No Internet Connection";
            color = Color.WHITE;
            backgroundcolor=Color.RED;
        }

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
