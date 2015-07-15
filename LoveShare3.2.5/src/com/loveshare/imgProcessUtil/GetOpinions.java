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
		// ��ʾ��ȥ�����ؽ���ͼƬ������ȥ����ͼƬ�Ŀ��
		options.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

		// �õ�ͼƬ�Ŀ��
		int height = options.outHeight;
		int width = options.outWidth;

		// �������ű���
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
		// ��������ͼƬ����ͼƬ��ʾ��imgView��
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		return options;
	}

	public static BitmapFactory.Options getGetOpinions() {
		// ��ʾ��ȥ�����ؽ���ͼƬ������ȥ����ͼƬ�Ŀ��
		Options options = new Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 4;
		options.inJustDecodeBounds = false;
		return options;
	}
}
