package com.loveshare.imgProcessUI;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loveshare.UI.CloudActivity;
import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.GetOpinions;
import com.loveshare.imgProcessUtil.ImgAddBroader;
import com.loveshare.imgProcessUtil.ImgHandler;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;
import com.loveshare.view.CreateCustomProgressDialog;
import com.loveshare.view.CustomProgressDialog;

public class ImgBroaderActivity extends Activity implements OnClickListener {

	private ImageView img_broader;

	private ImageButton btn_save;
	private ImageButton btn_share;

	private ImageView img_broader1;
	private ImageView img_broader2;
	private ImageView img_broader3;
	private ImageView img_broader4;
	private ImageView img_broader5;
	private ImageView img_broader6;
	private ImageView img_broader7;
	private ImageView img_broader8;
	private ImageView img_broader9;

	private HorizontalScrollView scroll_border_beautiful;

	private String imgPath;
	private Bitmap imgBit;
	private Bitmap saveBitmap;

	private boolean isSave;
	private static boolean isEdit;

	private SaveImgListener sil;
	private ShareListener sl;
	private ImageView[] imageViews;

	private ImgHandler handler = new ImgHandler(this) {
		public void handleMessage(android.os.Message msg) {
			System.out.println("ImgBroaderActivity");
			SetImageView(msg.what);
			dialog.dismiss();
		};
	};

	private int lastCommonOpreation;
	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgbroader);

		init();
		setOnCliclkListener();

		imgPath = this.getIntent().getStringExtra("imgPath");
		img_broader = (ImageView) this.findViewById(R.id.img_broader);

		imgBit = BitmapFactory.decodeFile(imgPath, ImgActivity.options);
		
		img_broader.setImageBitmap(imgBit);

		saveBitmap = imgBit;
		sl = new ShareListener(this, saveBitmap, imgPath);
		btn_share.setOnClickListener(sl);

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath), ImgActivity.savePath);
		sil = new SaveImgListener(this, originFile, saveBitmap);
		btn_save.setOnClickListener(sil);
		imageViews = new ImageView[] { img_broader1, img_broader2,
				img_broader3, img_broader4, img_broader5, img_broader5,
				img_broader6, img_broader7, img_broader8, img_broader9, };

		// ��ʾ�ȴ��Ի���
		dialog = CreateCustomProgressDialog.createCustomProgressDialog(this);
	}

	private void setOnCliclkListener() {
		img_broader1.setOnClickListener(this);
		img_broader2.setOnClickListener(this);
		img_broader3.setOnClickListener(this);
		img_broader4.setOnClickListener(this);
		img_broader5.setOnClickListener(this);
		img_broader6.setOnClickListener(this);
		img_broader7.setOnClickListener(this);
		img_broader8.setOnClickListener(this);
		img_broader9.setOnClickListener(this);
	}

	private void init() {
		scroll_border_beautiful = (HorizontalScrollView) this
				.findViewById(R.id.scroll_border_beautiful);
		img_broader = (ImageView) this.findViewById(R.id.img_broader);

		img_broader1 = (ImageView) this.findViewById(R.id.img_broader1);
		img_broader2 = (ImageView) this.findViewById(R.id.img_broader2);
		img_broader3 = (ImageView) this.findViewById(R.id.img_broader3);
		img_broader4 = (ImageView) this.findViewById(R.id.img_broader4);
		img_broader5 = (ImageView) this.findViewById(R.id.img_broader5);
		img_broader6 = (ImageView) this.findViewById(R.id.img_broader6);
		img_broader7 = (ImageView) this.findViewById(R.id.img_broader7);
		img_broader8 = (ImageView) this.findViewById(R.id.img_broader8);
		img_broader9 = (ImageView) this.findViewById(R.id.img_broader9);

		btn_share = (ImageButton) this.findViewById(R.id.btn_share);
		btn_save = (ImageButton) this.findViewById(R.id.btn_save);
	}

	// �����ӱ߿�
	@Override
	public void onClick(View v) {

		final View view = v;
		final int id = view.getId();
		
		if(id == lastCommonOpreation){
			
		}
		else{
			dialog = CreateCustomProgressDialog
					.createCustomProgressDialog(ImgBroaderActivity.this);
			dialog.show();
			lastCommonOpreation = id;
		new Thread() {
			public void run() {
				Message message = new Message();
				message.what = id;
				int res = 0;
				switch (id) {
				case R.id.img_broader1:
					res = R.drawable.img_broader1;
					break;
				case R.id.img_broader2:
					res = R.drawable.img_broader2;
					break;
				case R.id.img_broader3:
					res = R.drawable.img_broader3;
					break;
				case R.id.img_broader4:
					res = R.drawable.img_broader4;
					break;
				case R.id.img_broader5:
					res = R.drawable.img_broader5;
					break;
				case R.id.img_broader6:
					res = R.drawable.img_broader6;
					break;
				case R.id.img_broader7:
					res = R.drawable.img_broader7;
					break;
				case R.id.img_broader8:
					res = R.drawable.img_broader8;
					break;
				case R.id.img_broader9:
					res = R.drawable.img_broader9;
					break;
				}

				saveBitmap = ImgAddBroader.addBroader(ImgBroaderActivity.this,
						imgBit, res);
				handler.sendMessage(message);
			};
		}.start();
		}
	}

	private void SetImageView(int viewID) {
		switch (viewID) {
		case R.id.img_broader1:
			addBroader(R.drawable.img_broader1, img_broader1);
			break;
		case R.id.img_broader2:
			addBroader(R.drawable.img_broader2, img_broader2);
			break;
		case R.id.img_broader3:
			addBroader(R.drawable.img_broader3, img_broader3);
			break;
		case R.id.img_broader4:
			addBroader(R.drawable.img_broader4, img_broader4);
			break;
		case R.id.img_broader5:
			addBroader(R.drawable.img_broader5, img_broader5);
			break;
		case R.id.img_broader6:
			addBroader(R.drawable.img_broader6, img_broader6);
			break;
		case R.id.img_broader7:
			addBroader(R.drawable.img_broader7, img_broader7);
			break;
		case R.id.img_broader8:
			addBroader(R.drawable.img_broader8, img_broader8);
			break;
		case R.id.img_broader9:
			addBroader(R.drawable.img_broader9, img_broader9);
			break;
		}
	}

	/*
	 * private void process(int res, ImageView imgColor) { saveBitmap =
	 * ImgAddBroader.addBigFrame(this, imgBit, res); isEdit = true;
	 * img_broader.setImageBitmap(null); img_broader.setImageBitmap(saveBitmap);
	 * addHint(); imgColor.setBackgroundResource(R.drawable.bg_broader); }
	 */

	private void addBroader(int res, ImageView broader) {
		// Ϊ�˷�ֹ�߿��Ч���ص�ͼƬ���Ƚ�img_broader�е�ͼƬ���
		img_broader.setImageBitmap(null);
		isEdit = true;
		img_broader.setImageBitmap(saveBitmap);
		sil.setMyBitmap(saveBitmap);
		sl.setBitmap(saveBitmap);
		sil.setSave(false);
		addHint();
		broader.setBackgroundResource(R.drawable.bg_broader);
	}

	private void addHint() {

		for (int i = 0; i < imageViews.length; i++) {
			imageViews[i].setBackgroundDrawable(null);
		}
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