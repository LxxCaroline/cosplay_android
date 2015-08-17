package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Comment;

import java.util.List;


/**
 * Created by hzjixinyu on 2015/7/27.
 */
public class DisplayItemEvalutionViewAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Comment> commentList;

    public DisplayItemEvalutionViewAdapter(Context context, List<Comment> commentList) {
        this.mcontext = context;
        this.commentList = commentList;
    }


    @Override
    public int getCount() {
        if (commentList.size() > 3) {
            return 3;
        } else {
            return commentList.size();
        }
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_display_item_evalution, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }

    class ViewHolder {

        private TextView tv_name;
        private TextView tv_evaluation;

        public ViewHolder(View root) {
            tv_name = (TextView) root.findViewById(R.id.tv_name);
            tv_evaluation = (TextView) root.findViewById(R.id.tv_evaluation);
        }

        public void setData(int position) {
            Comment item = commentList.get(position);
            tv_name.setText(item.fromNickName );
            tv_evaluation.setText(item.content);
        }
    }
}
