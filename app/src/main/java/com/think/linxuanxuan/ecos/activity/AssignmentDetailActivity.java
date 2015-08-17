package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.WorkDetailListViewAdapter;
import com.think.linxuanxuan.ecos.dialog.FirstDialog;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.course.GetAssignmentDetailRequest;
import com.think.linxuanxuan.ecos.request.course.PraiseRequest;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/8/1.
 */
public class AssignmentDetailActivity extends BaseActivity implements View.OnTouchListener, AdapterView.OnItemClickListener, View.OnClickListener, GestureOverlayView.OnGesturePerformedListener {

    private final String TAG = "Ecos---WorkDetail";
    public static final String Work_ID = "workId";
    public static final String Work_List = "workList";
    public static final String Work_Order = "workOrder";
    //to record the work id.
    private String workID = "";
    //to record the list of works
    private ArrayList<String> workList;
    //to record the order of the current work.
    private int workOrder;
    //widget for gesture
    @InjectView(R.id.gestureView)
    GestureOverlayView gestureOverlayView;
    //widget in the title bar
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    //widget in details
    @InjectView(R.id.workDetailImgVw)
    NetworkImageView networkImageView;
    @InjectView(R.id.personPicImgView)
    RoundImageView personPicImgView;
    @InjectView(R.id.personNameTxV)
    TextView personNameTxV;
    @InjectView(R.id.workDetailDate)
    TextView workDetailDate;
    @InjectView(R.id.workDetailDescpTxVw)
    TextView workDetailDescpTxVw;
    @InjectView(R.id.workDetailFavorTxVw)
    TextView workDetailFavorTxVw;
    @InjectView(R.id.workDetailsLsVw)
    ExtensibleListView commentListView;
    @InjectView(R.id.workDetailsCommentEdTx)
    EditText commentEdTx;
    @InjectView(R.id.favorBtn)
    LinearLayout favorBtn;
    @InjectView(R.id.iv_favor)
    ImageView iv_favor;
    @InjectView(R.id.tv_favor)
    TextView tv_favor;

    private WorkDetailListViewAdapter workDetailListViewAdapter;
    private GestureLibrary library;
    private final String RIGHT = "right";
    private final String LEFT = "left";

    //for NetWorkImageView
    static ImageLoader.ImageCache imageCache;
    ImageLoader imageLoader;

    //request
    private GetAssignmentDetailRequest request;
    private GetAssignmentDetailResponse assignmentDetailResponse;
    private PraiseRequest praiseRequest;
    private PraiseResponse praiseResponse;

    private Course.Assignment assignment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.work_detail_layout);
        ButterKnife.inject(this);

        firstEnter();
        initTitle();
        initData();
        initView();

    }

    private void firstEnter() {
        SharedPreferences setting = getSharedPreferences(TAG, 0);
        Boolean first = setting.getBoolean("FIRST", true);
        if (first) {
            new FirstDialog(AssignmentDetailActivity.this).show();
            setting.edit().putBoolean("FIRST", false).commit();
        }
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("");
    }

    void initData() {
        workID = getIntent().getExtras().getString(Work_ID);
        if (workID == null) {
            workList = getIntent().getExtras().getStringArrayList(Work_List);
            workOrder = getIntent().getExtras().getInt(Work_Order);
            for (int i = 0; i < workList.size(); i++)
                Log.d(TAG, workList.get(i));
            workID = workList.get(workOrder);
        }
        //code for gesture
        library = GestureLibraries.fromRawResource(this, R.raw.gesture);
        library.load();
        //always hide the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //init the adapter
        workDetailListViewAdapter = new WorkDetailListViewAdapter(this, false);
        //init the data for NetWorkImageView
        imageCache = new SDImageCache(200, 300);
        imageLoader = new ImageLoader(MyApplication.getRequestQueue(), imageCache);
        //get the work detail from the server
        request = new GetAssignmentDetailRequest();
        assignmentDetailResponse = new GetAssignmentDetailResponse();
        showProcessBar(getResources().getString(R.string.loading));
        request.request(assignmentDetailResponse, workID);
    }

    void initView() {
        //implementation on the title bar
        title_text.setText((workOrder + 1) + "/" + workList.size());

        // 3:2
        ViewGroup.LayoutParams params = networkImageView.getLayoutParams();
        params.height = (DisplayWidth - 80) * 3 / 2;
        networkImageView.setLayoutParams(params);

        //set the default image for NetWorkImageView
        networkImageView.setDefaultImageResId(R.drawable.img_default);
        //set the error image for NetWorkImageView
        networkImageView.setErrorImageResId(R.drawable.img_default);

        //setAdapter
        commentListView.setAdapter(workDetailListViewAdapter);

        //add listener
        commentListView.setOnItemClickListener(this);
        commentEdTx.setOnTouchListener(this);
        title_left.setOnClickListener(this);
        favorBtn.setOnClickListener(this);
        gestureOverlayView.addOnGesturePerformedListener(this);
    }

    private void setPraiseLayout() {
        if (assignment.hasPraised) {
            tv_favor.setText(getResources().getString(R.string.cancelFavor));
            iv_favor.setImageResource(R.mipmap.ic_praise_fill);
        } else {
            tv_favor.setText(getResources().getString(R.string.favour));
            iv_favor.setImageResource(R.mipmap.ic_praise_block);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(AssignmentDetailActivity.this, WriteContentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CommentDetailActivity.CommentType, Comment.CommentType.作业.getBelongs());
            bundle.putString(CommentDetailActivity.FromId, assignment.assignmentId);
            intent.putExtras(bundle);
            startActivityForResult(intent, CommentDetailActivity.RequestCodeForComment);
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
                praiseRequest.praiseAssignment(praiseResponse, assignment.assignmentId, !assignment.hasPraised);
                break;
            case R.id.lly_left_action:
                AssignmentDetailActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AssignmentDetailActivity.this, CommentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CommentDetailActivity.CommentType, Comment.CommentType.作业.getBelongs());
        bundle.putString(CommentDetailActivity.FromId, workList.get(workOrder));
        bundle.putBoolean(CommentDetailActivity.IsPraised, assignment.hasPraised);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommentDetailActivity.RequestCodeForComment && resultCode == CommentDetailActivity.ResultCodeForComment) {
            showProcessBar(getResources().getString(R.string.loading));
            request.request(assignmentDetailResponse, workID);
        }
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = library.recognize(gesture);
        Prediction prediction;
        for (int i = 0; i < predictions.size(); i++) {
            prediction = predictions.get(i);
            if (prediction.score > 1.0) {
                if (RIGHT.equals(prediction.name)) {
                    if (workOrder == 0) {
                        Toast.makeText(AssignmentDetailActivity.this, AssignmentDetailActivity.this.getString(R.string.alreadyFirstAssignment), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showProcessBar(getResources().getString(R.string.loadingLastAssignment));
                    workOrder--;
                    title_text.setText((workOrder + 1) + "/" + workList.size());
                } else if (LEFT.equals(prediction.name)) {
                    if (workOrder == workList.size() - 1) {
                        Toast.makeText(AssignmentDetailActivity.this, AssignmentDetailActivity.this.getString(R.string.alreadyLastAssignment), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showProcessBar(getResources().getString(R.string.loadingNextAssignment));
                    workOrder++;
                    title_text.setText((workOrder + 1) + "/" + workList.size());
                }
            }
        }
        workID = workList.get(workOrder);
        request.request(assignmentDetailResponse, workID);
    }

    /**
     * @ClassName: GetAssignmetnDetailResponse
     * @Description: 获取作业详情
     */
    class GetAssignmentDetailResponse extends BaseResponceImpl implements GetAssignmentDetailRequest.IAssignmentDetailRespnce {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(AssignmentDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProcessBar();
            Toast.makeText(AssignmentDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void success(Course.Assignment assignment, List<Comment> commentList) {
            dismissProcessBar();
            AssignmentDetailActivity.this.assignment = assignment;
            //set the work image.
            if (assignment.imageUrl != null && !assignment.imageUrl.equals(""))
                networkImageView.setImageUrl(assignment.imageUrl, imageLoader);
            networkImageView.setDefaultImageResId(R.drawable.img_default);
            networkImageView.setErrorImageResId(R.drawable.img_default);
            if (assignment.authorAvatarUrl != null && !assignment.authorAvatarUrl.equals(""))
                personPicImgView.setImageUrl(assignment.authorAvatarUrl, imageLoader);
            else
                personPicImgView.setImageResource(R.mipmap.bg_female_default);
            personPicImgView.setDefaultImageResId(R.mipmap.bg_female_default);
            personPicImgView.setErrorImageResId(R.mipmap.bg_female_default);
            personNameTxV.setText(assignment.author);
            workDetailDate.setText(assignment.getDateDescription());
            workDetailDescpTxVw.setText(assignment.content);
            workDetailFavorTxVw.setText(assignment.praiseNum + AssignmentDetailActivity.this.getString(R.string.favorCount));
            workDetailListViewAdapter.setCommentCount(assignment.commentNum);
            workDetailListViewAdapter.updateCommentList(commentList);
            setPraiseLayout();
        }
    }

    class PraiseResponse extends BaseResponceImpl implements PraiseRequest.IPraiseResponce {

        @Override
        public void success(String userId, boolean praise) {
            assignment.hasPraised = !assignment.hasPraised;
            if (assignment.hasPraised)
                assignment.praiseNum++;
            else
                assignment.praiseNum--;
            workDetailFavorTxVw.setText(assignment.praiseNum + AssignmentDetailActivity.this.getString(R.string.favorCount));
            setPraiseLayout();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(AssignmentDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(AssignmentDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }
}