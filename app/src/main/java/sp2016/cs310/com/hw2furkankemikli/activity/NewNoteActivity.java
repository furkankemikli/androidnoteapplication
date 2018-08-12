package sp2016.cs310.com.hw2furkankemikli.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import sp2016.cs310.com.hw2furkankemikli.util.RandomUtil;
import sp2016.cs310.com.hw2furkankemikli.util.SqlUtil;
import sp2016.cs310.com.hw2furkankemikli.util.TimeDateUtil;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by furkankemikli on 16.05.2016.
 */
public class NewNoteActivity extends Activity {
    private TimeDateUtil timeDate;
    private EditText contentText;
    private EditText titleText;
    private TextView txtLocation;
    private ImageView imageView;
    private String imageBase64String;

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

        titleText = (EditText) findViewById(R.id.titleText);
        contentText = (EditText) findViewById(R.id.contentText);
        txtLocation = (TextView) findViewById(R.id.add_location);
        imageView=(ImageView)findViewById(R.id.noteNewImage);

        // dev mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
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
        Toast.makeText(getApplicationContext(), "THERE IS NOTHING TO DELETE", Toast.LENGTH_SHORT).show();
    }

    public void save(View v) {
        Toast.makeText(getApplicationContext(), "SAVE", Toast.LENGTH_SHORT).show();
        final String content = contentText.getText().toString();
        final String title = titleText.getText().toString();
        final String noteDate = TimeDateUtil.returnCurrentTime();
        final String noteId = RandomUtil.generateNoteId();
        if (title.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Title cannot be blank!", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("note id during saving", noteId
                    + noteDate);
            boolean noteSyncFlag = false;
            SqlUtil sqlUtil = new SqlUtil(NewNoteActivity.this);
            NoteBean note = new NoteBean(noteId, title, content, noteDate,
                    noteSyncFlag);
            //image added
            if (imageBase64String != null) {
                note.setNoteImage(imageBase64String);
            }
            if(txtLocation.getText().toString() != null)
                note.setNoteLocation(txtLocation.getText().toString());
            long result = sqlUtil.addNote(note);
            titleText.setText("");
            contentText.setText("");
            imageView.setImageResource(0);

            timeDate = null;
            if (result > 0)
                Toast.makeText(getApplicationContext(), "Note saved!",
                        Toast.LENGTH_LONG).show();
            this.finish();
        }
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
                imageView.setImageBitmap(scaledBitmap);
                imageBase64String= ImageUtil.encodeString(bitmap);
                Log.d("NewNoteActivity",imageBase64String);
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

        if (ActivityCompat.checkSelfPermission(NewNoteActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(NewNoteActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
    }
}
