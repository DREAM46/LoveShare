package com.loveshare.imgProcessUtil;

import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUI.ImgHangWritingActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class HandWritingView extends ImageView {

	private Paint paint = null;
	private Bitmap originalBitmap = null;
	private Bitmap new1Bitmap = null;
	private Bitmap new2Bitmap = null;
	private float clickX = 0, clickY = 0;
	private float startX = 0, startY = 0;
	private boolean isMove = true;
	private boolean isClear = false;
	public static int color = Color.GREEN;

	public static float strokeWidth = 2.0f;

	private Bitmap saveBitmap;

	public HandWritingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		originalBitmap = BitmapFactory.decodeFile(
				ImgHangWritingActivity.imgPath, ImgActivity.options);
		originalBitmap = Bitmap.createBitmap(originalBitmap.getWidth(),
				originalBitmap.getHeight(), originalBitmap.getConfig());
		new1Bitmap = Bitmap.createBitmap(originalBitmap.getWidth(),
				originalBitmap.getHeight(), originalBitmap.getConfig());
		// �ڹ��췽������ʱ�͸�saveBitmap����ֹ�е��û�δ��Ϳѻ����δ����handWriting����
		// ��saveBitmap��ֵʱ�ͱ���ͼƬ������ָ���
		saveBitmap = new1Bitmap;
	}

	public void clear() {
		isClear = true;
		ImgHangWritingActivity.isEdit = false;
		// new2BitmapΪԭ���İ��������֮ǰ��������ʾͼƬ��Bitmap
		new2Bitmap = Bitmap.createBitmap(originalBitmap);
		invalidate();
	}

	public void setstyle(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// �ػ��������ʾ��ͼƬ
		new1Bitmap = Bitmap.createBitmap(new1Bitmap.getWidth(),
				new1Bitmap.getHeight(), new1Bitmap.getConfig());
		canvas.drawBitmap(handWriting(new1Bitmap), 0, 0, null);
	}

	public Bitmap handWriting(Bitmap originalBitmap) {
		Canvas canvas = null;

		if (isClear) {
			canvas = new Canvas(new2Bitmap);
		} else {
			canvas = new Canvas(originalBitmap);
		}
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setStrokeWidth(strokeWidth);
		if (isMove) {
			canvas.drawLine(startX, startY, clickX, clickY, paint);
		}

		startX = clickX;
		startY = clickY;
		// ������handWriting������������Ϳѻ��ʱ����saveBitmap��ֵ
		if (isClear) {
			saveBitmap = new2Bitmap;
		} else {
			saveBitmap = originalBitmap;
		}
		if (isClear) {
			return new2Bitmap;
		}
		// �����Ѿ���Ϳѻ��Bitmap

		return originalBitmap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		clickX = event.getX();
		clickY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isMove = false;
			invalidate();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			isMove = true;
			invalidate();
			return true;
		}
		// ֻҪ��ָһ����Ļ��Ϳѻ����ΪͼƬ�Ѿ�������,ͬʱ��Ϊû�б����µ�ͿѻЧ��
		ImgHangWritingActivity.isEdit = true;
		ImgHangWritingActivity.sil.setSave(false);
		// ImgHangWritingActivity.isSave = false;
		return super.onTouchEvent(event);
	}

	public Bitmap getSaveBitmap() {
		return saveBitmap;
	}

}
