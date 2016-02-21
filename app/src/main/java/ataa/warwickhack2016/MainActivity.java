package ataa.warwickhack2016;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;

/**
 * Our main activity class, opening the right instance of the app (settings or the lecture page)
 */
public class MainActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private SharedPreferences settings;
    private String link;
    private String surveyLink;

    // GUI components
    private Button lecnoteButton;
    private TextView textView;
    private Button surveyButton;

    private static SharedPreferences.Editor editor;

    // settings
    public static int studentID;
    public static boolean adjustWiFi;
    public static boolean adjustSound;
    public static boolean adjustBrightness;

    /**
     * Saves the settings from the checkboxes & student id textbox
     * @param sstudentID The id of the student
     * @param aadjustWiFi Whether the student wants the wifi to be toggled by the app
     * @param aadjustSound Whether the student wants the app to control the sound profile
     * @param aadjustBrightness Whether the student wants the app to control the brightness
     */
    public static void saveSettings(int sstudentID, boolean aadjustWiFi, boolean aadjustSound, boolean aadjustBrightness)
    {
        // saves the settings in the class fields
        adjustBrightness = aadjustBrightness;
        adjustWiFi = aadjustWiFi;
        adjustSound = aadjustSound;
        studentID = sstudentID;

        // saves the settings in the editor so they will be kept when the app will close
        editor.putInt("studentID",studentID);
        editor.putBoolean("adjustWiFi",adjustWiFi);
        editor.putBoolean("adjustSound",adjustSound);
        editor.putBoolean("adjustBrightness",adjustBrightness);
        editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // TODO -> EXIT
        // Just initialisation of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lecnoteButton = (Button)findViewById(R.id.lecnoteButton);
        lecnoteButton.setEnabled(false);
        surveyButton = (Button)findViewById(R.id.surveyButton);
        surveyButton.setEnabled(false);
        textView = (TextView)findViewById(R.id.welcomeMessage);

        // Restore stored preferences
        settings = getPreferences(Context.MODE_PRIVATE);
        editor = settings.edit();
        studentID = settings.getInt("studentID", 0);
        adjustWiFi = settings.getBoolean("adjustWiFi", true);
        adjustSound = settings.getBoolean("adjustSound", true);
        adjustBrightness = settings.getBoolean("adjustBrightness", true);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {

        // initialising
        super.onResume();
        textView.setText("LOADING...");

        // Checks whether the app was started by:
        //  - reading a tag  (which means we should communicate with the database and change phone settings)
        //  - the user (which means the welcome info and settings activity should start)
        Intent intent = this.getIntent();
        if (intent.getAction().contains("NDEF")) {

            // raw message from tag
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {

                // The tag
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                // its records
                NdefRecord[] records = Ndef.get(tag).getCachedNdefMessage().getRecords();

                // the info on it in byte shape
                byte[] payload = records[0].getPayload();

                // Get the Text Encoding
                String textEncoding;
                if ((payload[0] & 128) == 0)
                    textEncoding = "UTF-8";
                else
                    textEncoding = "UTF-16";

                // Get the Language Code
                int languageCodeLength = payload[0] & 0063;

                try {
                    // the configured message on the tag (the id of the room in which it is put)
                    String roomID = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

                    // the class which communicates with the sql database
                    DatabaseConnecter sql = new DatabaseConnecter(MainActivity.this);

                    // the id of the module which is currently on schedule
                    int moduleID = sql.getModule(Integer.parseInt(roomID));
                    if(moduleID == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("There is no lecture at this time in this room!").setTitle("Alert!");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                MainActivity.this.finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    // a welcome message on screen
                    textView.setText("The current lecture is " + sql.getModuleName(moduleID) + "! Your lecturer is " + sql.getLecturerName(moduleID));

                    // tells the server that the student scanned the tag (went in the lecture or went out)
                    sql.setPurpose(studentID);

                    // gets the link for the lecture notes and enables the button to browse them

                    if(sql.getPurpose(studentID).equals("out")) {
                        lecnoteButton.setVisibility(Button.INVISIBLE);
                        lecnoteButton.setEnabled(false);
                        surveyButton.setVisibility(Button.VISIBLE);
                        surveyButton.setEnabled(true);
                    }
                    else {
                        lecnoteButton.setVisibility(Button.VISIBLE);
                        lecnoteButton.setEnabled(true);
                        surveyButton.setVisibility(Button.INVISIBLE);
                        surveyButton.setEnabled(false);
                    }


                    link = sql.getLecnotes(moduleID);
                    if(link.equals("None"))
                        lecnoteButton.setText("No lecture notes available!");
                    else
                        lecnoteButton.setEnabled(true);

                    surveyLink = sql.getSurvey(moduleID);
                    if(surveyLink.equals("None"))
                        surveyButton.setText("No lecture notes available!");
                    else
                        surveyButton.setEnabled(true);
                }

                //should not happen if the tag is configured properly
                catch (UnsupportedEncodingException e) {
                    System.out.println("invalid tag " + e.toString());
                }
                catch (NumberFormatException e) {
                    System.out.println("invalid tag " + e.toString());
                }
            }

        } // end of if
        else
        {
            // starts the settings activity
            Intent configIntent = new Intent(MainActivity.this, ConfigActivity.class);
            startActivity(configIntent);
        }

    }

    /**
     * Opens the lecture notes in the browser - only called (automatically) when the button is pressed)
     * @param view The button
     */
    public void showLecnotes(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    /**
     * Opens the survey (feedback for the lecturer)
     * @param view The button that called the method
     */
    public void showSurvey(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(surveyLink));
        startActivity(browserIntent);
    }

}