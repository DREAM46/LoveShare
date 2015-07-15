package com.loveshare.imgProcessUI;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.GetOpinions;
import com.loveshare.imgProcessUtil.ImageUtil;
import com.loveshare.imgProcessUtil.ImgSketch;
import com.loveshare.imgProcessUtil.ImgHandler;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;
import com.loveshare.view.CreateCustomProgressDialog;
import com.loveshare.view.CustomProgressDialog;

public class ImgEffectsActivity extends Activity implements OnTouchListener,
		OnSeekBarChangeListener {

	private TextView title_processImg;

	// 为此Activity中的每个LinearLayout组件定义成成员变量
	private LinearLayout roundConcer;
	private LinearLayout oldremeber;
	private LinearLayout sharpen;
	private LinearLayout beam;
	private LinearLayout sketch;
	private LinearLayout negative;
	private LinearLayout rilievo;

	private ImageView img_process;// 用来此Activity用来显示图片的ImageView
	private String imgPath;// 图片的路径

	private Bitmap originBitmap;// 将会设入imgView_processActivity的Bitmap对象
	private Bitmap processBitmap;// 特效图片完成后的位图对象

	private ImageView img_roundConcer;
	private ImageView img_oldremeber;
	private ImageView img_sharpen;
	private ImageView img_beam;
	private ImageView img_rilievo;
	private ImageView img_negative;

	private RelativeLayout proSeekBarLinearLayout;
	private SeekBar proImgseekBar;// 特效效果进度条
	private TextView seekBarHint;// 特效进度条下的文本说明

	private ImageButton save;
	private ImageButton share;

	private String shareImgPath;// 将要进行分享的图片的路径

	private ImageUtil util = new ImageUtil();

	private int type;// 特效处理的类型
	private boolean isEdit;// 是否被编辑过的标志

	private SaveImgListener sil;// 保存按钮的监听器
	private ShareListener sl;// 分享按按钮的监听器
	private int saveFlag = 0;// 保存图片的标志

	private ImageView[] imageViews;

	public static final int ROUNDCONCER = 1;
	public static final int OLDREMEBER = 2;
	public static final int SHARPEN = 3;
	public static final int REVERSE = 4;
	public static final int BEAM = 5;
	public static final int SKETCH = 6;
	public static final int REFLECTION = 7;
	public static final int NEGATIVER = 8;
	public static final int RILIEVO = 9;
	public static final int BLURIMAGE = 10;

	private int lastCommonOpreation;
	private CustomProgressDialog dialog;

	private File originFile;
	private CreateOriginFile createOriginFile;

	private BackHint backHint;

	private ImgHandler handler = new ImgHandler(this) {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case R.id.roundConcer:
				ImgEffectsActivity.this.addHint(img_roundConcer);
				addView(what);
				title_processImg.setText("圆角模式");
				break;
			case R.id.beam:
				ImgEffectsActivity.this.addHint(img_beam);
				addView(what);
				title_processImg.setText("光照模式");
				break;
			case R.id.oldremeber:
				ImgEffectsActivity.this.addHint(img_oldremeber);
				setViewGone();
				isEdit = true;
				title_processImg.setText("怀旧模式");
				sil.setSave(false);
				break;
			case R.id.sharpen:
				ImgEffectsActivity.this.addHint(img_sharpen);
				setViewGone();
				isEdit = true;
				title_processImg.setText("锐化模式");
				sil.setSave(false);
				break;
			case R.id.negative:
				ImgEffectsActivity.this.addHint(img_negative);
				setViewGone();
				isEdit = true;
				title_processImg.setText("底片模式");
				sil.setSave(false);
				break;
			case R.id.rilievo:
				ImgEffectsActivity.this.addHint(img_rilievo);
				setViewGone();
				isEdit = true;
				title_processImg.setText("浮雕模式");
				sil.setSave(false);
				break;

			case ImgHandler.PROGREE_SUCCESS:
				isEdit = true;
				sil.setSave(false);
				sl.setBitmap(processBitmap);
				sil.setMyBitmap(processBitmap);
				img_process.setImageBitmap(processBitmap);
				break;

			}

			dialog.dismiss();
			isEdit = true;
			sil.setSave(false);
			sl.setBitmap(processBitmap);
			sil.setMyBitmap(processBitmap);
			img_process.setImageBitmap(processBitmap);

			save.setOnClickListener(sil);
			share.setOnClickListener(sl);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgprocess_effects);
		/*
		 * 由intent得到的由上一个Activity传过来的路径字符串，并且生成Bitmap对象imgBit
		 * 将生成的imgBit设入imgView_processActivity中显示图片
		 */
		imgPath = this.getIntent().getStringExtra("imgPath");
		System.out.println("1" + imgPath);
		shareImgPath = imgPath;// 设置要分享的图片的路径

		img_process = (ImageView) this.findViewById(R.id.img_process);

		originBitmap = BitmapFactory.decodeFile(imgPath, ImgActivity.options);
		processBitmap = originBitmap;
		img_process.setImageBitmap(originBitmap);

		init();
		setOnClickListener();

		imageViews = new ImageView[] { img_roundConcer, img_oldremeber,
				img_sharpen, img_beam, img_rilievo, img_negative, };

		dialog = CreateCustomProgressDialog.createCustomProgressDialog(this);
	}

	private void setOnClickListener() {
		// 为各个LinearLayout绑定监听器
		roundConcer.setOnTouchListener(this);
		oldremeber.setOnTouchListener(this);
		sharpen.setOnTouchListener(this);
		beam.setOnTouchListener(this);
		negative.setOnTouchListener(this);
		rilievo.setOnTouchListener(this);

		sl = new ShareListener(this, originBitmap, imgPath);
		share.setOnClickListener(sl);

		createOriginFile = new CreateOriginFile(this);
		originFile = createOriginFile.createOriginFile(new File(imgPath),
				ImgActivity.savePath);
		sil = new SaveImgListener(this, originFile, originBitmap);
		save.setOnClickListener(sil);
	}

	private void init() {

		title_processImg = (TextView) this.findViewById(R.id.title_processImg);

		roundConcer = (LinearLayout) this.findViewById(R.id.roundConcer);
		oldremeber = (LinearLayout) this.findViewById(R.id.oldremeber);
		sharpen = (LinearLayout) this.findViewById(R.id.sharpen);
		beam = (LinearLayout) this.findViewById(R.id.beam);
		negative = (LinearLayout) this.findViewById(R.id.negative);
		rilievo = (LinearLayout) this.findViewById(R.id.rilievo);

		proSeekBarLinearLayout = (RelativeLayout) this
				.findViewById(R.id.proSeekBarLinearLayout);
		proImgseekBar = (SeekBar) this.findViewById(R.id.proImgseekBar);
		seekBarHint = (TextView) this.findViewById(R.id.seekBarHint);

		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);

		img_roundConcer = (ImageView) this.findViewById(R.id.img_roundConcer);
		img_oldremeber = (ImageView) this.findViewById(R.id.img_oldremeber);
		img_sharpen = (ImageView) this.findViewById(R.id.img_sharpen);
		img_beam = (ImageView) this.findViewById(R.id.img_beam);
		img_rilievo = (ImageView) this.findViewById(R.id.img_rilievo);
		img_negative = (ImageView) this.findViewById(R.id.img_negative);
	}

	// 重写OnTouchListener接口中的onTouch实现对各个LinearLayout按下事件的实现
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// 当按下时，组件高亮显示
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int id = view.getId();

			if (id != R.id.roundConcer && id != R.id.beam) {
				dialog = CreateCustomProgressDialog
						.createCustomProgressDialog(this);
				dialog.show();
			}

			if (id == lastCommonOpreation) {

				if (id != R.id.roundConcer && id != R.id.beam) {
					dialog.dismiss();
				}

			} else {
				lastCommonOpreation = id;
				view.setBackgroundResource(R.drawable.background_toolbar);
				final View view1 = view;
				final ImageView img_process1 = img_process;
				final String imgPath1 = imgPath;

				new Thread() {
					public void run() {
						Message message = new Message();
						switch (view1.getId()) {
						case R.id.roundConcer:
							type = R.id.roundConcer;
							processBitmap = originBitmap;
							break;
						case R.id.beam:
							type = R.id.beam;
							processBitmap = originBitmap;
							break;
						case R.id.oldremeber:
							processBitmap = util.oldRemeber(originBitmap);
							break;
						case R.id.sharpen:
							processBitmap = util
									.sharpenImageAmeliorate(originBitmap);
							break;
						case R.id.negative:
							processBitmap = util.film(originBitmap);
							break;
						case R.id.rilievo:
							processBitmap = util.emboss(originBitmap);
							break;
						}
						// 为经过美化后的图片绑定监听器，可以在图片经过美化后保存图片
						
						File imgFile = new File(imgPath);

						originFile = createOriginFile.createOriginFile(imgFile, ImgActivity.savePath);
						message.what = view1.getId();
						handler.sendMessage(message);
					};
				}.start();
			}
		}
		return true;
	}

	private void setViewGone() {
		proImgseekBar.setVisibility(View.GONE);
		seekBarHint.setVisibility(View.GONE);
	}

	private void addHint(ImageView imageView) {
		for (int i = 0; i < imageViews.length; i++)
			imageViews[i].setBackgroundDrawable(null);
		imageView.setBackgroundResource(R.drawable.bg_broader);
	}

	// 添加组件
	private void addView(int type) {
		proImgseekBar.setProgress(0);
		proImgseekBar.setVisibility(View.VISIBLE);
		seekBarHint.setVisibility(View.VISIBLE);

		switch (type) {

		case R.id.roundConcer:
			seekBarHint.setText("   圆角弧度:" + proImgseekBar.getProgress());
			break;
		case R.id.beam:
			seekBarHint.setText("   光照强度:" + proImgseekBar.getProgress());
			break;
		/*
		 * case R.id.blurImage: seekBarHint.setText("   模糊程度:" +
		 * proImgseekBar.getProgress()); break;
		 */
		}
		// 为进度条绑定监听器
		proImgseekBar.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress1,
			boolean fromUser) {

		final int progress = progress1;
		dialog.dismiss();
		dialog = CreateCustomProgressDialog
				.createCustomProgressDialog(this);
		dialog.show();

		if (type == R.id.roundConcer) {
			seekBarHint.setText("圆角弧度:" + proImgseekBar.getProgress());
		} else if (type == R.id.beam) {
			seekBarHint.setText("光照强度:" + proImgseekBar.getProgress());
		}
		new Thread() {
			public void run() {
				switch (type) {
				case R.id.roundConcer:
					processBitmap = util.getRoundedCornerBitmap(originBitmap,
							progress);

					break;
				case R.id.beam:
					processBitmap = util.sunshine(originBitmap,
							originBitmap.getWidth(), originBitmap.getHeight(),
							progress);

					break;
				}

				Message message = new Message();
				message.what = ImgHandler.PROGREE_SUCCESS;
				handler.sendMessage(message);
			};
		}.start();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		backHint = new BackHint(this, imgPath, processBitmap, ImgActivity.class);
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
				// 若按下了保存按钮保存文件，则应该将文件路径设为保存后文件的路径
				imgPath = sil.getImgPath();
		} else
			// 若按下了分享按钮的同时保存文件，则应该将文件路径设为保存后文件的路径
			imgPath = sl.getImgPath();
		System.out.println("2================" + imgPath);
		if (saveFlag == 0) {
			backHint.gotoActivity(imgPath);
			this.finish();
		}

	}
}
