package ga.justdevelops.justcast.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class City implements Serializable {

    private String cityName;
    private String id;
    private ArrayList<String> weather;
    private ArrayList<String> temperatureList;
    private ArrayList<String> windList;
    private ArrayList<String> humidityList;
    private ArrayList<Date> dates;
    private ArrayList<String> iconNames;
    private String[] coordinates;
    private Date lastUpdate;


    public City(String cityName) {
        this.cityName = cityName;
        this.coordinates = new String[]{};
        this.temperatureList = new ArrayList<>();
        this.dates = new ArrayList<>();
        this.weather = new ArrayList<>();
        this.iconNames = new ArrayList<>();
        this.windList = new ArrayList<>();
        this.humidityList = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            temperatureList.add(null);
            dates.add(null);
            weather.add(null);
            iconNames.add(null);
            humidityList.add(null);
            windList.add(null);
        }
    }

    public City(String lat, String lon) {
        this.coordinates = new String[]{lat, lon};
        this.temperatureList = new ArrayList<>();
        this.dates = new ArrayList<>();
        this.weather = new ArrayList<>();
        this.iconNames = new ArrayList<>();
        this.windList = new ArrayList<>();
        this.humidityList = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            temperatureList.add(null);
            dates.add(null);
            weather.add(null);
            iconNames.add(null);
            humidityList.add(null);
            windList.add(null);
        }
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ArrayList<String> getTemperatureList() {
        return temperatureList;
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCoordinates() {
        return coordinates;
    }

    public ArrayList<String> getWeather() {
        return weather;
    }

    public void setIconName(int position, String iconName) {
        iconNames.set(position, "w" + iconName);
    }

    public ArrayList<String> getIconNames() {
        return iconNames;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<String> getWindList() {
        return windList;
    }

    public ArrayList<String> getHumidityList() {
        return humidityList;
    }
}
