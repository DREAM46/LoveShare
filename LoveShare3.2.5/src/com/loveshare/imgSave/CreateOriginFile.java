package com.loveshare.imgSave;

import java.io.File;

import com.loveshare.activity.R;

import android.content.Context;
import android.os.Environment;

public class CreateOriginFile {

	private Context context;

	public CreateOriginFile(Context context) {
		this.context = context;
	}

	/*
	 * ��������������¹��������ļ����ļ�·������Ϊ��ЩͼƬ���Ǵ�����ָ���ļ��е� ���Խ�������ļ�������һ�������ļ������·������ָ���ļ�����
	 */
	public File createOriginFile(File originFile, String path) {

		/*
		 * System.out.println("path======"+path);
		 * System.out.println("Name of originFile:"+originFile.getName());
		 */
		// ���originFile�ĸ�Ŀ¼���ǳ���ָ�����ļ��У����ع�һ���ļ����󣬴ﵽ������·����Ŀ��
		if (!originFile.getParentFile().getAbsolutePath().equals(path)) {
			originFile = new File(path + "/" + originFile.getName());
		}

		return originFile;
	}

}
