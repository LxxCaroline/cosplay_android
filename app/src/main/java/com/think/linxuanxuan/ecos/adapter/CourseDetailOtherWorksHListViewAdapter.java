package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.utils.RoundAngleImageView;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/21.
 */
public class CourseDetailOtherWorksHListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Course.Assignment> assignmentList = new ArrayList<Course.Assignment>();

    public CourseDetailOtherWorksHListViewAdapter(Context c, List<Course.Assignment> assignmentList) {
        mContext = c;
        this.assignmentList = assignmentList;
    }

    //TODO 假定数量
    @Override
    public int getCount() {
        return assignmentList.size();
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
            convertView = parent.inflate(mContext, R.layout.item_coursedetail_otherworks, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }

    class ViewHolder {
        private RoundAngleImageView iv_image;
        private TextView tv_time;
        private TextView tv_name;
        private RoundImageView iv_avatar;
        private LinearLayout ll_author;

        ViewHolder(View v) {
            iv_image = (RoundAngleImageView) v.findViewById(R.id.iv_image);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            iv_avatar = (RoundImageView) v.findViewById(R.id.iv_avatar);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            ll_author = (LinearLayout) v.findViewById(R.id.ll_author);
        }

        void setData(int position) {
            Course.Assignment item = assignmentList.get(position);
            if (item.imageUrl != null &&  !TextUtils.isEmpty(item.imageUrl))
                Picasso.with(mContext).load(item.imageUrl).placeholder(R.drawable.img_default).into(iv_image);
            else
                iv_image.setImageResource(R.drawable.img_default);
            iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
            iv_avatar.setErrorImageResId(R.mipmap.bg_female_default);
            if (item.authorAvatarUrl != null && !item.authorAvatarUrl.equals("")) {
                RequestQueue queue = MyApplication.getRequestQueue();
                ImageLoader.ImageCache imageCache = new SDImageCache();
                ImageLoader imageLoader = new ImageLoader(queue, imageCache);
                iv_avatar.setImageUrl(item.authorAvatarUrl, imageLoader);
            } else {
                iv_avatar.setImageResource(R.mipmap.bg_female_default);
            }
            tv_name.setText(item.author);
            tv_time.setText(item.getDateDescription());
        }
    }
}

