
package ConnexionHTTP;

import tpweather.TPWeather;

public class ConnexionManager {

    public static final String URL_Weather = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=fr&appid=45d4acd652b305eb86e78d2451be8888" + TPWeather.APP_ID;
    public static final String URL_Pollution = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=%s&lon=%s&units=metric&lang=fr&appid=45d4acd652b305eb86e78d2451be8888" + TPWeather.APP_ID;
    private static ConnexionManager manager = null;
    private final Callback callWeather;
    private final Callback callPollution;

    private ConnexionManager(Callback callWeather, Callback callPollution) {
        this.callWeather = callWeather;
        this.callPollution = callPollution;
    }

    public static ConnexionManager getConnexionManager(Callback callW, Callback callP) {
        if (manager == null) {
            manager = new ConnexionManager(callW, callP); 
        }
        return manager;
    }

    public void loadWeather() {
        loadWeather("Lyon");
    }

    public void loadWeather(String city) {
        ConnexionThread connexionThread = new ConnexionThread(callWeather, URL_Weather.formatted(city).replaceAll(" ", "%20"));
        connexionThread.start();
        try {
            connexionThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadPollution() {
        loadPollution(45.75, 4.5833);
    }

    public void loadPollution(double lat, double lon) {
        ConnexionThread connexionThread = new ConnexionThread(callPollution, URL_Pollution.formatted(lat, lon));
        connexionThread.start();
    }
}
