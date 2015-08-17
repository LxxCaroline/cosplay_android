package com.think.linxuanxuan.ecos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.NewRecruitmentActivity;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.share.ShareListRequest;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/8/3.
 */
public class RecruiteTypeChooseDialog extends Dialog implements View.OnClickListener {

    private ImageView makeupBtn, photographyBtn, backstageBtn, costumeBtn, propBtn, othersBtn;
    private LinearLayout ll_main;

    private Context mContext;

    private ShareListRequest request;
    private ShareListResponse response;

    private Recruitment.RecruitType recruitType;

    public RecruiteTypeChooseDialog(Context context) {
        super(context, R.style.Dialog_Transparent);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recruite_choosetype);
        makeupBtn = (ImageView) findViewById(R.id.makeuper_btn);
        photographyBtn = (ImageView) findViewById(R.id.photography_btn);
        backstageBtn = (ImageView) findViewById(R.id.backstage_btn);
        costumeBtn = (ImageView) findViewById(R.id.costume_btn);
        propBtn = (ImageView) findViewById(R.id.prop_btn);
        othersBtn = (ImageView) findViewById(R.id.others_btn);
        makeupBtn.setOnClickListener(this);
        photographyBtn.setOnClickListener(this);
        backstageBtn.setOnClickListener(this);
        costumeBtn.setOnClickListener(this);
        propBtn.setOnClickListener(this);
        othersBtn.setOnClickListener(this);

        //点击按钮以外地方关闭对话框
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        recruitType = Recruitment.RecruitType.妆娘;
        switch (v.getId()) {
            case R.id.photography_btn:
                recruitType = Recruitment.RecruitType.摄影;
                break;
            case R.id.backstage_btn:
                recruitType = Recruitment.RecruitType.后期;
                break;
            case R.id.costume_btn:
                recruitType = Recruitment.RecruitType.服装;
                break;
            case R.id.prop_btn:
                recruitType = Recruitment.RecruitType.道具;
                break;
            case R.id.others_btn:
                recruitType = Recruitment.RecruitType.其他;
                break;
        }
        Share.Tag tags = Share.Tag.getTagByRecruitType(recruitType);
        if (request == null)
            request = new ShareListRequest();
        if (response == null)
            response = new ShareListResponse();
        request.requestSomeOneShareWithTag(response, null, tags, 1);

    }

    class ShareListResponse extends BaseResponceImpl implements ShareListRequest.IShareListResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.noShareInTag), Toast.LENGTH_LONG).show();
            dismiss();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.noShareInTag), Toast.LENGTH_LONG).show();
            dismiss();
        }

        @Override
        public void success(List<Share> shareList) {
            if (shareList.size() == 0) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.noShareInTag), Toast.LENGTH_LONG).show();

            } else {
                Intent intent = new Intent(mContext, NewRecruitmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NewRecruitmentActivity.RecruitmentType, recruitType.getValue());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
            dismiss();
        }
    }
}
