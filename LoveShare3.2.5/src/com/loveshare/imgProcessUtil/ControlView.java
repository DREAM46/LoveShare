package com.loveshare.imgProcessUtil;

import com.loveshare.UI.ImgActivity;
import com.loveshare.imgProcessUI.ImgControlActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ControlView extends ImageView {
	private Context mContext;
	private int mScreenWidth;
	private int mScreenHeight;
	private float mCurrentRate = 1;// 当前放大的倍数
	private float mOldRate = 1;// 上一次放大的倍数
	private boolean mIsFirst = true;// 是否是第一次触摸屏幕
	private float mOriginalLength;// 刚触摸时两个手指的距离
	private float mCurrentLength;// 当前两个手指的距离
	private Bitmap saveBitmap;
	private Bitmap bitmap;

	public ControlView(Context context) {
		super(context);
		this.mContext = context;
		init(context);
	}

	public ControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init(context);
	}

	private void init(Context context) {
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		mScreenWidth = display.widthPixels;
		mScreenHeight = display.heightPixels;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() == 2) {
				if (mIsFirst) {
					mOriginalLength = (float) Math.sqrt(Math.pow(event.getX(0)
							- event.getX(1), 2)
							+ Math.pow(event.getY(0) - event.getY(1), 2));
					mIsFirst = false;
				} else {
					mCurrentLength = (float) Math.sqrt(Math.pow(event.getX(0)
							- event.getX(1), 2)
							+ Math.pow(event.getY(0) - event.getY(1), 2));
					mCurrentRate = (float) (mOldRate * (mCurrentLength / mOriginalLength));
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			mOldRate = mCurrentRate;
			mIsFirst = true;
			break;
		}
		ImgControlActivity.isEdit = true;
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();

		BitmapFactory.Options options = GetOpinions.getGetOpinions(
				ImgControlActivity.imgPath, ImgActivity.win_width,
				ImgActivity.win_height,null);

		bitmap = BitmapFactory.decodeFile(ImgControlActivity.imgPath,
				options);
		canvas.scale(mCurrentRate, mCurrentRate, mScreenWidth / 2,
				mScreenHeight / 2);
		int width = mScreenWidth / 2 - bitmap.getWidth() / 2;
		int height = mScreenHeight / 2 - bitmap.getHeight() / 2;
		
		saveBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
		canvas.drawBitmap(bitmap, width, height, null);
	}
	
	public Bitmap getBitmap(){
		return saveBitmap;
	}

}
