package com.csci4448.android.neighbor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final String queryString;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            queryString = extras.getString("Query");
        } else {
            Toast.makeText(SearchActivity.this, "Something went wrong. Redirecting to HomeScreen.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SearchActivity.this, HomescreenActivity.class);
            startActivity(intent);
            queryString = null;
        }

        final ParseQueryAdapter<RentalItem> RentalItemFeedQueryAdapter;
        final ListView itemsListView = (ListView) findViewById(R.id.item_feed_list);

        /* This is the ParseQueryAdapter which prepares us to retrieve results from our database */
        ParseQueryAdapter.QueryFactory<RentalItem> factory =
            new ParseQueryAdapter.QueryFactory<RentalItem>() {
                public ParseQuery<RentalItem> create() {

                    ParseQuery<RentalItem> query = RentalItem.getQuery();
                    query.whereEqualTo("Renter", null);
                    query.whereContains("itemName", queryString);
                    query.whereNotEqualTo("Owner", ParseUser.getCurrentUser());
                    query.include("Owner");
                    query.orderByDescending("createdAt");
                    return query;
                }
            };

        RentalItemFeedQueryAdapter = new ParseQueryAdapter<RentalItem>(this, factory) {
            @Override
            public View getItemView(final RentalItem post, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.rental_item, null);
                }

                TextView nameView = (TextView) view.findViewById(R.id.itemName);
                TextView costView = (TextView) view.findViewById(R.id.itemCost);
                TextView locationView = (TextView) view.findViewById(R.id.itemLocation);
                TextView descriptionView = (TextView) view.findViewById(R.id.itemDescription);
                TextView ownerNameView = (TextView) view.findViewById(R.id.OwnerTextView);
                ParseImageView itemImage = (ParseImageView) view.findViewById(R.id.userProfilePicture);

                Button rentItemButton = (Button) view.findViewById(R.id.rentItemButton);

                rentItemButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        final ParseUser currentUser = ParseUser.getCurrentUser();

                        RentalNotification newNotification = new RentalNotification();
                        post.setRenter(currentUser);
                        ParseRelation itemsRented = currentUser.getRelation("ItemsRented");
                        itemsRented.add(post);

                        post.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    currentUser.saveInBackground();
                                    Intent intent = new Intent(SearchActivity.this, HomescreenActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SearchActivity.this, "Could not rent this item, error occured..", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        newNotification.setItem(post);
                        newNotification.setFrom(currentUser);
                        newNotification.setTo(post.getOwner());
                        newNotification.submitRentalNotification();
                    }
                });


                ParseFile image = post.getPhoto();
                if (image != null) {
                    itemImage.setVisibility(View.VISIBLE);
                    itemImage.setParseFile(image);
                    itemImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                } else {
                    itemImage.setVisibility(View.GONE);
                    itemImage.setParseFile(null);
                }

                nameView.setText(post.getName());
                costView.setText(" $" + Double.toString(post.getCost()) + " per " + post.getPerTime());
                locationView.setText(" " + post.getLocation());
                descriptionView.setText(post.getDescription());
                final ParseUser owner = post.getOwner();
                String name = "";
                try {
                    name = owner.fetchIfNeeded().getString("memberName");
                } catch (ParseException e) {
                    Log.v("ERROR: ", e.toString());
                }
                ownerNameView.setText(" " + name);

                if (!name.equals("")) {
                    ownerNameView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchActivity.this, UserProfileViewActivity.class);
                            String userID = "";
                            try {
                                userID = owner.fetchIfNeeded().getObjectId();
                            } catch (ParseException e) {
                                Log.v("ERROR: ", e.toString());
                            }
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                        }
                    });
                }
                return view;
            }
        };

        itemsListView.setAdapter(RentalItemFeedQueryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            ParseUser.logOut();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
