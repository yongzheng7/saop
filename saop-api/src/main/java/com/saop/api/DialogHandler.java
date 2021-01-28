package com.saop.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface DialogHandler {

    boolean isShow(@NonNull DialogParameters parameters, @Nullable Object result);

    void onSure();

    void onCancel();
}
