package com.loveshare.imgProcessUI;

import java.io.File;

import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.ControlView;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;

public class ImgControlActivity extends Activity {
	
	private ControlView controlView;
	public static String imgPath;
	
	private ImageButton share;
	private ImageButton save;
	
	private SaveImgListener sil;
	private ShareListener sl;
	
	public static  boolean isEdit;
	private boolean isSave;
	
	private Bitmap saveBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgcontrol);
		
		imgPath = this.getIntent().getStringExtra("imgPath");
		controlView = (ControlView)this.findViewById(R.id.controlView);
		
		share = (ImageButton)this.findViewById(R.id.share);
		save  = (ImageButton)this.findViewById(R.id.save);
		
		saveBitmap = controlView.getBitmap();
		
		sl = new ShareListener(this, saveBitmap, imgPath);
		share.setOnClickListener(sl);

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath),ImgActivity.savePath);
		sil = new SaveImgListener(this, originFile, saveBitmap);
		save.setOnClickListener(sil);
	}
	
	// 当返回键被按下的另外一种重写写法
		@Override
		public void onBackPressed() {
			BackHint backHint = new BackHint(this, imgPath, saveBitmap,
					ImgActivity.class);
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
