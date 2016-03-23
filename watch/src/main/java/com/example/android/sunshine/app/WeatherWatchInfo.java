package com.example.android.sunshine.app;

import com.google.android.gms.wearable.Asset;

/**
 * Created by kimsuh on 3/19/16.
 */
public class WeatherWatchInfo {
    public String getmHigh() {
        return mHigh;
    }

    public void setmHigh(String mHigh) {
        this.mHigh = mHigh;
    }

    public String getmLow() {
        return mLow;
    }

    public void setmLow(String mLow) {
        this.mLow = mLow;
    }

    public Asset getmAsset() {
        return mAsset;
    }

    public void setmAsset(Asset mAsset) {
        this.mAsset = mAsset;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    private String mHigh;
    private String mLow;
    private Asset mAsset;
    private String mDesc;
}
