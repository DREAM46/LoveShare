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

	private ImageView pingImg = null;// ��ʾ����ƴͼ��һ��ͼƬ
	private List<String> pingImgPaths = new ArrayList<String>();// �����ƴͼ�ĸ���ͼƬ

	private ImageButton btn_addImg = null;// ���ͼƬ��ť
	private ImageButton btn_deleteImg = null;// ɾ��ͼƬ�İ�ť
	private ImageButton btn_startPingTu = null;// ���ѡ��ͼƬ��ť

	private ImageView img1 = null;
	private ImageView img2 = null;
	private ImageView img3 = null;
	private ImageView img4 = null;

	private Bitmap bitmap1 = null;// ��������img1��Bitmap
	private Bitmap bitmap2 = null;// ��������img2��Bitmap
	private Bitmap bitmap3 = null;// ��������img3��Bitmap
	private Bitmap bitmap4 = null;// ��������img4��Bitmap

	private static final int FLAG_ADDIMG = 1;
	private static final int FLAG_HANDLEBACK = 0;

	private int select = 0;// ͼƬ����ʾ��־������ɾ��ͼƬ�Ĳ���
	BitmapFactory.Options options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgpuzzle);

		// Ϊ����������г�ʼ��
		pingImg = (ImageView) this.findViewById(R.id.pingImg);

		btn_addImg = (ImageButton) this.findViewById(R.id.btn_addImg);
		btn_deleteImg = (ImageButton) this.findViewById(R.id.btn_deleteImg);
		btn_startPingTu = (ImageButton) this.findViewById(R.id.btn_startPingTu);

		img1 = (ImageView) this.findViewById(R.id.img1);
		img2 = (ImageView) this.findViewById(R.id.img2);
		img3 = (ImageView) this.findViewById(R.id.img3);
		img4 = (ImageView) this.findViewById(R.id.img4);

		// Ϊ����ImgaeView�󶨼�����
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);

		// ΪͼƬ��ť�󶨼�����
		btn_addImg.setOnClickListener(this);
		btn_deleteImg.setOnClickListener(this);
		btn_startPingTu.setOnClickListener(this);

		options = new BitmapFactory.Options();
		options.inSampleSize = 4;// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// �����µ����ͼƬ��ť�������ϵͳͼ��
		case R.id.btn_addImg:
			if (pingImgPaths.size() < 4) {
				Intent intent1 = new Intent();
				intent1.setAction(Intent.ACTION_PICK);
				intent1.setType("image/*");
				startActivityForResult(intent1, FLAG_ADDIMG);
			} else {
				Toast.makeText(PuzzleActivity.this, "�������4��ͼƬ", 0).show();
			}
			break;

		// ���������ɾ��ͼƬ��ť�������ͼƬɾ���Ĳ���
		case R.id.btn_deleteImg:

			// System.out.println("select = "+select);
			// ��bitmap1Ϊ�գ���img1��û��ͼƬ��ʾʱ��������˾��ʾ�û���ͼƬ���Ա�ɾ��
			if (select == 1) {
				if (bitmap1 == null) {
					showToast();
				} else {
					pingImgPaths.remove(0);// ɾ�������еĵ�һ��ͼƬ·��

					img1.setImageResource(R.drawable.frame);// ����һ��ͼƬ����Ϊ��
					bitmap1 = null;// ��һ��λͼ����Ϊ��
					pingImg.setImageResource(R.drawable.hint);// �����ͼƬ����Ϊ��
					// ɾ����һ��ͼƬ�󣬵����滹��ͼƬʱ������ͼƬ��ǰ�ƶ�
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
						// ��ǰ��ûͼƬ��select����
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

		// �����µ���img1��������bitmap1��Ϊ�գ���img1��ͼƬ��ʾ������£���img1�ϵ�ͼ����ʾ��pingImg��
		case R.id.img1:
			if (bitmap1 != null) {
				pingImg.setImageBitmap(bitmap1);
				select = 1;

			}
			break;

		// �����µ���img2��������bitmap2��Ϊ�գ���img2��ͼƬ��ʾ������£���img2�ϵ�ͼ����ʾ��pingImg��
		case R.id.img2:
			if (bitmap2 != null) {
				pingImg.setImageBitmap(bitmap2);
				select = 2;
			}
			break;

		// �����µ���img3��������bitmap3��Ϊ�գ���img3��ͼƬ��ʾ������£���img3�ϵ�ͼ����ʾ��pingImg��
		case R.id.img3:
			if (bitmap3 != null) {
				pingImg.setImageBitmap(bitmap3);
				select = 3;
			}
			break;

		// �����µ���img4��������bitmap4��Ϊ�գ���img4��ͼƬ��ʾ������£���img4�ϵ�ͼ����ʾ��pingImg��
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
				Toast.makeText(PuzzleActivity.this, "�����ͼƬ", 0).show();
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

					// �õ����ͼƬ��·��
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					pingImgPaths.add(path);
					Log.d("may", "path=" + path);

					// �����ʵ��ѡ�е�ͼƬ��ѡ��˳����ʾ��Activity�ײ�
					// ���bitmap1Ϊ��,��img1û��ͼ�������£���ѡ��ͼƬ��ʾ��img1
					if (bitmap1 == null) {
						bitmap1 = BitmapFactory.decodeFile(path, options);
						img1.setImageBitmap(bitmap1);
						// img1Path = path;
						// pingImgPaths.add(img1Path);
						// ��img1��ͼ��ʱ�����̽�img1��pingImg��ʾ����
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
