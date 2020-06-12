//Класс с добавлением фильмов в базу данных

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

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.example.myapplication_movieslist.data.MovieContract.MovieEntry;
import com.example.myapplication_movieslist.data.MovieDbHelper;

import com.example.myapplication_movieslist.dialogs.AddDialog;

public class MainAdd extends FragmentActivity implements OnTouchListener{
	
	//компоненты
	private Button buttonAdd;
	private Button buttonBack;
	
	private EditText editName;
	private EditText editDirector;
	private EditText editYear;
	private EditText editInfo;
	private EditText editRating;
	
	private Intent intent;
	
	//для работы с базой данных
	private MovieDbHelper dbHelper;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		init();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonAdd = (Button)findViewById(R.id.buttonAdd);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		
		editName = (EditText)findViewById(R.id.editName);
		editDirector = (EditText)findViewById(R.id.editDirector);
		editYear = (EditText)findViewById(R.id.editYear);
		editInfo = (EditText)findViewById(R.id.editInfo);
		editRating = (EditText)findViewById(R.id.editRating);
		
		//устанавливаем слушателей
		buttonAdd.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		
		//для работы с базой данных
		dbHelper = new MovieDbHelper(this);
	}
	
	//добавление фильма в базу данных
	public void insertMovie(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		//получаем данные из полей
		String tempName = editName.getText().toString().trim();
		String tempDirector = editDirector.getText().toString().trim();
		
		/*если строка пустая, то будет ошибка при переводе в число,
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
			
			//если все поля заполнены, добавляем данные в базу данных
			values.put(MovieEntry.COLUMN_NAME, tempName);
			values.put(MovieEntry.COLUMN_DIRECTOR, tempDirector);
			values.put(MovieEntry.COLUMN_YEAR, tempYear);
			values.put(MovieEntry.COLUMN_INFO, tempInfo);
			values.put(MovieEntry.COLUMN_RATING, tempRating);
			
			long newRowId = db.insert(
						MovieEntry.TABLE_NAME,
						null,
						values
					);
			
			//сообщение, что данные обновлены
			Toast.makeText(
					getApplicationContext(),
					"Обновлено",
					Toast.LENGTH_LONG
				).show();
			
			//возвращаемся на главный экран
			intent = new Intent(this, Main.class);
			startActivity(intent);
			
		}else{
			/*если хотя бы одно поле не заполненно, то выводим сообщение,
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
			//если нажали на кнопку добавления фильма
			if(view == buttonAdd){
				/*устанавливаем диалоговое окно для подтверждения 
				  добавления фильма*/
				FragmentManager manager = getSupportFragmentManager();
				AddDialog dialog = new AddDialog();
				dialog.show(manager, "add_dialog");
			}
			
			//если нажали на кнопку возврата
			if(view == buttonBack){
				intent = new Intent(this, Main.class);
				startActivity(intent);
			}
		}
		
		return false;
	}
}