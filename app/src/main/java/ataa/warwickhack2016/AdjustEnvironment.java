package ataa.warwickhack2016;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import ataa.warwickhack2016.MainActivity;


/**
 * Created by Andrei-Marius on 20/02/2016.
 */
public class AdjustEnvironment {
    private WifiManager mainWifiObj;
    private AudioManager mainAudioObj;
    private ContentResolver cResolver;

    /**
     * Constructor
     * @param activity Our main activity
     */
    public AdjustEnvironment(MainActivity activity) {
        mainWifiObj = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        mainAudioObj = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        cResolver = activity.getContentResolver();
    }

    /**
     * Actions performed when a student enters a lecture room
     */
    public void begin(){
        System.out.println("App started. Setting start environment.");
        wifiOn();
        soundOff();
        lowBrightness();
    }

    /**
     * Actions performed when a student exits a lecture room
     */
    public void end(){
        System.out.println("Setting end environment.");
        wifiOff();
        soundOn();
        highBrightness();

    }

    /**
     * Turning the wifi on
     */
    public void wifiOn(){
        mainWifiObj.setWifiEnabled(true);
    }

    /**
     * Turning the wifi off
     */
    public void wifiOff(){
        mainWifiObj.setWifiEnabled(false);
    }

    /**
     * Turning the sound on
     */
    public void soundOn(){
        mainAudioObj.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    /**
     * Turning the sound off
     */
    public void soundOff(){
        mainAudioObj.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    /**
     * Setting the minimum brightness
     */
    public void lowBrightness(){
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    /**
     * Setting the maximum brightness
     */
    public void highBrightness(){
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 255);

    }
}