package com.atom.saop;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.atom.aop.aspectj.AopPermissionVoid;
import com.atom.aop.aspectj.AopClick;
import com.atom.aop.aspectj.AopLog;
import com.atom.aop.aspectj.AopDialog;
import com.atom.aop.aspectj.AopPermission;
import com.atom.aop.enums.DialogRunType;
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
           // test_print_log(v);
            test_print_log_target(v);
            //Logger.e(test_print_log_by_return(v));
        });

        // 同步Dialog , 需要确定才能执行 , 一行注解搞定 , 限制被注解函数 返回void 否则注解无效
        findViewById(R.id.test_dialog_void_before).setOnClickListener(v -> {
            //test_dialog_before(v);
            test_dialog_before_by_callback(v, new DialogUtils.DialogCallback() {
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
            });
        });
        // 同步dialog , 执行后,显示dialog,若没有回调DialogUtils.DialogCallback , 只会是一个提示的作用 ,如果有则返回选择操作
        findViewById(R.id.test_dialog_void_after).setOnClickListener(v -> {
            //test_dialog_after(v);
            test_dialog_after_by_callback(v, new DialogUtils.DialogCallback() {

                @Override
                public void onSure() {
                    Logger.e("dialog 选择了 onSure  , 还回调回来了");
                    View viewById = findViewById(R.id.hello);
                    if (viewById instanceof TextView) {
                        final TextView text = (TextView) viewById;
                        text.setText("Hello Test" + (new Date(System.currentTimeMillis()).toString()));
                    }
                }

                @Override
                public void onCancel() {
                    Logger.e("dialog 选择了 onCancel  , 还回调回来了");
                }
            });
        });
        // 同步dialog , 注解在构造函数上
        findViewById(R.id.test_dialog_return_before).setOnClickListener(v -> {
            test_dialog_before_by_constructor(v);
        });

        // 同步权限
        findViewById(R.id.test_Permission_sync).setOnClickListener(v -> {
            // 同步获取 , 只需要执行一次 , 获取完权限,根据权限获取是否全部满足,进行判定执行与否
            // 注意 该注解使用@VPermission 方法返回为void ,返回其他注解无效
            // 如果方法需要返回数据,请使用@_Permission异步获取
            // 注意, 如果又想要同步执行,还想要返回参数,建议使用回掉接口形式 .
            test_permission_sync_void(v);
        });
        findViewById(R.id.test_Permission_async_void).setOnClickListener(v -> {
            // 异步获取 , 如果需要执行的操作无权限一般需要点击两次,
            // 第一次进行获取权限[同时因无权限不会执行] ,
            // 第二次直接进行权限判定,如果权限满足,直接进行执行方法内的操作
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

        findViewById(R.id.test_click_some).setOnClickListener(v -> {
            test_click_return_void_some(v);
            //Logger.e(test_click_return_string_some(v));
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



    public void test_print_log_target(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }


    /*------------ Dialog Test ------------*/
    // 如果参数中没有DialogCallback 参数即便写上@VDialog也不起任何作用
    @AopDialog(type = DialogRunType.runBefore, title = "重要提示", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行")
    public void test_dialog_before(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
        View viewById = findViewById(R.id.hello);
        if (viewById instanceof TextView) {
            final TextView text = (TextView) viewById;
            text.setText("Hello Test" + (new Date(System.currentTimeMillis()).toString()));
        }
    }

    @AopDialog(type = DialogRunType.runBefore, title = "重要提示", message = "当前功能需要使用网络下载,请检查是否是流量,如果是则点击sure继续执行 , [同时还会回调callback]")
    public void test_dialog_before_by_callback(View view, DialogUtils.DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @AopDialog(type = DialogRunType.runAfter, title = "重要提示", message = "当前下载完成,请退出后再进行刷新")
    public void test_dialog_after(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @AopDialog(type = DialogRunType.runAfter, title = "重要提示可带执行", message = "当前下载完成,点击sure后执行A操作  点击Cancel执行B操作")
    public void test_dialog_after_by_callback(View view, DialogUtils.DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    public void test_dialog_before_by_constructor(View v){
        Test test = new Test() ;
    }

    class Test {
        @AopDialog(type = DialogRunType.runBefore, title = "重要提示可带执行", message = "当前下载完成,点击sure后执行A操作  点击Cancel执行B操作")
        public Test() {
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
    public void test_click_return_void_some(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result  [> 2 * 500]time = " + (temp - curr));
        curr = temp;
    }

    @AopClick(number = 3, interval = 200)
    public String test_click_return_string_some(View view) {
        long temp = System.currentTimeMillis();
        Logger.e("test_ show click by  void -------------> result  [> 3 * 200]time = " + (temp - curr));
        long space = temp - curr;
        curr = temp;
        return "test_ show click by String -------------> [>2000] time =" + space;
    }
}