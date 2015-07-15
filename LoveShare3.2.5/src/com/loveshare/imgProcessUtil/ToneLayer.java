package com.loveshare.imgProcessUtil;

import java.util.ArrayList;

import com.loveshare.activity.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * ͼƬ��ɫ����
 * 
 * @author maylian7700@126.com
 * 
 */
public class ToneLayer {

	/**
	 * ���Ͷȱ�ʶ
	 */
	public static final int FLAG_SATURATION = 0x0;

	/**
	 * ���ȱ�ʶ
	 */
	public static final int FLAG_LUM = 0x1;

	/**
	 * ɫ���ʶ
	 */
	public static final int FLAG_HUE = 0x2;

	/**
	 * ���Ͷ�
	 */
	private TextView mSaturation;
	private SeekBar mSaturationBar;

	/**
	 * ɫ��
	 */
	private TextView mHue;
	private SeekBar mHueBar;

	/**
	 * ����
	 */
	private TextView mLum;
	private SeekBar mLumBar;

	private float mDensity;
	private static final int TEXT_WIDTH = 50;

	private LinearLayout mParent;

	private ColorMatrix mLightnessMatrix;
	private ColorMatrix mSaturationMatrix;
	private ColorMatrix mHueMatrix;
	private ColorMatrix mAllMatrix;

	/**
	 * ����
	 */
	private float mLumValue = 1F;

	/**
	 * ���Ͷ�
	 */
	private float mSaturationValue = 0F;

	/**
	 * ɫ��
	 */
	private float mHueValue = 0F;

	/**
	 * SeekBar���м�ֵ
	 */
	private static final int MIDDLE_VALUE = 127;

	/**
	 * SeekBar�����ֵ
	 */
	private static final int MAX_VALUE = 255;

	private ArrayList<SeekBar> mSeekBars = new ArrayList<SeekBar>();

	public ToneLayer(Context context) {
		init(context);
	}

	private void init(Context context) {
		mDensity = context.getResources().getDisplayMetrics().density;

		mSaturation = new TextView(context);
		mSaturation.setText(R.string.saturation);
		mHue = new TextView(context);
		mHue.setText(R.string.contrast);
		mLum = new TextView(context);
		mLum.setText(R.string.lightness);

		Resources res = context.getResources();

		mSaturationBar = new SeekBar(context);
		mSaturationBar
				.setThumb(res.getDrawable(R.drawable.seekbar_green_thumb));
		mSaturationBar.setProgressDrawable(res
				.getDrawable(R.drawable.seek_greed_drawable));

		mHueBar = new SeekBar(context);
		mHueBar.setThumb(res.getDrawable(R.drawable.seekbar_green_thumb));
		mHueBar.setProgressDrawable(res
				.getDrawable(R.drawable.seek_greed_drawable));

		mLumBar = new SeekBar(context);
		mLumBar.setThumb(res.getDrawable(R.drawable.seekbar_green_thumb));
		mLumBar.setProgressDrawable(res
				.getDrawable(R.drawable.seek_greed_drawable));

		mSeekBars.add(mSaturationBar);
		mSeekBars.add(mHueBar);
		mSeekBars.add(mLumBar);

		for (int i = 0, size = mSeekBars.size(); i < size; i++) {
			SeekBar seekBar = mSeekBars.get(i);
			seekBar.setMax(MAX_VALUE);
			seekBar.setProgress(MIDDLE_VALUE);
			seekBar.setTag(i);
		}

		LinearLayout saturation = new LinearLayout(context);
		saturation.setOrientation(LinearLayout.HORIZONTAL);
		saturation.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		LinearLayout.LayoutParams txtLayoutparams = new LinearLayout.LayoutParams(
				(int) (TEXT_WIDTH * mDensity),
				LinearLayout.LayoutParams.MATCH_PARENT);
		mSaturation.setGravity(Gravity.CENTER);
		saturation.addView(mSaturation, txtLayoutparams);

		LinearLayout.LayoutParams seekLayoutparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		seekLayoutparams.setMargins(3, 3, 3, 3);
		saturation.addView(mSaturationBar, seekLayoutparams);

		LinearLayout hue = new LinearLayout(context);
		hue.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layout_params_hue = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout_params_hue.setMargins(3, 0, 3, 3);
		hue.setLayoutParams(layout_params_hue);

		mHue.setGravity(Gravity.CENTER);
		hue.addView(mHue, txtLayoutparams);
		hue.addView(mHueBar, seekLayoutparams);

		LinearLayout lum = new LinearLayout(context);
		lum.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layouyparams_lum = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layouyparams_lum.setMargins(3, 0, 3, 3);
		lum.setLayoutParams(layouyparams_lum);

		mLum.setGravity(Gravity.CENTER);
		lum.addView(mLum, txtLayoutparams);
		lum.addView(mLumBar, seekLayoutparams);

		mParent = new LinearLayout(context);
		mParent.setOrientation(LinearLayout.VERTICAL);
		mParent.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mParent.addView(saturation);
		mParent.addView(hue);
		mParent.addView(lum);
	}

	public View getParentView() {
		return mParent;
	}

	/**
	 * ���ñ��Ͷ�ֵ
	 * 
	 * @param saturation
	 */
	public void setSaturation(int saturation) {
		mSaturationValue = saturation * 1.0F / MIDDLE_VALUE;
	}

	/**
	 * ����ɫ��ֵ
	 * 
	 * @param hue
	 */
	public void setHue(int hue) {
		mHueValue = hue * 1.0F / MIDDLE_VALUE;
	}

	/**
	 * ��������ֵ
	 * 
	 * @param lum
	 */
	public void setLum(int lum) {
		mLumValue = (lum - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
	}

	public ArrayList<SeekBar> getSeekBars() {
		return mSeekBars;
	}

	/**
	 * 
	 * @param flag
	 *            ����λ0 ��ʾ�Ƿ�ı�ɫ�࣬��λ1��ʾ�Ƿ�ı䱥�Ͷ�,����λ2��ʾ�Ƿ�ı�������
	 */
	public Bitmap handleImage(Bitmap bm, int flag) {

		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Bitmap.Config.ARGB_8888);
		// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
		Canvas canvas = new Canvas(bmp); // �õ����ʶ���
		Paint paint = new Paint(); // �½�paint
		paint.setAntiAlias(true); // ���ÿ����,Ҳ���Ǳ�Ե��ƽ������
		if (null == mAllMatrix) {
			mAllMatrix = new ColorMatrix();
		}

		if (null == mLightnessMatrix) {
			// ������ɫ�任�ľ���androidλͼ��ɫ�仯������Ҫ�ǿ��ö������
			mLightnessMatrix = new ColorMatrix();
		}

		if (null == mSaturationMatrix) {
			mSaturationMatrix = new ColorMatrix();
		}

		if (null == mHueMatrix) {
			mHueMatrix = new ColorMatrix();
		}

		switch (flag) {
		case FLAG_HUE: // ��Ҫ�ı�ɫ��
			mHueMatrix.reset();
			// �졢�̡�������������ͬ�ı���,���һ������1��ʾ͸���Ȳ����仯���˺�����ϸ˵���ο�
			mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);
			// // android
			// doc
			break;
		case FLAG_SATURATION: // ��Ҫ�ı䱥�Ͷ�
			// saturation ���Ͷ�ֵ����С����Ϊ0����ʱ��Ӧ���ǻҶ�ͼ(Ҳ�����׻��ġ��ڰ�ͼ��)��
			// Ϊ1��ʾ���ͶȲ��䣬���ô���1������ʾ������
			mSaturationMatrix.reset();
			mSaturationMatrix.setSaturation(mSaturationValue);
			break;
		case FLAG_LUM: // ����
			// hueColor����ɫ����ת�ĽǶ�,��ֵ��ʾ˳ʱ����ת����ֵ��ʾ��ʱ����ת
			mLightnessMatrix.reset(); // ��ΪĬ��ֵ
			mLightnessMatrix.setRotate(0, mLumValue); // �����ú�ɫ����ɫ������ת�ĽǶ�
			mLightnessMatrix.setRotate(1, mLumValue); // �������̺�ɫ����ɫ������ת�ĽǶ�
			mLightnessMatrix.setRotate(2, mLumValue); // ��������ɫ����ɫ������ת�ĽǶ�
			// �����൱�ڸı����ȫͼ��ɫ��
			break;
		}
		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix);
		mAllMatrix.postConcat(mSaturationMatrix); // Ч������
		mAllMatrix.postConcat(mLightnessMatrix); // Ч������

		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// ������ɫ�任Ч��
		canvas.drawBitmap(bm, 0, 0, paint); // ����ɫ�仯���ͼƬ������´�����λͼ��
		return bmp;// �����µ�λͼ��Ҳ����ɫ������ͼƬ
	}
}
