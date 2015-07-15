package com.loveshare.imgProcessUI;

import java.io.File;

import com.loveshare.UI.ImgActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUI.*;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.ImageUtil;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ImgSketchActivity extends Activity implements OnClickListener {

	private final int PROGRESS_WAIT_VISIBLE = 1;
	private final int PROGRESS_WAIT_GONE = 2;
	private final int IMAGEVIEW_INVALIDATE = 3;
	private ImageView img_process;
	private Bitmap mBitmap;
	private String imgPath;

	private TextView title_processImg = null;
	private ImageButton save = null;
	private ImageButton share = null;

	private SaveImgListener sil;
	private ShareListener sl;

	private boolean saveFlag;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PROGRESS_WAIT_VISIBLE:
				setProgressBarIndeterminateVisibility(true);
				break;
			case PROGRESS_WAIT_GONE:
				setProgressBarIndeterminateVisibility(false);
				break;
			case IMAGEVIEW_INVALIDATE:
				img_process.setImageBitmap(mBitmap);
				img_process.invalidate();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_imgeffect_result);

		img_process = (ImageView) findViewById(R.id.img_process);
		imgPath = this.getIntent().getStringExtra("imgPath");

		title_processImg = (TextView) this.findViewById(R.id.title_processImg);
		title_processImg.setText("�??模�?");

		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);

		cancelSketch();
		doSketch();

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath),ImgActivity.savePath);
		sil = new SaveImgListener(this, originFile, mBitmap);
		save.setOnClickListener(sil);
		sl = new ShareListener(this, mBitmap, imgPath);
		share.setOnClickListener(sl);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 1:
			doSketch();
			break;
		case 2:
			cancelSketch();
			break;
		default:
			break;
		}
		return true;
	}

	private void cancelSketch() {
		Bitmap bmp = BitmapFactory.decodeFile(imgPath, ImgActivity.options);
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] pixel = new int[width * height];

		bmp.getPixels(pixel, 0, width, 0, 0, width, height);
		mBitmap = Bitmap.createBitmap(width, height, bmp.getConfig());
		mBitmap.setPixels(pixel, 0, width, 0, 0, width, height);
		img_process.setImageBitmap(mBitmap);
		bmp.recycle();
	}

	private void doSketch() {

		new Thread(new Runnable() {
			public void run() {
				try {
					mHandler.sendEmptyMessage(PROGRESS_WAIT_VISIBLE);
					ImageUtil util = new ImageUtil();
					mBitmap = util.sketch(mBitmap);
					mHandler.sendEmptyMessage(IMAGEVIEW_INVALIDATE);
				} catch (Exception e) {
					Log.e("sketch", e.getMessage());
				} finally {
					mHandler.sendEmptyMessage(PROGRESS_WAIT_GONE);
				}
			}
		}).start();

	}

	public Bitmap getMBitmap() {
		return mBitmap;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	// ???�??onKeyDown?��?�?????�?????�?sSave?�为false?��????�?????件�?�??示�??��?�??�?
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BackHint backHint = new BackHint(this, imgPath, mBitmap,
				ImgActivity.class);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!sl.isSave()) {
				// �??�?????�????���???��??��???��?��????示�???��?��?�??�??�???��?
				if (!sil.isSave()) {

					backHint.showAlertDialog();
					/*
					 * ????��???????�??�???��?isEdit设为false�?以�?�??次�??��?????��?�??�?????示�??��?�?
					 */
					// �?���???�为?��?�??�??�???��???���???��?便�?�?????�?���??
					imgPath = backHint.getImgPath();
					saveFlag = true;
				} else
					imgPath = sil.getImgPath();
			} else
				imgPath = sl.getImgPath();
			if (!saveFlag)	falseProcessMethod(backHint);
		}
		return true;
	}
	

	// 对�?�??�??示�?�?????�???????
	private void falseProcessMethod(BackHint backHint) {
		backHint.gotoActivity(imgPath);
		this.finish();
	}

}