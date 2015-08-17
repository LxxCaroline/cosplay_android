package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Image;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/21.
 */
public class ActivityPhotoHListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Image> imageList;

    public ActivityPhotoHListViewAdapter(Context c, List<Image> imageList) {
        mContext = c;
        this.imageList = imageList;
    }

    //TODO 假定数量
    @Override
    public int getCount() {
        return imageList.size();
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
        private ImageView iv_image;
        private TextView tv_time;
        private TextView tv_name;
        private ImageView iv_avatar;
        private LinearLayout ll_author;

        ViewHolder(View v) {
            iv_image = (ImageView) v.findViewById(R.id.iv_image);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            ll_author = (LinearLayout) v.findViewById(R.id.ll_author);
        }

        void setData(int position) {
//            Course.Assignment item=assignmentList.get(position);
//            Picasso.with(mContext).load(item.imageUrl).placeholder(R.drawable.img_default).into(iv_image);
//            Picasso.with(mContext).load(item.authorAvatarUrl).placeholder(R.drawable.img_default).into(iv_avatar);
//            tv_name.setText(item.author);
//            tv_time.setText(item.getDateDescription());
        }
    }
}

