package com.loveshare.imgProcessUI;

import java.io.File;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.activity.R;
import com.loveshare.activity.R.id;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.RatingBarListener;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveAlertDialogListener;
import com.loveshare.imgSave.SaveAsAlertDialogListener;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.imgSave.SaveImgListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

public class ImgAlphaActicity extends Activity implements OnClickListener {

	private static final int SHARE = 0;

	private RatingBar ratingBar = null;// 调节图片透明程度的星条
	private ImageView imageView = null;// 用来显示图片的ImageView
	private Bitmap picBit = null;// ImageView的Bitmap
	private String imgPath = null;// 图片文件路径
	private Bitmap saveBit = null;// 修改后即将用于保存图片的Bitmap
	private Intent intent = null;
	private ImageButton save = null;// 保存按钮
	private ImageButton share = null;// 分享按钮
	private String shareImgPath = null;// 将要进行分享的图片的路径
	private File originFile;// 处理图片后保存图片的新路径
	private SaveFile sf;// 用于保存文件的类
	private boolean isEdit;

	private RatingBarListener listener;
	private CreateOriginFile createOriginFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgalpha);

		// 得到ImgeView和picBit
		imageView = (ImageView) this.findViewById(R.id.img_alphaActivity);
		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);
		ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);

		// 为ImageView设置图片
		Intent intent = this.getIntent();
		imgPath = intent.getStringExtra("imgPath");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// 图片宽高都为原来的二分之一，即图片为原来的四分之一
		picBit = BitmapFactory.decodeFile(imgPath, options);
		imageView.setImageBitmap(picBit);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

		listener = new RatingBarListener(this, new BitmapDrawable(picBit),
				ratingBar, imageView);

		shareImgPath = imgPath;// 设置要分享的图片的路径

		// 为星星进度条设置监听器
		ratingBar.setOnRatingBarChangeListener(listener);

		// 为保存按钮绑定监听器
		save.setOnClickListener(this);
		share.setOnClickListener(this);

		createOriginFile = new CreateOriginFile(this);
		originFile = createOriginFile.createOriginFile(new File(imgPath),
				ImgActivity.savePath);
		sf = new SaveFile(originFile, this);// 即将被保存的图片的文件

		/*
		 * 先为MyRatingBarChangeListener.saveBit设好值，防止出现因为出现未改动进度条而保存的现象，
		 * 导致MyRatingBarChangeListener的onClick方法未能回调使得saveBit的值为空从而使得保存不成功
		 */
		listener.setSaveBit(picBit);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.save:
			// 弹出对话框，询问是否保存图片，用户可以选择"保存"、"另存为"、以及"否"
		/*	new AlertDialog.Builder(this)
					.setTitle("温馨提醒")
					.setIcon(R.drawable.logo1)
					.setMessage("您是否要保存这张图片吗?")
					.setPositiveButton(
							"保存",
							new SaveAlertDialogListener(this, sf, listener
									.getSaveBit()))
					.setNeutralButton(
							"另存为",
							new SaveAsAlertDialogListener(this, sf, listener
									.getSaveBit()))
					.setNegativeButton("否", null).show();*/
			break;

		case R.id.share:

			// 实现分享图片的同时默认保存图片
			originFile = createOriginFile.createOriginFile(new File(imgPath),
					ImgActivity.savePath);
			try {
				sf.saveFile(listener.getSaveBit());
				// 分享并保存文件的同时将文件的保存标志设为true
				sf.setSave(true);
			} catch (Exception e) {
				sf.saveFail();
				e.printStackTrace();
			}
			Intent intent = new Intent(this, ShareActivity.class);

			// 分享的应该是处理后的图片，所以将shareImgPath设为处理后的图片的路径
			shareImgPath = originFile.getAbsolutePath();
			intent.putExtra("shareImgPath", shareImgPath);

			this.startActivity(intent);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, listener.getSaveBit(),
				ImgActivity.class);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!sf.isSave()) {
				if (listener.isEdit()) {
					backHint.showAlertDialog();
					imgPath = originFile.getAbsolutePath();
					/*
					 * 无论用户是否选择了保存，都将isEdit设为false， 以防下一次进入美化界面而不进行美化提示用户保存
					 */
					isEdit = false;
				} else {
					isEdit = false;
					imgPath = backHint.getImgPath();
					gotoImgActivity();
				}
			} else {
				// 如果可以运行到这里，说明美化后的图片已经保存，将图片路径设为新的保存后的路径
				imgPath = sf.getImgPath();
				gotoImgActivity();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void gotoImgActivity() {
		Intent intent = new Intent(this, ImgActivity.class);
		intent.putExtra("imgPath", imgPath);
		this.startActivity(intent);
		this.finish();
	}

}
