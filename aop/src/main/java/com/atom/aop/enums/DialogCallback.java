package com.atom.aop.enums;

public interface DialogCallback {

    boolean dialogShow(DialogListener listener , Object... result);

    void dialogResult(boolean isSure , Object... result) ;

    interface DialogListener{
        void select(boolean isSure) ;
    }
}
