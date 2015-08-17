package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.CourseDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hzjixinyu on 2015/7/24.
 */
public class Banner extends RelativeLayout {

    private Context mContext;

    private TextView tv_currentNum;
    private ImageView iv_currentNum;
    public MyViewPager vp_image;

    private int[] pagerViewIDs = new int[]{R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4};

    private List<String> URLList = new ArrayList<>();
    private List<View> ViewList = new ArrayList<>();
    private int count = 4;
    private int delayTime = 3000;

    private int pagerViewID = R.layout.item_bannerpager;
    private PagerAdapter pagerAdapter;

    private Timer timer;
    private TimerTask task;

    public Banner(Context context) {
        super(context);
        this.mContext = context;
        initView();
        initAuto();
        mQueue  = Volley.newRequestQueue(context);
    }


    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        initAuto();
    }


    public RequestQueue mQueue;
    public void setURLList(List<String> data) {
        this.URLList = data;
        for (int i = 0; i < data.size(); i++) {
            View v = View.inflate(mContext, pagerViewID, null);

            if (!TextUtils.isEmpty(URLList.get(i))){
                Picasso.with(mContext).load(URLList.get(i)).into((ImageView) v.findViewById(R.id.iv_image));
            }else{
                ((ImageView) v.findViewById(R.id.iv_image)).setImageResource(R.mipmap.img_default);
            }

            Log.i("setURLList", "--------------------------" + URLList.get(i));
            ViewList.add(v);
        }

        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ViewList.get(position % count).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                    Bundle bundle = new Bundle();
                    intent = new Intent(mContext, CourseDetailActivity.class);
                    switch (mygetCount()) {
                        case 0:
                            bundle.putString(CourseDetailActivity.CourseID, "250");
                            break;
                        case 1:
                            bundle.putString(CourseDetailActivity.CourseID, "251");
                            break;
                        case 2:
                            bundle.putString(CourseDetailActivity.CourseID, "249");
                            break;
                        case 3:
                            bundle.putString(CourseDetailActivity.CourseID, "252");
                            break;
                    }
                    intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
                container.addView(ViewList.get(position % count));
                return ViewList.get(position % count);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((View) ViewList.get(position % count)).clearFocus();
                container.removeView((View) ViewList.get(position % count));
            }
        };

        vp_image.setAdapter(pagerAdapter);
        vp_image.setCurrentItem(count * 20);
        vp_image.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int num = vp_image.getCurrentItem() % count;
                    tv_currentNum.setText(num + 1 + "");
                    iv_currentNum.setImageResource(pagerViewIDs[num]);
                }
            }
        });
    }

    public void setDelayTime(int time) {
        this.delayTime = time;
    }

    private void initView() {
        View.inflate(mContext, R.layout.item_bannerlayout, this);
        vp_image = (MyViewPager) findViewById(R.id.vp_image);
        tv_currentNum = (TextView) findViewById(R.id.tv_currentNum);
        iv_currentNum = (ImageView) findViewById(R.id.iv_currentNum);
        vp_image.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (timer == null) {
                        timer = new Timer();
                        timer.schedule(getNewTask(), delayTime, delayTime);
                    }
                }
                return false;
            }
        });
    }

    private void initAuto() {
        timer = new Timer();
        timer.schedule(getNewTask(), delayTime, delayTime);
    }

    private TimerTask getNewTask() {
        task = null;
        task = new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        vp_image.setCurrentItem(vp_image.getCurrentItem() + 1);
                    }
                });
            }
        };
        return task;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (timer == null) {
                timer = new Timer();
                timer.schedule(getNewTask(), delayTime, delayTime);
            }
        }
        return true;
    }

    public void releaseMemory() {
        vp_image.setAdapter(null);
    }

    public boolean isReleased(){
        return pagerAdapter==null || URLList==null || URLList.size()==0;
    }

    public void reloadData() {
        vp_image.setAdapter(pagerAdapter);
    }

    public int mygetCount(){
        return vp_image.getCurrentItem()%count;
    }
}
