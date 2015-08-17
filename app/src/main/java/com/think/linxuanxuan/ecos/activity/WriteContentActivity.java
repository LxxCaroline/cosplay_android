package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.comment.CreateCommentRequest;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/23.
 */
public class WriteContentActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "Ecos---WriteContent";
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.commentEdTx)
    EditText commentEdTx;

    private CreateCommentRequest createCommentRequest;
    private UploadCommentResponse response;
    private Comment.CommentType commentType;
    private String fromId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_content_layout);
        ButterKnife.inject(this);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_right.setVisibility(View.VISIBLE);
        title_right_text.setText("发送");
        title_text.setText("添加评论");
    }

    void initData() {
        commentType = Comment.CommentType.getCommentTypeByValue(getIntent().getExtras().getString(CommentDetailActivity.CommentType));
        fromId = getIntent().getExtras().getString(CommentDetailActivity.FromId);
        Log.d(TAG, "fromtId:" + fromId);

    }

    void initView() {
        //implementation on the title bar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_right_action:
                String content = commentEdTx.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(WriteContentActivity.this, getResources().getString(R.string.noComment), Toast.LENGTH_SHORT).show();
                    return;
                }
                Comment comment = new Comment();
                comment.content = content;
                comment.commentType = commentType;
                comment.commentTypeId = fromId;

                if (createCommentRequest == null)
                    createCommentRequest = new CreateCommentRequest();
                if (response == null)
                    response = new UploadCommentResponse();
                createCommentRequest.request(response, comment);
                showProcessBar(getResources().getString(R.string.e_loading));
                break;
            case R.id.lly_left_action:
                finish();
                break;
        }

    }


    class UploadCommentResponse extends BaseResponceImpl implements CreateCommentRequest.ICreateCommentResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(WriteContentActivity.this, message, Toast.LENGTH_SHORT).show();
            dismissProcessBar();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(WriteContentActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
            dismissProcessBar();
        }

        @Override
        public void success(Comment comment) {
            Toast.makeText(WriteContentActivity.this, getResources().getString(R.string.commentSuccess), Toast.LENGTH_SHORT).show();
            setResult(CommentDetailActivity.ResultCodeForComment);
            dismissProcessBar();
            finish();
        }
    }
}
