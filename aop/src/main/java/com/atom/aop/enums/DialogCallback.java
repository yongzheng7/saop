package com.atom.aop.enums;

public interface DialogCallback {

    boolean dialogShow(DialogListener listener , Object... result);

    interface DialogListener{
        void select(boolean isSure) ;
    }
}
