//Класс с обновлением информации о фильме

package com.example.myapplication_movieslist;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import android.content.Intent;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication_movieslist.data.MovieContract.MovieEntry;
import com.example.myapplication_movieslist.data.MovieDbHelper;

import com.example.myapplication_movieslist.dialogs.UpdateDialog;
import com.example.myapplication_movieslist.dialogs.BackDialog;
import com.example.myapplication_movieslist.dialogs.BackInfo;

public class MainUpdate extends FragmentActivity implements OnTouchListener{
	/*Id фильм, который мы передадим на данный экран, чтобы
	 знать, какой фильм обновить*/
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonBack;
	private Button buttonUpdate;
	private Button buttonMain;
	
	private EditText editName;
	private EditText editDirector;
	private EditText editYear;
	private EditText editInfo;
	private EditText editRating;
	
	private Intent intent;
	
	//Id фильма, для обновления
	private int currentId;
	
	//для работы с базой данных
	private MovieDbHelper dbHelper;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		init();
		//получаем ID
		getMovieId();
	}

	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonBack = (Button)findViewById(R.id.buttonBack);
		buttonMain = (Button)findViewById(R.id.buttonMain);
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		
		editName = (EditText)findViewById(R.id.editName);
		editDirector = (EditText)findViewById(R.id.editDirector);
		editYear = (EditText)findViewById(R.id.editYear);
		editInfo = (EditText)findViewById(R.id.editInfo);
		editRating = (EditText)findViewById(R.id.editRating);
		
		//установка слушателей
		buttonBack.setOnTouchListener(this);
		buttonMain.setOnTouchListener(this);
		buttonUpdate.setOnTouchListener(this);
		
		//для базы данных
		dbHelper = new MovieDbHelper(this);
	}
	
	//вызов перехода на главный экран из диалогового окна
	public void setBackMain(){
		//переходим на главный экран
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}
	
	//вызов перехода назад из диалогового окна
	public void setBackInfo(){
		//переходим на экран MainInfo, то есть назад
		intent = new Intent(this, MainInfo.class);
		
		//не забываем передать обратно ID
		intent.putExtra(KEY_ID, currentId);
		
		startActivity(intent);
	}
	
	//получаем ID текущего фильма
	public void getMovieId(){
		intent = getIntent();
		
		//получаем переданный ID фильма
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		//если полученный ID верный, то выводим данные на экран
		if(currentId >= 0)
			displayDatabaseInfo();
		else{
			//в противном случае - сообщаем об ошибке
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//вывод данных в поля
	public void displayDatabaseInfo(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String[] projection = {
			MovieEntry.COLUMN_NAME,
			MovieEntry.COLUMN_DIRECTOR,
			MovieEntry.COLUMN_YEAR,
			MovieEntry.COLUMN_INFO,
			MovieEntry.COLUMN_RATING
		};
		
		//выборка фильма по ID
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
			
			//пробегаемся по индексам
			while(cursor.moveToNext()){
				String tempName = cursor.getString(nameIndex);
				String tempDirector = cursor.getString(directorIndex);
				int tempYear = cursor.getInt(yearIndex);
				String tempInfo = cursor.getString(infoIndex);
				float tempRating = cursor.getFloat(ratingIndex);
				
				//теперь, заполняем поля полученными данными
				editName.setText(tempName);
				editDirector.setText(tempDirector);
				editYear.setText("" + tempYear);
				editInfo.setText(tempInfo);
				editRating.setText("" + tempRating);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		};
	}
	
	//обновление данных по фильму
	public void updateMovie(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		//сначала получаем данные из полей
		String tempName = editName.getText().toString().trim();
		String tempDirector = editDirector.getText().toString().trim();
		
		/*если строка пустая, то будет ошибка при переводе в число
		поэтому заранее проверяем, чтобы поле не было пустым*/
		int tempYear = 0;
		String tempYearElse = editYear.getText().toString().trim();
		if(tempYearElse.length() > 0)
			tempYear = Integer.parseInt(tempYearElse);
		
		String tempInfo = editInfo.getText().toString().trim();
		
		float tempRating = 0.0f;
		String tempRatingElse = editRating.getText().toString().trim();
		if(tempRatingElse.length() > 0)
			tempRating = Float.parseFloat(tempRatingElse);
		
		/*теперь, проверяем, заполнены ли все поля.
		 если хотя бы одно не заполненно, то выводим сообщение о том,
		 что нужно заполнить все поля, а если все заполненны, то добавляем
		 данные в базу данных и переходим на главный экран*/
		
		if(editName.length() > 0 && editDirector.length() > 0 && editYear.length() > 0 &&
		   editInfo.length() > 0 && editRating.length() > 0){
			
			/*если все поля заполнены, то указываем, 
			 какие именно данные мы хотим изменить*/
			values.put(MovieEntry.COLUMN_NAME, tempName);
			values.put(MovieEntry.COLUMN_DIRECTOR, tempDirector);
			values.put(MovieEntry.COLUMN_YEAR, tempYear);
			values.put(MovieEntry.COLUMN_INFO, tempInfo);
			values.put(MovieEntry.COLUMN_RATING, tempRating);
			
			//обновляем, по ID
			db.update(
					MovieEntry.TABLE_NAME,
					values,
					MovieEntry._ID + "=?",
					new String[]{"" + currentId}
				);
			
			//сообщение, что данные обновлены
			Toast.makeText(
					getApplicationContext(),
					"Обновлено",
					Toast.LENGTH_LONG
				).show();
			
			//возвращаемся на предыдущий экран
			intent = new Intent(this, MainInfo.class);
			
			//не забываем передать обратно ID
			intent.putExtra(KEY_ID, currentId);
			
			startActivity(intent);
			
		}else{
			/*если хотя бы одно не заполненно, то выводим сообщение,
			что нужно заполнить все поля*/
			Toast.makeText(
						getApplicationContext(),
						"Нужно заполпнить все поля",
						Toast.LENGTH_LONG
					).show();
		}
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка возврата на предыдущее окно
			if(view == buttonBack){
				/*устанавливаем диалоговое окно для 
				  возвращения на предыдущий экран*/
				FragmentManager manager = getSupportFragmentManager();
				BackInfo dialog = new BackInfo();
				dialog.show(manager, "backinfo_dialog");
			}
			
			//кнопка для обновления
			if(view == buttonUpdate){
				/*устанавливаем диалоговое окно для подтверждения 
				  обновления фильма*/
				FragmentManager manager = getSupportFragmentManager();
				UpdateDialog dialog = new UpdateDialog();
				dialog.show(manager, "update_dialog");
			}
			
			//кнопка для возврата на главный экран
			if(view == buttonMain){
				/*устанавливаем диалоговое окно для 
				  возвращения на главный экран*/
				FragmentManager manager = getSupportFragmentManager();
				BackDialog dialog = new BackDialog();
				dialog.show(manager, "backmain_dialog");
			}
		}
		
		return false;
	}
}