package sp2016.cs310.com.hw2furkankemikli.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import sp2016.cs310.com.hw2furkankemikli.bean.NoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by furkankemikli on 17.05.2016.
 */
public class SqlUtil extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 13;

    // Database Name
    private static final String DATABASE_NAME = "notes";

    // table name


    // Table Columns names
    private static class NoteColumns implements BaseColumns {
        private static final String TABLE_NOTES = "notes_table";
        private static final String NOTE_ID = "note_id";
        private static final String NOTE_TITLE = "note_title";
        private static final String NOTE_DATE = "note_date";
        private static final String NOTE_CONTENT = "note_content";
        //added this new field
        private static final String NOTE_IMAGE = "note_image";
        private static final String NOTE_LOCATION = "note_location";
        private static final String NOTE_SYNC_FLAG= "note_sync_flag";
    }

    public SqlUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //added new param  NoteColumns.NOTE_SYNC_FLAG + " INTEGER DEFAULT 0 "
        String CREATE_NOTES_TABLE = "CREATE TABLE " + NoteColumns.TABLE_NOTES  + "(" + NoteColumns._ID + " INTEGER PRIMARY KEY," + NoteColumns.NOTE_ID +" TEXT,"
                + NoteColumns.NOTE_TITLE + " TEXT,"+ NoteColumns.NOTE_CONTENT + " TEXT, " + NoteColumns.NOTE_DATE+ " TEXT, "+ NoteColumns.NOTE_IMAGE
                + " TEXT, "+ NoteColumns.NOTE_LOCATION + " TEXT, " + NoteColumns.NOTE_SYNC_FLAG
                + " INTEGER " + ")";
        Log.d("creating notes table : ",CREATE_NOTES_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + NoteColumns.TABLE_NOTES);

        // Create tables again
        onCreate(db);

    }

    public NoteBean getNote(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NoteColumns.TABLE_NOTES, new String[] { NoteColumns.NOTE_ID,NoteColumns.NOTE_TITLE,
                NoteColumns.NOTE_CONTENT,NoteColumns.NOTE_DATE,NoteColumns.NOTE_IMAGE,NoteColumns.NOTE_LOCATION,NoteColumns.NOTE_SYNC_FLAG},
                NoteColumns.NOTE_ID + "=?", new String[] { id }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        NoteBean note = new NoteBean(cursor.getString(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),
                cursor.getString(5),(cursor.getInt(6)==1));
        note.setNoteImage(cursor.getString(4));
        note.setNoteLocation(cursor.getString(5));
        return note;
    }
    public long addNote(NoteBean note) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("add note",note.getNoteDate());
        ContentValues values = new ContentValues();
        values.put(NoteColumns.NOTE_ID, note.getNoteId());
        values.put(NoteColumns.NOTE_TITLE, note.getNoteTitle());
        values.put(NoteColumns.NOTE_CONTENT,note.getNoteContent());
        values.put(NoteColumns.NOTE_DATE,note.getNoteDate());
        values.put(NoteColumns.NOTE_IMAGE,note.getNoteImage());
        values.put(NoteColumns.NOTE_LOCATION,note.getNoteLocation());
        //added this field
        if(note.getNoteSyncFlag()== true)
        {
            values.put(NoteColumns.NOTE_SYNC_FLAG,1);
        }
        else
            values.put(NoteColumns.NOTE_SYNC_FLAG,0);
        long noteId=db.insert(NoteColumns.TABLE_NOTES, null, values);
        db.close(); // Closing database connection
        return noteId;
    }
    public List<NoteBean> getAllNotes() {
        List<NoteBean> noteList = new ArrayList<NoteBean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + NoteColumns.TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                boolean noteSyncFlag=false;
                Log.d("sql :",cursor.getString(0)+cursor.getString(1)+cursor.getString(2)+cursor.getString(3)+cursor.getString(4)+ cursor.getString(5)+cursor.getInt(6));
                if(cursor.getInt(6)==1)
                {
                    noteSyncFlag=true;
                }
                NoteBean note = new NoteBean(cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5)
                                ,cursor.getString(6),noteSyncFlag);
                // Adding contact to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        // return note list
        return noteList;
    }
    public boolean updateNote(NoteBean note,String noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean status=false;
        ContentValues values = new ContentValues();
        values.put(NoteColumns.NOTE_ID, note.getNoteId());
        values.put(NoteColumns.NOTE_TITLE, note.getNoteTitle());
        values.put(NoteColumns.NOTE_CONTENT,note.getNoteContent());
        values.put(NoteColumns.NOTE_DATE,note.getNoteDate());
        if(note.getNoteImage()!=null)
            values.put(NoteColumns.NOTE_IMAGE,note.getNoteImage());
            values.put(NoteColumns.NOTE_LOCATION,note.getNoteLocation());
        if(note.getNoteSyncFlag()== false)
            values.put(NoteColumns.NOTE_SYNC_FLAG, 0);
        else
            values.put(NoteColumns.NOTE_SYNC_FLAG, 1);
        Log.d("not id for updatation :",note.getNoteId());
        int result=db.update(NoteColumns.TABLE_NOTES, values, NoteColumns.NOTE_ID + " = ?",
                new String[] {noteId});
        db.close();
        // updating row
        if(result>0)
        {
            status=true;
        }
        return status;
    }
    public void deleteNote(String noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Log.d("note id passed for deletion :",noteId);
        db.delete(NoteColumns.TABLE_NOTES, NoteColumns.NOTE_ID + " = ?",
                new String[] { noteId});
        Log.d("SQLUtil deleted",noteId);
        //db.delete(TABLE_NOTES,null,null);
        db.close();
    }
    }
