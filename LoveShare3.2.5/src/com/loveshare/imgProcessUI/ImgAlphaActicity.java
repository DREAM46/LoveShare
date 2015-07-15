package com.loveshare.imgProcessUI;

import java.io.File;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.activity.R;
import com.loveshare.activity.R.id;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.RatingBarListener;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveAlertDialogListener;
import com.loveshare.imgSave.SaveAsAlertDialogListener;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.imgSave.SaveImgListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

public class ImgAlphaActicity extends Activity implements OnClickListener {

	private static final int SHARE = 0;

	private RatingBar ratingBar = null;// ����ͼƬ͸���̶ȵ�����
	private ImageView imageView = null;// ������ʾͼƬ��ImageView
	private Bitmap picBit = null;// ImageView��Bitmap
	private String imgPath = null;// ͼƬ�ļ�·��
	private Bitmap saveBit = null;// �޸ĺ󼴽����ڱ���ͼƬ��Bitmap
	private Intent intent = null;
	private ImageButton save = null;// ���水ť
	private ImageButton share = null;// ����ť
	private String shareImgPath = null;// ��Ҫ���з����ͼƬ��·��
	private File originFile;// ����ͼƬ�󱣴�ͼƬ����·��
	private SaveFile sf;// ���ڱ����ļ�����
	private boolean isEdit;

	private RatingBarListener listener;
	private CreateOriginFile createOriginFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgalpha);

		// �õ�ImgeView��picBit
		imageView = (ImageView) this.findViewById(R.id.img_alphaActivity);
		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);
		ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);

		// ΪImageView����ͼƬ
		Intent intent = this.getIntent();
		imgPath = intent.getStringExtra("imgPath");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
		picBit = BitmapFactory.decodeFile(imgPath, options);
		imageView.setImageBitmap(picBit);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

		listener = new RatingBarListener(this, new BitmapDrawable(picBit),
				ratingBar, imageView);

		shareImgPath = imgPath;// ����Ҫ�����ͼƬ��·��

		// Ϊ���ǽ��������ü�����
		ratingBar.setOnRatingBarChangeListener(listener);

		// Ϊ���水ť�󶨼�����
		save.setOnClickListener(this);
		share.setOnClickListener(this);

		createOriginFile = new CreateOriginFile(this);
		originFile = createOriginFile.createOriginFile(new File(imgPath),
				ImgActivity.savePath);
		sf = new SaveFile(originFile, this);// �����������ͼƬ���ļ�

		/*
		 * ��ΪMyRatingBarChangeListener.saveBit���ֵ����ֹ������Ϊ����δ�Ķ������������������
		 * ����MyRatingBarChangeListener��onClick����δ�ܻص�ʹ��saveBit��ֵΪ�մӶ�ʹ�ñ��治�ɹ�
		 */
		listener.setSaveBit(picBit);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.save:
			// �����Ի���ѯ���Ƿ񱣴�ͼƬ���û�����ѡ��"����"��"���Ϊ"���Լ�"��"
		/*	new AlertDialog.Builder(this)
					.setTitle("��ܰ����")
					.setIcon(R.drawable.logo1)
					.setMessage("���Ƿ�Ҫ��������ͼƬ��?")
					.setPositiveButton(
							"����",
							new SaveAlertDialogListener(this, sf, listener
									.getSaveBit()))
					.setNeutralButton(
							"���Ϊ",
							new SaveAsAlertDialogListener(this, sf, listener
									.getSaveBit()))
					.setNegativeButton("��", null).show();*/
			break;

		case R.id.share:

			// ʵ�ַ���ͼƬ��ͬʱĬ�ϱ���ͼƬ
			originFile = createOriginFile.createOriginFile(new File(imgPath),
					ImgActivity.savePath);
			try {
				sf.saveFile(listener.getSaveBit());
				// ���������ļ���ͬʱ���ļ��ı����־��Ϊtrue
				sf.setSave(true);
			} catch (Exception e) {
				sf.saveFail();
				e.printStackTrace();
			}
			Intent intent = new Intent(this, ShareActivity.class);

			// �����Ӧ���Ǵ�����ͼƬ�����Խ�shareImgPath��Ϊ������ͼƬ��·��
			shareImgPath = originFile.getAbsolutePath();
			intent.putExtra("shareImgPath", shareImgPath);

			this.startActivity(intent);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, listener.getSaveBit(),
				ImgActivity.class);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!sf.isSave()) {
				if (listener.isEdit()) {
					backHint.showAlertDialog();
					imgPath = originFile.getAbsolutePath();
					/*
					 * �����û��Ƿ�ѡ���˱��棬����isEdit��Ϊfalse�� �Է���һ�ν������������������������ʾ�û�����
					 */
					isEdit = false;
				} else {
					isEdit = false;
					imgPath = backHint.getImgPath();
					gotoImgActivity();
				}
			} else {
				// ����������е����˵���������ͼƬ�Ѿ����棬��ͼƬ·����Ϊ�µı�����·��
				imgPath = sf.getImgPath();
				gotoImgActivity();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void gotoImgActivity() {
		Intent intent = new Intent(this, ImgActivity.class);
		intent.putExtra("imgPath", imgPath);
		this.startActivity(intent);
		this.finish();
	}

}
