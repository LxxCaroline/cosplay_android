package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Course;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/27.
 */
public class CourseTypeActivity extends Activity implements View.OnClickListener {
    private final String TAG = "Ecos---CourseType";
    @InjectView(R.id.tv_type_1)
    ImageView makeuperImgVw;
    @InjectView(R.id.tv_type_2)
    ImageView propImgVw;
    @InjectView(R.id.tv_type_3)
    ImageView hairImgVw;
    @InjectView(R.id.tv_type_4)
    ImageView costumeImgVw;
    @InjectView(R.id.tv_type_5)
    ImageView photographyImgVw;
    @InjectView(R.id.tv_type_6)
    ImageView backstageImgVw;
    @InjectView(R.id.tv_type_7)
    ImageView experienceImgVw;
    @InjectView(R.id.tv_type_8)
    ImageView othersImgVw;
    @InjectView(R.id.rl_contain)
    RelativeLayout rl_contain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_activity);
        ButterKnife.inject(this);
        makeuperImgVw.setOnClickListener(this);
        propImgVw.setOnClickListener(this);
        hairImgVw.setOnClickListener(this);
        costumeImgVw.setOnClickListener(this);
        photographyImgVw.setOnClickListener(this);
        backstageImgVw.setOnClickListener(this);
        experienceImgVw.setOnClickListener(this);
        othersImgVw.setOnClickListener(this);
        rl_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Course.CourseType courseType = Course.CourseType.化妆;
        switch (v.getId()) {
            case R.id.tv_type_1:
                courseType = Course.CourseType.化妆;
                break;
            case R.id.tv_type_2:
                courseType = Course.CourseType.道具;
                break;
            case R.id.tv_type_3:
                courseType = Course.CourseType.摄影;
                break;
            case R.id.tv_type_4:
                courseType = Course.CourseType.后期;
                break;
            case R.id.tv_type_5:
                courseType = Course.CourseType.假发;
                break;
            case R.id.tv_type_6:
                courseType = Course.CourseType.服装;
                break;
            case R.id.tv_type_7:
                courseType = Course.CourseType.心得;
                break;
            case R.id.tv_type_8:
                courseType = Course.CourseType.其他;
                break;
        }
        Intent intent = new Intent(CourseTypeActivity.this, BuildCourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BuildCourseActivity.COURSE_TYPE, courseType.getBelongs());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
