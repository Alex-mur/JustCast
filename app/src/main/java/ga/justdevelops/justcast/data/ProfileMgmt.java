package ga.justdevelops.justcast.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProfileMgmt {
    private static final ProfileMgmt ourInstance = new ProfileMgmt();
    public static boolean isLoaded = false;
    private Profile profile;
    private File filesDir;
    private String fileName;
    private ConcurrentLinkedDeque<City> updateQueue;

    private ProfileMgmt() {
        this.profile = new Profile();
        this.updateQueue = new ConcurrentLinkedDeque();
    }

    public static ProfileMgmt getInstance() {
        return ourInstance;
    }

    public void setFilesDir(File filesDir) {
        this.filesDir = filesDir;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean addCity(City city) {
        for (City currentCity : profile.citiesList) {
            if (currentCity.getCityName().equalsIgnoreCase(city.getCityName())) {
                return false;
            }
        }

        profile.citiesList.add(city);
        saveProfile();
        return true;
    }

    public void addCity(int position, City city) {
        profile.citiesList.add(position, city);
    }

    public void removeCity(int position) {
        profile.citiesList.remove(position);
        saveProfile();
    }

    public CopyOnWriteArrayList<City> getCitiesList() {
        return profile.citiesList;
    }

    public void saveProfile() {
        try {
            File profilePath = new File(filesDir, fileName);

            if (profilePath.exists()) {
                RandomAccessFile raf = new RandomAccessFile(profilePath, "rw");
                raf.setLength(0);
                raf.close();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(profilePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(profile);
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProfile() {
        File profilePath = new File(filesDir, fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(profilePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            profile = (Profile) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            isLoaded = true;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeCityPrimary(int position) {
        City tmpCity = profile.citiesList.get(position);
        profile.citiesList.remove(position);
        addCity(0, tmpCity);
        saveProfile();
    }

    public int getCitiesCount() {
        return profile.citiesList.size();
    }

    public ConcurrentLinkedDeque getUpdateQueue() {
        return updateQueue;
    }
}
