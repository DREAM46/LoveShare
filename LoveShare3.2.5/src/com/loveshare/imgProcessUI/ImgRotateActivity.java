package com.loveshare.imgProcessUI;

import java.io.File;

import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.GetOpinions;
import com.loveshare.imgProcessUtil.ImageUtil;
import com.loveshare.imgProcessUtil.ImgRotate;
import com.loveshare.imgProcessUtil.ImgHandler;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ImgRotateActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private ImageView img_rotate;
	private String imgPath;
	private Bitmap imgBit;

	private ImageButton share;
	private ImageButton save;

	private Button btn_turnleft;
	private Button btn_turnright;
	private Button btn_reverse;


	public static boolean isEdit;
	public static boolean isSave;

	private Bitmap saveBitmap;

	private SaveImgListener sil;
	private ShareListener sl;

	private int sampleSize = 1;

	private static final int DEFAULT_WIDTH = 512;
	private static final int DEFAULT_HEIGHT = 384;

	private ImgHandler handler = new ImgHandler(this) {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ImgHandler.EDIT_SUCCESS:
				System.out.println("ImgRotateActivity.MyHandler");
				img_rotate.setImageBitmap(saveBitmap);
				sil.setMyBitmap(saveBitmap);
				sl.setBitmap(saveBitmap);
				sil.setSave(false);
				break;
			}
		};
	};

	private String direction;

	private Animation animation_left;
	private Animation animation_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgrotate);

		imgPath = this.getIntent().getStringExtra("imgPath");
		this.init();

		imgBit = BitmapFactory.decodeFile(imgPath, ImgActivity.options);
		saveBitmap = imgBit;
		this.setOnClickListener();
		img_rotate.setImageBitmap(imgBit);

	}

	private void init() {
		img_rotate = (ImageView) this.findViewById(R.id.img_rotate);
		share = (ImageButton) this.findViewById(R.id.share);
		save = (ImageButton) this.findViewById(R.id.save);
		btn_turnleft = (Button) this.findViewById(R.id.btn_turnleft);
		btn_turnright = (Button) this.findViewById(R.id.btn_turnright);
		btn_reverse = (Button) this.findViewById(R.id.btn_reverse);
	}

	private void setOnClickListener() {

		btn_turnleft.setOnClickListener(this);
		btn_turnright.setOnClickListener(this);
		btn_reverse.setOnClickListener(this);

		saveBitmap = imgBit;
		sl = new ShareListener(this, saveBitmap, imgPath);
		share.setOnClickListener(sl);

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath), ImgActivity.savePath);
		sil = new SaveImgListener(this, originFile, saveBitmap);
		save.setOnClickListener(sil);
	}

	@Override
	public void onClick(View v) {
		final View view = v;
		new Thread() {
			@Override
			public void run() {
				Matrix m = new Matrix();
				int width = imgBit.getWidth();
				int height = imgBit.getHeight();

				while ((imgBit.getWidth() / sampleSize > DEFAULT_WIDTH * 2)
						|| (imgBit.getHeight() / sampleSize > DEFAULT_HEIGHT * 2)) {
					sampleSize *= 2;
				}
				animation_left = (AnimationSet) AnimationUtils.loadAnimation(
						ImgRotateActivity.this, R.anim.rotateimg_left);

				animation_right = (AnimationSet) AnimationUtils.loadAnimation(
						ImgRotateActivity.this, R.anim.rotateimg_right);
				BitmapFactory.Options options = ImgActivity.options;

				switch (view.getId()) {

				case R.id.btn_turnleft:
					// 左转90度，将旋转处理后的图像设为现实的图像，这样可以实现连续旋转的功能
					// 利用case穿透执行旋转代码
					m.setRotate(-90);
					rotateBitmap(m, width, height);
					//img_rotate.setAnimation(animation_left);
					/*animation_left.start();
					animation_left.setFillAfter(true);*/
					break;

				case R.id.btn_turnright:
					// 右转90度，将旋转处理后的图像设为现实的图像，这样可以实现连续旋转的功能
					m.setRotate(90);
					rotateBitmap(m, width, height);
					//img_rotate.setAnimation(animation_right);
				/*	animation_right.start();
					animation_right.setFillAfter(true);*/

					break;

				case R.id.btn_reverse:
					saveBitmap = new ImageUtil().reverseBitmap(saveBitmap, 0);
					break;
				}
				isEdit = true;
				Message message = new Message();
				message.what = ImgHandler.EDIT_SUCCESS;
				handler.sendMessage(message);
			}
		}.start();
	}

	private void rotateBitmap(Matrix m, int width, int height) {
		try {
			imgBit = Bitmap.createBitmap(imgBit, 0, 0, width, height, m, true);
		} catch (OutOfMemoryError ooe) {

			m.postScale((float) 1 / sampleSize, (float) 1 / sampleSize);
			imgBit = Bitmap.createBitmap(imgBit, 0, 0, width, height, m, true);

		} finally {
			saveBitmap = imgBit;
		}
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
			System.out.println("silimgPath" + imgPath);
		} else
			// 若按下了分享按钮的同时保存文件，则应该将文件路径设为保存后文件的路径
			imgPath = sl.getImgPath();

		if (Saveflag == 0) {
			backHint.gotoActivity(imgPath);
			this.finish();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		/*
		 * switch (checkedId) { case R.id.rb_left: direction = "left";
		 * System.out.println(direction); break; case R.id.rb_right: direction =
		 * "right"; break; }
		 */
	}

}
