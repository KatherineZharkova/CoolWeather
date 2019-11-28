package ru.cocovella.WeatherApp.Model;

import java.util.ArrayList;
import java.util.Arrays;
import ru.cocovella.WeatherApp.R;


public class Settings implements Keys, Observable{
    private static Settings instance;
    private String city;
    private String description;
    private String icon;
    private int temperature;
    private String humidity;
    private ArrayList<String> citiesChoice;
    private boolean isHumidityCB;
    private String wind;
    private boolean isWindCB;
    private String barometer;
    private boolean isBarometerCB;
    private int themeID;
    private int serverResultCode;
    private ArrayList<ForecastLoader.Forecast> forecasts = new ArrayList<>();
    private ArrayList<Observer> observers = new ArrayList<>();
    private String message;


    private Settings() {
        city = "";
        themeID = R.style.ColdTheme;
        serverResultCode = 0;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemperature() {
        return temperature;
    }

    void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public ArrayList<String> getCitiesChoice() {
        return citiesChoice;
    }

    public void setCitiesChoice(String[] array) {
        if (citiesChoice == null) {
            this.citiesChoice = new ArrayList<>(Arrays.asList(array));
        }
    }

    public String getHumidity() {
        return humidity;
    }

    void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public boolean isHumidityCB() {
        return isHumidityCB;
    }

    public void setHumidityCB(boolean humidityCB) {
        isHumidityCB = humidityCB;
    }

    public String getWind() {
        return wind;
    }

    void setWind(String wind) {
        this.wind = wind;
    }

    public boolean isWindCB() {
        return isWindCB;
    }

    public void setWindCB(boolean windCB) {
        isWindCB = windCB;
    }

    public String getBarometer() {
        return barometer;
    }

    void setBarometer(String barometer) {
        this.barometer = barometer;
    }

    public boolean isBarometerCB() {
        return isBarometerCB;
    }

    public void setBarometerCB(boolean barometerCB) {
        isBarometerCB = barometerCB;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getServerResultCode() {
        if (serverResultCode == CONFIRMATION_CODE) {
            return 1;
        } else if (serverResultCode == 0) {
            return 0;
        } else return -1;
    }

    void setServerResultCode(int resultCode) {
        this.serverResultCode = resultCode;
        notifyObservers();
    }

    public ArrayList<ForecastLoader.Forecast> getForecasts() {
        return forecasts;
    }

    void setForecasts(ArrayList<ForecastLoader.Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

}
