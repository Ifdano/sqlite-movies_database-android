/*Стандартный класс для создания диалогового окна,
с подтверждением возврата на предыдущее окно*/

package com.example.myapplication_movieslist.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_movieslist.MainUpdate;

public class BackInfo extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("ВНИМАНИЕ!")
			   .setMessage("Данные будут потеряны. Выйти из редактора?")
			   .setCancelable(true)
			   .setPositiveButton(
					   		"Нет",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				//ответ "нет", поэтому просто закрываем диалоговое окно
					   				dialog.cancel();
					   			}
					   		}
					   )
			   .setNegativeButton(
					   		"Да",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				//вызываем метод setBackInfo, чтобы вернуться на экран MainInfo
					   				((MainUpdate)getActivity()).setBackInfo();
					   			}
					   		}
					   );
			   
		return builder.create();
	}	
}