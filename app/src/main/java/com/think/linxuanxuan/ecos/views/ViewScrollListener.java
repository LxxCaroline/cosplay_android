package com.think.linxuanxuan.ecos.views;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hzjixinyu on 2015/7/28.
 */
public class ViewScrollListener implements View.OnTouchListener{

    public interface IOnMotionEvent{
        void doInDown();
        void doInUp();
        void doInChangeToDown();
        void doInChangeToUp();
    }

    IOnMotionEvent onMotionEvent;
    int y=0;
    String state="up";

    public ViewScrollListener(IOnMotionEvent onMotionEvent){
        this.onMotionEvent=onMotionEvent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String nowState=state;
        if (event.getAction()==MotionEvent.ACTION_DOWN){
//                    int offsetY=v.getScrollY()-v.getScrollY();
//                    Log.v("sroll","down "+v.getScrollY()+"");
        }
        if (event.getAction()==MotionEvent.ACTION_MOVE){
//                    Log.v("sroll","move "+v.getScrollY()+"");
            int offsetY=v.getScrollY()-y;
            if (offsetY>30){
                nowState="down";
                y=v.getScrollY();
                Log.v("sroll", "down " + v.getScrollY() + "");
                onMotionEvent.doInDown();
            }
            if (offsetY<-30){
                nowState="up";
                y=v.getScrollY();
                Log.v("sroll","up "+v.getScrollY()+"");
                onMotionEvent.doInUp();
            }

            if (!TextUtils.equals(nowState, state)){
                if (TextUtils.equals(nowState,"up")){
                    Log.v("sroll","change up "+v.getScrollY()+"");
                    onMotionEvent.doInChangeToUp();
                }
                if (TextUtils.equals(nowState,"down")){
                    Log.v("sroll","change down "+v.getScrollY()+"");
                    onMotionEvent.doInChangeToDown();
                }
                state=nowState;
            }
        }

        if (event.getAction()==MotionEvent.ACTION_UP){
//                    Log.v("sroll","up " +v.getScrollY()+"");
        }

        return false;
    }
}
