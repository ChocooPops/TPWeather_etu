
package Model;

import ConnexionHTTP.Callback;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherReport extends Observable implements Callback {
    String main, description;
    double temp, temp_min, temp_max, wind_speed;
    double lon, lat;
    ImageIcon icon;
    boolean isWorkDone;
    int pressure, humidity; 

    public WeatherReport() {
        temp = 0.0;
        temp_min = 0.0;
        temp_max = 0.0;
        lon = 0.0;
        lat = 0.0;
        main = new String();
        description = new String();
        icon = null;
        isWorkDone = false;
        this.pressure = 0; 
        this.humidity = 0; 
        this.wind_speed = 0; 
    }

    @Override
    public String toString() {
        return "WeatherReport{" + "main=" + main + ", description=" + description + ", temp=" + temp + ", temp_min=" + temp_min + ", temp_max=" + temp_max + ", lon=" + lon + ", lat=" + lat + '}';
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public double getTemp() {
        return temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getHum(){
        return this.humidity; 
    }
    public int getPress(){
        return this.pressure;
    }
    public double getWind(){
        return this.wind_speed; 
    }
    public boolean isWorkDone() {
        return isWorkDone;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public void onWorkDone(JSONObject jsonObj) throws JSONException {
        isWorkDone = true;

        JSONObject mainObj = jsonObj.getJSONObject("main");

        this.setTemp(mainObj.getDouble("temp"));
        this.setTemp_min(mainObj.getDouble("temp_min"));
        this.setTemp_max(mainObj.getDouble("temp_max"));
        this.setLat(jsonObj.getJSONObject("coord").getDouble("lat"));
        this.setLon(jsonObj.getJSONObject("coord").getDouble("lon"));
        
        this.pressure = mainObj.getInt("pressure"); 
        this.humidity = mainObj.getInt("humidity");  
        mainObj = jsonObj.getJSONObject("wind");
        
        this.wind_speed = mainObj.getDouble("speed"); 
    
        this.setMain(jsonObj.getJSONArray("weather").getJSONObject(0).getString("main"));
        this.setDescription(jsonObj.getJSONArray("weather").getJSONObject(0).getString("description"));
        String iconUrl = "https://openweathermap.org/img/w/" + jsonObj.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png";

        try {
            this.setIcon(new ImageIcon(new URL(iconUrl)));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        this.setChanged();
        this.notifyObservers();
    }
}
