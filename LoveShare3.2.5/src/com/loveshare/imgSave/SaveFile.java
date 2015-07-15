package com.loveshare.imgSave;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.loveshare.activity.R;
import com.loveshare.view.ShowCustomToast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

public class SaveFile {

	private Context context;
	private File originFile;// ��������ͼƬ�ļ�
	private String imgPath;// �����ͼƬ��·��

	private boolean isSave;// ��ʾ�����ļ��ı�־

	public SaveFile(File originfile, Context context) {
		super();
		this.originFile = originfile;// ͼƬԴ�ļ�
		this.context = context;// �����Ķ������ڴ�ӡToastʱʹ��
	}

	// ���ΪͼƬ��������ı��沢����ͼƬ������������
	public void saveFile(Bitmap myBitmap, File targetFile) throws Exception {

		byte[] data = null;
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(targetFile));
		saveImage(myBitmap, outputStream);
		finalProcessOutputStream(outputStream);
		// ��Ϊ�����ΪͼƬ�����Ա���·��Ӧ�������Ϊ�����Ŀ���ļ�·��
		imgPath = targetFile.getAbsolutePath();
		this.saveSucceed(context, targetFile);
	}

	// ���沢����ͼƬ
	public void saveFile(Bitmap myBitmap) throws Exception {

		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(originFile));
		saveImage(myBitmap, outputStream);
		finalProcessOutputStream(outputStream);

		// ��Ϊ�Ǹ���ͼƬ�����Ա����·��Ӧ����ԭ�ļ���·��
		imgPath = originFile.getAbsolutePath();
		// ��ӡ���ɹ�����ʾ
		this.saveSucceed(context, originFile);

		// ģ��һ����Ϣ ֪ͨϵͳsd�������¹�����.
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		context.sendBroadcast(intent);
	}

	// OutputStream����λ������������flush������ȫ��д���ļ��к͵���close�ر�OutputStream
	private void finalProcessOutputStream(BufferedOutputStream outputStream)
			throws IOException {
		outputStream.flush();
		outputStream.close();
	}

	// ���湦��
	private void saveImage(Bitmap myBitmap, BufferedOutputStream outputStream)
			throws IOException {

		Bitmap bitmap = Bitmap.createBitmap(myBitmap.getWidth() * 2,
				myBitmap.getHeight() * 2, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		// ����ȫ��ͼ��
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.drawBitmap(bitmap, 10, 100, null);
		canvas.restore();
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

		// ģ��һ����Ϣ ֪ͨϵͳsd�������¹�����.
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		context.sendBroadcast(intent);
	}

	// ����ɹ�����ӡ�ɹ���Ϣ
	private void saveSucceed(Context context, File saveFile) {
		ShowCustomToast.show(context,
				"ͼƬ���������,����ͼƬ·��Ϊ:" + saveFile.getAbsolutePath().toString());
		// ��ʾͼƬ�ļ��ѱ���
		isSave = true;
	}

	// ����ʧ�ܣ���ӡʧ����Ϣ
	public void saveFail() {
		ShowCustomToast.show(context, R.string.saveFailed);
	}

	// ���ñ����־
	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	// ȡ�ñ����־
	public boolean isSave() {
		return isSave;
	}

	public String getImgPath() {
		return imgPath;
	}
}
