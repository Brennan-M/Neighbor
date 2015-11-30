package com.csci4448.android.neighbor;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Brennan on 11/12/15.
 */
@ParseClassName("Notification")
public class RentalNotification extends ParseObject {


    public void setTo(ParseUser user) {
        put("To_", user);
    }

    public void setFrom(ParseUser user) {
        put("From_", user);
    }

    public void setItem(RentalItem item) {
        put("Item", item);
    }

    public ParseUser getTo() {
        return getParseUser("To_");
    }

    public ParseUser getFrom() {
        return getParseUser("From_");
    }

    public ParseObject getItem() {
        return getParseObject("Item");
    }

    public void submitRentalNotification() {
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {}
        });
    }

    public static ParseQuery<RentalNotification> getQuery() {
        return ParseQuery.getQuery(RentalNotification.class);
    }
}
