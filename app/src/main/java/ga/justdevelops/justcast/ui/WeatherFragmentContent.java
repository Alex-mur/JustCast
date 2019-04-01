package ga.justdevelops.justcast.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.City;
import ga.justdevelops.justcast.data.ProfileMgmt;

public class WeatherFragmentContent extends BaseFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private City currentCity;
    private TextView tvCityName;
    private TextView tvTemperature;
    private TextView tvWeather;
    private TextView tvLastUpdate;
    private ImageView ivWeather;
    private RecyclerView weatherListRecycler;
    private int pageNumber;

    public WeatherFragmentContent() {
    }

    static WeatherFragmentContent newInstance(int page) {
        WeatherFragmentContent weatherFragment = new WeatherFragmentContent();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        weatherFragment.setArguments(arguments);
        return weatherFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        currentCity = ProfileMgmt.getInstance().getCitiesList().get(pageNumber);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        tvCityName = view.findViewById(R.id.tv_cityName);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvWeather = view.findViewById(R.id.tv_weather);
        tvLastUpdate = view.findViewById(R.id.tv_LastUpdate);
        ivWeather = view.findViewById(R.id.ivWeather);


        if (currentCity != null) {
            tvCityName.setVisibility(View.VISIBLE);
            tvCityName.setText(currentCity.getCityName());
            if (currentCity.getLastUpdate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                tvLastUpdate.setText("last update - " + dateFormat.format(currentCity.getLastUpdate()));
            }

            if (currentCity.getTemperatureList().get(0) != null) {
                tvTemperature.setText(currentCity.getTemperatureList().get(0));
            } else {
                tvTemperature.setText(R.string.no_data);
            }
            if (currentCity.getWeather().get(0) != null) {
                tvWeather.setText(currentCity.getWeather().get(0));

            } else {
                tvWeather.setText(R.string.no_data);
            }

            if (currentCity.getIconNames().get(0) != null) {
                switch (currentCity.getIconNames().get(0)) {
                    case ("w01d"):
                        ivWeather.setImageResource(R.drawable.clear);
                        break;
                    case ("w01n"):
                        ivWeather.setImageResource(R.drawable.clear);
                        break;
                    case ("w02d"):
                        ivWeather.setImageResource(R.drawable.clowdy);
                        break;
                    case ("w02n"):
                        ivWeather.setImageResource(R.drawable.clowdy);
                        break;
                    case ("w03d"):
                        ivWeather.setImageResource(R.drawable.clowdy);
                        break;
                    case ("w03n"):
                        ivWeather.setImageResource(R.drawable.clowdy);
                        break;
                    case ("w04d"):
                        ivWeather.setImageResource(R.drawable.clowdy);
                        break;
                    case ("w04n"):
                        ivWeather.setImageResource(R.drawable.clowdy);
                        break;
                    case ("w09d"):
                        ivWeather.setImageResource(R.drawable.rainy);
                        break;
                    case ("w09n"):
                        ivWeather.setImageResource(R.drawable.rainy);
                        break;
                    case ("w10d"):
                        ivWeather.setImageResource(R.drawable.rainy);
                        break;
                    case ("w10n"):
                        ivWeather.setImageResource(R.drawable.rainy);
                        break;
                    case ("w11d"):
                        ivWeather.setImageResource(R.drawable.lightning);
                        break;
                    case ("w11n"):
                        ivWeather.setImageResource(R.drawable.lightning);
                        break;
                    case ("w13d"):
                        ivWeather.setImageResource(R.drawable.snowy);
                        break;
                    case ("w13n"):
                        ivWeather.setImageResource(R.drawable.snowy);
                        break;
                    case ("w50d"):
                        ivWeather.setImageResource(R.drawable.clear);
                        break;
                    case ("w50n"):
                        ivWeather.setImageResource(R.drawable.clear);
                        break;
                }

            } else {
                ivWeather.setBackground(getDrawableByName("clear"));
            }

        } else {
            tvCityName.setVisibility(View.GONE);
            tvTemperature.setText(R.string.no_data);
        }

        weatherListRecycler = view.findViewById(R.id.weatherListRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        weatherListRecycler.setLayoutManager(linearLayoutManager);
        WeatherListRecyclerAdapter weatherListRecyclerAdapter = new WeatherListRecyclerAdapter(getContext(), currentCity);
        weatherListRecycler.setAdapter(weatherListRecyclerAdapter);
    }

    private Drawable getDrawableByName(String name) {
        int resourceId = getBaseActivity().getResources().getIdentifier(name, "drawable", getBaseActivity().getPackageName());
        return getBaseActivity().getDrawable(resourceId);
    }
}
