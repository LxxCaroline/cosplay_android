package com.think.linxuanxuan.ecos.views;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by hzjixinyu on 2015/7/22.
 */
public class AnimationHelper {

    public interface DoAfterAnimation
    {
        void doAfterAnimation();
    }

    /**
     * 控件动画
     * @param v  控件
     * @param transX x位移
     * @param transY y位移
     * @param alphaB 开始透明度
     * @param alphaE 结束透明度
     * @param duration 动画时间
     */
    public static void setViewMoveAnimation(final View v,final int transX,final int transY,int alphaB, int alphaE, int duration, final DoAfterAnimation doAfterAnimation){
        RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) v.getLayoutParams();

        AnimationSet animationSet=new AnimationSet(false);
        TranslateAnimation translateAnimation=new TranslateAnimation(0,transX,0,transY);
        translateAnimation.setDuration(duration);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setFillEnabled(true);

        AlphaAnimation alphaAnimation=new AlphaAnimation(alphaB,alphaE);
        alphaAnimation.setDuration(duration);
        translateAnimation.setRepeatCount(0);

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams ll = (RelativeLayout.LayoutParams) v.getLayoutParams();
                if (ll.rightMargin != 0 && ll.bottomMargin != 0) {
                    ll.setMargins(0, 0, ll.rightMargin - transX, ll.bottomMargin - transY);
                } else {
                    ll.setMargins(ll.leftMargin + transX, ll.topMargin + transY, 0, 0);
                }
                v.setLayoutParams(ll);

                if (doAfterAnimation!=null){
                    doAfterAnimation.doAfterAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(animationSet);

    }

}
