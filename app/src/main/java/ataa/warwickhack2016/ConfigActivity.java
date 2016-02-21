package ataa.warwickhack2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import ataa.warwickhack2016.R;

public class ConfigActivity extends AppCompatActivity {

    EditText studentID;
    CheckBox wifi;
    CheckBox sound;
    CheckBox bright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        studentID = (EditText)findViewById(R.id.editText);
        wifi = (CheckBox)findViewById(R.id.checkBox);
        sound = (CheckBox)findViewById(R.id.checkBox2);
        bright = (CheckBox)findViewById(R.id.checkBox3);

       // Toast.makeText(this, "" + MainActivity.studentID, Toast.LENGTH_SHORT).show();
        studentID.setText("" + MainActivity.studentID);
        wifi.setChecked(MainActivity.adjustWiFi);
        sound.setChecked(MainActivity.adjustSound);
        bright.setChecked(MainActivity.adjustBrightness);
    }

    public void saveClick(View view)
    {
        MainActivity.saveSettings(Integer.parseInt(studentID.getText().toString()), wifi.isChecked(), sound.isChecked(), bright.isChecked());
    }


}