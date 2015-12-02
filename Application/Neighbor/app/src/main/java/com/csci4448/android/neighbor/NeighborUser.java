package com.csci4448.android.neighbor;

import com.parse.ParseUser;

/**
 * Created by Brennan on 12/1/15.
 */
public class NeighborUser {


    private static NeighborUser currentUser = null;
    private static ParseUser pUser;

    protected NeighborUser() {
        // Exists only to defeat instantiation.
    }

    public static NeighborUser getInstance() {
        if (currentUser == null) {
            currentUser = new NeighborUser();
        }
        pUser = ParseUser.getCurrentUser();
        return currentUser;
    }

    public ParseUser getParseUser() {
        return pUser;
    }
}
