package com.choicely.outsideripoff.app;

import android.app.Application;

import io.realm.Realm;

public class OutsideRipOffApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
