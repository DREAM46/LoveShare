package com.loveshare.imgSave;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.view.CustomAlertDialog;
import com.loveshare.view.ShowCustomToast;

public class SaveAsAlertDialogListener implements OnClickListener {

	private Context context;
	private SaveFile sf;
	private Bitmap myBitmap;
	private CustomAlertDialog dialog;

	public SaveAsAlertDialogListener(Context context, SaveFile sf,
			Bitmap myBitmap, CustomAlertDialog dialog) {
		this.context = context;
		this.sf = sf;
		this.myBitmap = myBitmap;
		this.dialog = dialog;
	}

	@Override
	public void onClick(View view) {
		try {

			// 创建一个新文件
			File targetFile = createTargetFile();

			// 将修改后的图片保存到新文件中
			sf.saveFile(myBitmap, targetFile);

			dialog.dismiss();
		} catch (Exception e) {
			// 若保存成功，弹出Toast提示用户。
			ShowCustomToast.show(context, R.string.saveFailed);
			dialog.dismiss();
		}
	}

	private File createTargetFile() {
		String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File dir = null;

		// 判断手机是否有内存卡，如果有，则经图片文件存入内存卡中，如果没有，则存入手机自带内存空间中
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			dir = new File(ImgActivity.savePath);
		} else {
			// 存到手机自带的内存空间中
		}
		dir.mkdir();
		File targetFile = new File(dir, name);
		return targetFile;
	}

}
