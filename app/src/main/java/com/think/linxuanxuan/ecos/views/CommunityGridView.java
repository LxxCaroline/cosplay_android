package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CommunityGridView extends GridView {
    public CommunityGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommunityGridView(Context context) {
        super(context);
    }

    public CommunityGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
