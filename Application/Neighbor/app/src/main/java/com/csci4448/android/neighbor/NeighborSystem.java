package com.csci4448.android.neighbor;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by Brennan on 11/7/15.
 */
public class NeighborSystem extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(RentalItem.class);
        Parse.initialize(this, "8igWP6UKlYeVz0ROQPPaGJygz1AqMH3LcDZXWMNv", "qj077hg3oAFZbpmCeiomTTxEZxuWrNUFDZPR8alT");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
