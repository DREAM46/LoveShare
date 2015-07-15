package com.loveshare.share;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.ShareActivity;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveFile;
import com.loveshare.view.CustomAlertDialog;

public class ShareListener implements OnClickListener {

	private Context context;
	private Bitmap bitmap;
	private String imgPath;
	private SaveFile sf;
	private CustomAlertDialog dialog;

	public ShareListener(Context context, Bitmap bitmap, String imgPath) {
		this.context = context;
		this.bitmap = bitmap;
		this.imgPath = imgPath;
	}

	@Override
	public void onClick(View v) {

		dialog = new CustomAlertDialog(context);
		dialog.setTitleText("����ͼƬ").setMsg("���Ƿ�Ҫ��������ͼƬ��?")
				.setPositiveText("����")
				.setPositiveOnClickListener(new PositiveOnClickListener())
				.setNegativeText("ȡ��").setNeutralGone().show();

	}

	private class PositiveOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// ʵ�ַ���ͼƬ��ͬʱĬ�ϱ���ͼƬ
			File originFile1 = new CreateOriginFile(context).createOriginFile(
					new File(imgPath), ImgActivity.savePath);
			Log.v("loveshare", "ParentFile" + new File(imgPath).getParentFile());
			Log.v("loveshare", "ImgActivity.imgPath" + ImgActivity.imgPath);
			Log.v("loveshare", "originFile1" + originFile1.getAbsolutePath());
			sf = new SaveFile(originFile1, context);
			String shareImgPath;
			try {
				sf.saveFile(bitmap);
				sf.setSave(true);
			} catch (Exception e) {
				sf.saveFail();
				e.printStackTrace();
			}

			Intent intent = new Intent(context, ShareActivity.class);

			// �����Ӧ���Ǵ�����ͼƬ�����Խ�shareImgPath��Ϊ������ͼƬ��·��
			shareImgPath = originFile1.getAbsolutePath();
			imgPath = shareImgPath;
			intent.putExtra("shareImgPath", shareImgPath);
			context.startActivity(intent);

			dialog.dismiss();
		}

	};

	public boolean isSave() {
		if (sf != null)
			return sf.isSave();
		else
			return false;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
