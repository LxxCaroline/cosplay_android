package com.think.linxuanxuan.ecos.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.course.CreateAssignmentRequest;
import com.think.linxuanxuan.ecos.utils.UploadImageTools;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/8/1.
 */
public class UploadAssignmentActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "Ecos---UploadWork";
    public static final String CourseId = "courseId";
    public static final String ImagePath = "image_path";
    public static int REQUEST_CODE_FOR_UPLOAD_ASSIGNMENT = 1;
    public static int RESULT_CODE_FOR_UPLOAD_ASSIGNMENT = 2;

    private String courseId = "";
    private String image_path = "";
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.uploadWorkImgVw)
    ImageView imageView;
    @InjectView(R.id.uploadWorkEdTx)
    EditText uploadWorkEdTx;
    @InjectView(R.id.shareLayout)
    LinearLayout shareLayout;

    //for request
    private CreateAssignmentRequest createAssignmentRequest;

    private CreateAssignmentResponce createAssignmentResponce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_work_layout);
        ButterKnife.inject(this);

        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_right_text.setText("上传");
        title_text.setText("");
    }

    void initData() {
        createAssignmentRequest = new CreateAssignmentRequest();
        createAssignmentResponce = new CreateAssignmentResponce();
        image_path = getIntent().getExtras().getString(ImagePath);
        courseId = getIntent().getExtras().getString(CourseId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    void initView() {
        Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        if (bitmap == null)
            Log.d(TAG, "bitmap is null");
        imageView.setImageBitmap(bitmap);
        //implementation on the title bar
        shareLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_right_action:
                if (uploadWorkEdTx.getText().toString().equals("")) {
                    Toast.makeText(UploadAssignmentActivity.this, UploadAssignmentActivity.this.getString(R.string.notFinished), Toast.LENGTH_SHORT).show();
                    return;
                }
                showProcessBar(getResources().getString(R.string.uploading));
                File file = new File(image_path);
                UploadImageTools.uploadImageSys(file, new UploadWorkCallBack(), UploadAssignmentActivity.this, false);
                break;
            case R.id.lly_left_action:
                UploadAssignmentActivity.this.finish();
                break;
            case R.id.shareLayout:
                Toast.makeText(UploadAssignmentActivity.this, getResources().getString(R.string.noWeixin), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    class UploadWorkCallBack implements UploadImageTools.UploadCallBack {

        @Override
        public void success(String originUrl, String thumbUrl) {
            Log.d(TAG, "have got the url already.");
            Course.Assignment assignment = new Course.Assignment();
            assignment.courseId = courseId;
            assignment.imageUrl = originUrl;
            assignment.content = uploadWorkEdTx.getText().toString();
            createAssignmentRequest.request(createAssignmentResponce, assignment);
        }

        @Override
        public void fail() {
            Log.e("test", "failed to upload the image.");
        }

        @Override
        public void onProcess(Object fileParam, long current, long total) {
            Log.i(TAG, "总数" + total + "  ," + "已上传" + current);
        }

    }

    class CreateAssignmentResponce extends BaseResponceImpl implements CreateAssignmentRequest.ICreateAssignmentResponce {

        @Override
        public void success(Course.Assignment assignment) {
            dismissProcessBar();
            Toast.makeText(UploadAssignmentActivity.this, getResources().getString(R.string.uploadAssignmentSuccessfully), Toast.LENGTH_SHORT).show();
            setResult(RESULT_CODE_FOR_UPLOAD_ASSIGNMENT);
            finish();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(UploadAssignmentActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(UploadAssignmentActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }
}

