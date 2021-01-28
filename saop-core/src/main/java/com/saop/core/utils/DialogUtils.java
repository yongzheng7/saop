package com.saop.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.saop.api.DialogHandler;
import com.saop.api.DialogParameters;
import com.saop.core.SAOP;

/**
 * 弹窗相关工具类
 */
public final class DialogUtils {

    private static DialogUtils sInstance;
    private DialogHandler mDialogCallback;
    private final DialogParameters mParameters = new DialogParameters();

    public static DialogUtils dialog() {
        return new DialogUtils();
    }

    private DialogUtils() {
        sInstance = this;
    }

    public DialogUtils callback(final DialogHandler callback) {
        mDialogCallback = callback;
        return this;
    }

    public DialogUtils setTitle(final String dialogTitle) {
        this.mParameters.setTitle(dialogTitle);
        return this;
    }

    public DialogUtils setMessage(final String dialogMessage) {
        this.mParameters.setMessage(dialogMessage);
        return this;
    }

    public void show() {
        if (mDialogCallback.isShow(mParameters, null)) {
            DialogActivity.start(SAOP.getContext());
        }
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
                    .setTitle(sInstance.mParameters.getTitle())
                    .setMessage(sInstance.mParameters.getMessage())
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
            SAOP.handler().post(() -> {
                if (sInstance != null) {
                    sInstance.requestCallback();
                }
            });
        }
    }
}