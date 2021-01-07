package com.atom.saop;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atom.aop.aspectj.VPermission;
import com.atom.aop.aspectj._Log;
import com.atom.aop.aspectj.VDialog;
import com.atom.aop.aspectj._Permission;
import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;
import com.atom.aop.utils.PermissionConsts;
import com.atom.aop.utils.log.Logger;

@_Log(priority = Log.ERROR, tag = "MainActivity_init")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_log).setOnClickListener(v -> {
            test_print_log(v);
            Logger.e(test_print_log_by_return(v));
        });


        findViewById(R.id.test_dialog_void_before_invalid).setOnClickListener(v -> {
            test_dialog_before(v);
        });

        findViewById(R.id.test_dialog_void_before).setOnClickListener(v -> {
            test_dialog_before(v, new DialogCallback() {
                @Override
                public boolean dialogShow(DialogListener listener, Object... result) {
                    v.post(() -> new AlertDialog.Builder(MainActivity.this)
                            .setTitle("asdasd")
                            .setMessage("asdasdad")
                            .setPositiveButton("确定", (dialog, which) -> listener.select(true))
                            .setNegativeButton("取消", (dialog, which) -> listener.select(false))
                            .create()
                            .show());
                    return false;
                }

                @Override
                public void dialogResult(boolean isSure, Object... result) {
                    Logger.e("test_ show dialog by void -------------> " + isSure);
                }
            });
        });

        findViewById(R.id.test_dialog_void_after).setOnClickListener(v -> {

        });
        findViewById(R.id.test_Permission_sync).setOnClickListener(v -> {
            // 同步获取 , 只需要执行一次 , 获取完权限,根据权限获取是否全部满足,进行判定执行与否
            // 注意 该注解使用@VPermission 方法返回为void ,返回其他注解无效
            // 如果方法需要返回数据,请使用@_Permission异步获取
            // 注意, 如果又想要同步执行,还想要返回参数,建议使用回掉接口形式 .
            test_permission_sync_void(v) ;
        });
        findViewById(R.id.test_Permission_async_void).setOnClickListener(v -> {
            // 异步获取 , 如果需要执行的操作无权限一般需要点击两次,
            // 第一次进行获取权限[同时因无权限不会执行] ,
            // 第二次直接进行权限判定,如果权限满足,直接进行执行方法内的操作
            test_permission_async_void(v) ;

        });
        findViewById(R.id.test_Permission_async_return).setOnClickListener(v -> {
            String s = test_permission_async_return(v);
            Logger.e(s);
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
    @_Log(priority = Log.ERROR, tag = "MainActivity")
    public void test_print_log(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @_Log(priority = Log.ERROR, tag = "MainActivity_result")
    public String test_print_log_by_return(View view) {
        Logger.e("test_ show log by return String -------------> start");
        long test = test();
        Logger.e("test_ show log by return String -------------> end");
        return "test_ show log by return String -------------> return [ result = " + test + "]";
    }


    /*------------ Dialog Test ------------*/
    // 如果参数中没有DialogCallback 参数即便写上@VDialog也不起任何作用
    @VDialog(type = DialogRunType.runBefore)
    public void test_dialog_before(View view) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    @VDialog(type = DialogRunType.runBefore)
    public void test_dialog_before(View view, DialogCallback callback) {
        Logger.e("test_ show log by void -------------> start");
        Logger.e("test_ show log by void -------------> result [" + test() + "]");
        Logger.e("test_ show log by void -------------> end");
    }

    public void test_dialog_after(View view, DialogCallback callback) {

    }
    @_Permission({PermissionConsts.CAMERA , PermissionConsts.LOCATION})
    public void test_permission_async_void(View view) {
        Logger.e("test_ show permission by void -------------> result [" + test() + "]");
    }

    @_Permission({PermissionConsts.PHONE , Manifest.permission.RECORD_AUDIO , Manifest.permission.READ_EXTERNAL_STORAGE})
    public String test_permission_async_return(View view) {
        Logger.e("test_ show permission by String -------------> result [" + test() + "]");
        return "test_ show permission by String -------------> ";
    }

    @VPermission({Manifest.permission.READ_CONTACTS , Manifest.permission.SEND_SMS})
    public void test_permission_sync_void(View view) {
        Logger.e("test_ show permission by void sync-------------> result [" + test() + "]");
    }
}