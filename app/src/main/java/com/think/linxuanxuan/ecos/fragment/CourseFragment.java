package com.think.linxuanxuan.ecos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.CourseCategoryActivity;
import com.think.linxuanxuan.ecos.activity.CourseDetailActivity;
import com.think.linxuanxuan.ecos.activity.CourseTypeActivity;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.adapter.CourseListViewAdapter;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest;
import com.think.linxuanxuan.ecos.request.course.GetBannerRequest;
import com.think.linxuanxuan.ecos.views.AnimationHelper;
import com.think.linxuanxuan.ecos.views.Banner;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;
import com.think.linxuanxuan.ecos.views.FloadingButton;
import com.think.linxuanxuan.ecos.views.ViewScrollListener;

import java.util.List;

/**
 * 教程页面
 */
public class CourseFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private Banner banner;
    private ImageView tv_type_1;
    private ImageView tv_type_2;
    private ImageView tv_type_3;
    private ImageView tv_type_4;
    private ImageView tv_type_5;
    private ImageView tv_type_6;
    private ImageView tv_type_7;
    private ImageView tv_type_8;
    private FloadingButton btn_floading;
    private ExtensibleListView lv_course;
    private ScrollView sv;
    private RelativeLayout rl_banner;

    private CourseListViewAdapter courseListViewAdapter;

    private GetBannerRequest requestBanner;
    private GetBannerResponse getBannerResponse;
    private CourseListRequest courseListRequest;
    private GetCourseResponse getCourseResponse;

    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        return fragment;
    }

    public CourseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_course, container, false);
        bindView();
        initListener();
//        initData();
        return mainView;
    }


    private void bindView() {
        banner = (Banner) mainView.findViewById(R.id.banner);
        btn_floading = (FloadingButton) mainView.findViewById(R.id.btn_floading);
        lv_course = (ExtensibleListView) mainView.findViewById(R.id.lv_course);
        tv_type_1 = (ImageView) mainView.findViewById(R.id.tv_type_1);
        tv_type_2 = (ImageView) mainView.findViewById(R.id.tv_type_2);
        tv_type_3 = (ImageView) mainView.findViewById(R.id.tv_type_3);
        tv_type_4 = (ImageView) mainView.findViewById(R.id.tv_type_4);
        tv_type_5 = (ImageView) mainView.findViewById(R.id.tv_type_5);
        tv_type_6 = (ImageView) mainView.findViewById(R.id.tv_type_6);
        tv_type_7 = (ImageView) mainView.findViewById(R.id.tv_type_7);
        tv_type_8 = (ImageView) mainView.findViewById(R.id.tv_type_8);
        sv = (ScrollView) mainView.findViewById(R.id.sv);
        rl_banner=(RelativeLayout)mainView.findViewById(R.id.rl_banner);
        //sv.scrollTo(10,10);
    }


    private void initData() {
        /**获取banner信息**/
        requestBanner = new GetBannerRequest();
        getBannerResponse = new GetBannerResponse();
        requestBanner.request(getBannerResponse);

        courseListRequest = new CourseListRequest();
        getCourseResponse = new GetCourseResponse();
        courseListRequest.request(getCourseResponse, CourseListRequest.Type.推荐, null, null, null, 0);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_floading) {
            Intent intent = new Intent(CourseFragment.this.getActivity(), CourseTypeActivity.class);
            startActivity(intent);
            return;
        }
        Course.CourseType courseType = Course.CourseType.化妆;
        switch (v.getId()) {
            case R.id.tv_type_1:
                courseType = Course.CourseType.化妆;
                break;
            case R.id.tv_type_2:
                courseType = Course.CourseType.道具;
                break;
            case R.id.tv_type_3:
                courseType = Course.CourseType.摄影;
                break;
            case R.id.tv_type_4:
                courseType = Course.CourseType.后期;
                break;
            case R.id.tv_type_5:
                courseType = Course.CourseType.假发;
                break;
            case R.id.tv_type_6:
                courseType = Course.CourseType.服装;
                break;
            case R.id.tv_type_7:
                courseType = Course.CourseType.心得;
                break;
            case R.id.tv_type_8:
                courseType = Course.CourseType.其他;
                break;
        }
        Intent intent = new Intent(getActivity(), CourseCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CourseCategoryActivity.CourseCategory, courseType.getBelongs());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        lv_course.setFocusable(false);
        banner.setFocusable(true);
        banner.setFocusableInTouchMode(true);
        btn_floading.reset();
        sv.fullScroll(View.FOCUS_UP);
    }

    private void initListener() {
        lv_course.setDividerHeight(0);
        sv.setOnTouchListener(new ViewScrollListener(new ViewScrollListener.IOnMotionEvent() {
            @Override
            public void doInDown() {
                if (btn_floading.isAppear()) {
                    btn_floading.disappear(new AnimationHelper.DoAfterAnimation() {
                        @Override
                        public void doAfterAnimation() {
                            btn_floading.setIsDisappear();
                            btn_floading.setIsAnim(false);
                        }
                    });
                }
            }

            @Override
            public void doInUp() {
                if (btn_floading.isDisappear()) {
                    btn_floading.appear(new AnimationHelper.DoAfterAnimation() {
                        @Override
                        public void doAfterAnimation() {
                            btn_floading.setIsAppear();
                            btn_floading.setIsAnim(false);
                        }
                    });
                }
            }

            @Override
            public void doInChangeToDown() {
                btn_floading.disappear(new AnimationHelper.DoAfterAnimation() {
                    @Override
                    public void doAfterAnimation() {
                        btn_floading.setIsDisappear();
                        btn_floading.setIsAnim(false);
                    }
                });
            }

            @Override
            public void doInChangeToUp() {
                btn_floading.appear(new AnimationHelper.DoAfterAnimation() {
                    @Override
                    public void doAfterAnimation() {
                        btn_floading.setIsAppear();
                        btn_floading.setIsAnim(false);
                    }
                });
            }
        }));

        btn_floading.setOnClickListener(this);
        tv_type_1.setOnClickListener(this);
        tv_type_2.setOnClickListener(this);
        tv_type_3.setOnClickListener(this);
        tv_type_4.setOnClickListener(this);
        tv_type_5.setOnClickListener(this);
        tv_type_6.setOnClickListener(this);
        tv_type_7.setOnClickListener(this);
        tv_type_8.setOnClickListener(this);
        banner.vp_image.requestFocus();
    }

    class GetBannerResponse extends BaseResponceImpl implements GetBannerRequest.IGetBannerResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(getActivity(), "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(List<String> bannerList) {
            banner.setURLList(bannerList);
        }

    }

    class GetCourseResponse extends BaseResponceImpl implements CourseListRequest.ICourseListResponse {

        @Override
        public void success(List<Course> courseList) {
            courseListViewAdapter = new CourseListViewAdapter(getActivity(), courseList);
            lv_course.setAdapter(courseListViewAdapter);
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(getActivity(), "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        Log.i("course", "----------页面隐藏--------------" + (!hidden ? "教程页面显示" : "教程页面隐藏"));
        Toast.makeText(getActivity(),!hidden?"教程页面显示":"教程页面隐藏",Toast.LENGTH_LONG).show();
    }


    /***
     * 释放内存
     */
    public void releaseMemory(){
        lv_course.setAdapter(null);
//        courseListViewAdapter = null;
//        banner.releaseMemory();
        Log.i("course", "释放内存");
    }

    public void reloadData(){

        if(courseListViewAdapter==null){
            requestBanner = new GetBannerRequest();
            getBannerResponse = new GetBannerResponse();
            requestBanner.request(getBannerResponse);
            Log.i("course", "重新加载请求数据");
        }
        else{
            lv_course.setAdapter(courseListViewAdapter);
            Log.i("course", "数据已经加载过");
        }

        requestBanner = new GetBannerRequest();
        getBannerResponse = new GetBannerResponse();
        requestBanner.request(getBannerResponse);

       /* if(banner.isReleased()){
            requestBanner = new GetBannerRequest();
            getBannerResponse = new GetBannerResponse();
            requestBanner.request(getBannerResponse);
        }
        else{
            reloadData();
        }*/
    }


}
