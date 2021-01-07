package com.atom.saop;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atom.aop.aspectj.VPermission;
import com.atom.aop.aspectj.SDialog;
import com.atom.aop.aspectj.Permission;
import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;
import com.atom.aop.utils.PermissionConsts;


public class MainActivity extends AppCompatActivity {
    long curr = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMainClic5(v);
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

    @Permission({PermissionConsts.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public String onMainClic4(View v2 ) {
        long l = System.currentTimeMillis();
        Log.e("MainActivity", (l - curr) + "   爱仕达");
        curr = l;
        return "asdasdad" ;
    }

    @VPermission({PermissionConsts.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onMainClic5(View v2 ) {
        long l = System.currentTimeMillis();
        Log.e("MainActivity", (l - curr) + "   爱仕达asdasdasd");
        curr = l;
    }


}