//Класс с выводом подробной информации

package com.example.myapplication_movieslist;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication_movieslist.data.MovieContract.MovieEntry;
import com.example.myapplication_movieslist.data.MovieDbHelper;

import com.example.myapplication_movieslist.dialogs.DeleteDialogInfo;

public class MainInfo extends FragmentActivity implements OnTouchListener{
	/*для передачи ID фильма на экран с подробной информацией, чтобы знать
	  подробную информацию какого фильм нужно вывести*/
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonBack;
	private Button buttonDelete;
	private Button buttonUpdate;
	
	private TextView textName;
	private TextView textDirector;
	private TextView textYear;
	private TextView textInfo;
	private TextView textRating;
	
	private Intent intent;
	
	//для работы с базой данных
	private MovieDbHelper dbHelper;
	
	//ID текущего фильма
	private int currentId;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		init();
		//получаем ID текущего фильма
		getMovieId();
	}
	
	//начальная инициализация
	public void init(){
		buttonBack = (Button)findViewById(R.id.buttonBack);
		buttonDelete = (Button)findViewById(R.id.buttonDelete);
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		
		textName = (TextView)findViewById(R.id.textName);
		textDirector = (TextView)findViewById(R.id.textDirector);
		textYear = (TextView)findViewById(R.id.textYear);
		textInfo = (TextView)findViewById(R.id.textInfo);
		textRating = (TextView)findViewById(R.id.textRating);
		
		//устанавливаем слушателей
		buttonBack.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		buttonUpdate.setOnTouchListener(this);
		
		//для работы с базой данных
		dbHelper = new MovieDbHelper(this);
	}
	
	//получим ID фильма, который мы хотим вывести на экран
	public void getMovieId(){
		intent = getIntent();
		
		//получаем переданный ID
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		//если полученный ID верный, то выводим данные на экран
		if(currentId >= 0)
			displayDatabaseInfo();
		else{
			//в противном случае, выводим сообщение об ошибке
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//вывод данных из базы данных на экран
	public void displayDatabaseInfo(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		//какие столбцы будут выведены
		String[] projection = {
				MovieEntry.COLUMN_NAME,
				MovieEntry.COLUMN_DIRECTOR, 
				MovieEntry.COLUMN_YEAR,
				MovieEntry.COLUMN_INFO,
				MovieEntry.COLUMN_RATING
		}; 
		
		//условия выборки фильма по ID
 		String selection = MovieEntry._ID + "=?";
		String[] selectionArgs = {"" + currentId};
		
		Cursor cursor = db.query(
					MovieEntry.TABLE_NAME,
					projection,
					selection,
					selectionArgs,
					null,
					null,
					null
				);
		
		try{
			
			//получаем индексы столбцов таблицы
			int nameIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME);
			int directorIndex = cursor.getColumnIndex(MovieEntry.COLUMN_DIRECTOR);
			int yearIndex = cursor.getColumnIndex(MovieEntry.COLUMN_YEAR);
			int infoIndex = cursor.getColumnIndex(MovieEntry.COLUMN_INFO);
			int ratingIndex = cursor.getColumnIndex(MovieEntry.COLUMN_RATING);
			
			//пробегаемся по всем индексам
			while(cursor.moveToNext()){
				//получаем данные их столбцов базы данных
				String tempName = cursor.getString(nameIndex);
				String tempDirector = cursor.getString(directorIndex);
				int tempYear = cursor.getInt(yearIndex);
				String tempInfo = cursor.getString(infoIndex);
				float tempRating = cursor.getFloat(ratingIndex);
				
				//выводим на экран, полученные данные из базы данных
				textName.setText(tempName);
				textDirector.setText(tempDirector);
				textYear.setText("" + tempYear);
				textInfo.setText(tempInfo);
				textRating.setText("" + tempRating);
			}
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		};
	}
	
	//удаление фильма по ID
	public void deleteMovie(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		//удаляем по текущему ID
		db.delete(
				MovieEntry.TABLE_NAME,
				MovieEntry._ID + "=?",
				new String[]{"" + currentId}
			);
		
		//выводим сообщение об удалении
		Toast.makeText(
				getApplicationContext(),
				"Удалено!",
				Toast.LENGTH_LONG
			).show();
		
		//после удаления - переходим на главный экран
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}

	
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка возврата на главный экран
			if(view == buttonBack){
				intent = new Intent(this, Main.class);
				startActivity(intent);
			}
			
			//кнопка удаления
			if(view == buttonDelete){
				/*устанавливаем диалоговое окно перед удалением,
			 	  для подтверждения удаления*/
				FragmentManager manager = getSupportFragmentManager();
				DeleteDialogInfo dialog = new DeleteDialogInfo();
				dialog.show(manager, "delete_dialog");
			}
			
			//кнопка для обновления данных о фильме
			if(view == buttonUpdate){
				//переходим на экран обновления
				intent = new Intent(this, MainUpdate.class);
				
				//передаем ID фильма, который нужно будет обновить
				intent.putExtra(KEY_ID, currentId);
				
				startActivity(intent);
			}
		}
		
		return false;
	}
}