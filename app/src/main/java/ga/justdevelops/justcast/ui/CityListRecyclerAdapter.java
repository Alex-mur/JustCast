package ga.justdevelops.justcast.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.CopyOnWriteArrayList;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.City;

public class CityListRecyclerAdapter extends RecyclerView.Adapter<CityListRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private CopyOnWriteArrayList<City> cityList;
    private OnItemClickListener onItemClickListener;

    public CityListRecyclerAdapter(Context context, CopyOnWriteArrayList<City> cityList) {
        this.cityList = cityList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.city_list_recycler_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(cityList.get(position).getCityName());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);

        void onItemLongPress(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        TextView textView;
        LinearLayout cardLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_cityNameItem);
            cardLayout = itemView.findViewById(R.id.card_view_linear_layout);
            //for handling click and long press
            itemView.setOnClickListener(this);
            //for handling long press
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemLongPress(getAdapterPosition(), view);
            return true;
        }
    }

}
