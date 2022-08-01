package com.company.dementiacare.notificaion;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class EnableFlashLight extends ContextWrapper {

    /*
     * Constructor to get the context
     * */
    public EnableFlashLight(Context base) {
        super(base);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    /*
    * The function enableFLash will generate the flashlight notifications using the camera api
    * */
    public void enableFlash()
    {
        try {
            /* Getting the Camera Service Enabled*/
            final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
            String id = cameraManager.getCameraIdList()[0];
            /* Generating a thread for Flash Notification Class*/
            Thread turnOnOffThread = new Thread();
            /* Frequency Values for Flashlight Blinks*/
            int[] frequencyValues = new int[]{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
            turnOnOffThread.start();
            /* Enabling the flash light for first time*/
            cameraManager.setTorchMode(id, true);
            /* Iterating through the Frequency Values */
            for (int i = 0; i < frequencyValues.length; i++) {
                if (frequencyValues[i] == 0) {
                    /* Turning off the flash*/
                    turnOnOffThread.sleep(200);
                    cameraManager.setTorchMode(id, false);
                }
                if (frequencyValues[i] == 1) {
                    /* Turning on the flash*/
                    turnOnOffThread.sleep(200);
                    cameraManager.setTorchMode(id, true);
                }
            }

        }
        /* Handling Exceptions for the EnabledFLashLight Class*/
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            System.out.print("Finally for the FlashLight Notification Class");
        }
    }
}
