package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.CourseStepAdapter;
import com.think.linxuanxuan.ecos.dialog.SetPhotoDialog;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.course.CreateCourseRequest;
import com.think.linxuanxuan.ecos.utils.SetPhotoHelper;
import com.think.linxuanxuan.ecos.utils.UploadImageTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 类描述：创建教程
 * Created by enlizhang on 2015/7/22.
 */
public class BuildCourseActivity extends BaseActivity {

    private final String TAG = "Ecos---BuildCourse";
    public static final String COURSE_TYPE = "courseType";

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    /**
     * 教程名称
     */
    @InjectView(R.id.etv_course_title)
    TextView etv_course_title;

    /**
     * 教程封面
     */
    @InjectView(R.id.iv_course_cover)
    ImageView iv_course_cover;

    /**
     * 教程步骤列表
     */
    @InjectView(R.id.lv_build_course)
    ListView lv_build_course;

    /**
     * 添加步骤
     */
    @InjectView(R.id.btn_add_step)
    Button btn_add_step;


    /**
     * 发布教程
     */
    @InjectView(R.id.btn_iss_course)
    Button btn_iss_course;

    public SetPhotoHelper mSetPhotoHelper;

    /**
     * 当前正在设置封面的图片
     */
    public boolean isSettingCoverPhoto = false;

    /**
     * 当前正在设置教程步骤的图片
     */
    public boolean isSettingStepPhoto = false;

    /**
     * 当前正在设置第(couserStepPosition+1)步的教程图片
     */
    public int mCouserStepPosition = 0;


    CourseStepAdapter mCourseStepAdapter;

    /**
     * {@link com.think.linxuanxuan.ecos.model.Course.CourseType}枚举值, it's value not name.
     */
    public String mCourseTypeValue;

    /**
     * 教程封面本地路径
     */
    public String mCoverLocalPath;

    /**
     * 教程标题
     */
    public String mCourseTitle;


    protected void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        Log.i(CLASS_TAG, "onCreate()");
        setContentView(R.layout.activity_build_course);

        if (getIntent() != null) {
            mCourseTypeValue = getIntent().getExtras().getString(COURSE_TYPE);
        }

        //注解工具初始化
        ButterKnife.inject(this);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildCourseActivity.this.finish();
            }
        });
        title_right.setVisibility(View.INVISIBLE);
        title_right_text.setText("发布");
        title_text.setText("新建教程");
    }

    private void initData() {


        List<Course.Step> stepsList = new ArrayList<Course.Step>();

        int i = 0;
        Course.Step step = new Course.Step(i + 1);
        stepsList.add(step);

        mCourseStepAdapter = new CourseStepAdapter(this, stepsList, new CourseStepAdapter.AdapterAction() {
            @Override
            public void setPhotoAtPosition(int position) {

                isSettingStepPhoto = true;
                mCouserStepPosition = position;

                SetPhotoDialog dialog = new SetPhotoDialog(BuildCourseActivity.this,
                        new SetPhotoDialog.ISetPhoto() {

                            @Override
                            public void choosePhotoFromLocal() {
                                Toast.makeText(BuildCourseActivity.this, "选择本地图片", Toast.LENGTH_LONG).show();
                                isSettingStepPhoto = true;
                                mSetPhotoHelper.choosePhotoFromLocal();
                            }

                            @Override
                            public void takePhoto() {
                                Toast.makeText(BuildCourseActivity.this, "拍照", Toast.LENGTH_LONG).show();
                                isSettingStepPhoto = true;
                                mSetPhotoHelper.takePhoto(false);

                            }
                        });
                dialog.showSetPhotoDialog();
            }
        });
        lv_build_course.setAdapter(mCourseStepAdapter);


        mSetPhotoHelper = new SetPhotoHelper(this, null);
        //图片裁剪后输出宽度
        final int outPutWidth = 450;
        //图片裁剪后输出高度
        final int outPutHeight = 300;
        mSetPhotoHelper.setOutput(outPutWidth, outPutHeight);
        mSetPhotoHelper.setAspect(3, 2);
        iv_course_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPhotoDialog dialog = new SetPhotoDialog(BuildCourseActivity.this, new SetPhotoDialog.ISetPhoto() {

                    @Override
                    public void choosePhotoFromLocal() {
                        isSettingCoverPhoto = true;
                        mSetPhotoHelper.choosePhotoFromLocal();
                    }

                    @Override
                    public void takePhoto() {
                        isSettingCoverPhoto = true;
                        mSetPhotoHelper.takePhoto(true);
                    }
                });
                dialog.showSetPhotoDialog();
            }
        });

        btn_add_step.setOnClickListener(mOnClickListener);
        btn_iss_course.setOnClickListener(mOnClickListener);
    }


    public void initView() {
        iv_course_cover.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int width = getResources().getDisplayMetrics().widthPixels;
                RelativeLayout.LayoutParams coverParam = (RelativeLayout.LayoutParams) iv_course_cover.getLayoutParams();
                coverParam.height = width * 2 / 3;
                iv_course_cover.setLayoutParams(coverParam);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageViewResouce(iv_course_cover);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i("onSaveInstanceState", "<--------------------");
        savedInstanceState.putParcelableArrayList("stepData", (ArrayList<Course.Step>) mCourseStepAdapter.getStepDataList());
        savedInstanceState.putString("mConurseTypeValue", mCourseTypeValue);
        savedInstanceState.putString("mCoverLocalPath", mCoverLocalPath);
        savedInstanceState.putString("mCourseTitle", mCourseTitle);

        savedInstanceState.putBoolean("isSettingCoverPhoto", isSettingCoverPhoto);
        savedInstanceState.putBoolean("isSettingCoursePhoto", isSettingStepPhoto);
        savedInstanceState.putInt("mCouserStepPosition", mCouserStepPosition);

        Log.i(TAG, "教程类型:" + mCourseTypeValue);
        Log.i(TAG, "标题:" + mCourseTitle);
        Log.i(TAG, "封面本地路径:" + mCoverLocalPath);


        for (Course.Step step : mCourseStepAdapter.getStepDataList()) {
            Log.i(TAG, "步骤:" + step.toString());
        }

        Log.i(TAG, "设置封面图:" + "当前正在设置封面图" + isSettingCoverPhoto);
        Log.i(TAG, "在设置步骤:" + "当前正在设置步骤" + isSettingStepPhoto);
        Log.i(TAG, "当前操作的教程步骤序号:" + "当前操作的教程步骤序号" + mCouserStepPosition);

        Log.i("onSaveInstanceState", "-------------------->");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        List<Course.Step> list = savedInstanceState.getParcelableArrayList("stepData");
        Log.i("onRestoreInstanceState", "--------------------");

        for (Course.Step step : list) {
            Log.i("onRestoreInstanceState", step.toString());
        }

        mCourseTypeValue = savedInstanceState.getString("mConurseTypeValue");
        mCoverLocalPath = savedInstanceState.getString("mCoverLocalPath");
        mCourseTitle = savedInstanceState.getString("mCourseTitle");

        releaseImageViewResouce(iv_course_cover);
        iv_course_cover.setImageBitmap(BitmapFactory.decodeFile(mCoverLocalPath));
        etv_course_title.setText(mCourseTitle);

        isSettingCoverPhoto = savedInstanceState.getBoolean("mConurseTypeValue");
        isSettingStepPhoto = savedInstanceState.getBoolean("isSettingCoursePhoto");
        mCouserStepPosition = savedInstanceState.getInt("isSettingCoursePhoto");

        Log.i("onRestoreInstanceState", "--------------------");

    }

    public static synchronized void releaseImageViewResouce(ImageView imageView) {
        /*if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }*/
        return;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SetPhotoHelper.REQUEST_BEFORE_CROP:
                    //当前是设置封面
                    if (isSettingCoverPhoto) {
                        mSetPhotoHelper.setmSetPhotoCallBack(new SetPhotoHelper.SetPhotoCallBack() {

                            @Override
                            public void success(String imagePath) {
                                Log.i("裁剪后图片路径", "-----------path:" + imagePath);
                                mCoverLocalPath = imagePath;
                                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                                iv_course_cover.setImageBitmap(bitmap);
                                isSettingCoverPhoto = false;
                            }
                        });
                        mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_BEFORE_CROP, data);
                        return;
                    }

                    //当前是设置教程步骤图片
                    if (isSettingStepPhoto) {
                        if (mCouserStepPosition == -1) {
                            Log.e("设置教程步骤图片", "缺少position");
                        }

                        File imageFile = mSetPhotoHelper.getFileAfterChoose();
                        if (imageFile == null)
                            imageFile = mSetPhotoHelper.getFileBeforeCrop(data, 300, 200);

                        mCourseStepAdapter.refleshImageAtPosition(mCouserStepPosition, imageFile.getAbsolutePath());

                        isSettingStepPhoto = false;
                        mCouserStepPosition = -1;
                        return;
                    }

                    break;
                case SetPhotoHelper.REQUEST_AFTER_CROP:
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_AFTER_CROP, data);
                    isSettingCoverPhoto = false;
                    break;
                default:
                    isSettingCoverPhoto = false;
                    isSettingStepPhoto = false;
                    Log.e("CLASS_TAG", "onActivityResult() 无对应");
            }

        } else {
            isSettingCoverPhoto = false;
            isSettingStepPhoto = false;
            Log.e(CLASS_TAG, "操作取消");
        }
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                //添加步骤
                case R.id.btn_add_step:
                    //若已有的步骤都已经填写完整
                    if (mCourseStepAdapter.isSomeEmpty()) {
                        Toast.makeText(BuildCourseActivity.this, "请将步骤补充完整再进行添加", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mCourseStepAdapter.getStepDataList().add(new Course.Step(mCourseStepAdapter.getCount() + 1));
                    mCourseStepAdapter.notifyDataSetChanged();
                    break;
                //发布教程
                case R.id.btn_iss_course:
                    if (mCoverLocalPath == null || "".equals(mCoverLocalPath)) {
                        Toast.makeText(BuildCourseActivity.this, "请上传封面图", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if ("".equals(etv_course_title.getText().toString())) {
                        Toast.makeText(BuildCourseActivity.this, "请填写教程名称", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (etv_course_title.getText().length()>InputLength.CourseTitle_max){
                        Toast.makeText(BuildCourseActivity.this, "", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mCourseStepAdapter.isSomeEmpty()) {
                        Toast.makeText(BuildCourseActivity.this, "请将步骤补充完整再进行添加", Toast.LENGTH_LONG).show();
                        return;
                    }
                    createCourse();

                    break;
            }
        }
    };

    public void createCourse() {
        showProcessBar("上传图片");
        uploadImages(new UploadFilesCallBack() {
            /**
             *
             * @param course 包含封面图和步骤图片
             */
            @Override
            public void finish(Course course) {
                dismissProcessBar();
                getRequestCourseByPage(course);
                CreateCourseRequest request = new CreateCourseRequest();
                request.request(new CreateCourseResponse(), course);
            }
        });
    }

    /**
     * 获取页面数据，用于请求
     *
     * @return
     */
    public Course getRequestCourseByPage(Course course) {

        course.title = etv_course_title.getText().toString();
        course.courseType = Course.CourseType.getCourseType(mCourseTypeValue);

        Log.v(TAG, "发布教程数据:------------" + course.toString());
        return course;
    }


    public void uploadImages(final UploadFilesCallBack callBack) {

        final Course course = new Course();
        course.stepList = mCourseStepAdapter.getStepDataList();

        final AtomicInteger uploadNumber = new AtomicInteger(1 + course.stepList.size());
        //获取图片
        if (mCoverLocalPath != null && new File(mCoverLocalPath).exists()) {
            UploadImageTools.uploadImageSys(new File(mCoverLocalPath), new UploadImageTools.UploadCallBack() {

                @Override
                public void success(String originUrl, String thumbUrl) {
                    Log.e("图片上传", "封面图上传成功");

                    course.coverUrl = originUrl;
                    Log.e("图片上传", "原图路径" + originUrl);
                    Log.e("图片上传", "缩略图路径" + thumbUrl);
                    int restNum = uploadNumber.decrementAndGet();
                    Log.e("图片上传", "还剩" + restNum + "张图片");

                    if (uploadNumber.get() == 0) {
                        callBack.finish(course);
                    }
                }

                @Override
                public void fail() {
                    Toast.makeText(BuildCourseActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                    uploadNumber.decrementAndGet();

                    if (uploadNumber.get() == 0) {
                        callBack.finish(course);
                    }
                }

                @Override
                public void onProcess(Object fileParam, long current, long total) {
//                    Log.i("图片上传", "上传数" + total + "  ," + "已上传" + current);
                }

            }, BuildCourseActivity.this, false);
        } else {
            Log.e("图片上传", "图片封面为空");
            uploadNumber.decrementAndGet();
            if (uploadNumber.get() == 0) {
                callBack.finish(course);
            }
        }


        for (final Course.Step step : course.stepList) {
            if (step.imagePath != null && new File(step.imagePath).exists()) {
                UploadImageTools.uploadImageSys(new File(step.imagePath), new UploadImageTools.UploadCallBack() {

                    @Override
                    public void success(String originUrl, String thumbUrl) {
                        Log.e("图片上传", "封面图上传成功");
                        Log.e("图片上传", "原图路径" + originUrl);
                        Log.e("图片上传", "缩略图路径" + thumbUrl);

                        step.imageUrl = originUrl;
                        int restNum = uploadNumber.decrementAndGet();
                        Log.e("图片上传", "还剩" + restNum + "张图片");

                        if (uploadNumber.get() == 0) {
                            callBack.finish(course);
                        }
                    }

                    @Override
                    public void fail() {
                        int restNum = uploadNumber.decrementAndGet();
                        Toast.makeText(BuildCourseActivity.this, "上传失败", Toast.LENGTH_LONG).show();

                        if (uploadNumber.get() == 0) {
                            callBack.finish(course);
                        }
                    }

                    @Override
                    public void onProcess(Object fileParam, long current, long total) {
//                        Log.i("图片上传", "上传数" + total + "  ," + "已上传" + current);
                    }

                }, BuildCourseActivity.this, false);
            } else {
                Log.e("图片上传", "序号为" + step.stepIndex + "的图片本地路径为空");

                uploadNumber.decrementAndGet();

                if (uploadNumber.get() == 0) {
                    callBack.finish(course);
                }
            }
        }
    }


    interface UploadFilesCallBack {
        public void finish(Course course);
    }


    /**
     * 创建教程响应回调接口
     */
    class CreateCourseResponse extends BaseResponceImpl implements CreateCourseRequest.ICreateCourseResponse {

        @Override
        public void success(Course course) {
            Log.e(TAG, "上传成功");
            finish();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(BuildCourseActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

}
