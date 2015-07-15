package com.loveshare.imgProcessUtil;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.loveshare.UI.ImgActivity;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.view.CustomAlertDialog;
import com.loveshare.view.ShowCustomToast;

public class BackHint {

	private Context context;
	private String imgPath;
	private Bitmap bitmap;
	private boolean isSave;
	private Class class1;

	private CustomAlertDialog dialog;

	public BackHint(Context context, String imgPath, Bitmap bitmap, Class class1) {
		super();
		this.context = context;
		this.imgPath = imgPath;
		this.bitmap = bitmap;
		this.class1 = class1;
	}

	// �����Ի��������û�����
	public void showAlertDialog() {

		dialog = new CustomAlertDialog(context);
		dialog.setMsg("��ȷ�������ͼƬ���޸�?")
				.setPositiveOnClickListener(new MyPositiveListener())
				.setNegativeOnClickListener(new MyNegativeListener())
				.setNeutralGone().show();
	}

	// �����ǰ�ť�Ĵ���
	private class MyPositiveListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			File file = new CreateOriginFile(context).createOriginFile(
					new File(imgPath), ImgActivity.savePath);
			try {
				// ����ͼƬ
				new SaveFile(file, context).saveFile(bitmap);
				// �������־��Ϊ��
				isSave = true;
				// ��·������Ϊ�±�����޸Ĺ���ͼƬ�ļ��������������ͼƬ�������༭
				imgPath = file.getAbsolutePath();
			} catch (Exception e) {
				//Toast.makeText(context, "����ʧ��", Toast.LENGTH_LONG).show();
				ShowCustomToast.show(context, "����ʧ��");
				e.printStackTrace();
			} finally {
				// ���·�ť���ص�ԭ���Ľ��棬����ʾ����֮���ͼƬ
				// ʵ�ֽ������ת
				Intent intent = new Intent(context, class1);
				intent.putExtra("imgPath", imgPath);
				context.startActivity(intent);
				isSave = false;
				dialog.dismiss();

			}
		}

	}

	public boolean getSave() {
		return isSave;
	}

	// ���¡��񡱰�ť�Ĵ���
	public class MyNegativeListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// ���·�ť���ص�ԭ���Ľ��棬����ʾδ����֮ǰ��ͼƬ
			Intent intent = new Intent(context, class1);
			intent.putExtra("imgPath", imgPath);
			context.startActivity(intent);
			dialog.dismiss();
		}

	}

	public String getImgPath() {
		return imgPath;
	}

	// ������ת���
	public void gotoActivity(String imgPath1) {
		Intent intent = new Intent(context, class1);
		intent.putExtra("imgPath", imgPath1);
		context.startActivity(intent);
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
