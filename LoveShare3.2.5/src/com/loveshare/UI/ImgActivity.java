package com.loveshare.UI;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loveshare.activity.R;
import com.loveshare.imgProcessUI.ImgBroaderActivity;
import com.loveshare.imgProcessUI.ImgEffectsActivity;
import com.loveshare.imgProcessUI.ImgHangWritingActivity;
import com.loveshare.imgProcessUI.ImgRotateActivity;
import com.loveshare.imgProcessUI.ImgToneActivity;
import com.loveshare.imgProcessUtil.GetOpinions;
import com.loveshare.imgProcessUtil.ImgHandler;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;
import com.loveshare.view.CreateCustomProgressDialog;
import com.loveshare.view.CustomProgressDialog;

public class ImgActivity extends Activity implements OnTouchListener {

	public static int win_width;// 屏幕宽度
	public static int win_height;// 屏幕高度

	private SharedPreferences preferences;

	private static final int FLAG_CHOOSE = 11;
	private static final int FLAG_HANDLEBACK = 12;

	// 将ImgActivity中的各个LinearLayout组件设为成员变量
	private LinearLayout enhanceLinearLayout;
	private LinearLayout effectsLinearLayout;
	private LinearLayout wordLinearLayout;
	//private LinearLayout handWritingLinearLayout;
	private LinearLayout broaderLinearLayout;
	private LinearLayout rotateLinearLayout;

	// ImgActivity中的显示的主要图片img_picactivity
	private ImageView imgView_picactivity;
	// 即将设入ImgActivity的Bitmap对象
	private Bitmap imgBit = null;

	public static String imgPath = null;
	public static String savePath;

	public static BitmapFactory.Options options;
	private boolean isContinueToGetOpions = true;

	private ImgHandler handler = new ImgHandler(this) {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ImgHandler.DECODE_SUCCESS:
				// 进度条对话框消失
				dialog.dismiss();
				imgView_picactivity.setImageBitmap(imgBit);
				setSaveButtonLisetener();
				setShareButtonLisetener();
				break;
			}
		};
	};

	private CustomProgressDialog dialog;

	/*private ImageButton btn_save2;
	private ImageButton btn_share2;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img);

		//读取配置文件确定保存文件路径
		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);
		savePath = preferences.getString("savePath", null);

		// 得到由主界面MainActivity传入的图片路径
		imgPath = this.getIntent().getStringExtra("imgPath");
		System.out.println("3"+imgPath);

		// 给各个LinearLayout成员变量赋值
		enhanceLinearLayout = (LinearLayout) this
				.findViewById(R.id.enhanceLinearLayout);
		effectsLinearLayout = (LinearLayout) this
				.findViewById(R.id.effectsLinearLayout);
		/*handWritingLinearLayout = (LinearLayout) this
				.findViewById(R.id.handWritingLinearLayout);*/
		broaderLinearLayout = (LinearLayout) this
				.findViewById(R.id.broaderLinearLayout);
		rotateLinearLayout = (LinearLayout) this
				.findViewById(R.id.rotateLinearLayout);

		
		/*btn_save2 = (ImageButton) this.findViewById(R.id.btn_save2);
		btn_share2 = (ImageButton) this.findViewById(R.id.btn_share2);*/

		// 由图片路径去生成一个Bitmap对象，然后将其设入此Activity的主要图片ImageView img_picactivity中
		imgView_picactivity = (ImageView) this
				.findViewById(R.id.imgView_picactivity);

		// 得到手机的宽高
		WindowManager manager = (WindowManager) this
				.getSystemService(WINDOW_SERVICE);
		win_width = manager.getDefaultDisplay().getWidth();
		win_height = manager.getDefaultDisplay().getHeight();

		// 给此Activity中的各个LinearLayout绑定监听器
		enhanceLinearLayout.setOnTouchListener(this);
		effectsLinearLayout.setOnTouchListener(this);
		//handWritingLinearLayout.setOnTouchListener(this);
		broaderLinearLayout.setOnTouchListener(this);
		rotateLinearLayout.setOnTouchListener(this);

		options = GetOpinions.getGetOpinions(imgPath, win_width, win_height,
				null);

		// 显示等待对话框
		dialog = CreateCustomProgressDialog.createCustomProgressDialog(this);
		dialog.show();

		new Thread() {
			public void run() {
				while (isContinueToGetOpions) {
					try {
						imgBit = BitmapFactory.decodeFile(imgPath, options);
						isContinueToGetOpions = false;
						Message message = new Message();
						message.what = ImgHandler.DECODE_SUCCESS;
						handler.sendMessage(message);
					} catch (OutOfMemoryError e) {
						options = GetOpinions.getGetOpinions(imgPath,
								win_width, win_height, options);

						// 回收造成爆内存Bitmap
						if (imgBit != null && !imgBit.isRecycled()) {
							imgBit.recycle();
						}

					}
				}
			};
		}.start();
	}

	private void setShareButtonLisetener() {
		ShareListener sl = new ShareListener(this, imgBit, imgPath);
		//btn_share2.setOnClickListener(sl);
	}

	private void setSaveButtonLisetener() {
		File originFile = new CreateOriginFile(this).createOriginFile(
				new File(imgPath), ImgActivity.savePath);
		SaveImgListener sil = new SaveImgListener(this, originFile, imgBit);
		//btn_save2.setOnClickListener(sil);
	}

	// 重写OnTouchListener接口中的onTouch实现对各个LinearLayout按下事件的实现
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// 当按下时，组件高亮显示
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Intent intent = new Intent();
			switch (view.getId()) {
			// 如果点击的是enhanceLinearLayout就跳转到enhanceLinearLayout对图片进行美化操作
			case R.id.enhanceLinearLayout:
				intent.setClass(this, ImgToneActivity.class);
				break;
			// 如果点击的是ImageEffectsActivity就跳转到ImageEffectsActivity对图片进行特效处理
			case R.id.effectsLinearLayout:
				intent.setClass(this, ImgEffectsActivity.class);
				break;
		/*	case R.id.handWritingLinearLayout:
				intent.setClass(this, ImgHangWritingActivity.class);
				break;*/
			case R.id.broaderLinearLayout:
				intent.setClass(this, ImgBroaderActivity.class);
				break;
			case R.id.rotateLinearLayout:
				intent.setClass(this, ImgRotateActivity.class);
			}
			/*
			 * 将图片路径imgPath绑在Intent对象上传给下一个Activity，并且此Activity调用startActivity方法
			 * 实现跳转到下一个Activity
			 */
			intent.putExtra("imgPath", imgPath);
			this.startActivity(intent);
			this.finish();
		}
		return true;
	}

	@Override
	public void onBackPressed() {

		this.startActivity(new Intent(this, MainActivity.class));
		this.finish();

	}// onCreateOptionsMenu

}
