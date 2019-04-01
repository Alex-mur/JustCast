package ga.justdevelops.justcast.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.City;

public class WeatherListRecyclerAdapter extends RecyclerView.Adapter<WeatherListRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ItemClickListener clickListener;
    private City city;
    private SimpleDateFormat dateFormat;

    public WeatherListRecyclerAdapter(Context contex, City city) {
        this.context = contex;
        this.city = city;
        this.inflater = LayoutInflater.from(context);
        this.dateFormat = new SimpleDateFormat("dd MMMM - HH:mm", Locale.ENGLISH);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.weather_list_recycler_item, parent, false);
        return new WeatherListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (city.getIconNames().get(position) != null) {
            holder.weatherListImg.setImageDrawable(getDrawableByName(city.getIconNames().get(position)));
        } else {
            holder.weatherListImg.setImageDrawable(getDrawableByName("w50n"));

        }

        if (city.getDates().get(position) != null) {
            holder.weatherListDate.setText(dateFormat.format(city.getDates().get(position)));
        } else {
            holder.weatherListDate.setText("-");
        }

        if (city.getTemperatureList().get(position) != null) {
            holder.weatherListTemperature.setText(city.getTemperatureList().get(position) + " °C");
        } else {
            holder.weatherListTemperature.setText("-");
        }

        if (city.getWindList().get(position) != null) {
            holder.weatherListWind.setText(city.getWindList().get(position) + "m/s");
        } else {
            holder.weatherListWind.setText("-");
        }

        if (city.getHumidityList().get(position) != null) {
            holder.weatherListHumidity.setText(city.getHumidityList().get(position) + "%");
        } else {
            holder.weatherListHumidity.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return city.getTemperatureList().size();
    }

    void setClickListener(WeatherListRecyclerAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private Drawable getDrawableByName(String name) {
        int resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return context.getDrawable(resourceId);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView weatherListImg;
        TextView weatherListDate;
        TextView weatherListTemperature;
        TextView weatherListWind;
        TextView weatherListHumidity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            weatherListImg = itemView.findViewById(R.id.weather_list_img);
            weatherListDate = itemView.findViewById(R.id.weather_list_date);
            weatherListTemperature = itemView.findViewById(R.id.weather_list_temperature);
            weatherListWind = itemView.findViewById(R.id.weather_list_wind_text);
            weatherListHumidity = itemView.findViewById(R.id.weather_list_humidity_text);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
