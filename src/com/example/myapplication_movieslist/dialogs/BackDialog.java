/*Стандартный класс для создания диалогового окна,
с подтверждением на главный экран*/

package com.example.myapplication_movieslist.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_movieslist.MainUpdate;

public class BackDialog extends DialogFragment{
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("ВНИМАНИЕ!")
			   .setMessage("Данные будут потеряны. Выйти из редактора?")
			   .setCancelable(true)
			   .setPositiveButton(
					   		"Нет",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				//закрываем диалоговое окно, ибо ответ "нет"
					   				dialog.cancel();
					   			}
					   		}
					   )
			   .setNegativeButton(
					   		"Да",
					   		new DialogInterface.OnClickListener(){
					   			public void onClick(DialogInterface dialog, int id){
					   				/*если ответ "Да", то переходим на главный экрани
					   				  передав значение true, для разрешения перехода*/
					   				((MainUpdate)getActivity()).setBackMain();
					   			}
					   		}
					   );
		
		return builder.create();
	}
}