package com.think.linxuanxuan.ecos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.NewDisplayListAdater;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.recruitment.CreateRecruitmentRequest;
import com.think.linxuanxuan.ecos.request.share.ShareListRequest;
import com.think.linxuanxuan.ecos.views.XListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/31.
 */
public class NewRecruitmentActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private static final String TAG = "Ecos---NewRecruitment";
    public static final String RecruitmentType = "RecruitmentType";
    private Recruitment.RecruitType mRecruitType;

    @InjectView(R.id.displayListView)
    XListView displayLsVw;
    @InjectView(R.id.priceTxVw)
    TextView priceTxVw;
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    private NewDisplayListAdater newDisplayListAdater;

    //for request
    private CreateRecruitmentRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recruitment_layout);
        ButterKnife.inject(this);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_right_text.setText(R.string.auction);
        title_text.setText("设置价格和封面作品");
    }

    void initView() {
        //set the data
        priceTxVw.setText(mRecruitType.getPriceUnit());
        //set adapter
        displayLsVw.setAdapter(newDisplayListAdater);
        //set listener

        displayLsVw.initRefleshTime(this.getClass().getSimpleName());
        displayLsVw.setPullRefreshEnable(false);
        displayLsVw.setPullLoadEnable(true);
        displayLsVw.setXListViewListener(this);
    }

    void initData() {
        mRecruitType = Recruitment.RecruitType.getRecruitTypeByValue(getIntent().getExtras().getString(RecruitmentType));
        //init the adapter
        newDisplayListAdater = new NewDisplayListAdater(this);
        //for request
        request = new CreateRecruitmentRequest();
        getPersonalShareList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.lly_right_action:
                String price = newDisplayListAdater.getPrice();
                String descrp = newDisplayListAdater.getDes();

                if ("".equals(price)) {
                    Toast.makeText(NewRecruitmentActivity.this, "请输入价格 " + InputLength.RecruitePrice_min + "~" + InputLength.RecruitePrice_max, Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(descrp)) {
                    Toast.makeText(NewRecruitmentActivity.this, "请输入说明 " + InputLength.RecruitePrice_min + "~" + InputLength.RecruitePrice_max, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(price) > InputLength.RecruitePrice_max || Integer.parseInt(price) < InputLength.RecruitePrice_min) {
                    Toast.makeText(NewRecruitmentActivity.this, "亲，请输入合理价格 " + InputLength.RecruitePrice_min + "~" + InputLength.RecruitePrice_max, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (descrp.length() > InputLength.RecruiteDetail_max) {
                    Toast.makeText(NewRecruitmentActivity.this, " 亲，您输入超过字数限制" + InputLength.RecruiteDetail_max + "字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newDisplayListAdater.isTopViewEmpty()) {
                    Toast.makeText(NewRecruitmentActivity.this, this.getResources().getString(R.string.notAlreadyFinished), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newDisplayListAdater.isThereCoverUrl()) {
                    Toast.makeText(NewRecruitmentActivity.this, "请选择作品封面", Toast.LENGTH_SHORT).show();
                    return;
                }

                Recruitment recruitment = new Recruitment();
                recruitment.averagePrice = newDisplayListAdater.getPrice();
                recruitment.priceUnit = mRecruitType.getPriceUnit();
                recruitment.description = newDisplayListAdater.getDes();
                recruitment.coverUrl = newDisplayListAdater.getCheckedCoverUrl();
                recruitment.recruitType = mRecruitType;
                showProcessBar(getResources().getString(R.string.uploading));
                request.request(new CreateRecruitmentResponce(), recruitment);
                break;
        }

    }

    class CreateRecruitmentResponce extends BaseResponceImpl implements CreateRecruitmentRequest.ICreateRecruitmentResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(NewRecruitmentActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(NewRecruitmentActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void success(Recruitment recruit) {
            dismissProcessBar();
            Toast.makeText(NewRecruitmentActivity.this, getResources().getString(R.string.uploadSuccessfully), Toast.LENGTH_SHORT).show();
            NewRecruitmentActivity.this.finish();

        }
    }


    /**
     * 获取个人分享列表
     */
    public void getPersonalShareList() {
        showProcessBar("加载中...");
        ShareListRequest request = new ShareListRequest();
        Share.Tag tags = Share.Tag.getTagByRecruitType(mRecruitType);

        request.requestSomeOneShareWithTag(new ShareListResponse(), null, tags, (newDisplayListAdater.getCount() - 1) / 5 + 1);

    }

    /**
     * @author enlizhang
     * @ClassName: GetAssignmetnDetailResponse
     * @Description:
     * @date 2015年7月28日 下午5:24:35
     */
    class ShareListResponse extends BaseResponceImpl implements ShareListRequest.IShareListResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            displayLsVw.stopLoadMore();
            Toast.makeText(NewRecruitmentActivity.this, "小编还没找到您在该类别下的分享作品，所以不能发布新的招募哦，快去创建相应的分享吧亲:)", Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProcessBar();
            displayLsVw.stopLoadMore();
            Toast.makeText(NewRecruitmentActivity.this, "小编还没找到您在该类别下的分享作品，所以不能发布新的招募哦，快去创建相应的分享吧亲:)", Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void success(List<Share> shareList) {
            newDisplayListAdater.reflesh(shareList);
            dismissProcessBar();
            displayLsVw.stopLoadMore();

            if ((newDisplayListAdater.getCount() - 1) == 0) {
                Toast.makeText(NewRecruitmentActivity.this, "小编还没找到您在该类别下的分享作品，所以不能发布新的招募哦，快去创建相应的分享吧亲:)", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    @Override
    public void onRefresh() {


    }

    @Override
    public void onLoadMore() {
        getPersonalShareList();
    }

}
