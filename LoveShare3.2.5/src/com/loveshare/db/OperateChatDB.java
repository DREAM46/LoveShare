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
	 * 将数据插入到数据库中
	 * 
	 * @param chat
	 *            要被插入的数据
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
	 * 查询数据库中的所有数据
	 * 
	 * @return 查询到的数据
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
