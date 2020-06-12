//Класс с главным окном, где показан список с фильмами

package com.example.myapplication_movieslist;

import android.app.Activity;
import android.os.Bundle;

//добавление компонентов
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//для работы со списком
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

//обработка касаний
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import android.content.Intent;
import android.content.ContentValues;

//для использования фрагментов
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

//для работы с базой данных
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication_movieslist.data.MovieContract.MovieEntry;
import com.example.myapplication_movieslist.data.MovieDbHelper;

//для вызова диалогового окна
import com.example.myapplication_movieslist.dialogs.DeleteDialog;

import java.util.ArrayList;

public class Main extends FragmentActivity implements OnTouchListener, OnItemLongClickListener, OnItemClickListener{
	/*для передачи ID фильма на экран с подробной информацией, чтобы знать
	  подробную информацию какого фильм нужно вывести*/
	public static final String KEY_ID = "key_id";

	//наши компоненты
	private Button buttonAdd;
	private Button buttonDelete;
	
	private ListView listMovies;
	
	//для создания списка
	private ArrayList<String> movies;
	private ArrayList<Integer> moviesId;
	private ArrayAdapter<String> movieAdapter;
	
	//для перехода на другие экраны
	private Intent intent;
	
	//для работы с базой данных
	private MovieDbHelper dbHelper;
	
	//Id фильма для удаления
	private int currentIdForDelete;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}
	
	protected void onStart(){
		super.onStart();
		
		/*обнуляем текущие данные по фильмам, чтобы 
		  добавить обновленные данные*/
		movies.clear();
		moviesId.clear();
		
		//выводим данные из базы данных на экран
		displayDatabaseInfo();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonAdd = (Button)findViewById(R.id.buttonAdd);
		buttonDelete = (Button)findViewById(R.id.buttonDelete);
		
		listMovies = (ListView)findViewById(R.id.listMovie);
		
		//устанавливаем слушателей
		buttonAdd.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		
		listMovies.setOnItemClickListener(this);
		listMovies.setOnItemLongClickListener(this);
		
		//для списка
		movies = new ArrayList<String>();
		moviesId = new ArrayList<Integer>();
		
		//для работы с базой данных
		dbHelper = new MovieDbHelper(this);
		
		//Id фильма для удаления
		currentIdForDelete = -1;
	}
	
	//удаление фильма из базы данных
	public void deleteMovie(){
		//открываем базу данных для чтения
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		//если у нас есть Id фильма, который мы хотим удалить
		if(currentIdForDelete >= 0){
			//удаляем фильм по ID
			db.delete(
						MovieEntry.TABLE_NAME,
						MovieEntry._ID + "=?",
						new String[]{"" + currentIdForDelete}
					);
		
			//указываем адаптеру, что данные обновились
			movieAdapter.notifyDataSetChanged();
		
			//обновляем массивы
			movies.clear();
			moviesId.clear();
		
			//повторяем вывод на экран
			displayDatabaseInfo();
		
			//сообщаем об удалении
			Toast.makeText(
					getApplicationContext(),
					"Удалено!",
					Toast.LENGTH_LONG
					).show();
		
			/*обнуляем Id, который нужен для удаления,
		  	чтобы случайно не удалить другие данные*/
			currentIdForDelete = -1;
		}else{
			/*если Id нет, но мы зашли в метод для удаления,
			  значит, id не получен, фильм не удалится.
			  сообщаем об ошибке*/
			Toast.makeText(
					getApplicationContext(),
					"Ошибка удаления!",
					Toast.LENGTH_LONG
					).show();
		}
	}
	
	//вывод данных из базы данных на экран
	public void displayDatabaseInfo(){
		//получаем базу данных для записи
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		//столбцы таблицы, которые мы хотим вывести
		String[] projection = {
				MovieEntry._ID,
				MovieEntry.COLUMN_NAME
			};
		
		//условия выборки данных из базы данных
		Cursor cursor = db.query(
					MovieEntry.TABLE_NAME,
					projection, 
					null,
					null,
					null,
					null,
					null
			);
		
		try{
			
			//получаем индексы столбцов таблицы
			int idIndex = cursor.getColumnIndex(MovieEntry._ID);
			int nameIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME);
			
			//пробегаемся по индексам и получаем данные из таблицы
			while(cursor.moveToNext()){
				int currentId = cursor.getInt(idIndex);
				String currentName = cursor.getString(nameIndex);
				
				/*общая строка, для помещения в список.
				  в списке мы будем выводить лишь id и название фильма, 
				  поэтому и создаем общую строку из id и названия фильма,
				  ее и поместим в итоговый массив*/
				String textList = currentId + ". " + currentName;
				
				//для создания списка
				movieAdapter = new ArrayAdapter<String>(
							this,
							android.R.layout.simple_list_item_1,
							movies
						);
				
				//устанавливаем адаптер, чтобы отобразить список на экран
				listMovies.setAdapter(movieAdapter);
				
				//добавляем id фильмов в наш массив
				moviesId.add(currentId);
				//добавляем общие строки в массив
				movies.add(textList);
				
				//обновляем список
				movieAdapter.notifyDataSetChanged();
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//обработка долгого нажатия на элемент из списка, для удаления
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		//получаем ID фильма, на который мы нажали, и который мы хотим удалить
		currentIdForDelete = moviesId.get(position);
		
		/*устанавливаем диалоговое окно перед удалением,
	 	  для подтверждения удаления*/
		FragmentManager manager = getSupportFragmentManager();
		DeleteDialog dialog = new DeleteDialog();
		dialog.show(manager, "delete_dialog");
		
		return true;
	}
	
	//обработка одинарного нажатия на элемент из списка, чтобы узнать подробную информацию
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		intent = new Intent(this, MainInfo.class);
		
		/*передаем ID фильма на экран с подробной информацией.
		  так мы будем знать на какой фильм мы нажали*/
		intent.putExtra(KEY_ID, moviesId.get(position));
		
		startActivity(intent);
	}
	
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//если нажали на кнопку "добавить"
			if(view == buttonAdd){
				/*переходим на экран MainAdd, 
				  на котором можно будет добавить новый фильм*/
				intent = new Intent(this, MainAdd.class);
				startActivity(intent);
			}
			
			//если нажали на кнопку "удалить"
			if(view == buttonDelete){
				/*выведим сообщение о том, как можно
				  удалить фильм из списка*/
				
				Toast.makeText(
							getApplicationContext(),
							"Для удаления фильма - удерживайте его нажатым",
							Toast.LENGTH_LONG
						).show();
			}
		}
		
		return false;
	}
}