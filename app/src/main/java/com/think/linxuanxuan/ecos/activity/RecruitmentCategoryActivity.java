package com.think.linxuanxuan.ecos.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.RecruitmentListViewAdapter;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.recruitment.RecruitmentListRequest;
import com.think.linxuanxuan.ecos.views.PopupHelper;
import com.think.linxuanxuan.ecos.views.XListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/27.
 */
public class RecruitmentCategoryActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private static final String TAG = "Ecos---Recruitment";
    public static final String TRecruitmentType = "recruitmentType";
    private Recruitment.RecruitType recruitment_type;
    @InjectView(R.id.lv_list)
    XListView lv_list;
    @InjectView(R.id.lly_left_action)
    LinearLayout lly_left_action;
    @InjectView(R.id.ll_left)
    LinearLayout ll_left;
    @InjectView(R.id.tv_left)
    TextView tv_left;
    @InjectView(R.id.ll_location)
    LinearLayout ll_location;
    @InjectView(R.id.tv_left)
    TextView recruitmentTypeTxVw;

    @InjectView(R.id.ll_sortType)
    LinearLayout ll_sortType;
    @InjectView(R.id.tv_sortText)
    TextView tv_sortText;

    //for no data layout
    @InjectView(R.id.resultImageView)
    ImageView resultImageView;

    private PopupWindow popupSortType;
    private PopupWindow popupRecruiteType;

    private RecruitmentListRequest.SortRule sortRules[] = {RecruitmentListRequest.SortRule.智能排序,
            RecruitmentListRequest.SortRule.价格最低, RecruitmentListRequest.SortRule.最受欢迎, RecruitmentListRequest.SortRule.距离最近};
    private Recruitment.RecruitType recruitTypes[] = {Recruitment.RecruitType.妆娘, Recruitment.RecruitType.后期, Recruitment.RecruitType.摄影,
            Recruitment.RecruitType.服装, Recruitment.RecruitType.道具, Recruitment.RecruitType.其他};
    private int selectedSortRule = 0;
    private int selectRecruitType = 0;

    private RecruitmentListViewAdapter recruitmentListViewAdapter;
    //for request
    private RecruitmentListRequest request;
    private RecruitmentListResponse recruitmentListResponse;

    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_type);
        ButterKnife.inject(this);

        initListener();
        initData();
    }

    private void initListener() {
        ll_left.setOnClickListener(this);
        lly_left_action.setOnClickListener(this);
        ll_location.setOnClickListener(this);

        //排序事件
        ll_sortType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSortType.isShowing()) {
                    popupSortType.dismiss();
                } else {
                    PopupHelper.showRecruiteSortTypePopupWindow(popupSortType, RecruitmentCategoryActivity.this, v, new PopupHelper.IPopupListner() {
                        @Override
                        public void clickListner(int type, View v, PopupWindow popupWindow) {
                            pageIndex = 1;
                            tv_sortText.setText(((RadioButton) v).getText().toString());
                            selectedSortRule = type;
                            pageIndex = 1;
                            showProcessBar(getResources().getString(R.string.loading));
                            request.request(recruitmentListResponse, recruitment_type, "", sortRules[selectedSortRule], pageIndex);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                if (popupRecruiteType.isShowing()) {
                    popupRecruiteType.dismiss();
                } else {
                    popupRecruiteType = PopupHelper.newRecruiteTypePopupWindow(RecruitmentCategoryActivity.this);
                    PopupHelper.showRecruiteTypePopupWindow(popupRecruiteType, RecruitmentCategoryActivity.this, v, new PopupHelper.IPopupListner() {
                        @Override
                        public void clickListner(int type, View v, PopupWindow popupWindow) {
                            pageIndex = 1;
                            tv_left.setText(((RadioButton) v).getText().toString());
                            selectRecruitType = type;
                            pageIndex = 1;
                            recruitment_type = recruitTypes[selectRecruitType];
                            showProcessBar(getResources().getString(R.string.loading));
                            request.request(recruitmentListResponse, recruitment_type, "", sortRules[selectedSortRule], pageIndex);
                        }
                    });
                }
                break;
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.ll_location:
                Toast.makeText(RecruitmentCategoryActivity.this, getResources().getString(R.string.noWeixin), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initData() {
        pageIndex = 1;
        //下拉菜单
        popupSortType = PopupHelper.newRecruiteSortTypePopupWindow(RecruitmentCategoryActivity.this);
        popupRecruiteType = PopupHelper.newSixTypePopupWindow(RecruitmentCategoryActivity.this);

        recruitment_type = Recruitment.RecruitType.getRecruitTypeByValue(getIntent().getExtras().getString(TRecruitmentType));
        recruitmentTypeTxVw.setText(recruitment_type.name());

        //设置列表Adapter
        lv_list.setDividerHeight(2);
        lv_list.initRefleshTime(this.getClass().getSimpleName());
        lv_list.setPullLoadEnable(true);
        lv_list.setXListViewListener(this);

        //for request
        request = new RecruitmentListRequest();
        recruitmentListResponse = new RecruitmentListResponse();
        showProcessBar(getResources().getString(R.string.loading));
        request.request(recruitmentListResponse, recruitment_type, "", RecruitmentListRequest.SortRule.智能排序, pageIndex);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(RecruitmentCategoryActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
        //1秒后关闭刷新
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_list.stopRefresh();
            }
        }, 1000);
        pageIndex = 1;
        request.request(recruitmentListResponse, recruitment_type, "", sortRules[selectedSortRule], pageIndex);
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(RecruitmentCategoryActivity.this, getResources().getString(R.string.loadMore2), Toast.LENGTH_SHORT).show();

        //1秒后关闭加载
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_list.stopLoadMore();
            }
        }, 1000);

        pageIndex++;
        request.request(new RecruitmentListRequest.IRecruitmentListResponse() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

            @Override
            public void doAfterFailedResponse(String message) {

            }

            @Override
            public void responseNoGrant() {

            }

            @Override
            public void success(List<Recruitment> recruitmentList) {
                if (recruitmentList.size() == 0) {
                    Toast.makeText(RecruitmentCategoryActivity.this, getResources().getString(R.string.nothingLeft), Toast.LENGTH_SHORT).show();
                    pageIndex--;
                } else {
                    recruitmentListViewAdapter.getRecruitmentArrayList().addAll(recruitmentList); // 添加ListView的内容
                    recruitmentListViewAdapter.notifyDataSetChanged();
                }
            }
        }, recruitment_type, "", sortRules[selectedSortRule], pageIndex);
    }


    class RecruitmentListResponse extends BaseResponceImpl implements RecruitmentListRequest.IRecruitmentListResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            lv_list.setVisibility(View.GONE);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.mipmap.no_data);
            Toast.makeText(RecruitmentCategoryActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProcessBar();
            lv_list.setVisibility(View.GONE);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.mipmap.no_data);
            Toast.makeText(RecruitmentCategoryActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void success(List<Recruitment> recruitList) {
            dismissProcessBar();
            if (recruitList.size() == 0) {
                lv_list.setVisibility(View.GONE);
                resultImageView.setVisibility(View.VISIBLE);
                resultImageView.setImageResource(R.mipmap.no_data);
                return;
            }
            resultImageView.setVisibility(View.GONE);
            lv_list.setVisibility(View.VISIBLE);
            //获取recruit信息
            if (recruitList.size() == 0)
                Toast.makeText(RecruitmentCategoryActivity.this, getResources().getString(R.string.noRecruit), Toast.LENGTH_SHORT).show();
            recruitmentListViewAdapter = new RecruitmentListViewAdapter(RecruitmentCategoryActivity.this, recruitList);
            lv_list.setAdapter(recruitmentListViewAdapter);
        }
    }
}
