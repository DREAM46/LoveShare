package com.loveshare.imgProcessUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class GetOpinions {

	public static BitmapFactory.Options getGetOpinions(String imgPath,
			int win_width, int win_height, BitmapFactory.Options options) {

		if (options == null) {
			options = new Options();
		}
		// 表示不去真正地解析图片，而是去解析图片的宽高
		options.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

		// 得到图片的宽高
		int height = options.outHeight;
		int width = options.outWidth;

		// 计算缩放比例
		int scaleX = width / win_width;
		int scaleY = height / win_height;
		int scale = 1;

		if (scaleX > scaleY & scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleX < scaleY & scaleX >= 1) {
			scale = scaleY;
		}
		Log.v("Loveshare", scale + "");
		// 真正解析图片并将图片显示在imgView中
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		return options;
	}

	public static BitmapFactory.Options getGetOpinions() {
		// 表示不去真正地解析图片，而是去解析图片的宽高
		Options options = new Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 4;
		options.inJustDecodeBounds = false;
		return options;
	}
}
