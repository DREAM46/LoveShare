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
	
	// �����ؼ������µ�����һ����дд��
		@Override
		public void onBackPressed() {
			BackHint backHint = new BackHint(this, imgPath, saveBitmap,
					ImgActivity.class);
			int Saveflag = 0;// �Ƿ��Ѿ�����ı�־
			isSave = sil.isSave();

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
				backHint.gotoActivity(imgPath);
				this.finish();
			}

		}
}
