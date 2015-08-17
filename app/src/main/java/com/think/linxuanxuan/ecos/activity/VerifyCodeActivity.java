package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.user.CheckAutoRequest;
import com.think.linxuanxuan.ecos.request.user.SendAutocodeRequest;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/30.
 */
public class VerifyCodeActivity extends Activity implements TextWatcher {
    @InjectView(R.id.iv_return)
    ImageView iv_return;
    @InjectView(R.id.ll_container)
    LinearLayout ll_container;
    @InjectView(R.id.tv_next)
    TextView tv_next;
    @InjectView(R.id.tv_notice)
    TextView tv_notice;
    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.main)
    LinearLayout main;


    private int stepN = 1;  //默认step

    private View view_step1;
    private View view_step2;

    //step1
    private EditText et_phone;

    //step2
    private TextView tv_phone;
    private EditText et_code;

    private String phone = "";

    private Timer timer;
    private TimerTask task;
    private int timeCount = 60;

    //for request
    private SendAutocodeRequest request;
    private RequestCodeResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifycode);
        ButterKnife.inject(this);

        view_step1 = (View) View.inflate(VerifyCodeActivity.this, R.layout.view_verify_step1, null);
        view_step2 = (View) View.inflate(VerifyCodeActivity.this, R.layout.view_verify_step2, null);

        ll_container.addView(view_step1);//默认设置step1

        bindView();
        initListener();
        initData();

        iv_return.requestFocus();
    }

    private void bindView() {
        et_phone = (EditText) view_step1.findViewById(R.id.et_phone);
        tv_phone = (TextView) view_step2.findViewById(R.id.tv_phone);
        et_code = (EditText) view_step2.findViewById(R.id.et_code);

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

    private void initListener() {
        tv_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode();
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepN == 1) {
                    if("".equals(et_phone.getText().toString()) || et_phone.getText().toString().length()!=11){
                        Toast.makeText(VerifyCodeActivity.this, "请输入完整的手机号",Toast.LENGTH_LONG).show();
                        return ;
                    }
                    requestCode();
                } else {
                    //verify code
                    checkCode();
                }
            }
        });

        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepN == 1) {
                    finish();
                } else {
                    changePageTo(1);
                }
            }
        });

        et_phone.addTextChangedListener(this);
        et_code.addTextChangedListener(this);

    }

    /**
     * 请求验证码
     */
    private void requestCode() {
        request = new SendAutocodeRequest();
        response = new RequestCodeResponse();
        request.requestSend(response, et_phone.getText().toString());
    }

    /**
     * 核对验证码
     */
    public void checkCode() {
        CheckAutoRequest request = new CheckAutoRequest();

        String phone = et_phone.getText().toString();
        if("".equals(phone) || phone.length()!=11){
            Toast.makeText(VerifyCodeActivity.this, "请输入完整的手机号",Toast.LENGTH_LONG).show();
            return ;
        }

        //TODO change
        String autocode = et_code.getText().toString();
        if("".equals(autocode))
        {
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_LONG).show();
            return ;
        }

        request.requestCheck(new CheckAutocodeResponse(), phone, autocode);
    }

    private void initData() {
    }

    void setTimer() {
        timeCount = 60;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeCount--;
                        tv_notice.setText(timeCount + "s 后可重发验证码");
                        if (timeCount <= 0) {
                            timer.cancel();
                            tv_notice.setText("点击重新获取验证码");
                            tv_notice.setEnabled(true);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private void changePageTo(int i) {
        if (i == 1 && stepN != 1) {
            stepN = 1;
            ll_container.removeAllViews();
            ll_container.addView(view_step1);
            et_phone.setText(phone);
            tv_notice.setText("");
            tv_next.setText("获取验证码");
        }
        if (i == 2 && stepN != 2) {
            stepN = 2;
            ll_container.removeAllViews();
            ll_container.addView(view_step2);
            tv_phone.setText(phone);
            tv_notice.setText("验证码有效时间计时器");
            tv_next.setText("下一步");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /**当有文字输入时才可以点击下一步按钮**/
        if (stepN == 1 && !TextUtils.isEmpty(et_phone.getText().toString())) {
            phone = et_phone.getText().toString();
            tv_next.setEnabled(true);
        } else if (stepN == 2 && !TextUtils.isEmpty(et_code.getText().toString())) {
            tv_next.setEnabled(true);
        } else {
            tv_next.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    class RequestCodeResponse extends BaseResponceImpl implements SendAutocodeRequest.ISendAutocodeResponse {

        @Override
        public void success() {
            Toast.makeText(VerifyCodeActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
            changePageTo(2);
            tv_notice.setEnabled(false);
            setTimer();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(VerifyCodeActivity.this, "发送失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(VerifyCodeActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

    class CheckAutocodeResponse extends BaseResponceImpl implements CheckAutoRequest.ICheckAutocodeResponse {

        @Override
        public void success() {
            Toast.makeText(VerifyCodeActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("phone", et_phone.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(VerifyCodeActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(VerifyCodeActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

}
