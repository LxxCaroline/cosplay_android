package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/27.
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private Context mcontext;
    private List<String> list;

    public SearchHistoryAdapter(Context context, List<String> list) {
        this.mcontext = context;
        this.list=list;
    }

    public class SearchHistoryViewHolder {

        public TextView tv_search;
        public ImageView iv_delete;

        public SearchHistoryViewHolder(View root) {
            tv_search = (TextView) root.findViewById(R.id.tv_search);
            iv_delete = (ImageView) root.findViewById(R.id.iv_delete);
        }


        public void setData(final int position) {
            tv_search.setText(list.get(position));


            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public List<String> getList(){
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        SearchHistoryViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_search, null);
            viewHolder = new SearchHistoryViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchHistoryViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }
}
