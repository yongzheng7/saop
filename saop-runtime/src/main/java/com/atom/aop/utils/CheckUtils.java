package com.atom.aop.utils;

import com.atom.aop.aspectj.AopCheck;

public class CheckUtils {
    public interface CheckCallback {
        boolean isCorrect(AopCheck check);

        void checkSkip(AopCheck check);
    }
}
