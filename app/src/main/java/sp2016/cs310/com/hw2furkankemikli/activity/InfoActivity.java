package sp2016.cs310.com.hw2furkankemikli.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import sp2016.cs310.com.hw2furkankemikli.R;

/**
 * Created by furkankemikli on 16.05.2016.
 */
public class InfoActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_info);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
}
