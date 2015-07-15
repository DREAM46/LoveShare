package com.loveshare.imgProcessUI;

import java.io.File;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.MainActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.activity.R;
import com.loveshare.activity.R.layout;
import com.loveshare.imgProcessUI.ImgEffectsActivity.*;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.ImageUtil;
import com.loveshare.imgProcessUtil.ImgHandler;
import com.loveshare.imgProcessUtil.RatingBarListener;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveAlertDialogListener;
import com.loveshare.imgSave.SaveAsAlertDialogListener;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ImgEffectResultActivity extends Activity implements
		OnSeekBarChangeListener {

	private ImageView img_process;// 用于显示图片的ImageView
	private String imgPath;// 图片路径
	private TextView title_processImg;// 界面的title
	private ImageButton save;// 保存按钮
	private ImageButton share;// 分享按钮
	private Bitmap oriBitmap;
	private Bitmap processBitmap;// 特效图片完成后的位图对象
	
	private int saveFlag = 0;// 保存图片的标志

	private LinearLayout proSeekBarLinearLayout;
	private TextView proImgseekBarName;// 特效效果进度条的名字
	private SeekBar proImgseekBar;// 特效效果进度条
	private TextView seekBarHint;//特效进度条下的文本说明
	private SaveImgListener sil;// 保存按钮的监听器
	private ShareListener sl;// 分享按按钮的监听器
	
	private String shareImgPath;// 将要进行分享的图片的路径
	private ImageUtil util = new ImageUtil();

	private int type;// 特效处理的类型
	private boolean isEdit;//是否被编辑过的标志
	
	private static final int SPECIAL = 2;

	private ImgHandler handler = new ImgHandler(this){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
				case ImgHandler.EDIT_SUCCESS:
					System.out.println("ImgEffectResultActivity.MyHandler.SUCCESS");
					img_process.setImageBitmap(processBitmap);
					
					sl = new ShareListener(ImgEffectResultActivity.this, processBitmap, imgPath);
					share.setOnClickListener(sl);

					File originFile = new CreateOriginFile(ImgEffectResultActivity.this).createOriginFile(new File(
							imgPath),ImgActivity.savePath);
					sil = new SaveImgListener(ImgEffectResultActivity.this, originFile, processBitmap);
					save.setOnClickListener(sil);

					break;
				case ImgHandler.FAIL:
					break;
					
				case SPECIAL:
					System.out.println("ImgEffectResultActivity.SPECIAL");
					addView(type);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgeffect_result);

		img_process = (ImageView) this.findViewById(R.id.img_process);
		title_processImg = (TextView) this.findViewById(R.id.title_processImg);

		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);

		Intent intent = this.getIntent();
		imgPath = intent.getStringExtra("imgPath");

		shareImgPath = imgPath;// 设置要分享的图片的路径

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// 图片宽高都为原来的二分之一，即图片为原来的四分之一
		oriBitmap = BitmapFactory.decodeFile(imgPath, options);
		// processBitmap = ImageUtil.getRoundedCornerBitmap(oriBitmap, 40f);

		// 设置界面标题
		title_processImg.setText(intent.getStringExtra("title"));

		type = intent.getIntExtra("type", 0);

		if (type == ImgEffectsActivity.ROUNDCONCER
				|| type == ImgEffectsActivity.BEAM
				|| type == ImgEffectsActivity.BLURIMAGE) {
			proSeekBarLinearLayout = (LinearLayout) this
					.findViewById(R.id.proSeekBarLinearLayout);
			processBitmap = oriBitmap;
			
			new Thread(){
				public void run() {
					Message message = new Message();
					message.what = ImgEffectResultActivity.SPECIAL;
					handler.sendMessage(message);
				};
			}.start();
			
		}

		new Thread(){
			public void run() {
				// 图片处理
				switch (type) {

				case ImgEffectsActivity.OLDREMEBER:
				
					processBitmap = util.oldRemeber(oriBitmap);
					isEdit = true;
					break;

				case ImgEffectsActivity.SHARPEN:
					processBitmap = util.sharpenImageAmeliorate(oriBitmap);
					isEdit = true;
					break;

				case ImgEffectsActivity.REVERSE:
					processBitmap = util.reverseBitmap(oriBitmap, 0);
					isEdit = true;
					break;

				case ImgEffectsActivity.SKETCH:
					SketchImg();
					isEdit = true;
					break;

				case ImgEffectsActivity.NEGATIVER:
					processBitmap = util.film(oriBitmap);
					isEdit = true;
					break;

				case ImgEffectsActivity.RILIEVO:
					processBitmap = util.emboss(oriBitmap);
					isEdit = true;
					break;

				}
				Message message = new Message();
				message.what = handler.EDIT_SUCCESS;
				handler.sendMessage(message);
			};
		}.start();
		
	}

	// 添加组件
	private void addView(int type) {
		LinearLayout linearLayout1 = new LinearLayout(this);
		linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		
		proImgseekBarName = new TextView(this);
		proImgseekBar = new SeekBar(this);
		seekBarHint = new TextView(this);
		
		switch (type) {

			case ImgEffectsActivity.ROUNDCONCER:
				proImgseekBarName.setText("弧度");
				seekBarHint.append("圆角弧度:"+proImgseekBar.getProgress());
				break;
			case ImgEffectsActivity.BEAM:
				proImgseekBarName.setText("强度");
				seekBarHint.append("光照强度:"+proImgseekBar.getProgress());
				break;
			case ImgEffectsActivity.BLURIMAGE:
				proImgseekBarName.setText("程度");
				seekBarHint.append("模糊程度:"+proImgseekBar.getProgress());
				break;
				
		}
		proImgseekBar.setMax(100);
		proImgseekBar.setProgress(1);
		proImgseekBar.setOnSeekBarChangeListener(this);// 为进度条绑定监听器
		LinearLayout.LayoutParams seekLayoutparams = new LinearLayout.LayoutParams(
				50, 25);

		linearLayout1.addView(proImgseekBarName, seekLayoutparams);

		seekLayoutparams = new LinearLayout.LayoutParams(250, 25);
		linearLayout1.addView(proImgseekBar, seekLayoutparams);
		
		seekLayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		proSeekBarLinearLayout.addView(linearLayout1, seekLayoutparams);
		proSeekBarLinearLayout.addView(seekBarHint,seekLayoutparams);
		
	}

	// 进入图片素描界面
	private void SketchImg() {
		Intent intent1 = new Intent(this, ImgSketchActivity.class);
		intent1.putExtra("imgPath", imgPath);
		this.startActivity(intent1);
		this.finish();
	}

	@Override
	// 重新它的onKeyDown方法，如果按下返回键且isSave值为false即还未保存图片文件的话提示用户进行保存
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, processBitmap,
				ImgActivity.class);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 一旦用户进入了这个特效图片的界面，就意味着图片已经经过处理了，
			// 若图片未保存，则提示用户保存图片
			// 判断是否已经按下在分享的同时保存了美化后的图片文件
			if (!sl.isSave()) {
				// 判断是否按下保存按钮保存了美化后的图片文件
				if (!sil.isSave()) {
					if (isEdit) {
					// 弹出的提示对话框,提示用户是否保存按钮保存美化后的图片文件
					backHint.showAlertDialog();
					/*
					 * 无论用户是否选择了保存，都将isEdit设为false， 以防下一次进入美化界面而不进行美化提示用户保存
					 */
					// 将路径更新为新保存的修改过的图片文件，这样方便进行图片的连续编辑
					imgPath = backHint.getImgPath();
					saveFlag = 1;
					}
				} else
					imgPath = sil.getImgPath();// 若按下了保存按钮保存文件，则应该将文件路径设为保存后文件的路径
			} else
				imgPath = sl.getImgPath();// 若按下了分享按钮的同时保存文件，则应该将文件路径设为保存后文件的路径
		}
		if (saveFlag == 0)
			falseProcessMethod(backHint);
		return true;
	}

	// 对按下保存提示对话框的否定键的处理
	private void falseProcessMethod(BackHint backHint) {
		backHint.gotoActivity(imgPath);
		this.finish();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (type) {
		case ImgEffectsActivity.ROUNDCONCER:
			processBitmap = util.getRoundedCornerBitmap(oriBitmap, progress);
			seekBarHint.setText("圆角弧度:"+proImgseekBar.getProgress());
			break;
		case ImgEffectsActivity.BEAM:
			processBitmap = util.sunshine(oriBitmap, oriBitmap.getWidth(),
					oriBitmap.getHeight(), progress);
			seekBarHint.setText("光照强度:"+proImgseekBar.getProgress());
			break;
		case ImgEffectsActivity.BLURIMAGE:
			processBitmap = util.blurImageAmeliorate(oriBitmap, progress);
			seekBarHint.setText("模糊程度:"+proImgseekBar.getProgress());
			break;
		}
		sl.setBitmap(processBitmap);
		sil.setMyBitmap(processBitmap);
		img_process.setImageBitmap(processBitmap);
		isEdit = true;
		
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
}
