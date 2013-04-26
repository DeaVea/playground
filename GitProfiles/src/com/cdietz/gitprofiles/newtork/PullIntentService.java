package com.cdietz.gitprofiles.newtork;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

public abstract class PullIntentService extends IntentService {
    public static final String BROADCAST_CONNECTION_ERROR = "com.cdietz.gitprofiles.connectionerror";
    public static final String BROADCAST_READ_ERROR = "com.cdietz.gitprofiles.readerror";
    
    public PullIntentService(String name) {
        super(name);
        disableConnectionReuseIfNecessary();
    }

    protected void broadcastConnectionError() {
        final Intent broadcastIntent = new Intent(BROADCAST_CONNECTION_ERROR);
        sendBroadcast(broadcastIntent);
    }
    
    protected void broadcastReadError() {
        final Intent broadcastIntent = new Intent(BROADCAST_READ_ERROR);
        sendBroadcast(broadcastIntent);
    }
    
    /**
     * Opens a working connection to the given URL.
     * @return
     *      Working connection or null if exceptions are thrown.
     *          
     */
    protected HttpsURLConnection openInputConnection(String urlString, String acceptType) throws MalformedURLException, IOException {
        final String acceptFormat = (acceptType != null) ? acceptType : "text/plain";
        final URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("Accept", acceptFormat);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        return connection;
    }

    /**
     * Prior to GINGERBREAD, using HttpURLConnection had issues with closing the input stream and
     * poisoning the connection pool.  This will disable it if absolutely necessary.
     * Source: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
