package com.loveshare.puzzle;

import java.io.File;
import java.util.*;

import com.loveshare.activity.R;
import com.loveshare.imgSave.SaveFile;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PuzzleActivity extends Activity implements OnClickListener {

	private ImageView pingImg = null;// 显示即将拼图的一个图片
	private List<String> pingImgPaths = new ArrayList<String>();// 储存的拼图的各个图片

	private ImageButton btn_addImg = null;// 添加图片按钮
	private ImageButton btn_deleteImg = null;// 删除图片的按钮
	private ImageButton btn_startPingTu = null;// 完成选择图片按钮

	private ImageView img1 = null;
	private ImageView img2 = null;
	private ImageView img3 = null;
	private ImageView img4 = null;

	private Bitmap bitmap1 = null;// 即将设入img1的Bitmap
	private Bitmap bitmap2 = null;// 即将设入img2的Bitmap
	private Bitmap bitmap3 = null;// 即将设入img3的Bitmap
	private Bitmap bitmap4 = null;// 即将设入img4的Bitmap

	private static final int FLAG_ADDIMG = 1;
	private static final int FLAG_HANDLEBACK = 0;

	private int select = 0;// 图片的显示标志，用于删除图片的操作
	BitmapFactory.Options options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgpuzzle);

		// 为各个组件进行初始化
		pingImg = (ImageView) this.findViewById(R.id.pingImg);

		btn_addImg = (ImageButton) this.findViewById(R.id.btn_addImg);
		btn_deleteImg = (ImageButton) this.findViewById(R.id.btn_deleteImg);
		btn_startPingTu = (ImageButton) this.findViewById(R.id.btn_startPingTu);

		img1 = (ImageView) this.findViewById(R.id.img1);
		img2 = (ImageView) this.findViewById(R.id.img2);
		img3 = (ImageView) this.findViewById(R.id.img3);
		img4 = (ImageView) this.findViewById(R.id.img4);

		// 为各个ImgaeView绑定监听器
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);

		// 为图片按钮绑定监听器
		btn_addImg.setOnClickListener(this);
		btn_deleteImg.setOnClickListener(this);
		btn_startPingTu.setOnClickListener(this);

		options = new BitmapFactory.Options();
		options.inSampleSize = 4;// 图片宽高都为原来的二分之一，即图片为原来的四分之一

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// 若按下的添加图片按钮，则进行系统图库
		case R.id.btn_addImg:
			if (pingImgPaths.size() < 4) {
				Intent intent1 = new Intent();
				intent1.setAction(Intent.ACTION_PICK);
				intent1.setType("image/*");
				startActivityForResult(intent1, FLAG_ADDIMG);
			} else {
				Toast.makeText(PuzzleActivity.this, "最多可添加4张图片", 0).show();
			}
			break;

		// 若点击的是删除图片按钮，则进行图片删除的操作
		case R.id.btn_deleteImg:

			// System.out.println("select = "+select);
			// 当bitmap1为空，即img1中没有图片显示时，弹出土司提示用户无图片可以被删除
			if (select == 1) {
				if (bitmap1 == null) {
					showToast();
				} else {
					pingImgPaths.remove(0);// 删除集合中的第一张图片路径

					img1.setImageResource(R.drawable.frame);// 将第一张图片设置为空
					bitmap1 = null;// 第一张位图设置为空
					pingImg.setImageResource(R.drawable.hint);// 上面大图片设置为空
					// 删除第一张图片后，当后面还有图片时，所以图片向前移动
					if (bitmap2 != null) {
						bitmap1 = bitmap2;
						pingImg.setImageBitmap(bitmap1);
						img1.setImageBitmap(bitmap1);
						img2.setImageResource(R.drawable.frame);
						bitmap2 = null;
						if (bitmap3 != null) {
							bitmap2 = bitmap3;
							img2.setImageBitmap(bitmap2);
							img3.setImageResource(R.drawable.frame);
							bitmap3 = null;
							if (bitmap4 != null) {
								bitmap3 = bitmap4;
								img3.setImageBitmap(bitmap3);
								img4.setImageResource(R.drawable.frame);
								bitmap4 = null;
							}
						}
					} else {
						// 若前面没图片，select归零
						select = 0;
					}
				}
			}

			if (select == 2) {
				if (bitmap2 == null) {
					showToast();
				} else {
					pingImgPaths.remove(1);
					img2.setImageResource(R.drawable.frame);
					bitmap2 = null;
					pingImg.setImageResource(R.drawable.hint);
					if (bitmap3 != null) {
						bitmap2 = bitmap3;
						pingImg.setImageBitmap(bitmap2);
						img2.setImageBitmap(bitmap2);
						img3.setImageResource(R.drawable.frame);
						bitmap3 = null;
						if (bitmap4 != null) {
							bitmap3 = bitmap4;
							img3.setImageBitmap(bitmap3);
							img4.setImageResource(R.drawable.frame);
							bitmap4 = null;
						}
					} else {

						select = 1;
						pingImg.setImageBitmap(bitmap1);

					}
				}
			}

			if (select == 3) {
				if (bitmap3 == null) {
					showToast();
				} else {
					pingImgPaths.remove(2);
					img3.setImageResource(R.drawable.frame);
					bitmap3 = null;
					pingImg.setImageResource(R.drawable.hint);
					if (bitmap4 != null) {
						bitmap3 = bitmap4;
						pingImg.setImageBitmap(bitmap3);
						img3.setImageBitmap(bitmap3);
						img4.setImageResource(R.drawable.frame);
						bitmap4 = null;
					} else {
						select = 2;
						pingImg.setImageBitmap(bitmap2);
					}
				}
			}

			if (select == 4) {
				if (bitmap4 == null) {
					showToast();
				} else {
					pingImgPaths.remove(3);
					pingImg.setImageBitmap(bitmap3);
					bitmap4 = null;
					img4.setImageResource(R.drawable.frame);
					select = 3;
				}
			}

			break;

		// 若按下的是img1，并且在bitmap1不为空，即img1有图片显示的情况下，则将img1上的图像显示在pingImg上
		case R.id.img1:
			if (bitmap1 != null) {
				pingImg.setImageBitmap(bitmap1);
				select = 1;

			}
			break;

		// 若按下的是img2，并且在bitmap2不为空，即img2有图片显示的情况下，则将img2上的图像显示在pingImg上
		case R.id.img2:
			if (bitmap2 != null) {
				pingImg.setImageBitmap(bitmap2);
				select = 2;
			}
			break;

		// 若按下的是img3，并且在bitmap3不为空，即img3有图片显示的情况下，则将img3上的图像显示在pingImg上
		case R.id.img3:
			if (bitmap3 != null) {
				pingImg.setImageBitmap(bitmap3);
				select = 3;
			}
			break;

		// 若按下的是img4，并且在bitmap4不为空，即img4有图片显示的情况下，则将img4上的图像显示在pingImg上
		case R.id.img4:
			if (bitmap4 != null) {
				pingImg.setImageBitmap(bitmap4);
				select = 4;
			}
			break;

		case R.id.btn_startPingTu:
			if (pingImgPaths.size() > 0) {
				Intent intent = new Intent();
				intent.setClass(PuzzleActivity.this, PingPictures.class);
				Bundle b = new Bundle();
				b.putStringArrayList("pingImgPaths",
						(ArrayList<String>) pingImgPaths);

				intent.putExtras(b);
				startActivity(intent);
			} else {
				Toast.makeText(PuzzleActivity.this, "请添加图片", 0).show();
			}
			break;

		}

	}

	private void showToast() {
		Toast.makeText(this, R.string.deleteError, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case FLAG_ADDIMG:
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(this, "no found", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					cursor.moveToFirst();

					// 得到点击图片的路径
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					pingImgPaths.add(path);
					Log.d("may", "path=" + path);

					// 下面可实现选中的图片按选中顺序显示在Activity底部
					// 如果bitmap1为空,即img1没有图像的情况下，将选中图片显示在img1
					if (bitmap1 == null) {
						bitmap1 = BitmapFactory.decodeFile(path, options);
						img1.setImageBitmap(bitmap1);
						// img1Path = path;
						// pingImgPaths.add(img1Path);
						// 当img1有图像时，立刻将img1在pingImg显示出来
						pingImg.setImageBitmap(bitmap1);

						select = 1;

					} else if (bitmap2 == null) {
						// bitmap2 = BitmapFactory.decodeFile(path);
						bitmap2 = BitmapFactory.decodeFile(path, options);
						// img2Path = path;
						img2.setImageBitmap(bitmap2);
						// pingImgPaths.add(img2Path);
						pingImg.setImageBitmap(bitmap2);
						select = 2;

					} else if (bitmap3 == null) {
						// bitmap3 = BitmapFactory.decodeFile(path);
						bitmap3 = BitmapFactory.decodeFile(path, options);
						// img3Path = path;
						img3.setImageBitmap(bitmap3);
						// pingImgPaths.add(img3Path);
						pingImg.setImageBitmap(bitmap3);
						select = 3;

					} else if (bitmap4 == null) {
						// bitmap4 = BitmapFactory.decodeFile(path);
						bitmap4 = BitmapFactory.decodeFile(path, options);
						// img4Path = path;
						img4.setImageBitmap(bitmap4);
						// pingImgPaths.add(img4Path);
						pingImg.setImageBitmap(bitmap4);
						select = 4;
					}

				}
			}
			break;

		case FLAG_HANDLEBACK:
			String imagePath = data.getStringExtra("path");
			// mImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
			Log.d("may", "back");
			break;
		}

	}

}
