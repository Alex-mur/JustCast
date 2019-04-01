package ga.justdevelops.justcast.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ga.justdevelops.justcast.data.ProfileMgmt;

public class WeatherFragmentPagerAdapter extends FragmentPagerAdapter {
    public WeatherFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragmentContent.newInstance(position);
    }

    @Override
    public int getCount() {
        return ProfileMgmt.getInstance().getCitiesCount();
    }
}
