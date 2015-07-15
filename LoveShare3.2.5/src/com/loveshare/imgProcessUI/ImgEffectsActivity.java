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

	// Ϊ��Activity�е�ÿ��LinearLayout�������ɳ�Ա����
	private LinearLayout roundConcer;
	private LinearLayout oldremeber;
	private LinearLayout sharpen;
	private LinearLayout beam;
	private LinearLayout sketch;
	private LinearLayout negative;
	private LinearLayout rilievo;

	private ImageView img_process;// ������Activity������ʾͼƬ��ImageView
	private String imgPath;// ͼƬ��·��

	private Bitmap originBitmap;// ��������imgView_processActivity��Bitmap����
	private Bitmap processBitmap;// ��ЧͼƬ��ɺ��λͼ����

	private ImageView img_roundConcer;
	private ImageView img_oldremeber;
	private ImageView img_sharpen;
	private ImageView img_beam;
	private ImageView img_rilievo;
	private ImageView img_negative;

	private RelativeLayout proSeekBarLinearLayout;
	private SeekBar proImgseekBar;// ��ЧЧ��������
	private TextView seekBarHint;// ��Ч�������µ��ı�˵��

	private ImageButton save;
	private ImageButton share;

	private String shareImgPath;// ��Ҫ���з����ͼƬ��·��

	private ImageUtil util = new ImageUtil();

	private int type;// ��Ч���������
	private boolean isEdit;// �Ƿ񱻱༭���ı�־

	private SaveImgListener sil;// ���水ť�ļ�����
	private ShareListener sl;// ������ť�ļ�����
	private int saveFlag = 0;// ����ͼƬ�ı�־

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
				title_processImg.setText("Բ��ģʽ");
				break;
			case R.id.beam:
				ImgEffectsActivity.this.addHint(img_beam);
				addView(what);
				title_processImg.setText("����ģʽ");
				break;
			case R.id.oldremeber:
				ImgEffectsActivity.this.addHint(img_oldremeber);
				setViewGone();
				isEdit = true;
				title_processImg.setText("����ģʽ");
				sil.setSave(false);
				break;
			case R.id.sharpen:
				ImgEffectsActivity.this.addHint(img_sharpen);
				setViewGone();
				isEdit = true;
				title_processImg.setText("��ģʽ");
				sil.setSave(false);
				break;
			case R.id.negative:
				ImgEffectsActivity.this.addHint(img_negative);
				setViewGone();
				isEdit = true;
				title_processImg.setText("��Ƭģʽ");
				sil.setSave(false);
				break;
			case R.id.rilievo:
				ImgEffectsActivity.this.addHint(img_rilievo);
				setViewGone();
				isEdit = true;
				title_processImg.setText("����ģʽ");
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
		 * ��intent�õ�������һ��Activity��������·���ַ�������������Bitmap����imgBit
		 * �����ɵ�imgBit����imgView_processActivity����ʾͼƬ
		 */
		imgPath = this.getIntent().getStringExtra("imgPath");
		System.out.println("1" + imgPath);
		shareImgPath = imgPath;// ����Ҫ�����ͼƬ��·��

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
		// Ϊ����LinearLayout�󶨼�����
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

	// ��дOnTouchListener�ӿ��е�onTouchʵ�ֶԸ���LinearLayout�����¼���ʵ��
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// ������ʱ�����������ʾ
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
						// Ϊ�����������ͼƬ�󶨼�������������ͼƬ���������󱣴�ͼƬ
						
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

	// ������
	private void addView(int type) {
		proImgseekBar.setProgress(0);
		proImgseekBar.setVisibility(View.VISIBLE);
		seekBarHint.setVisibility(View.VISIBLE);

		switch (type) {

		case R.id.roundConcer:
			seekBarHint.setText("   Բ�ǻ���:" + proImgseekBar.getProgress());
			break;
		case R.id.beam:
			seekBarHint.setText("   ����ǿ��:" + proImgseekBar.getProgress());
			break;
		/*
		 * case R.id.blurImage: seekBarHint.setText("   ģ���̶�:" +
		 * proImgseekBar.getProgress()); break;
		 */
		}
		// Ϊ�������󶨼�����
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
			seekBarHint.setText("Բ�ǻ���:" + proImgseekBar.getProgress());
		} else if (type == R.id.beam) {
			seekBarHint.setText("����ǿ��:" + proImgseekBar.getProgress());
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
		// һ���û������������ЧͼƬ�Ľ��棬����ζ��ͼƬ�Ѿ����������ˣ�
		// ��ͼƬδ���棬����ʾ�û�����ͼƬ
		// �ж��Ƿ��Ѿ������ڷ����ͬʱ�������������ͼƬ�ļ�
		if (!sl.isSave()) {
			// �ж��Ƿ��±��水ť�������������ͼƬ�ļ�
			if (!sil.isSave()) {
				if (isEdit) {
					// ��������ʾ�Ի���,��ʾ�û��Ƿ񱣴水ť�����������ͼƬ�ļ�
					backHint.showAlertDialog();
					/*
					 * �����û��Ƿ�ѡ���˱��棬����isEdit��Ϊfalse�� �Է���һ�ν������������������������ʾ�û�����
					 */
					// ��·������Ϊ�±�����޸Ĺ���ͼƬ�ļ��������������ͼƬ�������༭
					imgPath = backHint.getImgPath();

					saveFlag = 1;
				}
			} else
				// �������˱��水ť�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
				imgPath = sil.getImgPath();
		} else
			// �������˷���ť��ͬʱ�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
			imgPath = sl.getImgPath();
		System.out.println("2================" + imgPath);
		if (saveFlag == 0) {
			backHint.gotoActivity(imgPath);
			this.finish();
		}

	}
}
