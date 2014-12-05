package com.dineshsunny.ListActivity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dineshsunny.DetailActivity.NoteItem;
import com.dineshsunny.DetailActivity.NotesDataSource;
import com.dineshsunny.ZayaLabs.R;

public class MainActivity extends ListActivity {

	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	private static final int MENU_DELETE_ID = 1002;
	private int currentNoteId;
	private NotesDataSource datasource;
	List<NoteItem> notesList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setContentView(R.layout.activity_main);
		}
		else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_main_pre3);
		}
		
		registerForContextMenu(getListView());
		
		datasource = new NotesDataSource(this);
		
		refreshDisplay();
		
	}

	private void refreshDisplay() {
		notesList = datasource.findAll();
		ArrayAdapter<NoteItem> adapter =
				new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout, notesList);
		setListAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_create) {
			createNote(null);
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void createNote(View v) {
		NoteItem note = NoteItem.getNew();
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra(NoteItem.KEY, note.getKey());
		intent.putExtra(NoteItem.TEXT, note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		NoteItem note = notesList.get(position);
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra(NoteItem.KEY, note.getKey());
		intent.putExtra(NoteItem.TEXT, note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			NoteItem note = new NoteItem();
			note.setKey(data.getStringExtra(NoteItem.KEY));
			note.setText(data.getStringExtra(NoteItem.TEXT));
			datasource.update(note);
			refreshDisplay();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		currentNoteId = (int)info.id;
		menu.add(0, MENU_DELETE_ID, 0, "Delete");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if (item.getItemId() == MENU_DELETE_ID) {
			NoteItem note = notesList.get(currentNoteId);
			datasource.remove(note);
			refreshDisplay();
		}
		
		return super.onContextItemSelected(item);
	}
	
}
