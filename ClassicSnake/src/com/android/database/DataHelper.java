package com.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataHelper {
	private Context context;
	private SQLiteDatabase db;

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(context);
		this.db = openHelper.getWritableDatabase();

	}

	public void Insert(String playerName, String gameMode, long score) {
		final String[] Params = new String[3];
		Params[0] = playerName;
		Params[1] = gameMode;
		Params[2] = String.valueOf(score);

		db.execSQL(
				"INSERT INTO score_table (playername,gamemode,score) VALUES(?,?,?)",
				Params);
	}

	public Cursor selectTOP10() {
		final Cursor c = db.rawQuery(
				"SELECT * FROM score_table ORDER BY score DESC LIMIT 10", null);

		return c;
	}
	
	public int selectMax(){
		int value=0;
		final Cursor c = db.rawQuery(
				"SELECT * FROM score_table ORDER BY score DESC LIMIT 1", null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			value = c.getInt(c.getColumnIndex("score"));
		}
		return value;
		
	}

	public boolean checkValue(long gameScroe) {
		// TODO Auto-generated method stub
		Cursor c;
			c = db.rawQuery("SELECT * FROM score_table", null);
		if (c != null && c.getCount() >= 10) {
			c = db.rawQuery("SELECT * FROM score_table WHERE score=(SELECT MIN(score) FROM score_table) LIMIT 1", null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				Long value = c.getLong(c.getColumnIndex("score"));
				if (gameScroe > value) {
					db.execSQL("DELETE FROM score_table WHERE _id="
							+ c.getInt(c.getColumnIndex("_id")));
					return true;
				}
			}else return false;
		}
		
		return true;
			
	}
}
