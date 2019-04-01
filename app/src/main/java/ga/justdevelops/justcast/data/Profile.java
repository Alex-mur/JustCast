package ga.justdevelops.justcast.data;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Profile implements Serializable {

    CopyOnWriteArrayList<City> citiesList;

    public Profile() {
        citiesList = new CopyOnWriteArrayList<>();
    }

}
