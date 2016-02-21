package ataa.warwickhack2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Activity in which the app settings are displayed
 */
public class ConfigActivity extends AppCompatActivity {

    private EditText studentID;
    private CheckBox wifi;
    private CheckBox sound;
    private CheckBox bright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        studentID = (EditText)findViewById(R.id.editText);
        wifi = (CheckBox)findViewById(R.id.checkBox);
        sound = (CheckBox)findViewById(R.id.checkBox2);
        bright = (CheckBox)findViewById(R.id.checkBox3);

        studentID.setText("" + MainActivity.studentID);
        wifi.setChecked(MainActivity.adjustWiFi);
        sound.setChecked(MainActivity.adjustSound);
        bright.setChecked(MainActivity.adjustBrightness);
    }

    /**
     * Method called automatically when the "save settings" button is pressed
     * @param view The button
     */
    public void saveClick(View view)
    {
        MainActivity.saveSettings(Integer.parseInt(studentID.getText().toString()), wifi.isChecked(), sound.isChecked(), bright.isChecked());
    }

    /**
     * Method called automatically when the "Restore settings" button is pressed
     * @param view
     */
    public void restoreClick(View view)
    {
        MainActivity.saveSettings(0,true,true,true);
        studentID.setText("0");
        wifi.setChecked(true);
        sound.setChecked(true);
        bright.setChecked(true);
    }

}