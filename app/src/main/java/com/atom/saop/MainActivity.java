package com.atom.saop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atom.aop.aspectj.SDialog;
import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;


public class MainActivity extends AppCompatActivity {
    long curr = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onMainClick2( new DialogCallback() {
//                    @Override
//                    public boolean dialogShow(Object... result) {
//                        if(!isSure){
//                            new AlertDialog.Builder(MainActivity.this)
//                                    .setTitle("asdasd")
//                                    .setMessage("asdasd")
//                                    .setPositiveButton("sure", (dialog, which) -> {
//                                        isSure = true;
//                                        Log.e("MainActivity", result[0].toString());
//                                    })
//                                    .setNegativeButton("Cancel", (dialog, which) -> {
//                                        isSure = false;
//                                        Log.e("MainActivity", result[0].toString());
//                                    }).create().show();
//                        }
//                        return isSure;
//                    }
//                } , v);
                onMainClick3 (v) ;
            }
        });
    }

    @SDialog(type = DialogRunType.runAfter)
    public String onMainClick2( DialogCallback callback  ,View v2 ) {
        long l = System.currentTimeMillis();
        Log.e("MainActivity", (l - curr) + "");
        curr = l;
        return "asdasdasda"  ;
    }

    @SDialog(type = DialogRunType.runAfter)
    public String onMainClick3(View v2 ) {
        long l = System.currentTimeMillis();
        Log.e("MainActivity", (l - curr) + "");
        curr = l;
        return "asdasdasda"  ;
    }

    boolean isSure = false;

}