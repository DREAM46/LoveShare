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
	private ImageView mImageView;// ������ʾͼƬ��ImgView
	private Bitmap mBitmap; // ��������mImageView��Bitmap
	private Bitmap handleBitmap;// ��������õ���Bitmap����

	private ImageButton save;// ���水ť
	private ImageButton share;// ����ť

	private File originFile;// ���ڱ����ļ���File����
	private boolean isSave;// �������ֵ�Ǳ�ʾͼƬ�Ƿ��Ѿ�����

	private String shareImgPath;// ����ͼƬ��·��
	private String imgPath;// ͼƬ·��

	private SaveImgListener sil;// ���水ť������
	private ShareListener sl;// ����ť������

	private boolean isEdit;// ��ʾ�Ƿ��޸�ͼƬ����booleanֵ

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
				Toast.makeText(this.getContext(), "����ʧ��", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};

	private CreateOriginFile createOriginFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgtone);
		// ��Activity���г�ʼ������
		init();
		isSave = false;
	}

	private void init() {

		title_processImg = (TextView) this.findViewById(R.id.title_processImg);
		// �õ���Intent��������·���ַ���������Bitmap��������ɴ������ڱ���ͼƬ�����File����
		imgPath = this.getIntent().getStringExtra("imgPath");
		shareImgPath = imgPath;// ͼƬ�ķ���·��

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

		// Ϊ�տ�����ҳ��ʱδ��������ͼƬ�󶨼�������������ͼƬδ������ʱ����ͼƬ
		prepareForShareAndSave(handleBitmap);
		// ����������
		dialog = CreateCustomProgressDialog
				.createCustomProgressDialog(ImgToneActivity.this);
	}

	private void prepareForShareAndSave(Bitmap handleBitmap1) {
		sil = new SaveImgListener(this, originFile, handleBitmap);
		sl = new ShareListener(this, handleBitmap1, imgPath);
		save.setOnClickListener(sil);
		share.setOnClickListener(sl);
	}

	// ��������ͬ�Ĺ�����ʱ�������ͼƬ���в�ͬ�Ĵ���
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		final int flag = (Integer) seekBar.getTag();
		final int progress1 = progress;
		
		// ��ʾ������
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
					// ����ʾ�Ƿ��޸�ͼƬ����booleanֵ��Ϊtrue����ʾ�û��Ѿ��޸Ĺ�ͼƬ��
					isEdit = true;
					// �õ��������ͼƬBitmap
					handleBitmap = mToneLayer.handleImage(mBitmap, flag);
					// Ϊ�����������ͼƬ�󶨼�������������ͼƬ���������󱣴�ͼƬ
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
	// ��������onKeyDown������������·��ؼ���isSaveֵΪfalse����δ����ͼƬ�ļ��Ļ���ʾ�û����б���
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, handleBitmap,
				ImgActivity.class);
		int Saveflag = 0;// �Ƿ��Ѿ�����ı�־
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �����·��ؼ���ͼƬ�Ѿ����Ĺ�ʱ���򵯳�������ʾ����ʾ�û������޸Ĺ���ͼƬ
			// �ж��Ƿ��Ѿ������ڷ����ͬʱ�������������ͼƬ�ļ�
			if (!sl.isSave()) {
				// �ж��Ƿ��±��水ť�������������ͼƬ�ļ�
				if (!sil.isSave()) {
					// �ж��Ƿ�������ͼƬ����û����������Ҳû�е����Ի�����ʾ�û�����ı�Ҫ
					if (isEdit) {

						backHint.showAlertDialog();
						/*
						 * �����û��Ƿ�ѡ���˱��棬����isEdit��Ϊfalse�� �Է���һ�ν������������������������ʾ�û�����
						 */
						isEdit = false;
						// ��·������Ϊ�±�����޸Ĺ���ͼƬ�ļ��������������ͼƬ�������༭
						imgPath = backHint.getImgPath();
						Saveflag = 1;
					}
				} else
					// �������˱��水ť�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
					imgPath = sil.getImgPath();
			} else
				// �������˷���ť��ͬʱ�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
				imgPath = sl.getImgPath();

			if (Saveflag == 0) {
				falseProcessMethod(backHint);
			}

		}
		return true;
	}

	// �԰��±�����ʾ�Ի���ķ񶨼��Ĵ���
	private void falseProcessMethod(BackHint backHint) {
		backHint.gotoActivity(imgPath);
		this.finish();
	}

}
