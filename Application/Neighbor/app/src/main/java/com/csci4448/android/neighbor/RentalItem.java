package com.csci4448.android.neighbor;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Brennan on 11/12/15.
 */

public class RentalItem extends ParseObject {


    public void setName(String value) {
        put("itemName", value);
    }

    public void setCost(double value) {
        put("cost", value);
    }

    public void setLocation(String value) {
        put("location", value);
    }

    public void setPerTime(String value) {
        put("perTime", value);
    }

    public void setDescription(String value) {
        put("description", value);
    }

    public void setOwner(ParseUser user) {
        put("Owner", user);
    }

    public void setRenter(ParseUser user) {
        put("Renter", user);
    }

    public void setPhoto(ParseFile image) {
        put("itemPicture", image);
    }

    public String getName() {
        return getString("itemName");
    }

    public Double getCost() {
        return getDouble("cost");
    }

    public String getLocation() {
        return getString("location");
    }

    public String getPerTime() {
        return getString("perTime");
    }

    public String getDescription() {
        return getString("description");
    }

    public ParseUser getOwner() {
        return getParseUser("Owner");
    }

    public ParseUser getRenter() {
        return getParseUser("Renter");
    }

    public ParseFile getPhoto() {
        return getParseFile("itemPicture");
    }

    public void submitRentalItem() {
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }

    public static ParseQuery<RentalItem> getQuery() {
        return ParseQuery.getQuery(RentalItem.class);
    }
}
