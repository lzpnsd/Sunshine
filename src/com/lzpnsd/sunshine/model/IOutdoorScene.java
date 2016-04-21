package com.lzpnsd.sunshine.model;

import android.graphics.Bitmap;

/**
 * 实景相关接口
 * @author lzpnsd
 * 2016年3月6日 下午9:54:25
 *
 */
public interface IOutdoorScene {

	/**
	 * 上传实景照片
	 * @param bitmap 要上传的图片
	 * @return 上传是否成功,true表示上传成功，false表示上传失败
	 */
	boolean upload(Bitmap bitmap);
	/**
	 * 下载实景照片
	 * @param picName 照片名字(从getPicList获得)
	 * @return 实景照片Bitmap对象,没有或出错返回空
	 */
	Bitmap downloadPic(String picName);
	/**
	 * 获得实景照片列表
	 * @return 返回实景照片的列表,没有或出错返回空字符串
	 */
	String getPicList();
	
}
