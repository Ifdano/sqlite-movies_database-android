/*Стандартный класс для создания диалогового окна,
с подтверждением удаления фильма из главного окна*/

package com.example.myapplication_movieslist.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;

import com.example.myapplication_movieslist.Main;

public class DeleteDialog extends DialogFragment{

	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		//создаем диалоговое окно с подтверждением удаления
		builder.setTitle("ВНИМАНИЕ!")
			   .setMessage("Вы подтверждаете удаление фильма из списка?")
			   .setCancelable(true)
			   .setPositiveButton(
					   		"Нет",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				//если мы не хотим удалять, то закрываем диалоговое окно
					   				dialog.cancel();
					   			}
					   		}
					   )
			   .setNegativeButton(
					   		"Да",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				//корявый метод вызова метода для удаления из нашего Main
					   				((Main)getActivity()).deleteMovie();
					   			}
					   		}
					 );
		
		return builder.create();
	}
}