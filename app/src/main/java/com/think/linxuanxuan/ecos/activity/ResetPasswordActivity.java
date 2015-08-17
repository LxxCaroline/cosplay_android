package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.request.user.ResetPasswordRequest;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/30.
 */
public class ResetPasswordActivity extends Activity implements View.OnClickListener,TextWatcher{
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.tv_reset_password)
    TextView tv_reset_password;
    @InjectView(R.id.iv_return)
    ImageView iv_return;
    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.main)
    LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.inject(this);

        initListener();
        initData();

        iv_return.requestFocus();
    }

    private void initListener() {
        et_password.addTextChangedListener(this);
        tv_reset_password.setOnClickListener(this);
        iv_return.setOnClickListener(this);

        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ((main.getRootView().getHeight() - main.getHeight()) > 100) {
                    logo.setVisibility(View.GONE);
                } else {
                    logo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_reset_password:
                ResetPasswordRequest reqeust = new ResetPasswordRequest();
                //TODO phone
                reqeust.request(new ResetPwdResponse(), getIntent().getStringExtra("phone"), et_password.getText().toString());
                break;
            case R.id.iv_return:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (et_password.getText().toString().length()>= InputLength.Password_min&&et_password.getText().toString().length()<=InputLength.Password_max){
            tv_reset_password.setEnabled(true);
        }else{
            tv_reset_password.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    class ResetPwdResponse extends BaseResponceImpl implements NorResponce{

        @Override
        public void success() {
            Toast.makeText(ResetPasswordActivity.this, "RESET SUCCESS", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ResetPasswordActivity.this, "NETWORK FAIL", Toast.LENGTH_SHORT).show();
        }
    }
}
