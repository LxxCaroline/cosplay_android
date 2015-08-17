package com.think.linxuanxuan.ecos.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.think.linxuanxuan.ecos.activity.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tools for handler picture
 * 
 * @author Ryan.Tang
 * 
 */
public final class ImageTools {

	private final static String TAG = "ImageTools";
	
	/**
	 * Transfer drawable to bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap to drawable
	 *
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * Input stream to bitmap
	 *
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Bitmap inputStreamToBitmap(InputStream inputStream)
			throws Exception {
		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * Byte transfer to bitmap
	 *
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * Byte transfer to drawable
	 *
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = null;
		if (byteArray != null) {
			ins = new ByteArrayInputStream(byteArray);
		}
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * Bitmap transfer to bytes
	 *
	 * @param byteArray
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		byte[] bytes = null;
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	/**
	 * Drawable transfer to bytes
	 *
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		byte[] bytes = bitmapToBytes(bitmap);
		;
		return bytes;
	}

	/**
	 * Base64 to byte[]
//	 */
//	public static byte[] base64ToBytes(String base64) throws IOException {
//		byte[] bytes = Base64.decode(base64);
//		return bytes;
//	}
//
//	/**
//	 * Byte[] to base64
//	 */
//	public static String bytesTobase64(byte[] bytes) {
//		String base64 = Base64.encode(bytes);
//		return base64;
//	}

	/**
	 * Create reflection images
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Get rounded corner images
	 *
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Resize the bitmap
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * Resize the drawable
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		matrix.postScale(sx, sy);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * Get images from SD card by path and the name of image
	 * @param photoName
	 * @return
	 */
	public static Bitmap getPhotoFromSDCard(String path,String photoName){
		Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName + ".png");
		if (photoBitmap == null) {
			return null;
		}else {
			return photoBitmap;
		}
	}

	/**
	 * Check the SD card
	 * @return
	 */
	public static boolean checkSDCardAvailable(){
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * Get image from SD card by path and the name of image
	 * @param fileName
	 * @return
	 */
	public static boolean findPhotoFromSDCard(String path,String photoName){
		boolean flag = false;
		
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName().split("\\.")[0];
					if (fileName.equals(photoName)) {
						flag = true;
					}
				}
			}else {
				flag = false;
			}
//			File file = new File(path + "/" + photoName  + ".jpg" );
//			if (file.exists()) {
//				flag = true;
//			}else {
//				flag = false;
//			}
			
		}else {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * Save image to the SD card 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()){
				dir.mkdirs();
			}
			
			File photoFile = new File(path , photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
//						fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	
	
	public static void savePhotoToSDCard(Bitmap photoBitmap,String photoName)
	{
		if (checkSDCardAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			
			
			File photoFile = new File(path , photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Log.d("savePhotoToSDCard", "end");
			}
			
		} 
	}
	
	
	
	
	
	
	
	/**
	 * Delete the image from SD card
	 * @param context
	 * @param path
	 * file:///sdcard/temp.jpg
	 */
	public static void deleteAllPhoto(String path){
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}
	
	public static void deletePhotoAtPathAndName(String path,String fileName){
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getName());
				if (files[i].getName().equals(fileName)) {
					files[i].delete();
				}
			}
		}
	}
	
	
	/**
	 * 选择本地图片后，根据Intent获取图片路径
	 * @param data
	 * @param context
	 * @return
	 */
	public static File getPathFromData(Intent data,Context context)
	{
		if (data != null) 
		{

			Uri selectedImage = null;
			selectedImage = data.getData();
			Log.e("本地图片路径","----------------" +selectedImage.toString() + "----------------------");

			String[] filePathColumn = { MediaStore.Images.Media.DATA};

			Cursor cursor = context.getContentResolver().query(selectedImage,
			     filePathColumn, null, null, null);
			if(cursor==null){
				Log.e("cursor","cursor 为null");
				selectedImage = geturi(data);
				cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			}

			Log.e("本地图片路径","------cursor==null----------" );

			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			return new File(picturePath);

	}
		
		return null;
	}

	public static Uri geturi( android.content.Intent intent){
		Uri uri=intent.getData();
		String type = intent.getType();
		if (uri.getScheme().equals("file") && (type.contains("image/"))) {
			String path = uri.getEncodedPath();
			Log.i("uri.getEncodedPath()",path);
			if (path != null) {
				path = Uri.decode(path);
				Log.i("Uri.decode(path)",path);
				ContentResolver cr = MyApplication.getCurrentActivity().getContentResolver();
				StringBuffer buff = new StringBuffer();
				buff.append("(")
						.append(MediaStore.Images.ImageColumns.DATA)
						.append("=")
						.append("'" + path + "'")
						.append(")");
				Log.i("queryBuffer",buff.toString());
				Cursor cur = cr.query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.ImageColumns._ID },
						buff.toString(), null, null);
				int index = 0;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur
						.moveToNext()) {
					index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
					// set _id value
					index = cur.getInt(index);
				}
				if (index == 0) {
					//do nothing
				} else {
					Uri uri_temp = Uri
							.parse("content://media/external/images/media/"
									+ index);
					if (uri_temp != null) {
						uri = uri_temp;
						Log.i("urishi", uri.toString());
					}
				}
			}
		}
		return uri;
	}
	
	
	//记得与调用者的所使用的Fragmetn包来保持一致
	public static void cropImage(Fragment fragment, Uri srcUri,Uri descUri,
						int aspectX, int aspectY,int outputX, int outputY, int requestCode){
		
		Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(srcUri, "image/*");  
        intent.putExtra("crop", "true");  
        
        //裁剪区域是可以大小变动的，但是裁剪区域（裁剪框)的宽高(横向与纵向)比=aspectX/aspectY
        intent.putExtra("aspectX", aspectX);  
        intent.putExtra("aspectY", aspectY);  
        //裁剪结束后，输出的bitmap的宽度为outputX，高度为outputY
        intent.putExtra("outputX", outputX);   
        intent.putExtra("outputY", outputY); 
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, descUri);
        intent.putExtra("return-data", false);  
        fragment.startActivityForResult(intent, requestCode);
	}
	

	public static void cropImage(Activity activity, Uri srcUri,Uri descUri,
						int aspectX, int aspectY,int outputX, int outputY, int requestCode){
			
		Intent intent = new Intent("com.android.camera.action.CROP");
	    intent.setDataAndType(srcUri, "image/*");  
	    intent.putExtra("crop", "true");  
	        
	    //裁剪区域是可以大小变动的，但是裁剪区域（裁剪框)的宽高(横向与纵向)比=aspectX/aspectY
	    intent.putExtra("aspectX", aspectX);  
	    intent.putExtra("aspectY", aspectY);  
	    //裁剪结束后，输出的bitmap的宽度为outputX，高度为outputY
	    intent.putExtra("outputX", outputX);
	    intent.putExtra("outputY", outputY);
	    intent.putExtra("outputFormat", "PNG");
	    intent.putExtra("noFaceDetection", false);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, descUri);
	    intent.putExtra("return-data", false);  
	    activity.startActivityForResult(intent, requestCode);
	}
	
	
	
	
	
	//将URI转为bitmap
	public static Bitmap decodeUriAsBitmap(Context context,Uri uri){
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	
	//把图片保存到file中
	public static void saveBitmap(Bitmap shopBitmap,File file)
	{
		Log.e(TAG, "保存图片");
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			shopBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			shopBitmap.recycle();
			out.flush();
			out.close();
		Log.i(TAG, "已经保存");
		} 
		catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} 
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	//把图片保存到file中,根据参数确定是否回收
	public static void saveBitmap(Bitmap shopBitmap,File file,boolean recycle)
	{
		Log.e(TAG, "保存图片");
			
		try {
			FileOutputStream out = new FileOutputStream(file);
			shopBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			if(recycle)
			{
				shopBitmap.recycle();
			}
			out.flush();
			out.close();
			
			
			 Log.i(TAG, "saveBitmap--end");
		Log.i(TAG, "已经保存");
		} 
		catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} 
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	/***
	 * 判断指定文件是否已经存在
	 * @param file 要查找的文件
	 * @return 若file存在则返回true.否则返回false
	 */
	public static boolean isImageExist(File file)
	{
		if(file.exists())
			return true;
		else
			return false;	
	}
	
	/**
	 * 获取图片文件被自动旋转的角度(有些手机会自动旋转上传图片或者拍照后的图片)
	 * @param imageFile 必须是图片
	 * @return 被自动旋转的角度，默认值为0
	 */
	public static int getRotate(File imageFile)
	{
		ExifInterface exifInterface;
		int rotate = 0;   
		try {
			exifInterface = new ExifInterface(imageFile.getAbsolutePath());
		 
        int result = exifInterface.getAttributeInt(   
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        
        switch(result) {   
        case ExifInterface.ORIENTATION_ROTATE_90:
            rotate = 90;   
            Log.e(TAG, "doBeforeCrop,rotate = " + rotate);
            break;   
        case ExifInterface.ORIENTATION_ROTATE_180:
            rotate = 180;   
            Log.e(TAG, "doBeforeCrop,rotate = " + rotate);
            break;   
        case ExifInterface.ORIENTATION_ROTATE_270:
            rotate = 270;   
            Log.e(TAG, "doBeforeCrop,rotate = " + rotate);
            break;   
        default:   
            break;   
        }   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return rotate;
	}


	public static void copyFileTo(File src, File des){
		if(src != null){
			try {
				InputStream is;
				is = new FileInputStream(src);
				FileOutputStream out = new FileOutputStream(des);

				byte bytes[] = new byte[1024];
				int length=0;

				while((length=is.read(bytes))!=-1){

					out.write(bytes,0,length);
				}
				out.flush();



			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}

	
	