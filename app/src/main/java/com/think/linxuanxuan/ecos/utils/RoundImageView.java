package com.think.linxuanxuan.ecos.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.android.volley.toolbox.NetworkImageView;
import com.think.linxuanxuan.ecos.R;

/**
 * 圆形ImageView
 * Created by hzzhanyawei on 2015/7/27.
 */
public class RoundImageView extends NetworkImageView{

    private int circle_color;
    private Boolean is_circle_show;
    private float circle_lind_width;


    public RoundImageView(Context context){
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        /**
         * 自定义控件属性
         * */
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.round_circle);
        circle_color = ta.getColor(R.styleable.round_circle_circle_color,  0xff00ff00);
        is_circle_show = ta.getBoolean(R.styleable.round_circle_showable, false);
        circle_lind_width = ta.getFloat(R.styleable.round_circle_line_width, 3.0f);
        ta.recycle();
    }

    /**
     * 绘制图片*/
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b =  ((BitmapDrawable)drawable).getBitmap();
        if(null == b)
        {
            return;
        }
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        int w = getWidth(), h = getHeight();
        paintWhiteRoundBackground(canvas, w);
        Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);


        if (is_circle_show){
            paintCircle(canvas, w);
        }
    }

    /**
     * 获取圆形图片
     * @param bmp
     * @param radius
     * @return Bitmap
     */
    private static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        Paint p = new Paint();
        p.setAntiAlias(true); //去除锯齿
        p.setColor(Color.parseColor("#ff0000"));
        p.setFilterBitmap(true);
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//两图相交，去上面图片的交集部分
        canvas.drawBitmap(sbmp, rect, rect, p);

        return output;
    }

    private void paintCircle(Canvas canvas, int Radius){
        Paint p = new Paint();
        p.setColor(circle_color);
        p.setAntiAlias(true); //去除锯齿
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(circle_lind_width);
        canvas.drawCircle(Radius / 2 + 0.7f, Radius / 2 + 0.7f, Radius / 2 - 2.0f, p);
    }

    private void paintWhiteRoundBackground(Canvas canvas, int Radius){
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setAntiAlias(true); //去除锯齿
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(Radius / 2 + 0.7f, Radius / 2 + 0.7f, Radius / 2 - 2.0f, p);
    }
}
