package com.think.linxuanxuan.ecos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.NewRecruitmentActivity;
import com.think.linxuanxuan.ecos.model.Recruitment;

/**
 * Created by hzjixinyu on 2015/8/3.
 */
public class FirstDialog extends Dialog implements View.OnClickListener {

    private ImageView makeupBtn, photographyBtn, backstageBtn, costumeBtn, propBtn, othersBtn;
    private LinearLayout ll_main;

    private Context mContext;

    public FirstDialog(Context context) {
        super(context, R.style.Dialog_Transparent);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_first);

        //点击关闭对话框
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
        Recruitment.RecruitType recruitType = Recruitment.RecruitType.妆娘;
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
        Intent intent = new Intent(mContext, NewRecruitmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewRecruitmentActivity.RecruitmentType, recruitType.getValue());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        dismiss();
    }
}
