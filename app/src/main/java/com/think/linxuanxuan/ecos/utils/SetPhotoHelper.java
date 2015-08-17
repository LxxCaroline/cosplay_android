package com.think.linxuanxuan.ecos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.think.linxuanxuan.ecos.constants.FileManager;

import java.io.File;

/***
 * 
* @ClassName: SetPhotoHelper 
* @Description: TODO(设置图片辅助类) 
* @author enlizhang
* @date 2015年1月23日 上午9:11:58 
*
 */
public class SetPhotoHelper {
	
	private static final String TAG = "SetPhotoHelper";
	
	private final String PREFERENCE_NAME = "setPhotoTempData";
	
	private final int READ_MODE = Context.MODE_WORLD_READABLE;
	private final int WRITE_MODE = Context.MODE_WORLD_WRITEABLE;
	
	/*** 选择图片(拍照)返回码*/
	public static final int REQUEST_BEFORE_CROP = 1;
	
	/*** 裁剪图片返回码*/
	public static final int REQUEST_AFTER_CROP = 2;
	
	
	private final int IMG_OUTPUT_X = 100;
	private final int IMG_OUTPUT_Y = 100;
	private final int IMG_ASPECT_X = 1;
	private final int IMG_ASPECT_Y = 1;
	
	private int mAspectX = 3;//裁剪框宽度比例
	private int mAspectY = 2;//裁剪框高度比例
	
	private int mOutputX = 100;//图片裁剪后的宽
	private int mOutputY = 100;//图片裁剪后的高
	
	private final File mBefCropFile;
	
	private final File mAfterCropFile;


	private Activity mActivity;
	private final Context mContext;
	
	private Fragment mFragment;
	
	private SetPhotoCallBack mSetPhotoCallBack;

	
	public SetPhotoHelper(Activity activity, SetPhotoCallBack setPhotoCallBack)
	{
		mActivity = activity;
		mSetPhotoCallBack = setPhotoCallBack;
		
		mBefCropFile = FileManager.getInstance().getTempFileBeforeCrop();
		mAfterCropFile = FileManager.getInstance().getDefFileAfterCrop();

		mContext = mActivity;
	}
	
	
	public SetPhotoHelper(Fragment fragment, SetPhotoCallBack setPhotoCallBack)
	{
		mFragment = fragment;
		mSetPhotoCallBack = setPhotoCallBack;

		mBefCropFile = FileManager.getInstance().getTempFileBeforeCrop();
		mAfterCropFile = FileManager.getInstance().getDefFileAfterCrop();

		mContext = fragment.getActivity();
	}
	
	
	/***
	 * 调用此构造函数前必须确保在整个设置图片过程中已经调用过
	 * {@link SetPhotoHelper#SetPhotoHelper(Activity, int, File, File)}
	 */
	/*public SetPhotoHelper()
	{
		restoreData();
	}*/
	
	
	/***
	 * 处理sub-activity返回的数据
	 */
	public void choosePhotoFromLocal() {
		// TODO Auto-generated method stub
		 fileAfterChoose = null;
		 Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
		 
		openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		if(mFragment!=null)
		{
			mFragment.startActivityForResult(openAlbumIntent, REQUEST_BEFORE_CROP);
		}
		else
			if(mActivity!=null)
			{
				mActivity.startActivityForResult(openAlbumIntent, REQUEST_BEFORE_CROP);
			}
				
	}

	public File fileAfterChoose;

	/***
	 * 拍照
	 * @param needCrop 是否需要裁剪,true:需要 false:不需要
	 */
	public void takePhoto(boolean needCrop) {
		fileAfterChoose = null;

		Uri imgUriBeforeCrop;
		if( needCrop)
			imgUriBeforeCrop = Uri.fromFile(mBefCropFile);
		else
		{
			fileAfterChoose = FileManager.getInstance().getPhotoOutFile();
			imgUriBeforeCrop = Uri.fromFile( fileAfterChoose );
		}

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		//设置拍照输入Uri
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriBeforeCrop);
		
		openCameraIntent.putExtra("return-data", false);

		if(mFragment!=null)
		{
			mFragment.startActivityForResult(openCameraIntent, REQUEST_BEFORE_CROP);
		}
		else
			if(mActivity!=null)
			{
				mActivity.startActivityForResult(openCameraIntent, REQUEST_BEFORE_CROP);
			}
		
	}
	
	
	
	public void handleActivityResult(int requestCode, Intent data)
	{
		switch (requestCode) {
		case REQUEST_BEFORE_CROP://拍照或者选择本地图片
			if (data != null) {
				File picturePath = ImageTools.getPathFromData(data, mContext);
				doBeforeCrop(picturePath);
				break;
			}
			else
			{
				System.out.println("onActivityResult, File");
				File tempFileBeforeCROP = mBefCropFile;
				doBeforeCrop(tempFileBeforeCROP);
				break;
			}
		case REQUEST_AFTER_CROP://裁剪
			Log.d(TAG, "CROP_PICTURE");
			

			//获取裁剪后图片的URI，mTempImgFileAfterCrop上传图片时根据点击区域确定
			if(mAfterCropFile != null){

				File des = FileManager.getInstance().getPhotoOutFile();
				ImageTools.copyFileTo(mAfterCropFile, des);
				if(mSetPhotoCallBack!=null)
				{
					mSetPhotoCallBack.success(des.getAbsolutePath());
				}

			}

			break;
		}
			
	}
	
		
	/**
	 * 在图片裁剪前，对图片进行压缩，并存到裁剪路径
	 * @param picturePath
	 */
	private void doBeforeCrop(File picturePath) {
			
		File tempFileBeforeCROP = mBefCropFile;
		
		Bitmap chooenBitmap = CompressImageUitl.decodeSampledBitmapFromFile(picturePath.getAbsolutePath(),350,350);
		
//		Bitmap chooenBitmap = BitmapFactory.decodeFile(picturePath.getAbsolutePath());
		
		//若拍照后图片自动旋转，则调整角度
		int rotate = ImageTools.getRotate(tempFileBeforeCROP);
		if(rotate != 0)
		{
			Matrix matrix = new Matrix();
	        matrix.setRotate(rotate);
	        chooenBitmap =  Bitmap.createBitmap(chooenBitmap, 0, 0, chooenBitmap.getWidth(), chooenBitmap.getHeight(), matrix, true);
			
		}
		
		ImageTools.saveBitmap(chooenBitmap,tempFileBeforeCROP);
			
		Uri imgUriBeforeCrop = Uri.fromFile(tempFileBeforeCROP);
		Uri imgUriAfterCrop = Uri.fromFile(mAfterCropFile);
		
		
		Log.e(TAG, "mAspectX:" + mAspectX + "  mAspectY:" + mAspectY);
		Log.e(TAG, "mOutputX:" + mOutputX + "  mOutputY:" + mOutputY);
		
		if(mFragment!=null)
		{
			ImageTools.cropImage(mFragment,imgUriBeforeCrop,imgUriAfterCrop, mAspectX, mAspectY, mOutputX, mOutputY, REQUEST_AFTER_CROP);	
		}
		else
			if(mActivity!=null)
			{
				ImageTools.cropImage(mActivity,imgUriBeforeCrop,imgUriAfterCrop, mAspectX, mAspectY, mOutputX, mOutputY, REQUEST_AFTER_CROP);	
			}
//		ImageTools.cropImage(mActivity,imgUriBeforeCrop,imgUriAfterCrop, mAspectX, mAspectY, mOutputX, mOutputY, REQUEST_AFTER_CROP);	
		
	}
	

	/***
	 * 获取裁剪前的图片(选择本地图片或者拍照后的图片)
	 
	 * @return
	 */
	public Bitmap getPhotoBeforeCrop()
	{
		Bitmap bitmap = BitmapFactory.decodeFile(mAfterCropFile.getAbsolutePath());

		return bitmap;
	}

	/***
	 * 在选择完图片或者拍照后不进行裁剪的图片存储文件；
	 * @return
	 */
	public File getFileAfterChoose(){
		File file = fileAfterChoose;
		fileAfterChoose = null;

		if(file!=null)
		{
			Bitmap chooenBitmap = CompressImageUitl.decodeSampledBitmapFromFile(file.getAbsolutePath(),300,200);
			ImageTools.saveBitmap(chooenBitmap,file);
		}

		return file;

	}
	
	/***
	 * 获取裁剪前的图片(选择本地图片或者拍照后的图片)所处的File
	 * @data 含图片位置信息
	 * @width 图片宽度
	 * @height 图片高度
	 * @return
	 */
	public File getFileBeforeCrop(Intent data, int reqWidth, int reqHeight)
	{
		File picturePath;
		if (data != null) {
			picturePath = ImageTools.getPathFromData(data, mContext);  
		}
		else{
			picturePath = mBefCropFile;
		}
		
		Bitmap chooenBitmap = CompressImageUitl.decodeSampledBitmapFromFile(picturePath.getAbsolutePath(),reqWidth,reqHeight);
		
		//若拍照后图片自动旋转，则调整角度
		int rotate = ImageTools.getRotate( picturePath );
		if(rotate != 0)
		{
			Matrix matrix = new Matrix();
	        matrix.setRotate(rotate);
	        chooenBitmap =  Bitmap.createBitmap(chooenBitmap, 0, 0, chooenBitmap.getWidth(), chooenBitmap.getHeight(), matrix, true);
			
		}
		
		ImageTools.saveBitmap(chooenBitmap,picturePath);
		
		return picturePath;
	}
	
	/****
	 * 
	* @ClassName: SetPhotoCallBack 
	* @Description: TODO(裁剪图片成功响应回调函数) 
	* @author enlizhang
	* @date 2015年4月19日 下午8:49:14 
	*
	 */
	public interface SetPhotoCallBack
	{
		public void success(String imagePath);
	}


	public SetPhotoCallBack getmSetPhotoCallBack() {
		return mSetPhotoCallBack;
	}

	public void setmSetPhotoCallBack(SetPhotoCallBack mSetPhotoCallBack) {
		this.mSetPhotoCallBack = mSetPhotoCallBack;
	}


	public void setOutput(int mOutputX, int mOutputY) {
		this.mOutputX = mOutputX;
		this.mOutputY = mOutputY;
	}


	public void setAspect(int aspectX, int aspectY){
		this.mAspectX = aspectX;
		this.mAspectY = aspectY;
	}
	
}
