package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.CourseDetailActivity;
import com.think.linxuanxuan.ecos.model.Course;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/8/2.
 */

public class PersonCourseAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mcontext;
    private List<Course> courseList;

    public PersonCourseAdapter(Context context) {
        this.mcontext = context;
    }

    public PersonCourseAdapter(Context context, List<Course> courseList) {
        this.mcontext = context;
        this.courseList = courseList;
    }

    public void SetCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    class ViewHolder {

        private ImageView iv_cover;
        private TextView tv_praiseNum;
        private TextView tv_title;
        private TextView tv_time;


        public ViewHolder(View root) {
            iv_cover = (ImageView) root.findViewById(R.id.iv_cover);

            tv_praiseNum = (TextView) root.findViewById(R.id.tv_praiseNum);
            tv_title = (TextView) root.findViewById(R.id.tv_title);
            tv_time = (TextView) root.findViewById(R.id.tv_time);
        }

        public void setData(final int position) {
            Course item = courseList.get(position);
            if (item.coverUrl != null &&  !TextUtils.isEmpty(item.coverUrl))
                Picasso.with(mcontext).load(item.coverUrl).placeholder(R.drawable.img_default).into(iv_cover);
            else
                iv_cover.setImageResource(R.drawable.img_default);
            tv_title.setText(item.title);
            tv_praiseNum.setText(item.praiseNum + "");
            tv_time.setText(item.getDateDescription());

            iv_cover.setTag(position);
            tv_title.setTag(position);
            iv_cover.setOnClickListener(PersonCourseAdapter.this);
            tv_title.setOnClickListener(PersonCourseAdapter.this);


        }
    }

    @Override
    public int getCount() {
        if (courseList == null) {
            return 0;
        } else {
            return courseList.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_personage_course, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Intent intent;
        Bundle bundle = new Bundle();
//        switch (v.getId()) {
//            default:
        intent = new Intent(mcontext, CourseDetailActivity.class);
        bundle.putString(CourseDetailActivity.CourseID, courseList.get(position).courseId);
//                break;
//        }
        intent.putExtras(bundle);
        mcontext.startActivity(intent);

    }


}
