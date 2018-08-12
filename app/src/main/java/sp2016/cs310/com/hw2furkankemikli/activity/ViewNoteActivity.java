package sp2016.cs310.com.hw2furkankemikli.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sp2016.cs310.com.hw2furkankemikli.R;
import sp2016.cs310.com.hw2furkankemikli.bean.NoteBean;
import sp2016.cs310.com.hw2furkankemikli.constants.TakeNotesConstants;
import sp2016.cs310.com.hw2furkankemikli.util.ImageUtil;
import sp2016.cs310.com.hw2furkankemikli.util.SqlUtil;
import sp2016.cs310.com.hw2furkankemikli.util.TimeDateUtil;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by furkankemikli on 17.05.2016.
 */
public class ViewNoteActivity extends Activity {

    static boolean editFlag=false;
    String editedContentText;
    String editedTitleText;
    String editedLocation;
    String noteId;
    String imageBase64String;
    SqlUtil sqlUtil;
    NoteBean note;
    private TextView txtLocation;
    ImageView imageEditView;
    EditText editNote;
    EditText editTitle;


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
        setContentView(R.layout.activity_newnote);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        txtLocation = (TextView) findViewById(R.id.add_location);
        editNote=(EditText)findViewById(R.id.contentText);
        editTitle=(EditText)findViewById(R.id.titleText);
        imageEditView=(ImageView)findViewById(R.id.noteNewImage);

        noteId=getIntent().getStringExtra("noteId");
        sqlUtil=new SqlUtil(this);
        note=sqlUtil.getNote(noteId);

        if(note!=null)
        {
            editNote.setText(note.getNoteContent());
            editTitle.setText(note.getNoteTitle());
            txtLocation.setText(note.getNoteLocation());

            if(note.getNoteImage()!=null)
            {
                String encodedString=note.getNoteImage();
                String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if(bitmap!=null){
                    int scaleFactor = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, scaleFactor, true);
                    imageEditView.setImageBitmap(scaledBitmap);
                }
            }
        }
        //dev mode
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void add_photo(View v) {
        Toast.makeText(getApplicationContext(), "ADD PHOTO", Toast.LENGTH_SHORT).show();
        showFileChooser();
    }

    public void add_location(View v) {
        Toast.makeText(getApplicationContext(), "ATTACH LOCATION", Toast.LENGTH_SHORT).show();
        locationAdder();
    }

    public void delete_note(View v) {
        Toast.makeText(getApplicationContext(), "DELETE NOTE", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alert=new AlertDialog.Builder(ViewNoteActivity.this);
        alert.setTitle("Delete note?");
        alert.setPositiveButton("OK", new AlertDialog.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqlUtil.deleteNote(noteId);
                Toast.makeText(ViewNoteActivity.this,"Note Deleted!",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ViewNoteActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    public void save(View v) {
        Toast.makeText(getApplicationContext(), "SAVE", Toast.LENGTH_SHORT).show();
        editedContentText=editNote.getText().toString();
        editedTitleText=editTitle.getText().toString();
        editedLocation = txtLocation.getText().toString();


        if(editedTitleText.equals("")||editedTitleText==null)
        {
            Toast.makeText(ViewNoteActivity.this,"Title cannot be empty!",Toast.LENGTH_SHORT).show();
        }

        String noteDate=TimeDateUtil.returnCurrentTime();
        Boolean noteSyncFlag=false;
        NoteBean noteBean=new NoteBean(noteId,editedTitleText,editedContentText,noteDate,imageBase64String,editedLocation,noteSyncFlag);
        if(imageBase64String!=null)
        {
            Log.d("ViewNoteActivity edit",imageBase64String);
            noteBean.setNoteImage(imageBase64String);
        }
        if(txtLocation.getText().toString() != null)
            noteBean.setNoteLocation(txtLocation.getText().toString());

        sqlUtil.updateNote(noteBean,noteId);
        Toast.makeText(this, "Note Saved!", Toast.LENGTH_LONG).show();
        this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TakeNotesConstants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                int scaleFactor = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, scaleFactor, true);
                imageEditView.setImageBitmap(scaledBitmap);
                imageBase64String= ImageUtil.encodeString(bitmap);
                Log.d("ViewNoteActivity",imageBase64String);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), TakeNotesConstants.PICK_IMAGE_REQUEST);
    }

    private void locationAdder() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                DecimalFormat df = new DecimalFormat("#,###,##0.000000");

                //Toast.makeText(NoteDetailActivity.this, "Location attached", Toast.LENGTH_SHORT).show();
                txtLocation.setText("LOCATION: " + df.format(location.getLatitude()) + " | " + df.format(location.getLongitude()) );
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                txtLocation.setText("LOCATION: 41.0082" + " | " + "28.9784"); // Default is Istanbul's location
            }
        };

        if (ActivityCompat.checkSelfPermission(ViewNoteActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ViewNoteActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
    }
}