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

	private Context context;//上下文对象
	private BitmapDrawable drawable;//传进来的处理图片
	private ImageView imageView;
	private RatingBar ratingBar;//改变透明度的星条对象
	private Bitmap saveBit;//用于保存的位图对象
	private boolean isEdit;

	
	public RatingBarListener(Context context, BitmapDrawable drawable,
			RatingBar ratingBar,ImageView imageView) {
		this.context = context;
		this.drawable = drawable;
		this.ratingBar = ratingBar;
		this.imageView = imageView;
	}

	// 设置星星条监听器绑定淡化效果
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
