package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by hzjixinyu on 2015/8/12.
 */
public class MyViewPager extends ViewPager{
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
