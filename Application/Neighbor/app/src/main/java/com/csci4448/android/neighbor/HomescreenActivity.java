package com.csci4448.android.neighbor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class HomescreenActivity extends AppCompatActivity {

    private EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Button postItemButton = (Button) findViewById(R.id.post_item);
        postItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this, PostItemActivity.class);
                startActivity(intent);
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
                    Intent intent = new Intent(HomescreenActivity.this, SearchActivity.class);
                    intent.putExtra("Query", searchingFor);
                    startActivity(intent);
                }
            }
        });
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
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
