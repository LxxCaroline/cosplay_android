package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.R.id;
import com.think.linxuanxuan.ecos.utils.yunxin.NetworkUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class YunXinLoginActivity extends BaseActivity{

	
	EditText etv_account;
	
	EditText etv_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		etv_account = (EditText) findViewById(id.etv_account);
		
		etv_password = (EditText) findViewById(id.etv_password);
		
		findViewById(id.btn_login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
		
		/*File file2 = new File("/storage/emulated/0/com.think.linxuanxuan.ecos/nim/thumb/188939b191018f57f3f7ef3a4ace6fd4");
		System.out.println("图片" + (file2.exists()?"存在":"不存在"));
		
		Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
		BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file2.getAbsolutePath(), options);
        Log.e("展示图片", "drawable原图宽度:" + options.outWidth);
		Log.e("展示图片", "drawable原图高度:" + options.outHeight);
		ImageView iv_demo = (ImageView) findViewById(id.iv_demo);
		iv_demo.setImageBitmap(bitmap);
		

		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "com.think.linxuanxuan.ecos/" + "nim/" + "thumb";
		File file = new File(path);
		if (file.exists())
		{
			Log.e("TAG", "目录存在 ");
			for(File f:file.listFiles()){
				Log.e("TAG", f.getAbsolutePath());
			}
		}
		else{
			Log.e("TAG", Environment.getExternalStorageDirectory().getAbsolutePath());
		}*/
		
	}

	private AbortableFuture<LoginInfo> loginRequest;
	
	public void login(){
//		String account = etv_account.getText().toString();
//		String token = etv_password.getText().toString();

//		String account = "2b584d0f3e0243008582579802d28901";
//		String token = "cc7be9877aacd5248b53d2e0b823096f";

		String account126 = "2255be0951400e260832c85c5d191247";
		String token126 = "4d8f6d6f1d3d534544be4b9bbdad5559";

		if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_LONG).show();
            return;
        }



		
		//用账号和token进行登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account126, token126));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
            	
            	Log.i("登录", "登录成功");
            	
            	startActivity(new Intent(YunXinLoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailed(int code) {
            	Log.i("登录", "登录失败");
            	if (code == 302 || code == 404) {
                    Toast.makeText(YunXinLoginActivity.this,  getResources().getString(R.string.accountPasswordError), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(YunXinLoginActivity.this, "login error: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
            	Log.i("登录", "登录异常");
            }
            
        });
	}
	
}
