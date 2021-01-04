package com.atom.saop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atom.aop.aspectj.Log;
import com.atom.aop.enums.LogType;
@Log(tag = "asdzxcasd" , priority = android.util.Log.ERROR , type = LogType.all)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            //@Log(tag = "zxczxczxc" , priority = android.util.Log.ERROR , type = LogType.after)
            @Override
            public void onClick(View v) {
                for (int idx = 100; idx > 0; idx--) {

                }
                ((TextView) v).setText("计算了计算了");

            }
        });
    }
    //@Log(priority = android.util.Log.ERROR)
    public void test(View v){
        for (int idx = 100; idx > 0; idx--) {

        }
        ((TextView) v).setText("计算了计算了");
    }
}