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

	private TextView list_path; // ��ʾ�ļ�·����TextView

	private ListView list_file; // ��ʾ�ļ�Ŀ¼��ListView

	private Button button_create;// �½��ļ��а�ť

	private int pos = -1;// ��ʾlist_file�е����λ��

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

		// ��ȡϵͳ��SDCard��Ŀ¼
		File root = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath().toString());
		// ���sd�����ڵĻ�
		if (root.exists()) {

			currentParent = root;
			currentFiles = root.listFiles();

			processDir();

			// ʹ�õ�ǰĿ¼�µ�ȫ���ļ����ļ��������ListView
			inflateListView1(currentFiles);
		}
		setClickListener();
	}

	/*
	 * bug�޸��� ��currentFiles����Ŀ¼�Ҳ�Ϊ�����ļ���Ԫ����ӵ�currentFiles1��
	 * �ٽ�currentFiles1�е������ȡ������currentFiles �������currentFiles�е�Ԫ�ض���Ŀ¼�����
	 * ��ΪListView����ʾcurrentFiles��ΪĿ¼��Ԫ�أ���currentFiles��
	 * ��Ŀ¼��Ԫ�ر��������������Ǳ�ɾ�����ʵ�����ListView�е���ʱ�� ���ܻ� ���ְ��µ�Ŀ¼��ʵ�������еĲ�һ�������
	 */
	private void processDir() {
		// �����currentFiles1�е�Ԫ�أ���ֹ�ϴ����µ�Ԫ������ε�Ԫ�ػ���
		currentFiles1.clear();
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory()
					&& (!currentFiles[i].getName().startsWith("."))) {
				currentFiles1.add(currentFiles[i]);
			}
		}
		currentFiles = null;

		/*
		 * �¶���һ��Comparator�������ڲ��� ��26����ĸ���ִ�Сд��˳����¼��ϵ�Ԫ�ؽ�������
		 * ���ǵ���Collections.sort(list),�򼯺��ڲ�Ԫ�ذ���ASCII����˳������
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
		// ����PopupWindow�ĵ�������ʧЧ��
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
	 * �����ļ������ListView
	 * 
	 * @param files
	 */
	public void inflateListView1(File[] files) {

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < files.length; i++) {
			if (files[i] == null)
				continue;
			Map<String, Object> listItem = new HashMap<String, Object>();

			// �ж����������ļ��Ƿ���һ���ļ����Ҳ�Ϊ�����ļ��������ڳ�������ʾ��������ʾ
			if (files[i].isDirectory() && (!files[i].getName().startsWith("."))) {
				// ������ļ��о���ʾ��ͼƬΪ�ļ��е�ͼƬ
				listItem.put("icon", R.drawable.folder);
				// ���һ���ļ�����
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

		// ����һ��SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.activity_file_cell,
				new String[] { "filename", "icon" }, new int[] {
						R.id.text_fileName, R.id.img_folder });

		// ��������
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
		view_tv_path.setText("���ļ���\"" + currentFiles[pos].getName() + "\"�Ĳ���");
		popupWindow.showAtLocation(view_btn_select, Gravity.CENTER, 0, 0);

	}

	// ����Ŀ¼�ļ�
	private void enterDir(int position) {
		// ����û��������ļ���ֱ�ӷ��أ������κδ���
		if (currentFiles[position].isFile()) {
			// Ҳ���Զ�����չ������ļ���
			return;
		}
		// ��ȡ�û�������ļ��� �µ������ļ�
		File[] tem = currentFiles[position].listFiles();

		// ��ȡ�û��������б����Ӧ���ļ��У���Ϊ��ǰ�ĸ��ļ���
		currentParent = currentFiles[position];
		// ���浱ǰ�ĸ��ļ����ڵ�ȫ���ļ����ļ���
		currentFiles = tem;
		// �ٴθ���ListView
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
		ShowCustomToast.show(this, "��ѡ���ˣ�" + curpath);
	}

	// ��ȡ�½��ļ��е�����
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
					ShowCustomToast.show(FileExplorerActivity.this, "�ļ���������Ϊ��");
					return;
				}else if(fileName.startsWith(".")){
					ShowCustomToast.show(FileExplorerActivity.this, "�ļ������Ƿ�");
					return;
				}else if (newFile.exists()) {
					ShowCustomToast.show(FileExplorerActivity.this, "�ļ����Ѵ���");
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
					ShowCustomToast.show(FileExplorerActivity.this, "�ļ�����ʧ��");
					e.printStackTrace();
				}

			}
		});
	}

	/*
	 * ���»��˼�����Ϊ��������� 1������ʱĿ¼����SD�������һ��Ŀ¼ʱ������ǰһ��Ŀ¼ 2�����ǣ����˳���ʱ��Activity
	 */
	@Override
	public void onBackPressed() {
		try {

			if (!currentParent.getCanonicalPath().equals(
					Environment.getExternalStorageDirectory().getAbsolutePath()
							.toString())) {

				// ��ȡ��һ��Ŀ¼
				currentParent = currentParent.getParentFile();
				ListCurrentFiles();
			} else
				super.onBackPressed();
		} catch (Exception e) {

		}
	}

	private void ListCurrentFiles() {
		// �г���ǰĿ¼�µ������ļ�
		currentFiles = currentParent.listFiles();
		// �ٴθ���ListView
		processDir();
		inflateListView1(currentFiles);
	}
}
