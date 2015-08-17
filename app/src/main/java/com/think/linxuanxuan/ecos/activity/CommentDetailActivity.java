package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.WorkDetailListViewAdapter;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.comment.CommentListRequest;
import com.think.linxuanxuan.ecos.request.course.PraiseRequest;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/23.
 */
public class CommentDetailActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "Ecos---CommentDetail";
    public static final String FromId = "fromId";
    public static final String CommentType = "commentType";
    public static final String IsPraised = "IsPraised";
    private String fromId = "";
    private Comment.CommentType commentType;
    private boolean isPraised;
    @InjectView(R.id.commentLsVw)
    ListView commentListView;
    @InjectView(R.id.workDetailsCommentEdTx)
    EditText commentEdTx;
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.favorBtn)
    LinearLayout favorBtn;
    @InjectView(R.id.iv_favor)
    ImageView iv_favor;
    @InjectView(R.id.tv_favor)
    TextView tv_favor;

    //for NetWorkImageView
    static ImageLoader.ImageCache imageCache;
    RequestQueue queue;
    ImageLoader imageLoader;
    //for request
    private CommentListRequest commentListRequest;
    private GetCommentListResponse getCommentListResponse;
    private PraiseRequest praiseRequest;
    private PraiseResponse praiseResponse;

    private WorkDetailListViewAdapter workDetailListViewAdapter;


    public static final int RequestCodeForComment = 1;
    public static final int ResultCodeForComment = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_comment_detail);
        ButterKnife.inject(this);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_right_text.setText("");
        title_text.setText("所有评论");
    }

    void initData() {
        fromId = getIntent().getExtras().getString(FromId);
        commentType = Comment.CommentType.getCommentTypeByValue(getIntent().getExtras().getString(CommentType));
        isPraised = getIntent().getExtras().getBoolean(IsPraised);
        //init the data for NetWorkImageView
        queue = com.think.linxuanxuan.ecos.activity.MyApplication.getRequestQueue();
        imageCache = new SDImageCache();
        imageLoader = new ImageLoader(queue, imageCache);
        //init the adapter
        workDetailListViewAdapter = new WorkDetailListViewAdapter(this, true);

        commentListRequest = new CommentListRequest();
        getCommentListResponse = new GetCommentListResponse();
        Comment comment = new Comment();
        comment.commentType = commentType;
        comment.commentTypeId = fromId;
        showProcessBar(getResources().getString(R.string.loading));
        commentListRequest.request(getCommentListResponse, comment, 1);
        //set listener
        title_left.setOnClickListener(this);
    }

    void initView() {
        //implementation on the title bar
        //always hide the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //set adapter
        commentListView.setAdapter(workDetailListViewAdapter);
        //set listener
        commentEdTx.setOnTouchListener(this);
        //if it already is praised
        if (isPraised) {
            tv_favor.setText(getResources().getString(R.string.cancelFavor));
            iv_favor.setImageResource(R.mipmap.ic_praise_fill);
        }
        favorBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeForComment && resultCode == ResultCodeForComment) {
            Comment comment = new Comment();
            comment.commentType = commentType;
            comment.commentTypeId = fromId;
            showProcessBar(getResources().getString(R.string.loading));
            commentListRequest.request(getCommentListResponse, comment, 1);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(CommentDetailActivity.this, com.think.linxuanxuan.ecos.activity.WriteContentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CommentType, commentType.getBelongs());
            bundle.putString(FromId, fromId);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCodeForComment);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorBtn:
                if (praiseRequest == null)
                    praiseRequest = new PraiseRequest();
                if (praiseResponse == null)
                    praiseResponse = new PraiseResponse();
                if (commentType == Comment.CommentType.教程) {
                    praiseRequest.praiseCourse(praiseResponse, fromId, !isPraised);
                } else if (commentType == Comment.CommentType.分享) {
                    praiseRequest.praiseShare(praiseResponse, fromId, !isPraised);
                } else if (commentType == Comment.CommentType.作业) {
                    praiseRequest.praiseAssignment(praiseResponse, fromId, !isPraised);
                }
                break;
            case R.id.lly_right_action:
            case R.id.lly_left_action:
                CommentDetailActivity.this.finish();
                break;
        }
    }

    class GetCommentListResponse extends BaseResponceImpl implements CommentListRequest.ICommentListResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(CommentDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProcessBar();
            Toast.makeText(CommentDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void success(List<Comment> commentList) {
            dismissProcessBar();
            workDetailListViewAdapter.updateCommentList(commentList);
        }
    }

    class PraiseResponse extends BaseResponceImpl implements PraiseRequest.IPraiseResponce {

        @Override
        public void success(String userId, boolean praise) {
            isPraised = !isPraised;
            if (isPraised) {
                tv_favor.setText(getResources().getString(R.string.cancelFavor));
                iv_favor.setImageResource(R.mipmap.ic_praise_fill);
            } else {
                tv_favor.setText(getResources().getString(R.string.favour));
                iv_favor.setImageResource(R.mipmap.ic_praise_block);
            }
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(CommentDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(CommentDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }


}
