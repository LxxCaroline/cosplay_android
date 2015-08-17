package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.recruitment.GetRecruitmentDetailRequest;
import com.think.linxuanxuan.ecos.request.share.ShareListRequest;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecruitmentDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView
        .OnItemClickListener {

    private static final String TAG = "Ecos---RecruitmentDet";
    public static final String RecruitID = "RecruitID";
    public static final String RecruitType = "RecruitType";
    public static final String UserId = "UserId";

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    @InjectView(R.id.ll_author)
    LinearLayout ll_author;
    @InjectView(R.id.iv_avatar)
    RoundImageView iv_avatar;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.tv_distance)
    TextView tv_distance;
    @InjectView(R.id.tv_price)
    TextView tv_price;
    @InjectView(R.id.tv_talk)
    TextView tv_talk;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.tv_detail)
    TextView tv_detail;
    @InjectView(R.id.lv_list)
    ListView lv_list;

    private com.think.linxuanxuan.ecos.adapter.RecruitmentDetailWorkAdapter recruitmentDetailWorkAdapter;
    private String recruitID = "";
    private Recruitment recruitment;
    private Recruitment.RecruitType recruitType;
    private String userId = "";

    //for request
    private GetRecruitmentDetailRequest request;
    private GetRecruitmentLDetailResponse response;
    private ShareListRequest shareListRequest;
    private ShareListResponse shareListResponse;
    private List<Share> shareList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_detail);
        ButterKnife.inject(this);

        initTitle();
        initListener();
        initData();
        getSupportActionBar().hide();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_right_text.setText("评论");
        title_text.setText("招募详情");
    }


    private void initListener() {
        ll_author.setOnClickListener(this);
        tv_talk.setOnClickListener(this);
        lv_list.setOnItemClickListener(this);
        lv_list.setDividerHeight(0);
    }

    private void initData() {
        recruitID = getIntent().getExtras().getString(RecruitID);
        recruitType = Recruitment.RecruitType.getRecruitTypeByValue(getIntent().getExtras().getString(RecruitType));
        userId = getIntent().getExtras().getString(UserId);
        request = new GetRecruitmentDetailRequest();
        response = new GetRecruitmentLDetailResponse();
        shareListRequest = new ShareListRequest();
        shareListResponse = new ShareListResponse();
        Share.Tag tag = new Share.Tag();
        if (recruitType == Recruitment.RecruitType.妆娘)
            tag.isMakeup = true;
        else if (recruitType == Recruitment.RecruitType.后期)
            tag.isLater = true;
        else if (recruitType == Recruitment.RecruitType.其他)
            tag.isLater = true;
        else if (recruitType == Recruitment.RecruitType.摄影)
            tag.isLater = true;
        else if (recruitType == Recruitment.RecruitType.服装)
            tag.isLater = true;
        else if (recruitType == Recruitment.RecruitType.道具)
            tag.isLater = true;
        showProcessBar(getResources().getString(R.string.loading));
        shareListRequest.requestSomeOneShareWithTag(shareListResponse, userId, tag, 1);
        request.request(response, recruitID);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.ll_author:
                intent = new Intent(RecruitmentDetailActivity.this, PersonageDetailActivity.class);
                bundle.putString(PersonageDetailActivity.UserID, recruitment.userId);
                bundle.putBoolean(PersonageDetailActivity.IsOwn, false);
                intent.putExtras(bundle);
                startActivity(intent);
                android.util.Log.v("id", "++++++" + recruitment.userId);
                break;
            case R.id.tv_talk:
                intent = new Intent(RecruitmentDetailActivity.this, ContactActivity.class);
                bundle.putString(ContactActivity.TargetUserID, recruitment.userId);
                bundle.putString(ContactActivity.TargetUserName, recruitment.nickname);
                bundle.putString(ContactActivity.TargetUserAvatar, recruitment.avatarUrl);
                bundle.putString(ContactActivity.TargetUserIMID, recruitment.imId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RecruitmentDetailActivity.this, DisplayDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DisplayDetailActivity.ShareId, this.shareList.get(position).shareId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class GetRecruitmentLDetailResponse extends BaseResponceImpl implements GetRecruitmentDetailRequest
            .IGetRecruitmentLDetailResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(RecruitmentDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(Recruitment recruit) {
            RecruitmentDetailActivity.this.recruitment = recruit;
            iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
            iv_avatar.setErrorImageResId(R.mipmap.bg_female_default);
            if (recruit.avatarUrl != null && !recruit.avatarUrl.equals("")) {
                RequestQueue queue = MyApplication.getRequestQueue();
                ImageLoader.ImageCache imageCache = new SDImageCache(300, 200);
                ImageLoader imageLoader = new ImageLoader(queue, imageCache);
                if (recruit.avatarUrl != null && !recruit.avatarUrl.equals(""))
                    iv_avatar.setImageUrl(recruit.avatarUrl, imageLoader);
            } else {
                iv_avatar.setImageResource(R.mipmap.bg_female_default);
            }
            tv_name.setText(recruit.nickname);
            tv_distance.setText(recruit.distanceKM + getResources().getString(R.string.KM));
            tv_price.setText(recruit.averagePrice.substring(0, recruit.averagePrice.length() - 2) + recruit
                    .recruitType.getPriceUnit());
            tv_detail.setText(recruit.description);

        }

    }

    class ShareListResponse extends BaseResponceImpl implements ShareListRequest.IShareListResponse {

        @Override
        public void success(List<Share> shareList) {
            dismissProcessBar();
            RecruitmentDetailActivity.this.shareList = shareList;
            if (shareList.size() == 0) {
                Toast.makeText(RecruitmentDetailActivity.this, getResources().getString(R.string.noOtherShare), Toast
                        .LENGTH_SHORT).show();
            }
            recruitmentDetailWorkAdapter = new com.think.linxuanxuan.ecos.adapter.RecruitmentDetailWorkAdapter
                    (RecruitmentDetailActivity.this, shareList);
            lv_list.setAdapter(recruitmentDetailWorkAdapter);
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(RecruitmentDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(RecruitmentDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError
                    (volleyError), Toast.LENGTH_SHORT).show();
        }
    }
}
