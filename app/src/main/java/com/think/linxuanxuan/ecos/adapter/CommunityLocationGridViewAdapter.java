package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.PhotoAlbumActivity;
import com.think.linxuanxuan.ecos.utils.ImageLoader;
import com.think.linxuanxuan.ecos.utils.OnImageLoad;

public class CommunityLocationGridViewAdapter extends BaseAdapter {

    private Context context;
    private String[] strings;
    private ViewHolder holder;
//    private int[] locationCommunityCount;

    public CommunityLocationGridViewAdapter(Context context, String[] strings) {
        this.context = context;
        this.strings = strings;
//        this.locationCommunityCount = locationCommunityCount;
    }

    @Override
    public int getCount() {
        System.out.println(strings.length);
        return strings.length;
    }

    @Override
    public String getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_location_button, null);
            holder = new ViewHolder();
            holder.btn = (Button) convertView.findViewById(R.id.btn_gridview_location);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

//        // locationCommunityCount[]记录了地区活动的数量
//        if (locationCommunityCount[position] <= 99)
//            holder.btn.setText(strings[position] + " ( " + locationCommunityCount[position] + " )");
//        else
//            holder.btn.setText(strings[position] + " ( " + 99 + "+" + " )");

        holder.btn.setText(strings[position]);

        holder.btn.setTag("Button" + position);

        // 使button不可以被点击，才会在点击button的时候触发父容器的onItemClick函数
        holder.btn.setPressed(false);
        holder.btn.setFocusable(false);
        holder.btn.setClickable(false);

        return convertView;
    }

    class ViewHolder {
        Button btn;
    }
}
