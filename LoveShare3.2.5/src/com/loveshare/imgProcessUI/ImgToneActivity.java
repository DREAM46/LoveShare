package com.loveshare.imgProcessUI;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.MainActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.GetOpinions;
import com.loveshare.imgProcessUtil.ImgHandler;
import com.loveshare.imgProcessUtil.RatingBarListener;
import com.loveshare.imgProcessUtil.ToneLayer;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveAlertDialogListener;
import com.loveshare.imgSave.SaveAsAlertDialogListener;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;
import com.loveshare.view.CreateCustomProgressDialog;
import com.loveshare.view.CustomProgressDialog;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ImgToneActivity extends Activity implements
		OnSeekBarChangeListener, OnClickListener {

	private TextView title_processImg;
	private ToneLayer mToneLayer;
	private ImageView mImageView;// 用于显示图片的ImgView
	private Bitmap mBitmap; // 即将设入mImageView的Bitmap
	private Bitmap handleBitmap;// 经过处理得到的Bitmap对象

	private ImageButton save;// 保存按钮
	private ImageButton share;// 分享按钮

	private File originFile;// 用于保存文件的File对象
	private boolean isSave;// 这个布尔值是表示图片是否已经保存

	private String shareImgPath;// 分享图片的路径
	private String imgPath;// 图片路径

	private SaveImgListener sil;// 保存按钮监听器
	private ShareListener sl;// 分享按钮监听器

	private boolean isEdit;// 表示是否修改图片过的boolean值

	private int lastCommonOpreation;
	private CustomProgressDialog dialog;

	private ImgHandler handler = new ImgHandler(this) {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			switch (msg.what) {
			case ImgHandler.EDIT_SUCCESS:
				mImageView.setImageBitmap(handleBitmap);
				sl.setBitmap(handleBitmap);
				sil.setMyBitmap(handleBitmap);
				sil.setSave(false);
				break;
			case ImgHandler.FAIL:
				Toast.makeText(this.getContext(), "美化失败", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};

	private CreateOriginFile createOriginFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgtone);
		// 对Activity进行初始化操作
		init();
		isSave = false;
	}

	private void init() {

		title_processImg = (TextView) this.findViewById(R.id.title_processImg);
		// 得到由Intent传进来的路径字符串，生成Bitmap对象和生成处理用于保存图片对象的File对象
		imgPath = this.getIntent().getStringExtra("imgPath");
		shareImgPath = imgPath;// 图片的分享路径

		createOriginFile = new CreateOriginFile(this);
		originFile = createOriginFile.createOriginFile(new File(imgPath),
				ImgActivity.savePath);
		mToneLayer = new ToneLayer(this);

		mBitmap = BitmapFactory.decodeFile(imgPath, ImgActivity.options);
		if (mImageView == null)
			System.out.println("mImageView == null");
		mImageView = (ImageView) findViewById(R.id.img_Toneview);
		mImageView.setImageBitmap(mBitmap);

		((LinearLayout) findViewById(R.id.tone_view)).addView(mToneLayer
				.getParentView());
		ArrayList<SeekBar> seekBars = mToneLayer.getSeekBars();
		for (int i = 0, size = seekBars.size(); i < size; i++) {
			seekBars.get(i).setOnSeekBarChangeListener(this);
		}
		handleBitmap = mBitmap;
		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);

		// 为刚开进入页面时未经美化的图片绑定监听器，可以在图片未经美化时保存图片
		prepareForShareAndSave(handleBitmap);
		// 创建进度条
		dialog = CreateCustomProgressDialog
				.createCustomProgressDialog(ImgToneActivity.this);
	}

	private void prepareForShareAndSave(Bitmap handleBitmap1) {
		sil = new SaveImgListener(this, originFile, handleBitmap);
		sl = new ShareListener(this, handleBitmap1, imgPath);
		save.setOnClickListener(sil);
		share.setOnClickListener(sl);
	}

	// 当滑动不同的滚动条时，程序对图片进行不同的处理
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		final int flag = (Integer) seekBar.getTag();
		final int progress1 = progress;
		
		// 显示进度条
		dialog.dismiss();
		dialog = CreateCustomProgressDialog
					.createCustomProgressDialog(ImgToneActivity.this);
		dialog.show();
		
		
		new Thread() {
			public void run() {
				{
					switch (flag) {
					case ToneLayer.FLAG_SATURATION:
						mToneLayer.setSaturation(progress1);
						break;
					case ToneLayer.FLAG_LUM:
						mToneLayer.setLum(progress1);
						break;
					case ToneLayer.FLAG_HUE:
						mToneLayer.setHue(progress1);
						break;
					}
					// 将表示是否修改图片过的boolean值设为true，表示用户已经修改过图片了
					isEdit = true;
					// 得到美化后的图片Bitmap
					handleBitmap = mToneLayer.handleImage(mBitmap, flag);
					// 为经过美化后的图片绑定监听器，可以在图片经过美化后保存图片
					originFile = createOriginFile.createOriginFile(new File(
							imgPath), ImgActivity.savePath);

					Message message = new Message();
					message.what = ImgHandler.EDIT_SUCCESS;
					handler.sendMessage(message);
				}
			};
		}.start();
	}

	private void ProcessTextView(TextView title_processImg2) {
		// TODO Auto-generated method stub

	}

	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	// 重新它的onKeyDown方法，如果按下返回键且isSave值为false即还未保存图片文件的话提示用户进行保存
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, handleBitmap,
				ImgActivity.class);
		int Saveflag = 0;// 是否已经保存的标志
		if (keyCode == KeyEvent.KEYCODE_BACK) {
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
				falseProcessMethod(backHint);
			}

		}
		return true;
	}

	// 对按下保存提示对话框的否定键的处理
	private void falseProcessMethod(BackHint backHint) {
		backHint.gotoActivity(imgPath);
		this.finish();
	}

}
