package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.CourseDetailOtherWorksHListViewAdapter;
import com.think.linxuanxuan.ecos.adapter.CourseDetailStepAdapter;
import com.think.linxuanxuan.ecos.dialog.SetPhotoDialog;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.course.GetCourseDetailRequest;
import com.think.linxuanxuan.ecos.request.course.PraiseRequest;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.think.linxuanxuan.ecos.utils.SetPhotoHelper;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;
import com.think.linxuanxuan.ecos.views.HorizontalListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "Ecos---CourseDetail";
    public static final String CourseID = "CourseID";
    //the widget of the title bar
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.iv_cover)
    ImageView iv_cover;
    @InjectView(R.id.ll_praise)
    LinearLayout ll_praise;
    @InjectView(R.id.tv_praise)
    TextView tv_praise;
    @InjectView(R.id.iv_praise)
    ImageView iv_praise;
    @InjectView(R.id.tv_title1)
    TextView tv_title;
    @InjectView(R.id.tv_praiseNum)
    TextView tv_praiseNum;
    @InjectView(R.id.ll_author)
    LinearLayout ll_author;
    @InjectView(R.id.iv_avatar)
    RoundImageView iv_avatar;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.tv_otherWorks)
    TextView tv_otherWorks;
    @InjectView(R.id.hlv_otherWorks)
    HorizontalListView hlv_otherWorks;
    @InjectView(R.id.ll_uploadMyWork)
    LinearLayout ll_updoadMyWork;
    @InjectView(R.id.btn_allEvaluation)
    Button btn_allEvaluation;
    @InjectView(R.id.course_detail_scrollveiw)
    ScrollView scrollView;
    @InjectView(R.id.lv_courseStep)
    ExtensibleListView lv_courseStep;

    //教程步骤
    private CourseDetailStepAdapter courseDetailStepAdapter;
    //作业列表
    private CourseDetailOtherWorksHListViewAdapter courseDetailOtherWorksHListViewAdapter;

    private SetPhotoHelper mSetPhotoHelper;
    //图片裁剪后输出宽度
    private final int outPutWidth = 300;
    //图片裁剪后输出高度
    private final int outPutHeight = 450;

    //record the list of assignment id.
    private ArrayList<String> workList;
    private String courseId = "";
    private Course course;
    //for request
    private GetCourseDetailRequest getCourseDetailRequest;
    private GetCourseDetailResponse getCourseDetailResponse;
    private PraiseRequest praiseRequest;
    private PraiseResponse praiseResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.inject(this);
        initTitle();
        initData();
        initView();
        getSupportActionBar().hide();
    }

    private void initTitle() {
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseDetailActivity.this.finish();
            }
        });
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("教程详情");
    }

    private void initData() {
        courseId = getIntent().getExtras().getString(CourseID);
        getCourseDetailRequest = new GetCourseDetailRequest();
        getCourseDetailResponse = new GetCourseDetailResponse();
        showProcessBar(getResources().getString(R.string.loading));
        getCourseDetailRequest.request(getCourseDetailResponse, courseId);
    }

    private void initView() {
        btn_allEvaluation.setOnClickListener(this);
        ll_updoadMyWork.setOnClickListener(this);
        ll_author.setOnClickListener(this);
        ll_praise.setOnClickListener(this);
        lv_courseStep.setDividerHeight(0);

        ViewGroup.LayoutParams params = iv_cover.getLayoutParams();
        params.height = DisplayWidth * 2 / 3;
        iv_cover.setLayoutParams(params);

        hlv_otherWorks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseDetailActivity.this, AssignmentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(AssignmentDetailActivity.Work_List, workList);
                bundle.putInt(AssignmentDetailActivity.Work_Order, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //拦截listview的touch事件，不会造成scrollview的滑动。
        hlv_otherWorks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mSetPhotoHelper = new SetPhotoHelper(this, null);
        mSetPhotoHelper.setOutput(outPutWidth, outPutHeight);
        mSetPhotoHelper.setAspect(2, 3);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.ll_author:
                intent = new Intent(CourseDetailActivity.this, PersonageDetailActivity.class);
                bundle = new Bundle();
                bundle.putBoolean(PersonageDetailActivity.IsOwn, false);
                bundle.putString(PersonageDetailActivity.UserID, course.userId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.ll_uploadMyWork:
                SetPhotoDialog dialog = new SetPhotoDialog(CourseDetailActivity.this, new SetPhotoDialog.ISetPhoto() {

                    @Override
                    public void choosePhotoFromLocal() {
                        mSetPhotoHelper.choosePhotoFromLocal();
                    }

                    @Override
                    public void takePhoto() {
                        mSetPhotoHelper.takePhoto(true);
                    }
                });
                dialog.showSetPhotoDialog();
                break;
            case R.id.btn_allEvaluation:
                intent = new Intent(CourseDetailActivity.this, CommentDetailActivity.class);
                bundle = new Bundle();
                bundle.putString(CommentDetailActivity.FromId, courseId);
                bundle.putString(CommentDetailActivity.CommentType, Comment.CommentType.教程.getBelongs());
                bundle.putBoolean(CommentDetailActivity.IsPraised, course.hasPraised);
                intent.putExtras(bundle);
                startActivityForResult(intent, UploadAssignmentActivity.REQUEST_CODE_FOR_UPLOAD_ASSIGNMENT);
                break;
            case R.id.ll_praise:
                if (praiseRequest == null)
                    praiseRequest = new PraiseRequest();
                if (praiseResponse == null)
                    praiseResponse = new PraiseResponse();
                praiseRequest.praiseCourse(praiseResponse, course.courseId, !course.hasPraised);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SetPhotoHelper.REQUEST_BEFORE_CROP:
                    mSetPhotoHelper.setmSetPhotoCallBack(
                            new SetPhotoHelper.SetPhotoCallBack() {
                                @Override
                                public void success(String imagePath) {
                                    Intent intent = new Intent(CourseDetailActivity.this, UploadAssignmentActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(UploadAssignmentActivity.CourseId, courseId);
                                    bundle.putString(UploadAssignmentActivity.ImagePath, imagePath);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, UploadAssignmentActivity.REQUEST_CODE_FOR_UPLOAD_ASSIGNMENT);
                                }
                            });
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_BEFORE_CROP, data);
                    break;
                case SetPhotoHelper.REQUEST_AFTER_CROP:
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_AFTER_CROP, data);
                    break;
                default:
                    Log.e("CLASS_TAG", "onActivityResult() 无对应");
            }
        } else if (requestCode == UploadAssignmentActivity.REQUEST_CODE_FOR_UPLOAD_ASSIGNMENT) {
            showProcessBar(getResources().getString(R.string.loading));
            getCourseDetailRequest.request(getCourseDetailResponse, courseId);
        }
    }

    /**
     * 网络返回事件处理类
     */
    class GetCourseDetailResponse extends BaseResponceImpl implements GetCourseDetailRequest.ICourseDetailResponse {

        @Override
        public void success(Course course) {
            bindData(course);
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(CourseDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(CourseDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 绑定网络返回数据Course
     *
     * @param course
     */
    private void bindData(Course course) {
        this.course = course;
        tv_title.setText(course.title);
        tv_name.setText(course.author);
        tv_praiseNum.setText(course.praiseNum + getResources().getString(R.string.manyFavor));
        setPraiseLayout();
        iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
        iv_avatar.setErrorImageResId(R.mipmap.bg_female_default);
        if (course.authorAvatarUrl != null && !course.authorAvatarUrl.equals("")) {
            RequestQueue queue = MyApplication.getRequestQueue();
            ImageLoader.ImageCache imageCache = new SDImageCache(300,200);
            ImageLoader imageLoader = new ImageLoader(queue, imageCache);
            if (course.authorAvatarUrl != null && !course.authorAvatarUrl.equals(""))
                iv_avatar.setImageUrl(course.authorAvatarUrl, imageLoader);
            iv_avatar.setDefaultImageResId(R.drawable.img_default);
            iv_avatar.setErrorImageResId(R.drawable.img_default);
        } else {
            iv_avatar.setImageResource(R.mipmap.bg_female_default);
        }
        if (course.coverUrl != null &&  !TextUtils.isEmpty(course.coverUrl))
            Picasso.with(CourseDetailActivity.this).load(course.coverUrl).placeholder(R.drawable.img_default).into(iv_cover);
        else
            iv_cover.setImageResource(R.drawable.img_default);
        tv_otherWorks.setText(course.assignmentList.size() + getResources().getString(R.string.manyAssignment));
        courseDetailStepAdapter = new CourseDetailStepAdapter(this, course.stepList);
        lv_courseStep.setAdapter(courseDetailStepAdapter);
        //if there is no assignment.
        if (course.assignmentList.size() != 0)
            hlv_otherWorks.setVisibility(View.VISIBLE);
        courseDetailOtherWorksHListViewAdapter = new CourseDetailOtherWorksHListViewAdapter(this, course.assignmentList);
        hlv_otherWorks.setAdapter(courseDetailOtherWorksHListViewAdapter);

        workList = new ArrayList<String>();
        for (int i = 0; i < course.assignmentList.size(); i++) {
            workList.add(course.assignmentList.get(i).assignmentId);

        }
        dismissProcessBar();
    }

    private void setPraiseLayout() {
        if (course.hasPraised) {
            tv_praise.setText(getResources().getString(R.string.cancelFavor));
            iv_praise.setImageResource(R.mipmap.ic_praise_fill);
        } else {
            tv_praise.setText(getResources().getString(R.string.favour));
            iv_praise.setImageResource(R.mipmap.ic_praise_block);
        }
    }

    class PraiseResponse extends BaseResponceImpl implements PraiseRequest.IPraiseResponce {

        @Override
        public void success(String userId, boolean praise) {
            course.hasPraised = !course.hasPraised;
            setPraiseLayout();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(CourseDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(CourseDetailActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }
}
