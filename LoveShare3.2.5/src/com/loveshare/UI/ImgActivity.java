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

	public static int win_width;// ��Ļ���
	public static int win_height;// ��Ļ�߶�

	private SharedPreferences preferences;

	private static final int FLAG_CHOOSE = 11;
	private static final int FLAG_HANDLEBACK = 12;

	// ��ImgActivity�еĸ���LinearLayout�����Ϊ��Ա����
	private LinearLayout enhanceLinearLayout;
	private LinearLayout effectsLinearLayout;
	private LinearLayout wordLinearLayout;
	//private LinearLayout handWritingLinearLayout;
	private LinearLayout broaderLinearLayout;
	private LinearLayout rotateLinearLayout;

	// ImgActivity�е���ʾ����ҪͼƬimg_picactivity
	private ImageView imgView_picactivity;
	// ��������ImgActivity��Bitmap����
	private Bitmap imgBit = null;

	public static String imgPath = null;
	public static String savePath;

	public static BitmapFactory.Options options;
	private boolean isContinueToGetOpions = true;

	private ImgHandler handler = new ImgHandler(this) {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ImgHandler.DECODE_SUCCESS:
				// �������Ի�����ʧ
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

		//��ȡ�����ļ�ȷ�������ļ�·��
		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);
		savePath = preferences.getString("savePath", null);

		// �õ���������MainActivity�����ͼƬ·��
		imgPath = this.getIntent().getStringExtra("imgPath");
		System.out.println("3"+imgPath);

		// ������LinearLayout��Ա������ֵ
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

		// ��ͼƬ·��ȥ����һ��Bitmap����Ȼ���������Activity����ҪͼƬImageView img_picactivity��
		imgView_picactivity = (ImageView) this
				.findViewById(R.id.imgView_picactivity);

		// �õ��ֻ��Ŀ��
		WindowManager manager = (WindowManager) this
				.getSystemService(WINDOW_SERVICE);
		win_width = manager.getDefaultDisplay().getWidth();
		win_height = manager.getDefaultDisplay().getHeight();

		// ����Activity�еĸ���LinearLayout�󶨼�����
		enhanceLinearLayout.setOnTouchListener(this);
		effectsLinearLayout.setOnTouchListener(this);
		//handWritingLinearLayout.setOnTouchListener(this);
		broaderLinearLayout.setOnTouchListener(this);
		rotateLinearLayout.setOnTouchListener(this);

		options = GetOpinions.getGetOpinions(imgPath, win_width, win_height,
				null);

		// ��ʾ�ȴ��Ի���
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

						// ������ɱ��ڴ�Bitmap
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

	// ��дOnTouchListener�ӿ��е�onTouchʵ�ֶԸ���LinearLayout�����¼���ʵ��
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// ������ʱ�����������ʾ
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Intent intent = new Intent();
			switch (view.getId()) {
			// ����������enhanceLinearLayout����ת��enhanceLinearLayout��ͼƬ������������
			case R.id.enhanceLinearLayout:
				intent.setClass(this, ImgToneActivity.class);
				break;
			// ����������ImageEffectsActivity����ת��ImageEffectsActivity��ͼƬ������Ч����
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
			 * ��ͼƬ·��imgPath����Intent�����ϴ�����һ��Activity�����Ҵ�Activity����startActivity����
			 * ʵ����ת����һ��Activity
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
