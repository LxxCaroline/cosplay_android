package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;

/**
 * 主页悬浮按钮
 * Created by hzjixinyu on 2015/7/23.
 */
public class FloadingButton extends TextView{

    private Boolean isDisappear=false;
    private Boolean isAppear=true;
    private Boolean isAnim=false;

    public FloadingButton(Context context) {
        super(context);
        init();
    }

    public FloadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_publish));
    }

    /**
     * button appear
     * @param doAfterAnimation
     */
    public synchronized void appear(AnimationHelper.DoAfterAnimation doAfterAnimation){
        if (!IsAnim()&&isDisappear){
            setIsAnim(true);
            AnimationHelper.setViewMoveAnimation(this, 0, -300, 0, 1, 500, doAfterAnimation);
        }
    }

    /**
     * button disappear
     * @param doAfterAnimation
     */
    public synchronized void disappear(AnimationHelper.DoAfterAnimation doAfterAnimation){
        if (!IsAnim()&&isAppear){
            setIsAnim(true);
            AnimationHelper.setViewMoveAnimation(this, 0, 300, 1, 0, 500, doAfterAnimation);
        }
    }

    public synchronized void setIsDisappear(){
        isDisappear=true;
        isAppear=false;
    }

    public synchronized void setIsAppear(){
        isAppear=true;
        isDisappear=false;
    }

    public synchronized Boolean isAppear(){
        return isAppear;
    }

    public synchronized Boolean isDisappear(){
        return isDisappear;
    }

    public synchronized Boolean IsAnim(){
        return isAnim;
    }

    public synchronized void setIsAnim(Boolean b){
        isAnim=b;
    }

    public void reset(){
        if (!isAppear()){
            appear(new AnimationHelper.DoAfterAnimation(){

                @Override
                public void doAfterAnimation() {
                    setIsAppear();
                    setIsAnim(false);
                }
            });
        }else if(isAppear()) {
            setIsAppear();
        }
    }
}
