package com.wufanguitar.annotate;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 吴凡
 * @Email: wufan01@sunlands.com
 * @Time: 2018/6/26  15:18
 * @Description:
 */

@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.TYPE,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@Inherited
@IntDef(value = {
        Dismiss.EVENT_KEY_BACK,
        Dismiss.EVENT_OUT_SIDE,
        Dismiss.EVENT_CANCEL
})

public @interface Dismiss {
    /**
     * 返回键
     */
    int EVENT_KEY_BACK = 0;

    /**
     * 外部
     */
    int EVENT_OUT_SIDE = 1;

    /**
     * CANCEL
     */
    int EVENT_CANCEL = 2;

}
