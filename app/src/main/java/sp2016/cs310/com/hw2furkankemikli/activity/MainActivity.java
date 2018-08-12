package sp2016.cs310.com.hw2furkankemikli.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import sp2016.cs310.com.hw2furkankemikli.R;
import sp2016.cs310.com.hw2furkankemikli.adapter.NoteAdapter;
import sp2016.cs310.com.hw2furkankemikli.bean.NoteBean;
import sp2016.cs310.com.hw2furkankemikli.constants.TakeNotesConstants;
import sp2016.cs310.com.hw2furkankemikli.util.SqlUtil;

import java.util.List;

public class MainActivity extends Activity {
    Button btnNote, btnHome, btnInfo;
    SqlUtil sqlUtil;
    List<NoteBean> noteList;
    ArrayAdapter adapter;
    String noteId;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        // Toast.makeText(this, "on resume", Toast.LENGTH_LONG).show();
        super.onResume();
        IntentFilter refreshNotepadMenuActivityIntentFilter = new IntentFilter(
                TakeNotesConstants.ACTION_REFRESH_ACTIVITY);
        refreshActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
        ListView listView = (ListView) findViewById(R.id.parent);
        listView.invalidate();
    }

    public void refreshActivity() {
        sqlUtil = new SqlUtil(MainActivity.this);
        noteList = sqlUtil.getAllNotes();
        if (noteList.size() > 0) {
            adapter = new NoteAdapter(this, R.layout.note, noteList);
            final ListView listView = (ListView) findViewById(R.id.parent);
            listView.invalidate();
            listView.setAdapter(adapter);
            listView.setDividerHeight(0);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent viewFileIntent = new Intent(
                            MainActivity.this, ViewNoteActivity.class);
                    LinearLayout noteView = (LinearLayout) view;
                    noteId = (String) noteView.getTag();
                    Log.i("Refresh", noteId);
                    viewFileIntent.putExtra("noteId", noteId);
                    startActivity(viewFileIntent);
                }
            });
        }
    }

    public void about(View v){
        Toast.makeText(getApplicationContext(), "ABOUT", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,InfoActivity.class);
        startActivity(i);
    }
    public void home(View v) {
        Toast.makeText(getApplicationContext(), "HOME", Toast.LENGTH_SHORT).show();
    }
    public void newnote(View v) {
        Toast.makeText(getApplicationContext(), "NEW NOTE", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,NewNoteActivity.class);
        startActivity(i);
    }
}
