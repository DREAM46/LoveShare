package com.loveshare.puzzle;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;

public class PingPictures extends Activity {
	// int count;

	private ImageView iv;

	private ImageButton share = null;
	private ImageButton save = null;
	private String imgPath;

	private Bitmap bmp;// ƴͼ���ͼƬλͼ����

	private SaveImgListener sil;// ����ļ�����
	private ShareListener sl;// ����ļ�����

	private boolean isEdit;// �Ƿ񾭹��༭�ı�־
	private boolean isSave;// �Ƿ��Ѿ������ͼƬ�ı�־

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_puzzle);
		setContentView(R.layout.activity_imgadd);
		iv = (ImageView) findViewById(R.id.iv_image);
		System.out.println("ok");
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		ArrayList<String> pingImgPaths = b.getStringArrayList("pingImgPaths");
		Object[] obj = pingImgPaths.toArray(new String[1]);
		String[] paths = (String[]) obj;
		// String[] pingImgPaths = (String[])
		// bd.getCharSequenceArray("pingImgPaths");
		// System.out.println(pingImgPaths.size());
		// count = pingImgPaths.size();
		bmp = pingPictures(paths);
		// iv.setImageBitmap(bmp);
		// Bitmap bmp = pingPictures(new
		// String[]{"/mnt/sdcard/showPictures/11.jpg","/mnt/sdcard/showPictures/22.jpg","/mnt/sdcard/showPictures/11.jpg"});
		iv.setImageBitmap(bmp);

		imgPath = createImgPath();

		share = (ImageButton) this.findViewById(R.id.btn_share1);
		save = (ImageButton) this.findViewById(R.id.btn_save1);

		sl = new ShareListener(this, bmp, imgPath);
		share.setOnClickListener(sl);

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath), ImgActivity.imgPath);
		sil = new SaveImgListener(this, originFile, bmp);
		save.setOnClickListener(sil);

	}

	// �����µ�ͼƬ·��
	private String createImgPath() {
		String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File dir = null;

		// �ж��ֻ��Ƿ����ڴ濨������У���ͼƬ�ļ������ڴ濨�У����û�У�������ֻ��Դ��ڴ�ռ���
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + this.getString(R.string.imgPath));
		} else {
			// �浽�ֻ��Դ����ڴ�ռ���
		}
		dir.mkdir();
		File targetFile = new File(dir, name);
		return targetFile.getAbsolutePath();
	}

	private Bitmap pingPictures(String[] imagePaths) {

		/*
		 * BitmapFactory.Options options = new BitmapFactory.Options();
		 * options.inSampleSize = 2;//ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ Bitmap bitmap =
		 * BitmapFactory.decodeStream(cr .openInputStream(uri), null, options);
		 * preview.setImageBitmap(bitmap);
		 */
		int height = 0, width = 0;
		System.out.println("imagePaths.length :" + imagePaths.length);
		Bitmap[] bitmaps = new Bitmap[imagePaths.length];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
		for (int i = 0; i < imagePaths.length; i++) {
			bitmaps[i] = BitmapFactory.decodeFile(imagePaths[i], options);
			// bitmaps[i] = BitmapFactory.decodeFile(imagePaths[i]);

		}

		for (int i = 0; i < imagePaths.length; i++) {
			if (bitmaps[i].getWidth() > width) {
				width = bitmaps[i].getWidth();
			}
			height += bitmaps[i].getHeight();
		}

		Bitmap result = Bitmap.createBitmap(width, height,
				bitmaps[0].getConfig());
		Paint paint = new Paint();
		Canvas canvas = new Canvas(result);

		for (int i = 0, y = 0; i < imagePaths.length; i++) {
			// y = 0;
			System.out.println("y:   " + y);
			// canvas.drawBitmap(bitmaps[i], 0, y, paint);
			canvas.drawBitmap(bitmaps[i], (width - bitmaps[i].getWidth()) / 2,
					y, paint);

			y += bitmaps[i].getHeight();
		}

		isEdit = true;
		return result;
	}

/*	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:

			saveBitmap();
			break;
		case R.id.share:
			saveBitmap();
			Intent intent = new Intent(this, ShareActivity.class);
			intent.putExtra("shareImgPath", imgPath);
			this.startActivity(intent);
			break;
		}
	}*/

	private void saveBitmap() {
		String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File dir = null;

		// �ж��ֻ��Ƿ����ڴ濨������У���ͼƬ�ļ������ڴ濨�У����û�У�������ֻ��Դ��ڴ�ռ���
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + this.getString(R.string.imgPath));
		} else {
			// �浽�ֻ��Դ����ڴ�ռ���
		}
		dir.mkdir();
		imgPath = dir + "/" + name;
		try {
			new SaveFile(new File(imgPath), this).saveFile(bmp);
		} catch (Exception e) {
			// ������ʧ�ܣ�����Toast��ʾ�û���
			Toast.makeText(this, R.string.saveFailed, Toast.LENGTH_LONG).show();

			e.printStackTrace();
		}
	}

	// �����ؼ������µ�����һ����дд��
	@Override
	public void onBackPressed() {
		BackHint backHint = new BackHint(this, imgPath, bmp, ImgActivity.class);
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
