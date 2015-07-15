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

	// 当点击时，弹出对话框，询问是否保存、另存为图片文件
	@Override
	public void onClick(View arg0) {

		/*
		 * new AlertDialog.Builder(context).setTitle("保存文件") .setTitle("温馨提醒")
		 * .setIcon(R.drawable.logo1) .setMessage("您是否要保存这张图片吗?")
		 * .setPositiveButton("保存", new
		 * SaveAlertDialogListener(context,sf,myBitmap))
		 * .setNeutralButton("另存为", new
		 * SaveAsAlertDialogListener(context,sf,myBitmap))
		 * .setNegativeButton("否", null).show();
		 */
		CustomAlertDialog dialog = new CustomAlertDialog(context)
				.setTitleText("保存文件")
				.setMsg("您是否要保存这张图片吗?")
				.setPositiveText("保存")
				.setNeutralText("另存为")
				.setNegativeText("否");
		
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