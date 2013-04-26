package com.cdietz.gitprofiles.newtork;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * This IntentService will download a single image from the github server. The
 * image will be saved to the application's local directory under the name
 * PROFILE_PIC_FILE_NAME if a download was successful.
 * 
 * @author Christopher Dietz
 * 
 */
public class PullProfilePictureIntentService extends PullIntentService {
    public static final String TAG = PullProfilePictureIntentService.class
            .getSimpleName();

    public static final String BROADCAST_NEW_PROFILE_PIC = "com.cdietz.gitprofiles.new_profile_pic";

    public static final String BUNDLE_STRING_ARG_FILE_URL = "fileurl";

    public static final String PROFILE_PIC_FILE_NAME = "profile_pic.png";

    public PullProfilePictureIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        final String urlString;
        if (extras == null
                || (urlString = extras.getString(BUNDLE_STRING_ARG_FILE_URL)) == null) {
            return;
        }

        // resizing the picture to PIXEL_SIZE pixels per github api
        final String modifiedUrlString = urlString + "&s=" + getSizeForDevice();
        HttpsURLConnection connection = null;
        BufferedInputStream in = null;
        FileOutputStream fos = null;
        Bitmap image = null;
        
        try {
            connection = openInputConnection(modifiedUrlString, "img/png");
            in = new BufferedInputStream(connection.getInputStream());
            // image files do not have MD5 hashes to check.

            image = BitmapFactory.decodeStream(in);

            // TODO: GIThub profile pics are small and only one, so putting to
            // local cache.
            final File imageFile = new File(getFilesDir(), PROFILE_PIC_FILE_NAME);
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, bytes);

            fos = new FileOutputStream(imageFile);
            fos.write(bytes.toByteArray());
            fos.close(); // Doc claims this does nothing so not worry about file IO.

            broadcastNewProfilePic();
            
        } catch (MalformedURLException e) {
            // TODO:Bad URL.
        } catch (IOException e) {
            broadcastConnectionError();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            
            if(image != null) {
                image.recycle();
            }
            
            try {
                if(in != null) {
                    in.close();
                }
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // TODO: Unable to communicate with streams at all
            }
        }
    }

    public void broadcastNewProfilePic() {
        final Intent broadcastIntent = new Intent(BROADCAST_NEW_PROFILE_PIC);
        sendBroadcast(broadcastIntent);
    }
    
    /**
     * Retrieve the size of the image that it should be based on device dpi.
     * 
     * @return
     *        The size in pixels the image should be.
     *             
     */
    private int getSizeForDevice() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final int scaleFactor;
        // The images we're getting sare @2x to scale them down.
        switch(dm.densityDpi) {
            case DisplayMetrics.DENSITY_LOW: // 120 dpi
                scaleFactor = 120;
                break;
            case DisplayMetrics.DENSITY_MEDIUM: // 160 dpi
                scaleFactor = 160;
                break;
            case DisplayMetrics.DENSITY_HIGH: // 240 dpi
                scaleFactor = 240;
                break;
            default:
                scaleFactor = 320;
                break;
        }
        
        return scaleFactor;
    }
}
