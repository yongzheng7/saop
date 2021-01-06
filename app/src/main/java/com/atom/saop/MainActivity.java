package com.atom.saop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atom.aop.aspectj.SDialog;
import com.atom.aop.aspectj.SPermission;
import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;
import com.atom.aop.utils.PermissionConsts;
import com.atom.aop.utils.PermissionUtils;


public class MainActivity extends AppCompatActivity {
    long curr = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = onMainClic4(v);
                if(s != null ){
                    Log.e("MainActivity", s);
                }
                Log.e("MainActivity", "气不气");
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

    @SPermission({PermissionConsts.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public String onMainClic4(View v2 ) {
        long l = System.currentTimeMillis();
        Log.e("MainActivity", (l - curr) + "   爱仕达");
        curr = l;
        return "asdasdad" ;
    }


    boolean isSure = false;

}