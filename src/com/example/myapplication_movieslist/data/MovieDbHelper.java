//Стандартный класс с созданием базы данных

package com.example.myapplication_movieslist.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

import com.example.myapplication_movieslist.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper{
	
	//название базы данных и начальная версия
	public static final String DATABASE_NAME = "my_movies_list";
	public static final int DATABASE_VERSION = 1;
	
	//сырой запрос на создание таблицы в базе данных
	public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
			MovieEntry.TABLE_NAME + " (" +
			MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
			MovieEntry.COLUMN_DIRECTOR + " TEXT NOT NULL, " + 
			MovieEntry.COLUMN_YEAR + " INTEGER NOT NULL DEFAULT 1900, " +
			MovieEntry.COLUMN_INFO + " TEXT NOT NULL, " + 
			MovieEntry.COLUMN_RATING + " REAL NOT NULL DEFAULT 0);";
	;
	
	public MovieDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//создание таблицы
	public void onCreate(SQLiteDatabase db){
		db.execSQL(SQL_CREATE_TABLE);
	}
	
	//обновление, если есть новая версия
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		//удаляем старую таблицу
		db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME + ";");
		
		//и создаем новую
		onCreate(db);
	}
}