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
	 * 这个方法用来重新构建保存文件的文件路径，因为有些图片并非存在于指定文件夹的 所以将保存的文件对象做一个处理，文件对象的路径设在指定文件夹中
	 */
	public File createOriginFile(File originFile, String path) {

		/*
		 * System.out.println("path======"+path);
		 * System.out.println("Name of originFile:"+originFile.getName());
		 */
		// 如果originFile的父目录不是程序指定的文件夹，则重构一个文件对象，达到更改其路径的目的
		if (!originFile.getParentFile().getAbsolutePath().equals(path)) {
			originFile = new File(path + "/" + originFile.getName());
		}

		return originFile;
	}

}
