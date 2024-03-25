
package Model;

import ConnexionHTTP.Callback;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

public class DbManager extends Observable implements Callback {
    Connection con;
    // https://blog.paumard.org/cours/jdbc/chap02-apercu-exemple.html
    // https://learn.microsoft.com/fr-fr/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver16

    public DbManager(String url) throws SQLException {
        con = DriverManager.getConnection(url);
    }

    public Boolean isOpen() {
        return con != null;
    }

    public Boolean createTable() {
        String query = "CREATE TABLE IF NOT EXISTS pollution(id INTEGER PRIMARY KEY, dt INTEGER, aqi INTEGER, co REAL, no REAL, no2 REAL, o3 REAL, so2 REAL, lat REAL, lon REAL)";
        return executeUpdate(query);
    }

    public Boolean addData(int dt, int aqi, double co, double no, double no2, double o3, double so2, double lat, double lon) {
        if (!entryExists(dt, lat, lon)) {
            String query = "INSERT INTO pollution (dt,aqi,co,no,no2,o3,so2,lat,lon) VALUES (" + dt + "," + aqi + "," + co + "," + no + "," + no2 + "," + o3 + "," + so2 + ",'" + lat + "','" + lon + "')";
            return executeUpdate(query);
        }

        return false;
    }


    public Boolean removeData(int dt, String city) {
        String query = "DELETE FROM pollution WHERE dt = " + dt + " AND city = '" + city + "'";
        return executeUpdate(query);
    }


    public void printAllData() {
        String query = "SELECT * FROM pollution";
        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            ResultSet resultSet = statement.executeQuery(query);

            // Print results from select statement
            while (resultSet.next()) {
                int dt = resultSet.getInt("dt");
                int aqi = resultSet.getInt("aqi");
                String city = resultSet.getString("city");

                System.out.println(city + "===" + dt + " " + aqi);
            }
        } catch (SQLException e) {
            System.out.println("Error : printAllData()" + e.getMessage());
        }
    }

    public Boolean entryExists(int dt, double lat, double lon) {
        Boolean exists = false;

        String query = "SELECT dt FROM pollution WHERE dt = " + dt + " AND lat = '" + lat + "' AND lon = '" + lon + "'";
        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            ResultSet resultSet = statement.executeQuery(query);

            // Print results from select statement
            exists = resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error : entryExists()");
        }

        return exists;
    }

    public Boolean removeAllData() {
        String query = "DELETE FROM pollution";
        return executeUpdate(query);
    }

    private Boolean executeUpdate(String query) {
        Boolean success = false;

        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            int number = statement.executeUpdate(query);

            if (number > 0) success = true;
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return success;
    }

    public JSONArray getData(double lat, double lon, int intervalInHours) {
        JSONArray JA = new JSONArray();

        String query = "SELECT * FROM pollution WHERE lat = " + lat + " AND lon = " + lon;
        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            ResultSet resultSet = statement.executeQuery(query);

            int olddt = 0;

            // Print results from select statement
            while (resultSet.next()) {
                int dt = resultSet.getInt("dt");

                if(dt - olddt >= intervalInHours * 3600) {
                    olddt = dt;

                    int aqi = resultSet.getInt("aqi");
                    int co = resultSet.getInt("co");
                    int no = resultSet.getInt("no");
                    int no2 = resultSet.getInt("no2");
                    int o3 = resultSet.getInt("o3");
                    int so2 = resultSet.getInt("so2");

                    JSONObject data = new JSONObject();
                    data.put("dt", dt);
                    data.put("aqi", aqi);
                    data.put("co", co);
                    data.put("no", no);
                    data.put("no2", no2);
                    data.put("o3", o3);
                    data.put("so2", so2);
                    data.put("lat", lat);
                    data.put("lon", lon);

                    JA.put(data);
                }
            }
        } catch (Exception e) {
            System.out.println("Error : getData()" + e.getMessage());
        }
        return JA;
    }

    @Override
    public void onWorkDone(JSONObject JO) throws JSONException {
        JSONArray results = JO.getJSONArray("list");
        JSONObject coord = JO.getJSONObject("coord");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);

            long epoch = result.getLong("dt"); // to long and millisecond

            int aqi = result.getJSONObject("main").getInt("aqi");
            int co = result.getJSONObject("components").getInt("co");
            int no = result.getJSONObject("components").getInt("no");
            int no2 = result.getJSONObject("components").getInt("no2");
            int o3 = result.getJSONObject("components").getInt("o3");
            int so2 = result.getJSONObject("components").getInt("so2");

            double lat = coord.getDouble("lat");
            double lon = coord.getDouble("lon");

            if (!entryExists((int) epoch, lat, lon)) {
                addData((int) epoch, aqi, co, no, no2, o3, so2, lat, lon);
            }
        }


        this.setChanged();
        this.notifyObservers();
    }
}
