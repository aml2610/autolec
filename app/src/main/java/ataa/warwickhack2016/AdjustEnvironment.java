package ataa.warwickhack2016;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

/**
 * A class that controls the phone settings
 */
public class AdjustEnvironment {
    private WifiManager mainWifiObj;
    private AudioManager mainAudioObj;
    private ContentResolver cResolver;

    /**
     * Constructor
     * @param activity Our main activity
     */
    public AdjustEnvironment(Activity activity) {
        mainWifiObj = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        mainAudioObj = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        cResolver = activity.getContentResolver();
    }

    /**
     * Actions performed when a student enters a lecture room
     */
    public void begin(){
        System.out.println("App started. Setting start environment.");
        if(MainActivity.adjustWiFi)
            wifiOn();
        if(MainActivity.adjustSound)
            soundOff();
        if(MainActivity.adjustBrightness)
            lowBrightness();
    }

    /**
     * Actions performed when a student exits a lecture room
     */
    public void end(){
        System.out.println("Setting end environment.");
        if(MainActivity.adjustWiFi)
            wifiOff();
        if(MainActivity.adjustSound)
            soundOn();
        if(MainActivity.adjustBrightness)
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
        mainAudioObj.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
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