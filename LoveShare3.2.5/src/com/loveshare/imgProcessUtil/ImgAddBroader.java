package com.loveshare.imgProcessUtil;

import com.loveshare.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

public class ImgAddBroader {

	/**
	 * 添加边框
	 * 
	 * @param bm
	 *            原图片
	 * @param res
	 *            边框资源
	 * @return
	 */
	public static Bitmap addBigFrame(Context mContext, Bitmap bm, int res) {
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				res);
		Drawable[] array = new Drawable[2];
		array[0] = new BitmapDrawable(bm);
		Bitmap b = resize(bitmap, bm.getWidth(), bm.getHeight());
		array[1] = new BitmapDrawable(b);
		LayerDrawable layer = new LayerDrawable(array);
		return drawableToBitmap(layer);
	}

	public static Bitmap resize(Bitmap bitmap, int width, int height) {
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight());
	}

	/**
	 * 将Drawable转换成Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	private static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap addBroader(Context mContext, Bitmap bmp, int res) {

		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		// 对边框图片进行缩放
		Bitmap overlay = BitmapFactory.decodeResource(mContext.getResources(),
				res);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);

		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix,
				true);
		
		int pixColor = 0;
		int layColor = 0;

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;

		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;

		final float alpha = 0.5F;

		int[] srcPixels = new int[width * height];
		int[] layPixels = new int[width * height];
		bmp.getPixels(srcPixels, 0, width, 0, 0, width, height);
		overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);

		int pos = 0;
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pos = i * width + k;
				pixColor = srcPixels[pos];
				layColor = layPixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);

				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);

				newR = (int) (pixR * alpha + layR * (1 - alpha));
				newG = (int) (pixG * alpha + layG * (1 - alpha));
				newB = (int) (pixB * alpha + layB * (1 - alpha));
				layA = (int) (pixA * alpha + layA * (1 - alpha));

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));

				srcPixels[pos] = Color.argb(newA, newR, newG, newB);
			}
		}

		bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "overlayAmeliorate used time=" + (end - start));
		return bitmap;
	}

}
