package com.loveshare.imgProcessUtil;

import com.loveshare.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;


public class RatingBarListener implements OnRatingBarChangeListener {

	private Context context;//�����Ķ���
	private BitmapDrawable drawable;//�������Ĵ���ͼƬ
	private ImageView imageView;
	private RatingBar ratingBar;//�ı�͸���ȵ���������
	private Bitmap saveBit;//���ڱ����λͼ����
	private boolean isEdit;

	
	public RatingBarListener(Context context, BitmapDrawable drawable,
			RatingBar ratingBar,ImageView imageView) {
		this.context = context;
		this.drawable = drawable;
		this.ratingBar = ratingBar;
		this.imageView = imageView;
	}

	// �����������������󶨵���Ч��
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		
		drawable.setAlpha((int) (rating * 255 / 5));
		//imageView.setAlpha((int) (rating * 255 / 5));
		ratingBar.setVisibility(View.GONE);
		Toast.makeText(context, R.string.toast_alppic_success,
				Toast.LENGTH_SHORT).show();
		//imageView.setDrawingCacheEnabled(true);
		isEdit = true;
		//saveBit = imageView.getDrawingCache();
		saveBit = drawable.getBitmap();
		imageView.setImageBitmap(saveBit);
		if(saveBit == null)		System.out.println("1");
		else	System.out.println("2");
		
	}

	public boolean isEdit() {
		return isEdit;
	}

	public Bitmap getSaveBit() {
		return saveBit;
	}

	public void setSaveBit(Bitmap saveBit) {
		this.saveBit = saveBit;
	}

	
	
}
