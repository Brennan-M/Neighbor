package com.csci4448.android.neighbor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class HomescreenActivity extends AppCompatActivity {

    private EditText searchQuery;
    private NeighborUser neighbor = NeighborUser.getInstance();
    private byte[] profilePicture;
    ImageView userProfilePic;
    private TextView fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        fullName = (TextView) findViewById(R.id.userName);
        userProfilePic = (ImageView) findViewById(R.id.userProfilePicture);

        fullName.setText(neighbor.getParseUser().getString("memberName"), TextView.BufferType.EDITABLE);
        ParseFile profilePictureParseFile = neighbor.getParseUser().getParseFile("pictureFile");

        if (profilePictureParseFile != null) {
            try {
                profilePicture = profilePictureParseFile.getData();
                userProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length));
            } catch (ParseException e) {
                profilePicture = null;
            }
        }

        final Button itemsOwnedButton = (Button) findViewById(R.id.viewItemsOwnedButton);
        itemsOwnedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                neighbor.viewItemsOwned(HomescreenActivity.this);
            }
        });

        Button itemsRentedButton = (Button) findViewById(R.id.viewRentingItemsButton);
        itemsRentedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                neighbor.viewItemsRented(HomescreenActivity.this);
            }
        });

        Button postItemButton = (Button) findViewById(R.id.post_item);
        postItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                neighbor.postItem(HomescreenActivity.this);
            }
        });

        searchQuery = (EditText) findViewById(R.id.search_item_edittext);

        Button searchQueryButton = (Button) findViewById(R.id.search_button);
        searchQueryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String searchingFor = searchQuery.getText().toString().trim();
                searchQuery.setText("");
                if (searchingFor.matches("")) {
                    Toast.makeText(HomescreenActivity.this, "Please specify an item to search for...", Toast.LENGTH_LONG).show();
                } else {
                    neighbor.searchItems(HomescreenActivity.this, searchingFor);
                }
            }
        });

        final ParseQueryAdapter<RentalNotification> RentalNotificationFeedQueryAdapter;
        final ListView notificationsListView = (ListView) findViewById(R.id.item_notification_list);

        /* This is the ParseQueryAdapter which prepares us to retrieve results from our database */
        ParseQueryAdapter.QueryFactory<RentalNotification> factory =
                new ParseQueryAdapter.QueryFactory<RentalNotification>() {
                    public ParseQuery<RentalNotification> create() {

                        ParseQuery<RentalNotification> query = RentalNotification.getQuery();
                        query.whereEqualTo("To_", neighbor.getParseUser());
                        return query;
                    }
                };

        RentalNotificationFeedQueryAdapter = new ParseQueryAdapter<RentalNotification>(this, factory) {
            @Override
            public View getItemView(final RentalNotification notification, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.rental_notification, null);
                }

                TextView nameView = (TextView) view.findViewById(R.id.item_renter);
                TextView itemName = (TextView) view.findViewById(R.id.item_renting);
                TextView emailView = (TextView) view.findViewById(R.id.renter_email);


                final ParseUser renter = notification.getFrom();
                String renterName = "";
                try {
                    renterName = renter.fetchIfNeeded().getString("memberName");
                } catch (ParseException e) {
                    Log.v("ERROR: ", e.toString());
                }
                nameView.setText(renterName);

                if (!renterName.equals("")) {
                    nameView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(HomescreenActivity.this, UserProfileViewActivity.class);
                            String userID = "";
                            try {
                                userID = renter.fetchIfNeeded().getObjectId();
                            } catch (ParseException e) {
                                Log.v("ERROR: ", e.toString());
                            }
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                        }
                    });
                }

                String renterEmail = "";
                try {
                    renterEmail = renter.fetchIfNeeded().getString("email");
                } catch (ParseException e) {
                    Log.v("ERROR: ", e.toString());
                }
                emailView.setText(renterEmail);

                final ParseObject rentItem =  notification.getItem();
                String rentItemName = "";
                try {
                    rentItemName = rentItem.fetchIfNeeded().getString("itemName");
                } catch (ParseException e) {
                    Log.v("ERROR: ", e.toString());
                }
                itemName.setText(rentItemName);

                return view;
            }
        };

        notificationsListView.setAdapter(RentalNotificationFeedQueryAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            ParseUser.logOut();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.edit_profile) {
            neighbor.editProfile(HomescreenActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }
}
