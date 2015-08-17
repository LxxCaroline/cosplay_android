package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 类描述：
 * Created by enlizhang on 2015/7/22.
 */
public class ExtensibleListView extends ListView{


    public ExtensibleListView(Context context) {
        super(context);
    }

    public ExtensibleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
