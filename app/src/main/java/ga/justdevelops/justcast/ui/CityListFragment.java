package ga.justdevelops.justcast.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import ga.justdevelops.justcast.R;
import ga.justdevelops.justcast.data.ProfileMgmt;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class CityListFragment extends BaseFragment {

    private RecyclerView cityListRecycler;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_list_fragment, container, false);
    }

    @Override
    protected void initLayout(final View view, Bundle savedInstanceState) {

        cityListRecycler = view.findViewById(R.id.cityListRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        cityListRecycler.setLayoutManager(linearLayoutManager);

        final CityListRecyclerAdapter cityListRecyclerAdapter = new CityListRecyclerAdapter(getContext(), ProfileMgmt.getInstance().getCitiesList());
        cityListRecycler.setAdapter(cityListRecyclerAdapter);

        cityListRecyclerAdapter.setOnItemClickListener(new CityListRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final int position, View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.cities_list_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case (R.id.citiesListPopupRemove):
                                ProfileMgmt.getInstance().removeCity(position);
                                if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                                    getBaseActivity().addFragment(new WeatherFragment());
                                    getBaseActivity().addFragment_right(new CityListFragment());
                                } else {
                                    getBaseActivity().addFragment(new CityListFragment());
                                }
                                break;

                            case (R.id.citiesListPopupMakePrimary):
                                ProfileMgmt.getInstance().makeCityPrimary(position);
                                if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                                    getBaseActivity().addFragment(new WeatherFragment());
                                    getBaseActivity().addFragment_right(new CityListFragment());
                                } else {
                                    getBaseActivity().addFragment(new WeatherFragment());
                                }
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }

            @Override
            public void onItemLongPress(int position, View v) {

            }
        });

        fab = getBaseActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                    getBaseActivity().addFragment_right(new AddCityFragment());
                } else {
                    getBaseActivity().addFragment(new AddCityFragment());
                }
            }
        });
    }

}
