package com.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper  extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME="score.db";
	public static final int DATABASE_VERSION=1;
	public static final String TABLE_NAME="score_table";

	public OpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE score_table (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"playername TEXT,gamemode TEXT,score REAL)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXIST "+ TABLE_NAME);
		onCreate(db);
		
	}

}
