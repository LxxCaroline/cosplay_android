package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.PhotoAdappter;
import com.think.linxuanxuan.ecos.model.PhotoAibum;
import com.think.linxuanxuan.ecos.model.PhotoItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/26.
 */
public class PhotoActivity extends Activity {
    private GridView gv;
    private PhotoAibum aibum;
    private PhotoAdappter adapter;
    private TextView tv;
    private int chooseNum = 0;
    private Button btn_sure;
    private LayoutInflater inflater;
    private ImageView imageViewPhotoSelect;

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    private ArrayList<String> paths = new ArrayList<String>();
    private ArrayList<String> ids = new ArrayList<String>();
    private ArrayList<PhotoItem> gl_arr = new ArrayList<PhotoItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoalbum_gridview);
        ButterKnife.inject(this);

        btn_sure = (Button) findViewById(R.id.btn_sure);
        aibum = (PhotoAibum) getIntent().getExtras().get("aibum");

        for (int i = 0; i < aibum.getBitList().size(); i++) {
            if (aibum.getBitList().get(i).isSelect()) {
                chooseNum++;
            }
        }
        gv = (GridView) findViewById(R.id.photo_gridview);

        adapter = new PhotoAdappter(PhotoActivity.this, aibum, null);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoItem gridItem = aibum.getBitList().get(position);
                if (aibum.getBitList().get(position).isSelect()) {
                    aibum.getBitList().get(position).setSelect(false);
                    paths.remove(aibum.getBitList().get(position).getPath());
                    ids.remove(aibum.getBitList().get(position).getPhotoID() + "");
                    gl_arr.remove(aibum.getBitList().get(position));
                    chooseNum--;
                    gv.findViewWithTag(position).setVisibility(View.INVISIBLE);
                } else {
                    aibum.getBitList().get(position).setSelect(true);
                    ids.add(aibum.getBitList().get(position).getPhotoID() + "");
                    paths.add(aibum.getBitList().get(position).getPath());
                    gl_arr.add(aibum.getBitList().get(position));
                    chooseNum++;
                    gv.findViewWithTag(position).setVisibility(View.VISIBLE);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoActivity.this, UploadDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("paths", paths);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        initTitle();
    }

    private void initTitle() {
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("选择图片");
    }
}
