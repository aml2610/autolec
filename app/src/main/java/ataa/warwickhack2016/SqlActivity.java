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
    AdjustEnvironment adjustEnvironment;

    public SqlActivity(Activity main) {
        mainActivity = main;

        // Constructing the class that manipulates the wifi, brightness, sound profile
        adjustEnvironment = new AdjustEnvironment(main);

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
            String select = "SELECT lecnotes FROM module WHERE id = '" + moduleID + "'"; // aici bagai ca sa nu primeasca decat de la ce vrem link
            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("lecnotes");
            return "None";
        } catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }

    }

    public String getLecturerName(int moduleID) {

        try {
            String select = "SELECT lecturer_name FROM module WHERE id = '" + moduleID + "'";
            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("lecturer_name");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    public String getSurvey(int moduleID) {

        try {
            String select = "SELECT survey FROM module WHERE id = '" + moduleID + "'";
            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("survey");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    public String getModuleName(int moduleID) {

        try {
            String select = "SELECT module_name FROM module WHERE id = '" + moduleID + "'";
            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("module_name");
            return "None";
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

    public String getPurpose(int studentID) {

        try {
            String select = "SELECT purpose FROM student WHERE id = '" + studentID + "'";
            System.out.println(select);
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("purpose");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    public void setTimeIn(int studentID, String time) {

        try {
            adjustEnvironment.begin();
            // TODO - set time out to null
            String query = "UPDATE student SET time_in = '" + time + "', time_out = NULL WHERE id = " + studentID;
            System.out.println(query);
            sqlState.executeUpdate(query);
        } catch(SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
        }

    }

    public void setTimeOut(int studentID, String time) {

        try {
            adjustEnvironment.end();
            String query = "UPDATE student SET time_out = '" + time + "' WHERE id = " + studentID;
            System.out.println(query);
            sqlState.executeUpdate(query);
        } catch(SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
        }

    }

    public void setPurpose(int studentID) {

        try {

            Date myDate = new Date();
            SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format(myDate);

            System.out.println(time);

            String purpose = getPurpose(studentID);

            if(purpose.equals("in")) {

                String query = "UPDATE student SET purpose = 'out' WHERE id = " + studentID;
                setTimeOut(studentID, time);
                sqlState.executeUpdate(query);
            } else if(purpose.equals("out")) {

                String query = "UPDATE student SET purpose = 'in' WHERE id = " + studentID;
                setTimeIn(studentID, time);
                sqlState.executeUpdate(query);
            }

        } catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());

        }


    }

}

