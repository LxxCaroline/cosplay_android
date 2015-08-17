package com.think.linxuanxuan.ecos.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.think.linxuanxuan.ecos.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

/**
 * Created by hzzhanyawei on 2015/8/2.
 */
public class RoundAngleImageView extends ImageView{

    private Paint paint;
    private int roundWidth = 5;
    private int roundHeight = 5;
    private int leftUpRoundWidth;
    private int leftUpRoundHeight;
    private int rightUpRoundWidth;
    private int rightUpRoundHeight;
    private int leftDownRoundWidth;
    private int leftDownRoundHeight;
    private int rightDownRoundWidth;
    private int rightDownRoundHeight;
    private Paint paint2;

    private byte angleSwitch = 0x00;

    private Target mTarget;


    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            initAngleWH(ta);
            initAngleSwitch(ta);
        }else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth*density);
            roundHeight = (int) (roundHeight*density);
            leftUpRoundWidth = rightUpRoundWidth = leftDownRoundWidth = rightDownRoundWidth = roundWidth;
            leftUpRoundHeight = rightUpRoundHeight = leftDownRoundHeight = rightDownRoundHeight = roundHeight;
            angleSwitch |= 0x0f;
        }
        initPaint();
    }

    /**
     * initialization every corner angle width and height according xml.
     * */
    private void initAngleWH(TypedArray ta){
        roundWidth = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, roundWidth);
        roundHeight = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, roundHeight);
        leftUpRoundWidth = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_leftUpRoundWidth, roundWidth);
        leftUpRoundHeight = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_leftUpRoundHeight, roundHeight);
        rightUpRoundWidth = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_rightUpRoundWidth, roundWidth);
        rightUpRoundHeight = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_rightUpRoundHeight, roundHeight);
        leftDownRoundWidth = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_leftDownRoundWidth, roundWidth);
        leftDownRoundHeight = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_leftDownRoundHeight, roundHeight);
        rightDownRoundWidth = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_rightDownRoundWidth, roundWidth);
        rightDownRoundHeight = ta.getDimensionPixelSize(R.styleable.RoundAngleImageView_rightDownRoundHeight, roundHeight);
    }

    /**
     * set angle switch according xml property.
     * */
    private void initAngleSwitch(TypedArray ta){
        if(ta.getBoolean(R.styleable.RoundAngleImageView_isLeftUp, true)){
            angleSwitch |= 0x08;
        }
        if(ta.getBoolean(R.styleable.RoundAngleImageView_isRightUp, true)){
            angleSwitch |= 0x04;
        }
        if(ta.getBoolean(R.styleable.RoundAngleImageView_isLeftDown, true)){
            angleSwitch |= 0x02;
        }
        if(ta.getBoolean(R.styleable.RoundAngleImageView_isRightDown, true)){
            angleSwitch |= 0x01;
        }
    }

    private void initPaint(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        if ((angleSwitch & 0x08) != 0x00) {
            drawLeftUp(canvas2, leftUpRoundWidth, leftUpRoundHeight);
        }
        if ((angleSwitch & 0x04) != 0x00) {
            drawRightUp(canvas2, rightUpRoundWidth, rightUpRoundHeight);
        }
        if ((angleSwitch & 0x02) != 0x00) {
            drawLeftDown(canvas2, leftDownRoundWidth, leftDownRoundHeight);
        }
        if ((angleSwitch & 0x01) != 0x00) {
            drawRightDown(canvas2, rightDownRoundWidth, rightDownRoundHeight);
        }
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLeftUp(Canvas canvas, int roundWidth, int roundHeight) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(0,0,roundWidth*2,roundHeight*2),-90,-90);
        path.close();
        canvas.drawPath(path, paint);
    }


    private void drawLeftDown(Canvas canvas, int roundWidth, int roundHeight) {
        Path path = new Path();
        path.moveTo(0, getHeight()-roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(0,getHeight()-roundHeight*2,0+roundWidth*2,getHeight()),90,90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas, int roundWidth, int roundHeight) {
        Path path = new Path();
        path.moveTo(getWidth()-roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight()-roundHeight);
        path.arcTo(new RectF(getWidth()-roundWidth*2,getHeight()-roundHeight*2,getWidth(),getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas, int roundWidth, int roundHeight) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth()-roundWidth, 0);
        path.arcTo(new RectF(getWidth()-roundWidth*2,0,getWidth(),0+roundHeight*2),-90,90);
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    /**此方法需要在onCreateView之前调用*/
    public void setImageFromUrl(String Url){
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }
        };
        /**可以在此处增加加载和出错图片*/
        Picasso.with(this.getContext()).load(Url).into(mTarget);
    }


}
