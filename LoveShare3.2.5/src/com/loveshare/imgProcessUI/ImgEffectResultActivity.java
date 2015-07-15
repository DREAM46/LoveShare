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

	private ImageView img_process;// ������ʾͼƬ��ImageView
	private String imgPath;// ͼƬ·��
	private TextView title_processImg;// �����title
	private ImageButton save;// ���水ť
	private ImageButton share;// ����ť
	private Bitmap oriBitmap;
	private Bitmap processBitmap;// ��ЧͼƬ��ɺ��λͼ����
	
	private int saveFlag = 0;// ����ͼƬ�ı�־

	private LinearLayout proSeekBarLinearLayout;
	private TextView proImgseekBarName;// ��ЧЧ��������������
	private SeekBar proImgseekBar;// ��ЧЧ��������
	private TextView seekBarHint;//��Ч�������µ��ı�˵��
	private SaveImgListener sil;// ���水ť�ļ�����
	private ShareListener sl;// ������ť�ļ�����
	
	private String shareImgPath;// ��Ҫ���з����ͼƬ��·��
	private ImageUtil util = new ImageUtil();

	private int type;// ��Ч���������
	private boolean isEdit;//�Ƿ񱻱༭���ı�־
	
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

		shareImgPath = imgPath;// ����Ҫ�����ͼƬ��·��

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
		oriBitmap = BitmapFactory.decodeFile(imgPath, options);
		// processBitmap = ImageUtil.getRoundedCornerBitmap(oriBitmap, 40f);

		// ���ý������
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
				// ͼƬ����
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

	// ������
	private void addView(int type) {
		LinearLayout linearLayout1 = new LinearLayout(this);
		linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		
		proImgseekBarName = new TextView(this);
		proImgseekBar = new SeekBar(this);
		seekBarHint = new TextView(this);
		
		switch (type) {

			case ImgEffectsActivity.ROUNDCONCER:
				proImgseekBarName.setText("����");
				seekBarHint.append("Բ�ǻ���:"+proImgseekBar.getProgress());
				break;
			case ImgEffectsActivity.BEAM:
				proImgseekBarName.setText("ǿ��");
				seekBarHint.append("����ǿ��:"+proImgseekBar.getProgress());
				break;
			case ImgEffectsActivity.BLURIMAGE:
				proImgseekBarName.setText("�̶�");
				seekBarHint.append("ģ���̶�:"+proImgseekBar.getProgress());
				break;
				
		}
		proImgseekBar.setMax(100);
		proImgseekBar.setProgress(1);
		proImgseekBar.setOnSeekBarChangeListener(this);// Ϊ�������󶨼�����
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

	// ����ͼƬ�������
	private void SketchImg() {
		Intent intent1 = new Intent(this, ImgSketchActivity.class);
		intent1.putExtra("imgPath", imgPath);
		this.startActivity(intent1);
		this.finish();
	}

	@Override
	// ��������onKeyDown������������·��ؼ���isSaveֵΪfalse����δ����ͼƬ�ļ��Ļ���ʾ�û����б���
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, processBitmap,
				ImgActivity.class);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
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
					imgPath = sil.getImgPath();// �������˱��水ť�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
			} else
				imgPath = sl.getImgPath();// �������˷���ť��ͬʱ�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
		}
		if (saveFlag == 0)
			falseProcessMethod(backHint);
		return true;
	}

	// �԰��±�����ʾ�Ի���ķ񶨼��Ĵ���
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
			seekBarHint.setText("Բ�ǻ���:"+proImgseekBar.getProgress());
			break;
		case ImgEffectsActivity.BEAM:
			processBitmap = util.sunshine(oriBitmap, oriBitmap.getWidth(),
					oriBitmap.getHeight(), progress);
			seekBarHint.setText("����ǿ��:"+proImgseekBar.getProgress());
			break;
		case ImgEffectsActivity.BLURIMAGE:
			processBitmap = util.blurImageAmeliorate(oriBitmap, progress);
			seekBarHint.setText("ģ���̶�:"+proImgseekBar.getProgress());
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
