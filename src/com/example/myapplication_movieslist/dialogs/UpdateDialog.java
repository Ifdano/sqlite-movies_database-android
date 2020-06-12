/*Стандартный класс для создания диалогового окна,
с подтверждением обновления данных по фильму*/

package com.example.myapplication_movieslist.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_movieslist.MainUpdate;

public class UpdateDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("ВНИМАНИЕ")
		       .setMessage("Вы подтверждаете обновление данных фильма?")
		       .setCancelable(true)
		       .setPositiveButton(
		    		   		"Нет",
		    		   		new DialogInterface.OnClickListener(){
		    		   			public void onClick(DialogInterface dialog, int id){
		    		   				//закрываем диалоговое окно, ибо выбор "нет"
		    		   				dialog.cancel();
		    		   			}
		    		   		}
		    		   )
		       .setNegativeButton(
		    		   		"Да",
		    		   		new DialogInterface.OnClickListener(){
		    		   			public void onClick(DialogInterface dialog, int id){
		    		   				//вызываем метод обновления данных
		    		   				((MainUpdate)getActivity()).updateMovie();
		    		   			}
		    		   		}
		    		   );
		
		return builder.create();
	}
}