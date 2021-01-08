package com.atom.aop.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.atom.aop.SAOP;
import com.atom.aop.aspectj.VDialog;


public final class DialogUtils {

    private static DialogUtils sInstance;
    private DialogCallback mDialogCallback;
    private VDialog dialog;

    public static DialogUtils dialog() {
        return new DialogUtils();
    }

    private DialogUtils() {
        sInstance = this;
    }

    public DialogUtils callback(final DialogCallback callback) {
        mDialogCallback = callback;
        return this;
    }

    public DialogUtils set(final VDialog dialog) {
        this.dialog = dialog ;
        return this;
    }

    public void show() {
        DialogActivity.start(SAOP.getContext());
    }

    private volatile Boolean isSure = null;

    private void requestCallback() {
        if (mDialogCallback != null) {
            if (isSure) {
                mDialogCallback.onSure();
            } else {
                mDialogCallback.onCancel();
            }
            mDialogCallback = null;
        }
    }

    public static class DialogActivity extends Activity {

        public static void start(final Context context) {
            Intent starter = new Intent(context, DialogActivity.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (sInstance == null) {
                finish();
            }
            final Boolean[] isSure = new Boolean[1];
            // 请求权限 , 弹出dialog
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(sInstance.dialog.title())
                    .setMessage(sInstance.dialog.message())
                    .setPositiveButton("Sure", (dialog, which) -> {
                        isSure[0] = true;
                        dialog.dismiss();

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        isSure[0] = false;
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.setOnDismissListener(dialog -> {
                sInstance.isSure = isSure[0];
                finish();
            });
            alertDialog.setOnCancelListener(dialog -> {
                sInstance.isSure = false;
                finish();
            });
            alertDialog.show();
        }

        @Override
        public void finish() {
            super.finish();
            SAOP.getHandler().post(() -> {
                if (sInstance != null) {
                    sInstance.requestCallback();
                }
            });
        }
    }

    public interface DialogCallback {

        void onSure();

        void onCancel();

    }

}