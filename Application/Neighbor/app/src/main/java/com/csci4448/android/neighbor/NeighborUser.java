package com.csci4448.android.neighbor;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Brennan on 12/1/15.
 */
public class NeighborUser {

    private static NeighborUser currentUser = null;
    private static ParseUser pUser;

    protected NeighborUser() {
        // This defeats instantiation.
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

    public void postItem(Context mContext) {
        Intent intent = new Intent(mContext, PostItemActivity.class);
        mContext.startActivity(intent);
    }

    public void searchItems(Context mContext, String query) {
        Intent intent = new Intent(mContext, SearchActivity.class);
        intent.putExtra("Query", query);
        mContext.startActivity(intent);
    }

    public void viewItemsOwned(Context mContext) {
        Intent intent = new Intent(mContext, ItemsOwnedActivity.class);
        mContext.startActivity(intent);
    }

    public void viewItemsRented(Context mContext) {
        Intent intent = new Intent(mContext, ItemsRentedActivity.class);
        mContext.startActivity(intent);
    }

    public void editProfile(Context mContext) {
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        mContext.startActivity(intent);
    }

    public void rentItem(final Context mContext, RentalItem post) {

        RentalNotification newNotification = new RentalNotification();
        post.setRenter(pUser);
        ParseRelation itemsRented = pUser.getRelation("ItemsRented");
        itemsRented.add(post);

        post.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    pUser.saveInBackground();
                    Intent intent = new Intent(mContext, HomescreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Could not rent this item, error occured..", Toast.LENGTH_LONG).show();
                }
            }
        });

        newNotification.setItem(post);
        newNotification.setFrom(pUser);
        newNotification.setTo(post.getOwner());
        newNotification.submitRentalNotification();
    }
}
