package com.loveshare.imgProcessUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImgSketch {

	private final int PROGRESS_WAIT_VISIBLE = 1;
	private final int PROGRESS_WAIT_GONE = 2;
	private final int IMAGEVIEW_INVALIDATE = 3;

	private Context context;
	private ImageView img_process;
	private Bitmap mBitmap;
	private String imgPath;

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public ImgSketch(Context context, ImageView img_process, String imgPath) {
		this.context = context;
		this.img_process = img_process;
		this.imgPath = imgPath;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PROGRESS_WAIT_VISIBLE:
				((Activity) context)
						.setProgressBarIndeterminateVisibility(true);
				break;
			case PROGRESS_WAIT_GONE:
				((Activity) context)
						.setProgressBarIndeterminateVisibility(false);
				break;
			case IMAGEVIEW_INVALIDATE:
				img_process.setImageBitmap(mBitmap);
				img_process.invalidate();
				break;
			default:
				break;
			}
		}
	};

	public void doSketch() {

		new Thread(new Runnable() {
			public void run() {
				try {
					mHandler.sendEmptyMessage(PROGRESS_WAIT_VISIBLE);
					ImageUtil util = new ImageUtil();
					mBitmap = util.sketch(mBitmap);
					mHandler.sendEmptyMessage(IMAGEVIEW_INVALIDATE);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					mHandler.sendEmptyMessage(PROGRESS_WAIT_GONE);
				}
			}
		}).start();
	}

	public void cancelSketch() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// 图片宽高都为原来的二分之一，即图片为原来的四分之一
		Bitmap bmp = BitmapFactory.decodeFile(imgPath, options);
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] pixel = new int[width * height];

		bmp.getPixels(pixel, 0, width, 0, 0, width, height);
		mBitmap = Bitmap.createBitmap(width, height, bmp.getConfig());
		mBitmap.setPixels(pixel, 0, width, 0, 0, width, height);
		img_process.setImageBitmap(mBitmap);
		bmp.recycle();
	}

}
