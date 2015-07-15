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
	private File originFile;// 传过来的图片文件
	private String imgPath;// 保存的图片的路径

	private boolean isSave;// 表示保存文件的标志

	public SaveFile(File originfile, Context context) {
		super();
		this.originFile = originfile;// 图片源文件
		this.context = context;// 上下文对象，用于打印Toast时使用
	}

	// 另存为图片，与下面的保存并覆盖图片方法构成重载
	public void saveFile(Bitmap myBitmap, File targetFile) throws Exception {

		byte[] data = null;
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(targetFile));
		saveImage(myBitmap, outputStream);
		finalProcessOutputStream(outputStream);
		// 因为是另存为图片，所以保存路径应该是另存为保存的目标文件路径
		imgPath = targetFile.getAbsolutePath();
		this.saveSucceed(context, targetFile);
	}

	// 保存并覆盖图片
	public void saveFile(Bitmap myBitmap) throws Exception {

		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(originFile));
		saveImage(myBitmap, outputStream);
		finalProcessOutputStream(outputStream);

		// 因为是覆盖图片，所以保存的路径应该是原文件的路径
		imgPath = originFile.getAbsolutePath();
		// 打印保成功的提示
		this.saveSucceed(context, originFile);

		// 模拟一个消息 通知系统sd卡被重新挂载了.
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		context.sendBroadcast(intent);
	}

	// OutputStream的首位处理，即调用其flush将缓存全部写入文件中和调用close关闭OutputStream
	private void finalProcessOutputStream(BufferedOutputStream outputStream)
			throws IOException {
		outputStream.flush();
		outputStream.close();
	}

	// 保存功能
	private void saveImage(Bitmap myBitmap, BufferedOutputStream outputStream)
			throws IOException {

		Bitmap bitmap = Bitmap.createBitmap(myBitmap.getWidth() * 2,
				myBitmap.getHeight() * 2, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		// 保存全部图层
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.drawBitmap(bitmap, 10, 100, null);
		canvas.restore();
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

		// 模拟一个消息 通知系统sd卡被重新挂载了.
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		context.sendBroadcast(intent);
	}

	// 保存成功，打印成功信息
	private void saveSucceed(Context context, File saveFile) {
		ShowCustomToast.show(context,
				"图片保存完毕了,保存图片路径为:" + saveFile.getAbsolutePath().toString());
		// 表示图片文件已保存
		isSave = true;
	}

	// 保存失败，打印失败信息
	public void saveFail() {
		ShowCustomToast.show(context, R.string.saveFailed);
	}

	// 设置保存标志
	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	// 取得保存标志
	public boolean isSave() {
		return isSave;
	}

	public String getImgPath() {
		return imgPath;
	}
}
