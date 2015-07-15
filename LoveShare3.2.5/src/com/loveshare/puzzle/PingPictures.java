package com.loveshare.puzzle;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;

public class PingPictures extends Activity {
	// int count;

	private ImageView iv;

	private ImageButton share = null;
	private ImageButton save = null;
	private String imgPath;

	private Bitmap bmp;// 拼图后的图片位图对象

	private SaveImgListener sil;// 保存的监听器
	private ShareListener sl;// 分享的监听器

	private boolean isEdit;// 是否经过编辑的标志
	private boolean isSave;// 是否已经保存过图片的标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_puzzle);
		setContentView(R.layout.activity_imgadd);
		iv = (ImageView) findViewById(R.id.iv_image);
		System.out.println("ok");
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		ArrayList<String> pingImgPaths = b.getStringArrayList("pingImgPaths");
		Object[] obj = pingImgPaths.toArray(new String[1]);
		String[] paths = (String[]) obj;
		// String[] pingImgPaths = (String[])
		// bd.getCharSequenceArray("pingImgPaths");
		// System.out.println(pingImgPaths.size());
		// count = pingImgPaths.size();
		bmp = pingPictures(paths);
		// iv.setImageBitmap(bmp);
		// Bitmap bmp = pingPictures(new
		// String[]{"/mnt/sdcard/showPictures/11.jpg","/mnt/sdcard/showPictures/22.jpg","/mnt/sdcard/showPictures/11.jpg"});
		iv.setImageBitmap(bmp);

		imgPath = createImgPath();

		share = (ImageButton) this.findViewById(R.id.btn_share1);
		save = (ImageButton) this.findViewById(R.id.btn_save1);

		sl = new ShareListener(this, bmp, imgPath);
		share.setOnClickListener(sl);

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath), ImgActivity.imgPath);
		sil = new SaveImgListener(this, originFile, bmp);
		save.setOnClickListener(sil);

	}

	// 创建新的图片路径
	private String createImgPath() {
		String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File dir = null;

		// 判断手机是否有内存卡，如果有，则经图片文件存入内存卡中，如果没有，则存入手机自带内存空间中
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + this.getString(R.string.imgPath));
		} else {
			// 存到手机自带的内存空间中
		}
		dir.mkdir();
		File targetFile = new File(dir, name);
		return targetFile.getAbsolutePath();
	}

	private Bitmap pingPictures(String[] imagePaths) {

		/*
		 * BitmapFactory.Options options = new BitmapFactory.Options();
		 * options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一 Bitmap bitmap =
		 * BitmapFactory.decodeStream(cr .openInputStream(uri), null, options);
		 * preview.setImageBitmap(bitmap);
		 */
		int height = 0, width = 0;
		System.out.println("imagePaths.length :" + imagePaths.length);
		Bitmap[] bitmaps = new Bitmap[imagePaths.length];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;// 图片宽高都为原来的二分之一，即图片为原来的四分之一
		for (int i = 0; i < imagePaths.length; i++) {
			bitmaps[i] = BitmapFactory.decodeFile(imagePaths[i], options);
			// bitmaps[i] = BitmapFactory.decodeFile(imagePaths[i]);

		}

		for (int i = 0; i < imagePaths.length; i++) {
			if (bitmaps[i].getWidth() > width) {
				width = bitmaps[i].getWidth();
			}
			height += bitmaps[i].getHeight();
		}

		Bitmap result = Bitmap.createBitmap(width, height,
				bitmaps[0].getConfig());
		Paint paint = new Paint();
		Canvas canvas = new Canvas(result);

		for (int i = 0, y = 0; i < imagePaths.length; i++) {
			// y = 0;
			System.out.println("y:   " + y);
			// canvas.drawBitmap(bitmaps[i], 0, y, paint);
			canvas.drawBitmap(bitmaps[i], (width - bitmaps[i].getWidth()) / 2,
					y, paint);

			y += bitmaps[i].getHeight();
		}

		isEdit = true;
		return result;
	}

/*	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:

			saveBitmap();
			break;
		case R.id.share:
			saveBitmap();
			Intent intent = new Intent(this, ShareActivity.class);
			intent.putExtra("shareImgPath", imgPath);
			this.startActivity(intent);
			break;
		}
	}*/

	private void saveBitmap() {
		String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File dir = null;

		// 判断手机是否有内存卡，如果有，则经图片文件存入内存卡中，如果没有，则存入手机自带内存空间中
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + this.getString(R.string.imgPath));
		} else {
			// 存到手机自带的内存空间中
		}
		dir.mkdir();
		imgPath = dir + "/" + name;
		try {
			new SaveFile(new File(imgPath), this).saveFile(bmp);
		} catch (Exception e) {
			// 若保存失败，弹出Toast提示用户。
			Toast.makeText(this, R.string.saveFailed, Toast.LENGTH_LONG).show();

			e.printStackTrace();
		}
	}

	// 当返回键被按下的另外一种重写写法
	@Override
	public void onBackPressed() {
		BackHint backHint = new BackHint(this, imgPath, bmp, ImgActivity.class);
		int Saveflag = 0;// 是否已经保存的标志
		isSave = sil.isSave();

		// 当按下返回键且图片已经被改过时，则弹出返回提示框提示用户保存修改过的图片
		// 判断是否已经按下在分享的同时保存了美化后的图片文件
		if (!sl.isSave()) {
			// 判断是否按下保存按钮保存了美化后的图片文件
			if (!sil.isSave()) {
				// 判断是否美化过图片，若没有美化，则也没有弹出对话框提示用户保存的必要
				if (isEdit) {

					backHint.showAlertDialog();
					/*
					 * 无论用户是否选择了保存，都将isEdit设为false， 以防下一次进入美化界面而不进行美化提示用户保存
					 */
					isEdit = false;
					// 将路径更新为新保存的修改过的图片文件，这样方便进行图片的连续编辑
					imgPath = backHint.getImgPath();
					Saveflag = 1;
				}
			} else
				// 若按下了保存按钮保存文件，则应该将文件路径设为保存后文件的路径
				imgPath = sil.getImgPath();
		} else
			// 若按下了分享按钮的同时保存文件，则应该将文件路径设为保存后文件的路径
			imgPath = sl.getImgPath();

		if (Saveflag == 0) {
			backHint.gotoActivity(imgPath);
			this.finish();
		}

	}
}
