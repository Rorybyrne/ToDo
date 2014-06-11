package com.rory.todo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ToDoActivity extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView listView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		
		setupUI(findViewById(R.id.parent));
		
		listView1 = (ListView) findViewById(R.id.listView1);
		
		readItems();
		
		itemsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		listView1.setAdapter(itemsAdapter);
		
		//Placeholder
		items.add("Hello");
		items.add("It works!");
		
		
		//Add an item when "done" is tapped
		EditText addItemTextField = (EditText)
				findViewById(R.id.editText1);
		addItemTextField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					EditText editTextTemp = (EditText)
							findViewById(R.id.editText1);
					String todoItem = editTextTemp.getText().toString();
					itemsAdapter.add(todoItem);
					editTextTemp.setText("");
					saveItems();
				}
				return false;
			}
			
		});
		
		setupListViewListener();
	}
	
	//When the "add" button is tapped - same as when "done" is tapped
	public void addTodoItem(View v) {
		EditText editText1 = (EditText)
				findViewById(R.id.editText1);
		itemsAdapter.add(editText1.getText().toString());
		editText1.setText("");
		saveItems();
	}
	
	//Listen for a long click then delete the item
	private void setupListViewListener() {
		listView1.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long rowId) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
	}
	
	//Hide the soft keyboard
	public void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager)
				this.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
	
	//Recursively Apply OnTouchListener to every non-EditText view
	public void setupUI(View view) {
		if(!(view instanceof EditText)) {
			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard();
					return false;
				}
			});
		}
		
		if(view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				
				View innerView = ((ViewGroup) view).getChildAt(i);
				
				setupUI(innerView);
			}
		}
	}
	
	////////////////////////////////////
	//Saving/Loading items from a file//
	////////////////////////////////////
	
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "toto.txt");
		try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			e.printStackTrace();
		}
	}
	
	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		
		try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
