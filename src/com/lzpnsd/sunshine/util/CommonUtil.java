package com.lzpnsd.sunshine.util;

import android.net.Uri;

public class CommonUtil {

	public static  Uri getResourceUri(int resId,String packageName)
	{
	    return Uri.parse("android.resource://"+packageName+"/"+resId);
	}
	
}
