//Cтандартный класс для хранения таблиц базы данных

package com.example.myapplication_movieslist.data;

import android.provider.BaseColumns;

public final class MovieContract{
	public static final class MovieEntry implements BaseColumns{
		public final static String TABLE_NAME = "movies_list";
		
		public final static String _ID = BaseColumns._ID;
		public final static String COLUMN_NAME = "name";
		public final static String COLUMN_DIRECTOR = "director";
		public final static String COLUMN_YEAR = "year";
		public final static String COLUMN_INFO = "info";
		public final static String COLUMN_RATING = "rating";
	}
}