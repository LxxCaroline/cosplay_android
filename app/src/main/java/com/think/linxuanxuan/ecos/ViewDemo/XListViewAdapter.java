package com.think.linxuanxuan.ecos.ViewDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;

/**
 * 类描述：
 * Created by enlizhang on 2015/7/21.
 */
public class XListViewAdapter extends BaseAdapter
{
    Context mContext;

    String mDatas[];

    private LayoutInflater mInflater;

    public XListViewAdapter(Context context,String datas[]){
        mContext = context;
        mDatas = datas;

        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.length;
    }


    @Override
    public Object getItem(int position) {
        return mDatas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {

        /*** 批量选择框*/
        TextView tv_item;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.demo_item_xlistview, null);
            holder = new ViewHolder();

            holder.tv_item = (TextView)convertView.findViewById(R.id.tv_item);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_item.setText(mDatas[position]);

        return convertView;
    }
}
