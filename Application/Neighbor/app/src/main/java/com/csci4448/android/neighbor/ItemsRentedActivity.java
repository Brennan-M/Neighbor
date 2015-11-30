package com.csci4448.android.neighbor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class ItemsRentedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ParseQueryAdapter<RentalItem> RentalItemFeedQueryAdapter;
        final ListView itemsListView = (ListView) findViewById(R.id.item_feed_list);

        /* This is the ParseQueryAdapter which prepares us to retrieve results from our database */
        ParseQueryAdapter.QueryFactory<RentalItem> factory =
                new ParseQueryAdapter.QueryFactory<RentalItem>() {
                    public ParseQuery<RentalItem> create() {

                        ParseQuery<RentalItem> query = RentalItem.getQuery();
                        query.whereEqualTo("Renter", ParseUser.getCurrentUser());
                        query.orderByDescending("updatedAt");
                        return query;
                    }
                };

        RentalItemFeedQueryAdapter = new ParseQueryAdapter<RentalItem>(this, factory) {
            @Override
            public View getItemView(final RentalItem post, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.rental_item_rented, null);
                }

                TextView nameView = (TextView) view.findViewById(R.id.itemName);
                TextView costView = (TextView) view.findViewById(R.id.itemCost);
                TextView perTimeView = (TextView) view.findViewById(R.id.itemPerTime);
                TextView locationView = (TextView) view.findViewById(R.id.itemLocation);
                TextView descriptionView = (TextView) view.findViewById(R.id.itemDescription);
                ParseImageView itemImage = (ParseImageView) view.findViewById(R.id.profilePicture);

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
                costView.setText(Double.toString(post.getCost()));
                perTimeView.setText(post.getPerTime());
                locationView.setText(post.getLocation());
                descriptionView.setText(post.getDescription());
                return view;
            }
        };

        itemsListView.setAdapter(RentalItemFeedQueryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items_rented, menu);
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
