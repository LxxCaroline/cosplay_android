package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.ActivityDetailActivity;
import com.think.linxuanxuan.ecos.model.ActivityModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hzzhanyawei on 2015/8/5.
 * Email hzzhanyawei@corp.think.linxuanxuan.com
 */
public class PersonActivityAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mcontext;
    private List<ActivityModel> activityList;

    public PersonActivityAdapter(Context context) {
        this.mcontext = context;
    }
    public PersonActivityAdapter(Context context, List<ActivityModel> activityList) {
        this.mcontext = context;
        this.activityList = activityList;
    }


    public void setActivityList(List<ActivityModel> activityList) {
        this.activityList = activityList;
    }

    public List<ActivityModel> getActivityList() {
        return this.activityList;
    }

    public Context getMcontext() {
        return mcontext;
    }



    class ViewHolder {

        private ImageView iv_cover;
        private TextView tv_tag;
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_location;
        private LinearLayout ll_activity;


        public ViewHolder(View root) {
            iv_cover = (ImageView) root.findViewById(R.id.iv_campaign_dis);
            tv_tag = (TextView) root.findViewById(R.id.activityTypeTxVw);
            tv_title = (TextView) root.findViewById(R.id.tv_campaign_title);
            tv_time = (TextView) root.findViewById(R.id.tv_campaign_time);
            tv_location = (TextView) root.findViewById(R.id.tv_campaign_location);
            ll_activity = (LinearLayout) root.findViewById(R.id.ll_activity_item);
        }

        /**
         * 传入数据
         */
        public void setData(final int position) {
            ActivityModel item = activityList.get(position);
            if (item.coverUrl != null && !TextUtils.isEmpty(item.coverUrl))
                Picasso.with(mcontext).load(item.coverUrl).placeholder(R.drawable.img_default).into(iv_cover);
            else
                iv_cover.setImageResource(R.drawable.img_default);
            tv_title.setText(item.title);
            tv_tag.setText(item.activityType + "");
            tv_time.setText(item.activityTime.getDate());
            tv_location.setText(item.location.toString());
            ll_activity.setTag(position);
            ll_activity.setOnClickListener(PersonActivityAdapter.this);
        }
    }

    @Override
    public int getCount() {
        if (activityList != null){
            return activityList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return activityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_activity_personal, null);
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
        Intent intent = new Intent(mcontext, ActivityDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ActivityDetailActivity.ActivityID, activityList.get(position).activityId);
        intent.putExtras(bundle);
        mcontext.startActivity(intent);
    }
}
