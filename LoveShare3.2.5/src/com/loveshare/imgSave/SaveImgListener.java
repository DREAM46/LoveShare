package com.loveshare.imgSave;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.loveshare.activity.R;
import com.loveshare.view.CustomAlertDialog;

public class SaveImgListener implements OnClickListener {

	private Context context;
	private File originFile;
	private Bitmap myBitmap;
	private SaveFile sf;
	private File targetFile;
	private boolean isSave;

	public SaveImgListener(Context context, File originFile, Bitmap myBitmap) {
		super();
		this.context = context;
		this.originFile = originFile;
		this.myBitmap = myBitmap;
		this.targetFile = targetFile;
		sf = new SaveFile(originFile, context);
	}

	// �����ʱ�������Ի���ѯ���Ƿ񱣴桢���ΪͼƬ�ļ�
	@Override
	public void onClick(View arg0) {

		/*
		 * new AlertDialog.Builder(context).setTitle("�����ļ�") .setTitle("��ܰ����")
		 * .setIcon(R.drawable.logo1) .setMessage("���Ƿ�Ҫ��������ͼƬ��?")
		 * .setPositiveButton("����", new
		 * SaveAlertDialogListener(context,sf,myBitmap))
		 * .setNeutralButton("���Ϊ", new
		 * SaveAsAlertDialogListener(context,sf,myBitmap))
		 * .setNegativeButton("��", null).show();
		 */
		CustomAlertDialog dialog = new CustomAlertDialog(context)
				.setTitleText("�����ļ�")
				.setMsg("���Ƿ�Ҫ��������ͼƬ��?")
				.setPositiveText("����")
				.setNeutralText("���Ϊ")
				.setNegativeText("��");
		
				dialog.setPositiveOnClickListener(
						new SaveAlertDialogListener(context, sf, myBitmap,dialog))
				.setNeutralOnClickListener(
						new SaveAsAlertDialogListener(context, sf, myBitmap,dialog))
				.show();
		System.out.println("originFile:" + originFile.getAbsolutePath());
	}

	public boolean isSave() {
		return sf.isSave();
	}

	public SaveFile getSf() {
		return sf;
	}

	public void setSave(boolean isSave) {
		this.getSf().setSave(isSave);
	}

	public String getImgPath() {
		return sf.getImgPath();
	}

	public void setMyBitmap(Bitmap myBitmap) {
		this.myBitmap = myBitmap;
	}
}