package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.UploadWorksListAdapter;
import com.think.linxuanxuan.ecos.dialog.SetPhotoDialog;
import com.think.linxuanxuan.ecos.model.Image;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.share.CreateShareRequest;
import com.think.linxuanxuan.ecos.utils.SetPhotoHelper;
import com.think.linxuanxuan.ecos.utils.UploadImageTools;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/8/1.
 */
public class UploadDisplayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = "Ecos---UploadWorks";
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.tv_title)
    TextView titleTxVw;
    @InjectView(R.id.tv_right_text)
    TextView tv_right_text;
    @InjectView(R.id.lly_right_action)
    LinearLayout lly_right_action;
    @InjectView(R.id.tv_left)
    TextView backTxVw;
    @InjectView(R.id.uploadWorksLsVw)
    ExtensibleListView worksLsVw;
    @InjectView(R.id.uploadWorksCoverImgVw)
    ImageView coverImgView;
    @InjectView(R.id.uploadWorksCoverEdTx)
    EditText uploadWorksCoverEdTx;
    @InjectView(R.id.uploadWorksDescrpEdTx)
    EditText uploadWorksDescrpEdTx;
    @InjectView(R.id.makeuper_cb)
    CheckBox makeuper_cb;
    @InjectView(R.id.prop_cb)
    CheckBox prop_cb;
    @InjectView(R.id.photography_cb)
    CheckBox photography_cb;
    @InjectView(R.id.backstage_cb)
    CheckBox backstage_cb;
    @InjectView(R.id.other_cb)
    CheckBox other_cb;
    @InjectView(R.id.costume_cb)
    CheckBox costume_cb;

    private UploadWorksListAdapter uploadWorksListAdapter;
    /*
    to record the images' path not including the cover image path
     */
    private ArrayList<String> imagePaths;
    /*
    to record the cover image path.
     */
    private String coverImagePath = "";
    /*
    to record the returned image urls responding to different types of image, such as cover or details.
     */
    private ArrayList<Image> imagesArraylist;

    public SetPhotoHelper mSetPhotoHelper;
    //for request
    private CreateShareRequest request;
    private CreateShareResponse response;
    private Share share;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.upload_works_layout);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    void initData() {
        //choose the cover image
        mSetPhotoHelper = new SetPhotoHelper(this, null);
        //图片裁剪后输出宽度
        final int outPutWidth = 450;
        //图片裁剪后输出高度
        final int outPutHeight = 300;
        mSetPhotoHelper.setOutput(outPutWidth, outPutHeight);
        mSetPhotoHelper.setAspect(3, 2);
        request = new CreateShareRequest();
        response = new CreateShareResponse();
        share = new Share();

    }

    void initView() {
        //implementation on the title bar
        titleTxVw.setText("新建分享");
        tv_right_text.setText("发布");
        imagePaths = getIntent().getExtras().getStringArrayList("paths");
        uploadWorksListAdapter = new UploadWorksListAdapter(this, imagePaths);
        worksLsVw.setAdapter(uploadWorksListAdapter);
        //set listener
        coverImgView.setOnClickListener(this);
        lly_right_action.setOnClickListener(this);
        backTxVw.setOnClickListener(this);
        makeuper_cb.setOnCheckedChangeListener(this);
        prop_cb.setOnCheckedChangeListener(this);
        photography_cb.setOnCheckedChangeListener(this);
        costume_cb.setOnCheckedChangeListener(this);
        other_cb.setOnCheckedChangeListener(this);
        backstage_cb.setOnCheckedChangeListener(this);
        title_left.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SetPhotoHelper.REQUEST_BEFORE_CROP:
                    mSetPhotoHelper.setmSetPhotoCallBack(
                            new SetPhotoHelper.SetPhotoCallBack() {
                                @Override
                                public void success(String imagePath) {
                                    coverImagePath = imagePath;
                                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                                    coverImgView.setImageBitmap(bitmap);
                                }
                            });
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_BEFORE_CROP, data);
                    return;
                case SetPhotoHelper.REQUEST_AFTER_CROP:
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_AFTER_CROP, data);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_right_action:
                if (coverImagePath.equals("")
                        || uploadWorksCoverEdTx.getText().toString().equals("")
                        || uploadWorksDescrpEdTx.getText().toString().equals("")) {
                    Toast.makeText(UploadDisplayActivity.this, "请填写完所有内容:)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uploadWorksCoverEdTx.getText().length() > InputLength.DisplayTitle_max) {
                    Toast.makeText(UploadDisplayActivity.this, "标题限制 " + InputLength.DisplayTitle_max + " 字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uploadWorksDescrpEdTx.getText().length() > InputLength.DisplayContent_max) {
                    Toast.makeText(UploadDisplayActivity.this, "内容限制 " + InputLength.DisplayContent_max + " 字", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProcessBar(getResources().getString(R.string.uploading));
                share.title = uploadWorksCoverEdTx.getText().toString();
                share.content = uploadWorksDescrpEdTx.getText().toString();
                share.totalPageNumber = worksLsVw.getCount();
                imagesArraylist = new ArrayList<>();
                for (int i = 0; i < imagePaths.size(); i++) {
                    File file = new File(imagePaths.get(i));
                    UploadImageTools.uploadImageSys(file, new UploadWorksCallbacks(Image.ImageType.detailImage), UploadDisplayActivity.this, false);
                }
                File file = new File(coverImagePath);
                UploadImageTools.uploadImageSys(file, new UploadWorksCallbacks(Image.ImageType.coverImage), UploadDisplayActivity.this, false);
                break;
            case R.id.tv_left:
                UploadDisplayActivity.this.finish();
                break;
            case R.id.uploadWorksCoverImgVw:
                SetPhotoDialog dialog = new SetPhotoDialog(UploadDisplayActivity.this, new SetPhotoDialog.ISetPhoto() {

                    @Override
                    public void choosePhotoFromLocal() {
                        mSetPhotoHelper.choosePhotoFromLocal();
                    }

                    @Override
                    public void takePhoto() {
                        mSetPhotoHelper.takePhoto(true);
                    }
                });
                dialog.showSetPhotoDialog();
                break;
            case R.id.lly_left_action:
                finish();
                break;
        }
    }

    /*
    to record how many images have been uploaded already.
    every time it return success, add one to count.
    upload all the urls until the count up to imagePaths.size()+1.
     */
    private Integer count = 0;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.makeuper_cb:
                share.tags.isMakeup = isChecked;
                break;
            case R.id.prop_cb:
                share.tags.isProperty = isChecked;
                break;
            case R.id.backstage_cb:
                share.tags.isLater = isChecked;
                break;
            case R.id.costume_cb:
                share.tags.isCloth = isChecked;
                break;
            case R.id.other_cb:
                share.tags.isCoser = isChecked;
                break;
            case R.id.photography_cb:
                share.tags.isPhoto = isChecked;
                break;
        }
    }

    class UploadWorksCallbacks implements UploadImageTools.UploadCallBack {
        private Image.ImageType imageType;

        public UploadWorksCallbacks(Image.ImageType imageType) {
            this.imageType = imageType;
        }

        @Override
        public void onProcess(Object fileParam, long current, long total) {
        }

        @Override
        public void fail() {
            Log.e(TAG, "uploadWorksFailed.");
        }

        @Override
        public void success(String originUrl, String thumbUrl) {
            if (imageType == Image.ImageType.coverImage)
                share.coverUrl = originUrl;
            else {
                Image image = new Image();
                image.type = imageType;
                image.originUrl = originUrl;
                image.thumbUrl = thumbUrl;
                imagesArraylist.add(image);
            }
            synchronized (count) {
                count++;
                Log.d(TAG, "image " + count + " has been uploaded");
                if (count == (imagePaths.size() + 1)) {
                    Log.d(TAG, "all the images has been uploaded successfully.");
                    share.imageList = imagesArraylist;
                    request.request(response, share);
                }
            }
        }
    }

    class CreateShareResponse extends BaseResponceImpl implements CreateShareRequest.ICreateShareResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(UploadDisplayActivity.this, "doAfterFailedResponse", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProcessBar();
            Toast.makeText(UploadDisplayActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void success(Share share) {
            dismissProcessBar();
            Toast.makeText(UploadDisplayActivity.this, getResources().getString(R.string.uploadDisplaySuccessfully), Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}

