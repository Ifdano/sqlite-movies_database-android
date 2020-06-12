//Стандартный класс для фрагмента

package com.example.myapplication_movieslist.fragments;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import com.example.myapplication_movieslist.R;

public class FragmentAddOne extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.add_fragment_1, container, false);
		
		return view;
	}
}