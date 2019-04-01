package ga.justdevelops.justcast.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.City;
import ga.justdevelops.justcast.data.ProfileMgmt;
import ga.justdevelops.justcast.network.OpenWeatherHandler;

public class WeatherFragment extends BaseFragment {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Button btnOptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ProfileMgmt.isLoaded) {
            ProfileMgmt.getInstance().loadProfile();
        }

        if (ProfileMgmt.isLoaded) {
            if (ProfileMgmt.getInstance().getCitiesList().size() == 0) {
                getBaseActivity().addFragment(new AddCityFragment());
            } else {
                updateWeatherData(ProfileMgmt.getInstance().getCitiesList());
            }
        } else {
            getBaseActivity().addFragment(new AddCityFragment());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_fragment_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        pager = getBaseActivity().findViewById(R.id.weather_pager);
        pagerAdapter = new WeatherFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        btnOptions = view.findViewById(R.id.btnOptions);

        if (getActivity().getResources().getConfiguration().orientation == 2) {
            btnOptions.setVisibility(View.GONE);
        }

        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.base);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.options_menu_item:
                                ((BaseActivity) getActivity()).addFragment(new CityListFragment());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void updateWeatherData(CopyOnWriteArrayList<City> citiesList) {
        ProfileMgmt.getInstance().getUpdateQueue().clear();
        for (City city : citiesList) {
            if (city.getLastUpdate() != null) {
                Date currentDate = new Date();
                long diff = currentDate.getTime() - city.getLastUpdate().getTime();
                if (diff <= 1800000) {
                    continue;
                }
            }

            ProfileMgmt.getInstance().getUpdateQueue().add(city);
        }

        if (ProfileMgmt.getInstance().getUpdateQueue().size() != 0) {
            new updateWeatherData().execute();
        }
    }

    public void reinitPager() {
        //pager.setAdapter(null);
        try {
            pagerAdapter = new WeatherFragmentPagerAdapter(getChildFragmentManager());
            pager.setAdapter(pagerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class updateWeatherData extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            OpenWeatherHandler owh = new OpenWeatherHandler();
            owh.initRetrofit();
            return owh.requestRetrofit(getBaseActivity(), ProfileMgmt.getInstance().getUpdateQueue(), OpenWeatherHandler.MODE, OpenWeatherHandler.UNITS, OpenWeatherHandler.API_KEY);
        }
    }
}
