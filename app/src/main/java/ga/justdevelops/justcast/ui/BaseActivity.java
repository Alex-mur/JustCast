package ga.justdevelops.justcast.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.ProfileMgmt;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_base);
        initLayout();
        initProfile();
    }

    private void initProfile() {
        ProfileMgmt.getInstance().setFilesDir(getApplicationContext().getFilesDir());
        ProfileMgmt.getInstance().setFileName(getResources().getString(R.string.settingsFileName));
        ProfileMgmt.getInstance().loadProfile();
    }

    private void initLayout() {
        addFragment(new WeatherFragment());
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            addFragment_right(new CityListFragment());
        }
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void addFragment_right(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame_right, fragment)
                .commit();
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof CityListFragment) {
            addFragment(new WeatherFragment());
        } else if (getCurrentFragment() instanceof AddCityFragment) {
            addFragment(new CityListFragment());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.options_menu_item) {
            addFragment(new CityListFragment());
        }
        return super.onOptionsItemSelected(item);
    }
}
