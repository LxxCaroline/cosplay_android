package com.think.linxuanxuan.ecos.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.think.linxuanxuan.ecos.R;


/**
 * 上传图片对话框，用于选择从本地上传图片还是拍照获取图片.<br>
 * @author enlizhang
 * @since 2014.11.21
 */
public class SetPhotoDialog extends BaseDialog
{
	
	private final static String TAG = "SetPhotoDialog";
	
	private Dialog mSetPhotoDialog;
	
	/***对话框所在上下文环境 */
	protected Context mContext;
	
	/***选择本地图片或者拍照*/
	protected  ISetPhoto m_ISetPhoto;
	
	public SetPhotoDialog(Context context, ISetPhoto iSetPhoto)
	{
		super(context);
		this.mContext = context;
		this.m_ISetPhoto = iSetPhoto;
		
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init()
	{
		View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.dialog_set_photo,null);
		mSetPhotoDialog = new Dialog(mContext,R.style.customDialog);
		mSetPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSetPhotoDialog.setContentView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		//选择本地图片按钮
		Button choose_photo_button = (Button)view.findViewById(R.id.btn_choose_from_local);
		//拍照按钮
		Button take_photo_button = (Button)view.findViewById(R.id.btn_take_photo);
		//取消按钮
		Button cancel =(Button)view.findViewById(R.id.btn_cancel);
		
		
		choose_photo_button.setOnClickListener(buttonOnClickListener);
		take_photo_button.setOnClickListener(buttonOnClickListener);
		cancel.setOnClickListener(buttonOnClickListener);
		
		Window window = mSetPhotoDialog.getWindow();
		WindowManager.LayoutParams w1 = window.getAttributes();
		
		//这两个参数我也不知道怎么回事，只知道能将对话框设置在底部
		w1.x = 0;
		w1.y = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
		
		
		w1.width = ViewGroup.LayoutParams.MATCH_PARENT;
		w1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		
		//这句也不知道具体什么意思
		mSetPhotoDialog.onWindowAttributesChanged(w1);
		
		//点击对话框外能取消对话框
		mSetPhotoDialog.setCanceledOnTouchOutside(true);
		
		
		mSetPhotoDialog.setOnCancelListener(onCancelListener);
		
		//显示对话框
//		showSetPhotoDialog();
	}
		
	
	/**
	 * 选择本地图片按钮，拍照按钮，取消按钮的监听器
	 */
	OnClickListener buttonOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View button) {
			// TODO Auto-generated method stub
			dismissSetPhotoDialog();
			switch(button.getId())
			{
			//若是选择本地照片按钮
			case R.id.btn_choose_from_local:
				Log.d(TAG, "btn_choose_from_local");
				dismissSetPhotoDialog();
				m_ISetPhoto.choosePhotoFromLocal();
				break;
			//若是拍照按钮
			case R.id.btn_take_photo:
				Log.d(TAG, "btn_take_photo");
				dismissSetPhotoDialog();
				m_ISetPhoto.takePhoto();
				break;
			//若是取消按钮
			case R.id.btn_cancel:
				Log.d(TAG, "btn_cancel");
			    break;
			}
		}
		
	};
	
	OnCancelListener onCancelListener = new OnCancelListener()
	{
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			dismissSetPhotoDialog();
		}
		
	};
	
	/**
	 * 先显示遮罩层，后显示对话框.<br>
	 * 注意：依附于创建该对话框的Context
	 */
	public void showSetPhotoDialog()
	{
		//显示对话框
		mSetPhotoDialog.show();
	}
	


	/**
	 * 先取消对话框，再去除遮罩层
	 */
	private void dismissSetPhotoDialog()
	{
		//若当前mSetPhotoDialog存在，则先取消对话框，再去除遮罩层
		if(mSetPhotoDialog!=null)
		{
			mSetPhotoDialog.dismiss();
		}
		else
		{
			Log.e(TAG, "mSetPhotoDialog == null");
		}
	}


	public interface ISetPhoto {

		/**
		 * 从本地选择照片，并将照片存于
		 */
		public void choosePhotoFromLocal();

		/**
		 * 拍照，并将图片存于
		 */
		public void takePhoto();
	}
}
