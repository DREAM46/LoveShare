package com.loveshare.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loveshare.activity.R;
import com.loveshare.service.SetTheme;
import com.loveshare.view.CustomInputDialog;
import com.loveshare.view.ListDialogAdapter;
import com.loveshare.view.ShowCustomToast;

public class FileExplorerActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private TextView list_path; // 显示文件路径的TextView

	private ListView list_file; // 显示文件目录的ListView

	private Button button_create;// 新建文件夹按钮

	private int pos = -1;// 表示list_file中的项的位置

	private LinearLayout linear_btn = null;
	private AnimationSet in = null;
	private AnimationSet out = null;

	private Button btn_select;
	private Button btn_enter;
	private Button btn_cancel;

	private File currentParent;
	private ArrayList<File> currentFiles1;
	private File[] currentFiles;

	private Editor editor;

	private Handler handler = new Handler() {

	};

	private Button button;
	private Button view_btn_select;
	private Button view_btn_enter;
	private TextView view_tv_path;
	private Button cancleButton;
	private PopupWindow popupWindow;
	private View popupWindowView;

	private RadioButton view_filelist_rb;
	private View listFile;

	private ListDialogAdapter mAdapter;

	private ArrayList<HashMap<String, Object>> arrayList;

	private CustomInputDialog dialog;

	private LinearLayout titleLinear;

	private SharedPreferences preferences;

	@Override
	protected void onStart() {

		int drawable = preferences.getInt("theme", 0);

		if (drawable != 0)
			SetTheme.setBackground(titleLinear, drawable, null);
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fileexplorer);

		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);

		init();
		int k = 0;

		// 获取系统的SDCard的目录
		File root = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath().toString());
		// 如果sd卡存在的话
		if (root.exists()) {

			currentParent = root;
			currentFiles = root.listFiles();

			processDir();

			// 使用当前目录下的全部文件、文件夹来填充ListView
			inflateListView1(currentFiles);
		}
		setClickListener();
	}

	/*
	 * bug修复： 将currentFiles中是目录且不为隐藏文件的元素添加到currentFiles1中
	 * 再将currentFiles1中的数组的取出赋给currentFiles 用以完成currentFiles中的元素都是目录的请况
	 * 因为ListView仅显示currentFiles中为目录的元素，但currentFiles中
	 * 非目录的元素被隐藏起来，而非被删除，故当按下ListView中的项时， 可能会 出现按下的目录与实际数组中的不一样的情况
	 */
	private void processDir() {
		// 先清空currentFiles1中的元素，防止上次留下的元素与这次的元素混淆
		currentFiles1.clear();
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory()
					&& (!currentFiles[i].getName().startsWith("."))) {
				currentFiles1.add(currentFiles[i]);
			}
		}
		currentFiles = null;

		/*
		 * 新定义一个Comparator的匿名内部类 按26个字母不分大小写的顺序对新集合的元素进行排序
		 * 若是调用Collections.sort(list),则集合内部元素按照ASCII码表的顺序排列
		 */

		Collections.sort(currentFiles1, new Comparator<File>() {
			@Override
			public int compare(File lFile, File rFile) {
				return lFile.getName().compareToIgnoreCase(rFile.getName());
			}
		});
		currentFiles = currentFiles1.toArray(new File[currentFiles1.size()]);

	}

	private void init() {

		editor = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE).edit();

		list_path = (TextView) this.findViewById(R.id.text_filepath);
		list_file = (ListView) this.findViewById(R.id.list_file);

		titleLinear = (LinearLayout) this.findViewById(R.id.titleLinear);

		button_create = (Button) this.findViewById(R.id.button_create);

		currentFiles1 = new ArrayList<File>();

		linear_btn = (LinearLayout) this.findViewById(R.id.linear_btn);

		btn_enter = (Button) this.findViewById(R.id.btn_enter);
		btn_select = (Button) this.findViewById(R.id.btn_select);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		popupWindowView = inflater.inflate(R.layout.view_popupwindow, null);
		popupWindow = new PopupWindow(popupWindowView,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置PopupWindow的弹出和消失效果
		popupWindow.setAnimationStyle(R.style.popupAnimation);
		
		cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
		cancleButton.setOnClickListener(this);
		
		view_btn_select = (Button) popupWindowView
				.findViewById(R.id.view_btn_select);
		view_btn_select.setOnClickListener(this);
		view_btn_enter = (Button) popupWindowView
				.findViewById(R.id.view_btn_enter);
		view_btn_enter.setOnClickListener(this);

		view_tv_path = (TextView) popupWindowView
				.findViewById(R.id.view_tv_path);

		dialog = new CustomInputDialog(this);

	}

	private void setClickListener() {
		button_create.setOnClickListener(this);
		list_file.setOnItemClickListener(this);

		btn_enter.setOnClickListener(this);
		btn_select.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	/**
	 * 根据文件夹填充ListView
	 * 
	 * @param files
	 */
	public void inflateListView1(File[] files) {

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < files.length; i++) {
			if (files[i] == null)
				continue;
			Map<String, Object> listItem = new HashMap<String, Object>();

			// 判断所遍历的文件是否是一个文件夹且不为隐藏文件，是则在程序中显示，否则不显示
			if (files[i].isDirectory() && (!files[i].getName().startsWith("."))) {
				// 如果是文件夹就显示的图片为文件夹的图片
				listItem.put("icon", R.drawable.folder);
				// 添加一个文件名称
				listItem.put("filename", files[i].getName());
				listItems.add(listItem);

			} else
				continue;
		}

		arrayList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < currentFiles.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("item", currentFiles[i]);
			hashMap.put("checked", false);
			arrayList.add(hashMap);
		}

		// 定义一个SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.activity_file_cell,
				new String[] { "filename", "icon" }, new int[] {
						R.id.text_fileName, R.id.img_folder });

		// 绑定适配器
		list_file.setAdapter(adapter);

		try {
			list_path.setText(currentParent.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		pos = position;
		view_tv_path.setText("对文件夹\"" + currentFiles[pos].getName() + "\"的操作");
		popupWindow.showAtLocation(view_btn_select, Gravity.CENTER, 0, 0);

	}

	// 进入目录文件
	private void enterDir(int position) {
		// 如果用户单击了文件，直接返回，不做任何处理
		if (currentFiles[position].isFile()) {
			// 也可自定义扩展打开这个文件等
			return;
		}
		// 获取用户点击的文件夹 下的所有文件
		File[] tem = currentFiles[position].listFiles();

		// 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
		currentParent = currentFiles[position];
		// 保存当前的父文件夹内的全部文件和文件夹
		currentFiles = tem;
		// 再次更新ListView
		inflateListView1(currentFiles);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_create:
			createNewFile();
			break;

		case R.id.view_btn_select:
			selectDir();
			popupWindow.dismiss();
			break;

		case R.id.view_btn_enter:
			enterDir(pos);
			popupWindow.dismiss();
			break;
		case R.id.cancleButton:
			popupWindow.dismiss();
		}
	}

	private void selectDir() {
		String curpath = list_path.getText().toString() + "/"
				+ currentFiles[pos].getName();
		System.out.println("curpath" + curpath);
		System.out.println("list_path" + list_path);
		System.out.println("currentFiles[pos].getName()"
				+ currentFiles[pos].getName());
		editor.putString("savePath", curpath);
		editor.commit();
		ShowCustomToast.show(this, "你选择了：" + curpath);
	}

	// 获取新建文件夹的名字
	private void createNewFile() {
		dialog.setDefaultMsg();
		dialog.show();

		dialog.setPositiveListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String fileName = dialog.getFileName();

				File newFile = new File(currentParent.getPath() + "/"
						+ fileName);

				System.out.println("newFile" + newFile.getAbsolutePath());
				
				if (TextUtils.isEmpty(fileName)) {
					ShowCustomToast.show(FileExplorerActivity.this, "文件夹名不能为空");
					return;
				}else if(fileName.startsWith(".")){
					ShowCustomToast.show(FileExplorerActivity.this, "文件夹名非法");
					return;
				}else if (newFile.exists()) {
					ShowCustomToast.show(FileExplorerActivity.this, "文件夹已存在");
					return;
				}
				try {

					newFile.mkdir();

					File[] currentFiles2 = new File[currentFiles.length + 1];
					System.arraycopy(currentFiles, 0, currentFiles2, 0,
							currentFiles.length);
					currentFiles2[currentFiles.length] = newFile;
					currentFiles = currentFiles2;

					processDir();
					dialog.dismiss();
					inflateListView1(currentFiles);
				} catch (Exception e) {
					ShowCustomToast.show(FileExplorerActivity.this, "文件创建失败");
					e.printStackTrace();
				}

			}
		});
	}

	/*
	 * 按下回退键，分为两种情况： 1、当此时目录不是SD卡的最后一层目录时，返回前一层目录 2、若是，则退出此时的Activity
	 */
	@Override
	public void onBackPressed() {
		try {

			if (!currentParent.getCanonicalPath().equals(
					Environment.getExternalStorageDirectory().getAbsolutePath()
							.toString())) {

				// 获取上一级目录
				currentParent = currentParent.getParentFile();
				ListCurrentFiles();
			} else
				super.onBackPressed();
		} catch (Exception e) {

		}
	}

	private void ListCurrentFiles() {
		// 列出当前目录下的所有文件
		currentFiles = currentParent.listFiles();
		// 再次更新ListView
		processDir();
		inflateListView1(currentFiles);
	}
}
