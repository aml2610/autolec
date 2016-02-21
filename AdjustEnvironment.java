package aml.warwick_hack_part;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;


/**
 * Created by Andrei-Marius on 20/02/2016.
 */
public class AdjustEnvironment {
    private WifiManager mainWifiObj;
    private AudioManager mainAudioObj;
    private ContentResolver cResolver;

    public AdjustEnvironment(MainActivity activity) {
        mainWifiObj = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        mainAudioObj = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        cResolver = activity.getContentResolver();
    }

    public void begin(){
        System.out.println("App started. Setting start environment.");
        wifiOn();
        soundOff();
        lowBrightness();
    }
    public void end(){
        System.out.println("Setting end environment.");
        wifiOff();
        soundOn();
        highBrightness();

    }

    public void wifiOn(){
        mainWifiObj.setWifiEnabled(true);
    }

    public void wifiOff(){
        mainWifiObj.setWifiEnabled(false);
    }

    public void soundOn(){
        mainAudioObj.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public void soundOff(){
        mainAudioObj.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void lowBrightness(){
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    public void highBrightness(){
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 255);

    }
}
