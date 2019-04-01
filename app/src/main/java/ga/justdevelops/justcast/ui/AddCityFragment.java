package ga.justdevelops.justcast.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.City;
import ga.justdevelops.justcast.data.ProfileMgmt;
import ga.justdevelops.justcast.network.OpenWeatherHandler;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class AddCityFragment extends BaseFragment {

    private TextInputLayout cityInput;
    private Button addCityBtn;
    private Button useGeolocation;
    private LocationManager locationManager;
    private String locationProvider;
    private Criteria criteria;
    private City currentCity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_city_fragment, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        cityInput = view.findViewById(R.id.addCity_textInput);
        useGeolocation = view.findViewById(R.id.btn_useGeolocation);
        useGeolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocationPermissions()) {
                    getLocation();
                } else {
                    Toast.makeText(getContext(), getString(R.string.geo_perm_tryAgain), Toast.LENGTH_LONG).show();
                }
            }
        });

        addCityBtn = view.findViewById(R.id.btn_addCity);
        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(cityInput.getEditText()).getText().length() != 0) {
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    String inputCityName = cityInput.getEditText().getText().toString().trim();

                    checkIsCityExists(inputCityName);
                    if (currentCity.getId() == null) return;
                    addCityToList();

                } else {
                    Toast.makeText(getContext(), getString(R.string.msg_locationFieldEmpty), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkIsCityExists(String cityName) {
        currentCity = new City(cityName);
        OpenWeatherHandler owh = new OpenWeatherHandler();
        owh.initRetrofit();
        owh.requestRetrofitForCity(this, currentCity, OpenWeatherHandler.MODE, OpenWeatherHandler.UNITS, OpenWeatherHandler.API_KEY);
        Toast.makeText(getContext(), getString(R.string.city_loading_in_progress), Toast.LENGTH_LONG).show();
    }

    private void checkIsCityExist(Location location) {
        currentCity = new City(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        OpenWeatherHandler owh = new OpenWeatherHandler();
        owh.initRetrofit();
        owh.requestRetrofitForCityByCoord(this, currentCity, OpenWeatherHandler.MODE, OpenWeatherHandler.UNITS, OpenWeatherHandler.API_KEY);
        Toast.makeText(getContext(), getString(R.string.city_loading_by_coordinates), Toast.LENGTH_LONG).show();
    }

    private boolean checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {
            ActivityCompat.requestPermissions(getBaseActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            return false;
        }
    }

    private void getLocation() {
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationProvider = locationManager.getBestProvider(criteria, true);
        }

        if (locationProvider != null) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    checkIsCityExist(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            }, null);
        }
    }

    public void setCityInputText(String text) {
        Objects.requireNonNull(cityInput.getEditText()).setText(text);
    }

    public void addCityToList() {
        if (ProfileMgmt.getInstance().addCity(currentCity)) {

            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                getBaseActivity().addFragment_right(new CityListFragment());
            } else {
                getBaseActivity().addFragment(new CityListFragment());
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.msg_locationAlreadyInList), Toast.LENGTH_LONG).show();
        }
    }
}
