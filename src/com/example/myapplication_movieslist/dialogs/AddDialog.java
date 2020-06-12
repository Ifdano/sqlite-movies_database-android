/*Стандартный класс для создания диалогового окна,
с подтверждением добавления фильма с базу данных*/

package com.example.myapplication_movieslist.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_movieslist.MainAdd;

public class AddDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		//установка значений диалогового окна
		builder.setTitle("ВНИМАНИЕ")
			   .setMessage("Вы подтверждаете добавление фильма в список?")
			   .setCancelable(true)
			   .setPositiveButton(
					   		"Нет",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				//если ответ отрицательный, то закрываем диалоговое окно
					   				dialog.cancel();
					   			}
					   		}
					   )
			   .setNegativeButton(
					   		"Да",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				/*если ответ положительный, то добавляем фильм в список
					   				  вызвав метод insertMovie из MainAdd*/
					   				((MainAdd)getActivity()).insertMovie();
					   			}
					   		}
					   );
		
		return builder.create();
	}
}