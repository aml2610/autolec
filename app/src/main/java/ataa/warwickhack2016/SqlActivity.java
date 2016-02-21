package ataa.warwickhack2016;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class SqlActivity {

    Activity mainActivity;
    Connection conn;
    Statement sqlState;

    public SqlActivity(Activity main) {
        mainActivity = main;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://db-uni.cntvjeyihz8y.us-west-2.rds.amazonaws.com/db_uni", "ataa_admin", "ampulamare123");
            sqlState = conn.createStatement();
        }
        catch (ClassNotFoundException ex) {
             // won't happen
        }
        catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
        }
    }

    public String getLecnotes(int moduleID) {

        try {
            String select = "SELECT lecnotes FROM module where id = '" + moduleID + "'"; // aici bagai ca sa nu primeasca decat de la ce vrem link
            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("lecnotes");
            return "http://bbc.co.uk";
        } catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }

    }

    public int getModule(int roomID)  {
        try {

            Date myDate = new Date();
            SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format (myDate);

            System.out.println(time);

            String select = "SELECT room" + roomID + ".moduleID FROM room" + roomID + ", module WHERE module.id = room" + roomID + ".moduleID " +
                    "AND room" + roomID + ".start_time <= '" + time + "' AND room" + roomID + ".end_time >= '" + time + "'";

            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next()) {

                return results.getInt("moduleID");
            }
            return 0;

        } catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
            return 0;
        }

    }





}

