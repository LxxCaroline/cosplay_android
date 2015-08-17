package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.CourseListViewAdapter;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest;
import com.think.linxuanxuan.ecos.views.AnimationHelper;
import com.think.linxuanxuan.ecos.views.FloadingButton;
import com.think.linxuanxuan.ecos.views.ListViewListener;
import com.think.linxuanxuan.ecos.views.PopupHelper;
import com.think.linxuanxuan.ecos.views.XListView;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/27.
 */
public class CourseCategoryActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private static final String TAG = "Ecos---CourseCategory";
    public static final String CourseCategory = "CourseCategory";
    private Course.CourseType courseType;
    public static Course.CourseType courseTypes[] = {Course.CourseType.化妆, Course.CourseType.后期, Course.CourseType.摄影,
            Course.CourseType.服装, Course.CourseType.道具, Course.CourseType.假发, Course.CourseType.心得, Course.CourseType.其他,};
    @InjectView(R.id.iv_search)
    ImageView iv_search;
    @InjectView(R.id.lv_list)
    XListView lv_list;
    @InjectView(R.id.ll_sort)
    LinearLayout ll_sort;
    @InjectView(R.id.btn_floading)
    FloadingButton btn_floading;
    @InjectView(R.id.lly_left_action)
    LinearLayout lly_left_action;
    @InjectView(R.id.ll_left)
    LinearLayout ll_left;
    @InjectView(R.id.tv_left)
    TextView tv_left;

    @InjectView(R.id.ll_sortType)
    LinearLayout ll_sortType;
    @InjectView(R.id.tv_sortText)
    TextView tv_sortText;
    @InjectView(R.id.iv_sortIcon)
    ImageView iv_sortIcon;
    @InjectView(R.id.ll_location)
    LinearLayout ll_location;

    //for no data layout
    @InjectView(R.id.resultImageView)
    ImageView resultImageView;

    private PopupWindow popupSortType;
    private PopupWindow popupSixType = new PopupWindow();
    //to record which item is selected in  pop window
    private int selectPosition = 0;

    private static final CourseListRequest.SortRule[] SORT_RULES = {CourseListRequest.SortRule.时间, CourseListRequest.SortRule.被点赞数};

    private CourseListViewAdapter courseTypeListViewAdapter;
    //record the search keyword.
    private String searchWords = "";

    public static final int RequestCodeForSearch = 1;
    public static final int ResultCodeForSearch = 2;

    //for request.
    private CourseListRequest request;
    private CourseListResponse courseListResponse;

    private int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_type);
        ButterKnife.inject(this);
        initListener();
        initData();
    }

    private void initData() {
        //get the extras from intent
        courseType = Course.CourseType.getCourseType(getIntent().getExtras().getString(CourseCategory));
        tv_left.setText(courseType.name());
        ll_location.setVisibility(View.GONE);

        //设置列表Adapter
        lv_list.initRefleshTime(this.getClass().getSimpleName());
        lv_list.setPullLoadEnable(true);
        lv_list.setXListViewListener(this);
        ll_location.setOnClickListener(this);

        //获取course信息
        request = new CourseListRequest();
        courseListResponse = new CourseListResponse();
        showProcessBar(getResources().getString(R.string.loading));
        request.request(courseListResponse, CourseListRequest.Type.筛选, courseType, searchWords, SORT_RULES[selectPosition], 0);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_search:
                Intent intent1 = new Intent(CourseCategoryActivity.this, com.think.linxuanxuan.ecos.activity.SearchActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt(com.think.linxuanxuan.ecos.activity.SearchActivity.SEARCH_TYPE, com.think.linxuanxuan.ecos.activity
                        .SearchActivity.TYPE_COURSE);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.ll_left:
                if (popupSixType.isShowing()) {
                    popupSixType.dismiss();
                } else {
                    popupSixType = PopupHelper.newSixTypePopupWindow(CourseCategoryActivity.this);
                    PopupHelper.showSixTypePopupWindow(popupSixType, CourseCategoryActivity.this, v, new PopupHelper.IPopupListner() {
                        @Override
                        public void clickListner(int type, View v, PopupWindow popupWindow) {
                            pageIndex = 0;
                            tv_left.setText(((RadioButton) v).getText().toString());
                            courseType = courseTypes[type];
                            showProcessBar(getResources().getString(R.string.loading));
                            request.request(courseListResponse, CourseListRequest.Type.筛选, courseType, searchWords, SORT_RULES[selectPosition], 0);
                        }
                    });
                }
                break;
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.btn_floading:
                intent = new Intent(CourseCategoryActivity.this, BuildCourseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BuildCourseActivity.COURSE_TYPE, courseType.getBelongs());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_location:
                Toast.makeText(CourseCategoryActivity.this, getResources().getString(R.string.noWeixin), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(CourseCategoryActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
        //1秒后关闭刷新
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_list.stopRefresh();
            }
        }, 1000);

        request.request(new CourseListRequest.ICourseListResponse() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                lv_list.setVisibility(View.GONE);
                resultImageView.setImageResource(R.mipmap.server_error);
                Toast.makeText(CourseCategoryActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doAfterFailedResponse(String message) {
                lv_list.setVisibility(View.GONE);
                resultImageView.setImageResource(R.mipmap.server_error);
                Toast.makeText(CourseCategoryActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void responseNoGrant() {
            }

            @Override
            public void success(List<Course> courseList) {
                if (courseList.size() == 0) {
                    lv_list.setVisibility(View.GONE);
                    resultImageView.setVisibility(View.VISIBLE);
                    resultImageView.setImageResource(R.mipmap.no_data);
                    return;
                }
                lv_list.setVisibility(View.VISIBLE);
                if (courseTypeListViewAdapter == null) {
                    courseTypeListViewAdapter = new CourseListViewAdapter(CourseCategoryActivity.this, courseList);
                    lv_list.setAdapter(courseTypeListViewAdapter);
                } else {
                    courseTypeListViewAdapter.setCourseList(courseList); // 添加ListView的内容
                    courseTypeListViewAdapter.notifyDataSetChanged();
                    lv_list.smoothScrollToPosition(0);  // ListView回到顶部
                    pageIndex = 0;
                }
            }
        }, CourseListRequest.Type.筛选, courseType, searchWords, SORT_RULES[selectPosition], 0);
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(CourseCategoryActivity.this, getResources().getString(R.string.loadMore2), Toast.LENGTH_SHORT).show();

        //1秒后关闭加载
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_list.stopLoadMore();
            }
        }, 1000);

        if (courseTypeListViewAdapter == null)
            pageIndex = 0;
        pageIndex++;
        request.request(new CourseListRequest.ICourseListResponse() {


            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

            @Override
            public void doAfterFailedResponse(String message) {
                Toast.makeText(CourseCategoryActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void responseNoGrant() {

            }

            @Override
            public void success(List<Course> courseList) {
                if (courseTypeListViewAdapter == null) {
                    courseTypeListViewAdapter = new CourseListViewAdapter(CourseCategoryActivity.this, courseList);
                    lv_list.setAdapter(courseTypeListViewAdapter);
                } else {
                    if (courseList.size() == 0) {
                        Toast.makeText(CourseCategoryActivity.this, getResources().getString(R.string.nothingLeft), Toast.LENGTH_SHORT).show();
                        pageIndex--;
                    } else {
                        courseTypeListViewAdapter.getCourseList().addAll(courseList); // 添加ListView的内容
                        courseTypeListViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, CourseListRequest.Type.筛选, courseType, searchWords, SORT_RULES[selectPosition], pageIndex);
    }

    class CourseListResponse extends BaseResponceImpl implements CourseListRequest.ICourseListResponse {

        @Override
        public void success(List<Course> courseList) {
            dismissProcessBar();
            if (courseList.size() == 0) {
                lv_list.setVisibility(View.GONE);
                resultImageView.setVisibility(View.VISIBLE);
                resultImageView.setImageResource(R.mipmap.no_data);
                return;
            }
            resultImageView.setVisibility(View.GONE);
            lv_list.setVisibility(View.VISIBLE);
            courseTypeListViewAdapter = new CourseListViewAdapter(CourseCategoryActivity.this, courseList);
            lv_list.setAdapter(courseTypeListViewAdapter);
            pageIndex = 0;
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            lv_list.setVisibility(View.GONE);
            resultImageView.setImageResource(R.mipmap.server_error);
            Toast.makeText(CourseCategoryActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            lv_list.setVisibility(View.GONE);
            resultImageView.setImageResource(R.mipmap.server_error);
            Toast.makeText(CourseCategoryActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

    private void initListener() {
        ll_sortType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSortType == null) {
                    showSortTypePopupWindow(v);
                    iv_sortIcon.setImageResource(R.mipmap.ic_choose_gray_up);
                } else if (popupSortType.isShowing()) {
                    popupSortType.dismiss();
                    iv_sortIcon.setImageResource(R.mipmap.ic_choose_gray_down);
                } else {
                    showSortTypePopupWindow(v);
                    iv_sortIcon.setImageResource(R.mipmap.ic_choose_gray_up);
                }
            }
        });

        iv_search.setOnClickListener(this);
        ll_left.setOnClickListener(this);
        lly_left_action.setOnClickListener(this);
        btn_floading.setOnClickListener(this);
        lv_list.setOnTouchListener(new ListViewListener(new ListViewListener.IOnMotionEvent() {
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
    }

    private void showSortTypePopupWindow(final View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_course_sort, null);
        // 设置按钮的点击事件
        final RadioGroup rg = (RadioGroup) contentView.findViewById(R.id.rg_sort);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //use
                selectPosition = 0;
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rbtn1:
                        selectPosition = 0;
                        break;
                    case R.id.rbtn3:
                        selectPosition = 1;
                        break;
                }
                pageIndex = 0;
                tv_sortText.setText(((RadioButton) rg.getChildAt(selectPosition)).getText().toString());
                ((RadioButton) rg.getChildAt(selectPosition)).setTextColor(getResources().getColor(R.color.text_red));
                showProcessBar(getResources().getString(R.string.loading));
                request.request(courseListResponse, CourseListRequest.Type.筛选, courseType, searchWords, SORT_RULES[selectPosition], 0);
                iv_sortIcon.setImageResource(R.mipmap.ic_choose_gray_down);
                popupSortType.dismiss();
            }
        });

        popupSortType = new PopupWindow(contentView, ll_sortType.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupSortType.setTouchable(true);
        setPopupWindowTouchModal(popupSortType, false);// 使得popupWindow在显示的时候，popupWindow外部的控件也能够获得焦点
        popupSortType.setTouchInterceptor(new View.OnTouchListener() {

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
//        popupSortType.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.bg_popup_frame));
//        popupSortType.setBackgroundDrawable(getResources().getColor(android.R.color.transparent));

        // 设置好参数之后再show
        popupSortType.showAsDropDown(view);
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
