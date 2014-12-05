package com.dineshsunny.ListActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.dineshsunny.DetailActivity.NoteItem;
import com.dineshsunny.ZayaLabs.R;

public class NoteEditorActivity extends Activity {

	private NoteItem note;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			setContentView(R.layout.activity_note_editor);
		}
		else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_note_editor_pre3);
		}
				
		Intent intent = this.getIntent();
		note = new NoteItem();
		note.setKey(intent.getStringExtra(NoteItem.KEY));
		note.setText(intent.getStringExtra(NoteItem.TEXT));
		
		EditText et = (EditText) findViewById(R.id.noteText);
		et.setText(note.getText());
		et.setSelection(note.getText().length());
	}
	
	public void saveAndFinish(View v) {
		EditText et = (EditText) findViewById(R.id.noteText);
		String noteText = et.getText().toString();
		
		Intent intent = new Intent();
		intent.putExtra(NoteItem.KEY, note.getKey());
		intent.putExtra(NoteItem.TEXT, noteText);
		setResult(RESULT_OK, intent);
		finish();

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			saveAndFinish(null);
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		saveAndFinish(null);
	}
	
}
