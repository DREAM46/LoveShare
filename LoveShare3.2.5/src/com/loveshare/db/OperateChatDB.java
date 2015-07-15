package com.loveshare.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.loveshare.domin.Chat;

public class OperateChatDB {

	private ChatDBOpenHelper helper;

	public OperateChatDB(Context context) {
		this.helper = new ChatDBOpenHelper(context);
	}

	/**
	 * �����ݲ��뵽���ݿ���
	 * 
	 * @param chat
	 *            Ҫ�����������
	 */
	public void insert(Chat chat) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("content", chat.getContent());
		values.put("flag", chat.getFlag());

		db.insert("chat", null, values);
		db.close();
	}

	/**
	 * ��ѯ���ݿ��е���������
	 * 
	 * @return ��ѯ��������
	 */
	public List<Chat> queryAll() {
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.query("chat", null, null, null, null, null, null);

		if (cursor == null) {
			return null;
		}

		List<Chat> list = new ArrayList<Chat>();
		int indexOfContent = cursor.getColumnIndex("content");
		int indexOfFlag = cursor.getColumnIndex("flag");

		while (cursor.moveToNext()) {
			String content = cursor.getString(indexOfContent);
			int flag = cursor.getInt(indexOfFlag);
			list.add(new Chat(content, flag));
		}

		cursor.close();
		db.close();
		return list;
	}

	public void deleteAll() {
		SQLiteDatabase db = helper.getWritableDatabase();

		db.delete("chat", null, null);

	}

}
