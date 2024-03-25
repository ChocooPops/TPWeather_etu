/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpweather;

import Model.DbManager;
import View.Window;
import Model.WeatherReport;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONException;

/**
 *
 * @author apeyt
 */
public class TPWeather {
    public static String APP_ID = "";

    /**
     * @param args the command line arguments
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     * @throws JSONException
     */
    public static void main(String[] args) throws SQLException, IOException, InterruptedException, JSONException {
        
        String url = "jdbc:sqlite:data/pollution.sqlite"; // URL of the local database
        DbManager dbm = new DbManager(url);
        //dbm.removeAllData();
        dbm.createTable();

        /*if (dbm.isOpen())
        {
            dbm.createTable();   // Creates a table if it doesn't exist. Otherwise, it will use existing table.
            dbm.addData(1670310413,5);
            dbm.addData(1670346054,3);
            dbm.addData(1670382413,1);
            dbm.printAllData();
            dbm.removeData(1670346054);
            dbm.printAllData();
            dbm.removeAllData();
            dbm.printAllData();
            System.out.println("End");
        }
        else
        {
            System.out.println( "Database is not open!");
        }*/
          
        WeatherReport WR = new WeatherReport();

        /* Create and display the form */
        Window window = new Window(WR, dbm);
        window.setVisible(true);
        WR.addObserver(window);
        dbm.addObserver(window);
    }
    
}
