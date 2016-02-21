package ataa.warwickhack2016;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class that makes the connection between the app and the sql database.
 */
public class DatabaseConnecter {

    private Activity mainActivity;
    private Connection conn;
    private Statement sqlState;
    private AdjustEnvironment adjustEnvironment;

    /**
     * Initialises the connection
     * @param main The main activity of the app
     */
    public DatabaseConnecter(Activity main) {

        mainActivity = main;

        // Constructing the class that manipulates the wifi, brightness, sound profile
        adjustEnvironment = new AdjustEnvironment(main);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(main.getString(R.string.server_url), main.getString(R.string.username), main.getString(R.string.password));
            sqlState = conn.createStatement();
        }
        catch (ClassNotFoundException ex) {
             // won't happen
        }
        catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
        }
    }

    /**
     * Returns a string representing the link to the lecture notes by sending a query to the database
     * @param moduleID The ID of the module
     * @return An http link
     */
    public String getLecnotes(int moduleID)
    {
        try {
            String select = "SELECT lecnotes FROM module WHERE id = '" + moduleID + "'";
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("lecnotes");
            return "None";
        } catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }

    }

    /**
     * Returns the name of the lecturer by sending an sql request
     * @param moduleID The id of the module
     * @return A string representing the name of the lecturer
     */
    public String getLecturerName(int moduleID) {

        try {
            String select = "SELECT lecturer_name FROM module WHERE id = '" + moduleID + "'";
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("lecturer_name");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    /**
     * Returns the link to the feedback survey from the end of the lecture
     * @param moduleID The id of the module
     * @return The http link
     */
    public String getSurvey(int moduleID) {

        try {
            String select = "SELECT survey FROM module WHERE id = '" + moduleID + "'";
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("survey");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    /**
     * Return the name of a module, given its ID
     * @param moduleID The id of the module
     * @return The name of the corresponding module
     */
    public String getModuleName(int moduleID) {
        try {
            String select = "SELECT module_name FROM module WHERE id = '" + moduleID + "'";
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("module_name");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    /**
     * Return the id of the module currently scheduled to be given in the room
     * @param roomID The id of the room
     * @return The if of the current module
     */
    public int getModule(int roomID)  {
        try {
            Date myDate = new Date();
            SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format (myDate);


            String select = "SELECT room" + roomID + ".moduleID FROM room" + roomID + ", module WHERE module.id = room" + roomID + ".moduleID " +
                    "AND room" + roomID + ".start_time <= '" + time + "' AND room" + roomID + ".end_time >= '" + time + "'";

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

    /**
     * Return whether the last action of the module was going in the room or outside of it
     * @param studentID
     * @return Either "in" or "out"
     */
    public String getPurpose(int studentID) {
        try {
            String select = "SELECT purpose FROM student WHERE id = '" + studentID + "'";
            ResultSet results = sqlState.executeQuery(select);

            if(results.next())
                return results.getString("purpose");
            return "None";
        } catch (SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
            return "";
        }
    }

    /**
     * Sends the current time to the "in" purpose of the database
     * @param studentID The id of the student
     * @param time The current time
     */
    public void setTimeIn(int studentID, String time) {
        try {
            adjustEnvironment.begin();
            String query = "UPDATE student SET time_in = '" + time + "', time_out = NULL WHERE id = " + studentID;
            sqlState.executeUpdate(query);
        } catch(SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
        }
    }

    /**
     * Sends the current time to the "out" purpose of the database
     * @param studentID The id of the student
     * @param time The current time
     */
    public void setTimeOut(int studentID, String time) {

        try {
            adjustEnvironment.end();
            String query = "UPDATE student SET time_out = '" + time + "' WHERE id = " + studentID;
            sqlState.executeUpdate(query);
        } catch(SQLException ex) {

            System.out.println("Something wrong: " + ex.getMessage());
        }

    }

    /**
     * Changes the database purpose from in to out or the other way around
     * @param studentID The id of the student.
     */
    public void setPurpose(int studentID) {

        try {
            Date myDate = new Date();
            SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format(myDate);


            String purpose = getPurpose(studentID);

            if(purpose.equals("in")) {

                String query = "UPDATE student SET purpose = 'out' WHERE id = " + studentID;
                setTimeOut(studentID, time);
                sqlState.executeUpdate(query);
            } else if(purpose.equals("out")) {

                String query = "UPDATE student SET purpose = 'in' WHERE id = " + studentID;
                setTimeIn(studentID, time);
                sqlState.executeUpdate(query);
            } else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setMessage("There is no student with such an ID!").setTitle("Alert!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        mainActivity.finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        } catch (SQLException ex) {
            System.out.println("Something wrong: " + ex.getMessage());
        }

    }

}

