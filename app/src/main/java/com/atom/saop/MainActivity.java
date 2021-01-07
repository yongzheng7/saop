package com.atom.saop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atom.aop.aspectj.DLog;
import com.atom.aop.utils.log.Logger;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_log).setOnClickListener(v -> {
            test_print_log(v);
            //Logger.e(test_print_log_by_return(v));
        });
    }

    private long test() {
        long result = 0;
        for (int idx = 999; idx > 0; idx--) {
            int temp = (int) (Math.random() * 1000);
            if (temp > idx) {
                result = temp;
            }
        }
        return result;
    }

    /*------------ Log Test ------------*/
    @DLog(priority = Log.ERROR , tag = "MainActivity")
    public void test_print_log(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result ["+test()+"]");
        Logger.e("test_ show log by void -------------> end");
    }

    @DLog(priority = Log.ERROR, tag = "MainActivity_result")
    public String test_print_log_by_return(View view) {
        Logger.e("test_ show log by return String -------------> start");
        long test = test();
        Logger.e("test_ show log by return String -------------> end");
        return "test_ show log by return String -------------> return [ result = " + test + "]";
    }
}