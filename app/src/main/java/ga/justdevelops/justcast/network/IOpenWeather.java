package ga.justdevelops.justcast.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {

    @GET("/data/2.5/forecast")
    Call<OpenWeatherHandler.OpenWeatherReceiver> loadWeather(@Query("id") String cityID, @Query("mode") String mode, @Query("units") String units, @Query("appid") String keyApi);

    @GET("/data/2.5/forecast")
    Call<OpenWeatherHandler.OpenWeatherReceiver> loadWeatherByName(@Query("q") String cityName, @Query("mode") String mode, @Query("units") String units, @Query("appid") String keyApi);

    @GET("/data/2.5/forecast")
    Call<OpenWeatherHandler.OpenWeatherReceiver> loadWeatherByCoord(@Query("lat") String lat, @Query("lon") String lon, @Query("mode") String mode, @Query("units") String units, @Query("appid") String keyApi);

}
