package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Course;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/28.
 */
public class CourseDetailStepAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Course.Step> stepList;

    public CourseDetailStepAdapter(Context context, List<Course.Step> stepList) {
        this.mcontext = context;
        this.stepList = stepList;
    }


    class ViewHolder {

        private ImageView iv_stepImage;
        private TextView tv_stepNum;
        private TextView tv_stepText;
        private TextView gap1;
        private TextView gap2;


        public ViewHolder(View root) {
            iv_stepImage = (ImageView) root.findViewById(R.id.iv_stepImage);

            tv_stepNum = (TextView) root.findViewById(R.id.tv_stepNum);
            tv_stepText = (TextView) root.findViewById(R.id.tv_stepText);

            gap1 = (TextView) root.findViewById(R.id.gap1);
            gap2 = (TextView) root.findViewById(R.id.gap2);
            gap1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            gap2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        public void setData(int position) {
            Course.Step item = stepList.get(position);
            if (item.imageUrl != null &&  !TextUtils.isEmpty(item.imageUrl))
                Picasso.with(mcontext).load(item.imageUrl).placeholder(R.drawable.img_default).into(iv_stepImage);
            else
                iv_stepImage.setImageResource(R.drawable.img_default);
            tv_stepText.setText(item.description);
            tv_stepNum.setText(position + 1 + "");
        }
    }

    @Override
    public int getCount() {
        return stepList.size();
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
            convertView = parent.inflate(mcontext, R.layout.item_course_detail_step, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }
}
