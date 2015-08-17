package com.think.linxuanxuan.ecos.utils.yunxin;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {
	
	public static final String CHARSET_DEFAULT = "UTF-8";
	public static final String CHARSET_GB2312 = "GB2312";
	
	public static String doEncryptByCustomize(String source, String key) {
		byte[] sourceBuf = null;
		try {
			sourceBuf = source.getBytes(CHARSET_DEFAULT);//可能有汉字，故utf-8编码之(接口文档要求)
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte [] keyBuf = key.getBytes();
		int sourceBufLen = sourceBuf.length;
		int keyBufLen = keyBuf.length;
        String stmp="";
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < sourceBufLen; i++)
		{
			sourceBuf[i] = (byte)(sourceBuf[i]^keyBuf[i % keyBufLen]);//和key的对应字节求异或
			stmp=(Integer.toHexString(sourceBuf[i] & 0XFF));//将异或后的sourceBuf[i]转成16进制
            stmp.toUpperCase(Locale.US);//转换成大写
            if (stmp.length()==1) {
            	result.append("0").append(stmp);
            }
            else {
            	result.append(stmp);
            }
		}

		return result.toString();
	}
	
	public static String doDecryptByCustomize(String source, String key) {
		byte [] sourceBuf = hexStr2Bytes(source);
		byte [] keyBuf = key.getBytes();
		int sourceBufLen = sourceBuf.length;
		int keyBufLen = keyBuf.length;
		for(int i = 0; i < sourceBufLen; i++)
		{
			sourceBuf[i] = (byte)(sourceBuf[i]^keyBuf[i % keyBufLen]);//和key的对应字节求异或
		}
		try {
			return new String(sourceBuf,CHARSET_DEFAULT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    /**
     * String转换成十六进制bytes
     * @param src
     * @return
     */
    private static byte[] hexStr2Bytes(String src) {
        int m=0,n=0;
        int l=src.length()/2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m=i*2+1;
            n=m+1;
            ret[i] = uniteBytes(src.substring(i*2, m),src.substring(m,n));
        }
        return ret;
    }
    
    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);
        return ret;
    }
    
	public static byte[] desEncrypt(String src, byte[] key) {
		if (TextUtils.isEmpty(src) || key == null) {
			return null;
		}
		
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
			Cipher cipher = Cipher.getInstance("DES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化
			byte[] byteContent = src.getBytes("utf-8");
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (Exception e) {
            Log.d("shaobo", "encrypt http content error, key: " + (key) + " err: " + e);
        }
		return null;
	}
	
	public static byte[] desDecrypt(byte[] content, byte[] sKey) throws Exception {
	    if (sKey == null)
	        throw new Exception("Decrpty failed, skey = null");
	    byte[] raw = sKey;
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "DES");
	    Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(content);
	}
	
	public static byte[] desEncrypt(byte[] src, byte[] key) {
		if (src == null || key == null) {
			return null;
		}
		
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
			Cipher cipher = Cipher.getInstance("DES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化
			
			byte[] result = cipher.doFinal(src);
			return result; // 加密
		} catch (Exception e) {
            Log.d("shaobo", "encrypt http content error, key: " + key + " err: " + e);
        }
		return null;
	}
}
