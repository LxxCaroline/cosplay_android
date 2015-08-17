package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import com.think.linxuanxuan.ecos.R;
import java.lang.reflect.Method;

/**
 * Created by hzjixinyu on 2015/8/5.
 */
public class PopupHelper {


    public interface IPopupListner{
        void clickListner(int type, View v, PopupWindow popupWindow);
    }

    public static void showSixTypePopupWindow(final PopupWindow popupWindow, Context context,View view, final IPopupListner listner) {

        final RadioGroup rg = (RadioGroup) popupWindow.getContentView().findViewById(R.id.rg_type);
        final PopupWindow finalPopupWindow = popupWindow;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //use
                int type = 0;
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rbtn1:
                        type = 0;
                        break;
                    case R.id.rbtn2:
                        type = 1;
                        break;
                    case R.id.rbtn3:
                        type = 2;
                        break;
                    case R.id.rbtn4:
                        type = 3;
                        break;
                    case R.id.rbtn5:
                        type = 4;
                        break;
                    case R.id.rbtn6:
                        type = 5;
                        break;
                    case R.id.rbtn7:
                        type = 6;
                        break;
                    case R.id.rbtn8:
                        type = 7;
                        break;
                }
                listner.clickListner(type, popupWindow.getContentView().findViewById(checkedId), popupWindow);
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);
        setPopupWindowTouchModal(popupWindow, false);// 使得popupWindow在显示的时候，popupWindow外部的控件也能够获得焦点
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable(context.getResources().getColor(android.R.color.transparent)));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }

    public  static PopupWindow newSixTypePopupWindow(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_six_type, null);
        return  new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }



    public static void showShareSortTypePopupWindow(final PopupWindow popupWindow, Context context,View view, final IPopupListner listner) {

        final RadioGroup rg = (RadioGroup) popupWindow.getContentView().findViewById(R.id.rg_type);
        final PopupWindow finalPopupWindow = popupWindow;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //use
                int type = 0;
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rbtn1:
                        type = 0;
                        break;
                    case R.id.rbtn2:
                        type = 1;
                        break;
                    case R.id.rbtn3:
                        type = 2;
                        break;
                    case R.id.rbtn4:
                        type = 3;
                        break;
                }
                listner.clickListner(type, popupWindow.getContentView().findViewById(checkedId), popupWindow);
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);
        setPopupWindowTouchModal(popupWindow, false);// 使得popupWindow在显示的时候，popupWindow外部的控件也能够获得焦点
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable(context.getResources().getColor(android.R.color.transparent)));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }

    public  static PopupWindow newShareSortTypePopupWindow(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_share_sort_type, null);
        return  new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }


    public static void showRecruiteSortTypePopupWindow(final PopupWindow popupWindow, Context context,View view, final IPopupListner listner) {

        final RadioGroup rg = (RadioGroup) popupWindow.getContentView().findViewById(R.id.rg_sort);
        final PopupWindow finalPopupWindow = popupWindow;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //use
                int type = 0;
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rbtn1:
                        type = 0;
                        break;
                    case R.id.rbtn2:
                        type = 1;
                        break;
                    case R.id.rbtn3:
                        type = 2;
                        break;
                    case R.id.rbtn4:
                        type = 3;
                        break;
                }
                listner.clickListner(type, popupWindow.getContentView().findViewById(checkedId), popupWindow);
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);
        setPopupWindowTouchModal(popupWindow, false);// 使得popupWindow在显示的时候，popupWindow外部的控件也能够获得焦点
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable(context.getResources().getColor(android.R.color.transparent)));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }

    public  static PopupWindow newRecruiteSortTypePopupWindow(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_recruite_sort, null);
        return  new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }

    public static void showRecruiteTypePopupWindow(final PopupWindow popupWindow, Context context,View view, final IPopupListner listner) {

        final RadioGroup rg = (RadioGroup) popupWindow.getContentView().findViewById(R.id.rg_type);
        final PopupWindow finalPopupWindow = popupWindow;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //use
                int type = 0;
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rbtn1:
                        type = 0;
                        break;
                    case R.id.rbtn2:
                        type = 1;
                        break;
                    case R.id.rbtn3:
                        type = 2;
                        break;
                    case R.id.rbtn4:
                        type = 3;
                        break;
                    case R.id.rbtn5:
                        type = 4;
                        break;
                    case R.id.rbtn6:
                        type = 5;
                        break;
                }
                listner.clickListner(type, popupWindow.getContentView().findViewById(checkedId), popupWindow);
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);
        setPopupWindowTouchModal(popupWindow, false);// 使得popupWindow在显示的时候，popupWindow外部的控件也能够获得焦点
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable(context.getResources().getColor(android.R.color.transparent)));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }


    public  static PopupWindow newRecruiteTypePopupWindow(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_recruite_type, null);
        return  new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }




    public static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
