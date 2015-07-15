package com.loveshare.imgSave;

import com.loveshare.activity.R;
import com.loveshare.view.CustomAlertDialog;
import com.loveshare.view.ShowCustomToast;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SaveAlertDialogListener implements OnClickListener {

	private CustomAlertDialog dialog;
	private Context context;// 上下文对象
	private SaveFile sf;// 保存文件
	private Bitmap myBitmap;// 保存的文件视图
	private boolean isSave;

	public SaveAlertDialogListener(Context context, SaveFile sf,
			Bitmap myBitmap, boolean isSave,CustomAlertDialog dialog) {
		this(context, sf, myBitmap,dialog);
		this.isSave = isSave;
	}

	public SaveAlertDialogListener(Context context, SaveFile sf, Bitmap myBitmap,CustomAlertDialog dialog) {
		super();
		this.context = context;
		this.sf = sf;
		this.myBitmap = myBitmap;
		this.dialog = dialog;
	}

	@Override
	public void onClick(View v) {
		try {
			System.out.println("save1");
			if (myBitmap == null)
				System.out.println("saveNull");
			// 保存图片文件
			sf.saveFile(myBitmap);
			System.out.println("save2");

			// 将isSave设为true，表示已经保存
			isSave = true;
			dialog.dismiss();
		} catch (Exception e) {
			// 若保存失败，弹出Toast提示用户。
			ShowCustomToast.show(context, R.string.saveFailed);
			e.printStackTrace();
			dialog.dismiss();
		}
	}

	public boolean isSave() {
		return isSave;
	}

}
