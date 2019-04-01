package ga.justdevelops.justcast.network;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import ga.justdevelops.justcast.BuildConfig;
import ga.justdevelops.justcast.data.City;
import ga.justdevelops.justcast.data.ProfileMgmt;
import ga.justdevelops.justcast.ui.AddCityFragment;
import ga.justdevelops.justcast.ui.BaseActivity;
import ga.justdevelops.justcast.ui.WeatherFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherHandler {
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String UNITS = "metric";
    public static final String MODE = "json";
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private IOpenWeather iOpenWeather;

    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iOpenWeather = retrofit.create(IOpenWeather.class);
    }

    public boolean requestRetrofit(final BaseActivity activity, final ConcurrentLinkedDeque<City> updateQueue, final String mode, final String units, final String keyApi) {

        while (updateQueue.size() > 0) {
            final City currentCity = updateQueue.poll();
            iOpenWeather.loadWeather(currentCity.getId(), mode, units, keyApi).enqueue(new Callback<OpenWeatherReceiver>() {

                @Override
                public void onResponse(Call<OpenWeatherReceiver> call, Response<OpenWeatherReceiver> response) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    for (int i = 0; i < response.body().list.size(); i++) {
                        currentCity.getTemperatureList().set(i, df.format(response.body().list.get(i).main.temp));
                        currentCity.getWeather().set(i, response.body().list.get(i).weather.get(0).main);
                        currentCity.setIconName(i, response.body().list.get(i).weather.get(0).icon);
                        currentCity.setLastUpdate(new Date());
                        currentCity.getWindList().set(i, String.valueOf(response.body().list.get(i).wind.speed));
                        currentCity.getHumidityList().set(i, String.valueOf(response.body().list.get(i).main.humidity));

                        try {
                            currentCity.getDates().set(i, dateFormat.parse(response.body().list.get(i).dt_txt));
                            ProfileMgmt.getInstance().saveProfile();

                            if (updateQueue.size() == 0) {
                                for (Fragment fr : activity.getSupportFragmentManager().getFragments()) {
                                    if (fr instanceof WeatherFragment) {
                                        ((WeatherFragment) fr).reinitPager();
                                    }
                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OpenWeatherReceiver> call, Throwable t) {
                    Log.d("RETROFIT_REQUEST_FAILED", t.getMessage());
                }
            });
        }
        return true;
    }

    public void requestRetrofitForCity(final AddCityFragment fragment, final City city, String mode, String units, String keyApi) {
        iOpenWeather.loadWeatherByName(city.getCityName(), mode, units, keyApi).enqueue(new Callback<OpenWeatherReceiver>() {

            @Override
            public void onResponse(Call<OpenWeatherReceiver> call, Response<OpenWeatherReceiver> response) {
                if (response.body() == null) {
                    Toast.makeText(fragment.getContext(), "Can not find the City '" + city.getCityName() + "'", Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.body().cod != 200) {
                    Toast.makeText(fragment.getContext(), "Cannot find the City '" + city.getCityName() + "'", Toast.LENGTH_LONG).show();
                    return;
                }

                city.setCityName(response.body().city.name);
                city.setId(Integer.toString(response.body().city.id));
                fragment.setCityInputText(city.getCityName());
                fragment.addCityToList();
            }

            @Override
            public void onFailure(Call<OpenWeatherReceiver> call, Throwable t) {

            }
        });
    }

    public void requestRetrofitForCityByCoord(final AddCityFragment fragment, final City city, String mode, String units, String keyApi) {
        iOpenWeather.loadWeatherByCoord(city.getCoordinates()[0], city.getCoordinates()[1], mode, units, keyApi).enqueue(new Callback<OpenWeatherReceiver>() {

            @Override
            public void onResponse(Call<OpenWeatherReceiver> call, Response<OpenWeatherReceiver> response) {
                if (response.body() == null) {
                    Toast.makeText(fragment.getContext(), "Can not find the city by coordinates", Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.body().cod != 200) {
                    Toast.makeText(fragment.getContext(), "Can not find the city by coordinates", Toast.LENGTH_LONG).show();
                    return;
                }

                city.setCityName(response.body().city.name);
                city.setId(Integer.toString(response.body().city.id));
                fragment.setCityInputText(city.getCityName());
                fragment.addCityToList();
            }

            @Override
            public void onFailure(Call<OpenWeatherReceiver> call, Throwable t) {

            }
        });
    }

    public class OpenWeatherReceiver {
        public int cod;
        public double message;
        public int cnt;
        public ArrayList<ListItem> list;
        public OWCity city;

        public class OWCity {
            public int id;
            public String name;
        }

        public class ListItem {
            public long dt;
            public Main main;
            public List<Weather> weather;
            public Clouds clouds;
            public Wind wind;
            public Rain rain;
            public Sys sys;
            public String dt_txt;

            public class Main {
                public double temp;
                public double temp_min;
                public double temp_max;
                public double pressure;
                public double sea_level;
                public double grnd_level;
                public double humidity;
                public double temp_kf;
            }

            public class Weather {
                public int id;
                public String main;
                public String description;
                public String icon;
            }

            public class Clouds {
                public int all;
            }

            public class Wind {
                public double speed;
                public double deg;
            }

            public class Rain {
                @SerializedName("3h")
                @Expose
                public float rainState;
            }

            public class Sys {
                public String pod;
            }
        }
    }

}
