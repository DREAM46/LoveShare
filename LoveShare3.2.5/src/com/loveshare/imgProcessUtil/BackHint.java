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

	// 弹出对话框，提醒用户保存
	public void showAlertDialog() {

		dialog = new CustomAlertDialog(context);
		dialog.setMsg("你确定保存对图片的修改?")
				.setPositiveOnClickListener(new MyPositiveListener())
				.setNegativeOnClickListener(new MyNegativeListener())
				.setNeutralGone().show();
	}

	// 按下是按钮的处理，
	private class MyPositiveListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			File file = new CreateOriginFile(context).createOriginFile(
					new File(imgPath), ImgActivity.savePath);
			try {
				// 保存图片
				new SaveFile(file, context).saveFile(bitmap);
				// 将保存标志设为是
				isSave = true;
				// 将路径更新为新保存的修改过的图片文件，这样方便进行图片的连续编辑
				imgPath = file.getAbsolutePath();
			} catch (Exception e) {
				//Toast.makeText(context, "保存失败", Toast.LENGTH_LONG).show();
				ShowCustomToast.show(context, "保存失败");
				e.printStackTrace();
			} finally {
				// 按下否按钮跳回到原来的界面，是显示美化之后的图片
				// 实现界面的跳转
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

	// 按下“否”按钮的处理
	public class MyNegativeListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// 按下否按钮跳回到原来的界面，是显示未美化之前的图片
			Intent intent = new Intent(context, class1);
			intent.putExtra("imgPath", imgPath);
			context.startActivity(intent);
			dialog.dismiss();
		}

	}

	public String getImgPath() {
		return imgPath;
	}

	// 界面跳转语句
	public void gotoActivity(String imgPath1) {
		Intent intent = new Intent(context, class1);
		intent.putExtra("imgPath", imgPath1);
		context.startActivity(intent);
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
