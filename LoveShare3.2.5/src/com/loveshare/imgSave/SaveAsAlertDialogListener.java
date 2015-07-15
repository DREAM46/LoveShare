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

			// ����һ�����ļ�
			File targetFile = createTargetFile();

			// ���޸ĺ��ͼƬ���浽���ļ���
			sf.saveFile(myBitmap, targetFile);

			dialog.dismiss();
		} catch (Exception e) {
			// ������ɹ�������Toast��ʾ�û���
			ShowCustomToast.show(context, R.string.saveFailed);
			dialog.dismiss();
		}
	}

	private File createTargetFile() {
		String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File dir = null;

		// �ж��ֻ��Ƿ����ڴ濨������У���ͼƬ�ļ������ڴ濨�У����û�У�������ֻ��Դ��ڴ�ռ���
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			dir = new File(ImgActivity.savePath);
		} else {
			// �浽�ֻ��Դ����ڴ�ռ���
		}
		dir.mkdir();
		File targetFile = new File(dir, name);
		return targetFile;
	}

}
