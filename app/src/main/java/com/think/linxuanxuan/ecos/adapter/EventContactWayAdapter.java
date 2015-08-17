package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.ActivityModel;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/8/4.
 */
public class EventContactWayAdapter extends BaseAdapter {
    private Context mcontext;
    private List<ActivityModel.Contact> contactWayList;

    public EventContactWayAdapter(Context context, List<ActivityModel.Contact> contactWayList) {
        this.mcontext = context;
        this.contactWayList = contactWayList;
    }


    class ViewHolder {
        private ImageView iv_type;
        private TextView tv_detail;


        public ViewHolder(View root) {
            iv_type = (ImageView) root.findViewById(R.id.iv_type);
            tv_detail = (TextView) root.findViewById(R.id.tv_detail);
        }

        /**
         * 传入数据未定
         */
        public void setData(final int position) {
            ActivityModel.ContactWay contactWay = contactWayList.get(position).contactWay;
            if (contactWay == ActivityModel.ContactWay.QQ || contactWay == ActivityModel.ContactWay.QQ群){
                iv_type.setImageResource(R.mipmap.ic_qq_pink);
                tv_detail.setText("QQ  " + contactWayList.get(position).value);
            }
            else if (contactWay == ActivityModel.ContactWay.电话){
                iv_type.setImageResource(R.mipmap.ic_phone_pink);
                tv_detail.setText("手机 " + contactWayList.get(position).value);
            }
            else{
                iv_type.setImageResource(R.mipmap.ic_weibo_pink);
                tv_detail.setText("微博 " + contactWayList.get(position).value);
            }
        }
    }

    public int getCount() {
        return contactWayList.size();
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
            convertView = parent.inflate(mcontext, R.layout.item_event_contactway, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }
}
