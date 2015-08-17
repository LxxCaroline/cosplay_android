package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Think on 2015/7/28.
 */
public class RoungImageView extends ImageView {
    private int borderColor = 0xfff2457d;
    private int borderWidth = 10;

    public RoungImageView(Context context) {
        super(context);
    }

    public RoungImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoungImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBorderColor(int resourceID) {
        borderColor = resourceID;
    }

    public void setBorderWidth(int border) {
        borderWidth = border;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        int i = 0;
        System.out.println(i++);
        if (drawable == null || getWidth() == 0 || getHeight() == 0)
            return;

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (b != null) {
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

            int radius = (getWidth() < getHeight()) ? getWidth() / 2 : getHeight() / 2;

            Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, radius * 2, radius * 2, false);
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        // Draws a circle to create the border
        paint.setColor(borderColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        // Draws the image subtracting the border width
        BitmapShader s = new BitmapShader(scaledBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(s);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - borderWidth - 0.5f, paint);

        return output;
    }
}
