package com.atom.saop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.atom.aop.aspectj.AopDialogAfter;
import com.atom.aop.aspectj.AopDialogBefore;
import com.atom.aop.aspectj.AopPermissionVoid;
import com.atom.aop.aspectj.AopClick;
import com.atom.aop.aspectj.AopLog;
import com.atom.aop.aspectj.AopPermission;
import com.atom.aop.utils.DialogUtils;
import com.atom.aop.utils.PermissionConsts;
import com.atom.aop.utils.log.Logger;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_log).setOnClickListener(v -> {
            test_print_log(v);
            //test_print_log_target(v);
            //Logger.e(test_print_log_by_return(v));
        });

        // 执行前Dialog @AopDialogBefore
        DialogUtils.DialogCallback dialogCallbackBefore = new DialogUtils.DialogCallback() {

            @Override
            public boolean isShow(@Nullable Object result) {
                if (result != null) {
                    Logger.e("after dialog 调用 isshow 返回结果了  , result = " + result.toString());
                }
                return true;
            }

            @Override
            public void onSure() {
                Logger.e("dialog 选择了 onSure  , 还回调回来了 before");
                View viewById = findViewById(R.id.hello);
                if (viewById instanceof TextView) {
                    final TextView text = (TextView) viewById;
                    text.setText("Hello Test" + (new Date(System.currentTimeMillis()).toString()));
                }
            }

            @Override
            public void onCancel() {
                Logger.e("dialog 选择了 onCancel  , 还回调回来了 before");
            }
        };

        findViewById(R.id.test_dialog_construction_before).setOnClickListener(v -> {
            test_dialog_before_by_constructor(v);
        });
        findViewById(R.id.test_dialog_void_before).setOnClickListener(v -> {
            test_dialog_before_void(v);
        });
        findViewById(R.id.test_dialog_void_before_callback).setOnClickListener(v -> {
            test_dialog_before_by_callback_void(v, dialogCallbackBefore);
        });
        findViewById(R.id.test_dialog_string_before).setOnClickListener(v -> {
            test_dialog_string_before(v);
        });
        findViewById(R.id.test_dialog_string_before_callback).setOnClickListener(v -> {
            test_dialog_string_before_callback(v, dialogCallbackBefore);
        });

        // 执行后Dialog @AopDialogAfter


        findViewById(R.id.test_dialog_construction_after).setOnClickListener(v -> {
            test_dialog_after_by_constructor(v);
        });
        findViewById(R.id.test_dialog_void_after).setOnClickListener(v -> {
            test_dialog_after_void(v);
        });
        findViewById(R.id.test_dialog_void_after_callback).setOnClickListener(v -> {
            test_dialog_after_by_callback_void(v, dialogCallbackBefore);
        });
        findViewById(R.id.test_dialog_string_after).setOnClickListener(v -> {
            test_dialog_string_after(v);
        });
        findViewById(R.id.test_dialog_string_after_callback).setOnClickListener(v -> {
            test_dialog_string_after_callback(v, dialogCallbackBefore);
        });

        // 同步权限
        findViewById(R.id.test_Permission_sync).setOnClickListener(v -> {
            test_permission_sync_void(v);
        });
        findViewById(R.id.test_Permission_async_void).setOnClickListener(v -> {
            test_permission_async_void(v);

        });
        findViewById(R.id.test_Permission_async_return).setOnClickListener(v -> {
            String s = test_permission_async_return(v);
            Logger.e(s);
        });

        findViewById(R.id.test_click_single).setOnClickListener(v -> {
            //test_click_return_void_single(v) ;
            Logger.e(test_click_return_string_single(v));
        });

        findViewById(R.id.test_click_some_double).setOnClickListener(v -> {
            test_click_return_void_some_double(v);
            //Logger.e(test_click_return_string_some(v));
        });
        findViewById(R.id.test_click_some_three).setOnClickListener(v -> {
            Logger.e(test_click_return_string_some_three(v));
        });

        findViewById(R.id.test_click_some_infinite).setOnClickListener(v -> {
            Logger.e(test_click_return_string_some_all(v));
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
    @AopLog(priority = Log.ERROR, tag = "MainActivity")
    public void test_print_log(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @AopLog(priority = Log.ERROR, tag = "MainActivity_result")
    public String test_print_log_by_return(View view) {
        Logger.e("test_ show log by return String -------------> start");
        long test = test();
        Logger.e("test_ show log by return String -------------> end");
        return "test_ show log by return String -------------> return [ result = " + test + "]";
    }

    @AopLog(priority = Log.ERROR, tag = "MainActivity_result")
    public void test_print_log_target(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }


    /*------------ Dialog Test ------------*/
    //
    @AopDialogBefore(title = "执行前Dialog_Void", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行")
    public void test_dialog_before_void(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        View viewById = findViewById(R.id.hello);
        if (viewById instanceof TextView) {
            final TextView text = (TextView) viewById;
            text.setText("Hello Test" + (new Date(System.currentTimeMillis()).toString()));
        }
    }

    @AopDialogBefore(title = "执行前Dialog_Callback_Void", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
    public void test_dialog_before_by_callback_void(View view, DialogUtils.DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @AopDialogBefore(title = "执行前Dialog_String", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行")
    public String test_dialog_string_before(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        View viewById = findViewById(R.id.hello);
        String s = "Hello Test" + (new Date(System.currentTimeMillis()).toString());
        if (viewById instanceof TextView) {
            final TextView text = (TextView) viewById;
            text.setText(s);
        }
        return s;
    }

    @AopDialogBefore(title = "执行前Dialog_Callback_String", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
    public String test_dialog_string_before_callback(View view, DialogUtils.DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        String s = "Hello Test" + (new Date(System.currentTimeMillis()).toString());
        return s;
    }

    public void test_dialog_before_by_constructor(View v) {
        DialogBefore test = new DialogBefore();
    }

    class DialogBefore {
        @AopDialogBefore(title = "执行前Dialog_构造函数", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
        public DialogBefore() {
            Logger.e("test_ show log by void -------------> start");
            Logger.e("test_ show log by void -------------> result [" + test() + "]");
            Logger.e("test_ show log by void -------------> end");
        }
    }

    @AopDialogAfter(title = "执行后Dialog_Void", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行")
    public void test_dialog_after_void(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        View viewById = findViewById(R.id.hello);
        if (viewById instanceof TextView) {
            final TextView text = (TextView) viewById;
            text.setText("Hello Test" + (new Date(System.currentTimeMillis()).toString()));
        }
    }

    @AopDialogAfter(title = "执行后Dialog_Callback_Void", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
    public void test_dialog_after_by_callback_void(View view, DialogUtils.DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @AopDialogAfter(title = "执行后Dialog_String", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行")
    public String test_dialog_string_after(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        View viewById = findViewById(R.id.hello);
        String s = "Hello Test" + (new Date(System.currentTimeMillis()).toString());
        if (viewById instanceof TextView) {
            final TextView text = (TextView) viewById;
            text.setText(s);
        }
        return s;
    }

    @AopDialogAfter(title = "执行后Dialog_Callback_String", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
    public String test_dialog_string_after_callback(View view, DialogUtils.DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        String s = "Hello Test" + (new Date(System.currentTimeMillis()).toString());
        return s;
    }

    public void test_dialog_after_by_constructor(View v) {
        DialogAfter test = new DialogAfter();
    }


    class DialogAfter {
        @AopDialogAfter(title = "执行后Dialog_构造函数", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
        public DialogAfter() {
            Logger.e("test_ show log by void -------------> start");
            Logger.e("test_ show log by void -------------> result [" + test() + "]");
            Logger.e("test_ show log by void -------------> end");
        }
    }

    /*------------ Permission Test ------------*/
    @AopPermission({PermissionConsts.CAMERA, PermissionConsts.LOCATION})
    public void test_permission_async_void(View view) {
        Logger.e("test_ show permission by void -------------> result [" + test() + "]");
    }

    @AopPermission({PermissionConsts.PHONE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE})
    public String test_permission_async_return(View view) {
        Logger.e("test_ show permission by String -------------> result [" + test() + "]");
        return "test_ show permission by String -------------> ";
    }

    @AopPermissionVoid({Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
    public void test_permission_sync_void(View view) {
        Logger.e("test_ show permission by void sync-------------> result [" + test() + "]");
    }


    /*------------ Click Test ------------*/
    long curr = System.currentTimeMillis();

    @AopClick()
    public void test_click_return_void_single(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result [" + test() + "]   [>1000]time = " + (temp - curr));
        curr = temp;
    }

    @AopClick(value = 2000)
    public String test_click_return_string_single(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result [" + test() + "]   [>2000]time = " + (temp - curr));
        long space = temp - curr;
        curr = temp;
        return "test_ show click by String -------------> [>2000] time =" + space;
    }

    @AopClick(number = 2)
    public void test_click_return_void_some_double(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result  [> 2 * 500]time = " + (temp - curr));
        curr = temp;
    }

    @AopClick(number = 3, interval = 200)
    public String test_click_return_string_some_three(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result  [> 3 * 200]time = " + (temp - curr));
        long space = temp - curr;
        curr = temp;
        return "test_ show click by String -------------> [>2000] time =" + space;
    }

    @AopClick(number = Integer.MAX_VALUE)
    public String test_click_return_string_some_all(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result  [> 3 * 200]time = " + (temp - curr));
        long space = temp - curr;
        curr = temp;
        return "test_ show click by String -------------> [>2000] time =" + space;
    }
}