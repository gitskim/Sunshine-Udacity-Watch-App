package com.example.android.sunshine.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by kimsuh on 3/17/16.
 */
public class ListenerService extends WearableListenerService {
    private static final String WEARABLE_DATA_PATH = "/wearable_data";
    private static final String TAG = "Listener Service";
    private GoogleApiClient mGoogleApiClient;
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        DataMap dataMap;
        for (DataEvent event : dataEvents) {

            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {
                    DataItem item = event.getDataItem();
                    dataMap = DataMapItem.fromDataItem(item).getDataMap();

                    String high = dataMap.getString("high");
                    String low = dataMap.getString("low");
                    MyWatchFace.sHighTemp = high;
                    MyWatchFace.sLowTemp = low;

                    Asset profileAsset = dataMap.getAsset("icon");
                    if (profileAsset == null) {
                        Log.d(TAG, "asset was null");
                    } else {
                        Log.d(TAG, "asset not null");
                    }
                    MyWatchFace.sWeatherIcon = loadBitmapFromAsset(profileAsset);
                    if (MyWatchFace.sWeatherIcon == null) {
                        Log.d(TAG, "weatherIcon null");
                    } else {
                        Log.d(TAG, "weatherIcon null");
                    }
                }
            }
        }
    }

    public Bitmap loadBitmapFromAsset(Asset asset) {
        createApiCliene();

        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(3000, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }

        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

    private void createApiCliene() {
        if (mGoogleApiClient != null) {
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                        // Request access only to the Wearable API
                .addApiIfAvailable(Wearable.API)
                .build();
    }

}

