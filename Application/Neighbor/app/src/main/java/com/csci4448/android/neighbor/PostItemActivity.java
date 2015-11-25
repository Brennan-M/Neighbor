package com.csci4448.android.neighbor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class PostItemActivity extends AppCompatActivity {

    private int IMG_RESULT = 1;

    private EditText itemName;
    private EditText itemCost;
    private String perTime;
    private EditText itemDescription;
    private EditText itemLocation;
    private Spinner costPerTimeSpinner;

    private byte[] itemData;
    private ImageView itemPicture;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

        itemName = (EditText) findViewById(R.id.item_name_edittext);
        itemCost = (EditText) findViewById(R.id.item_cost_edittext);
        itemDescription = (EditText) findViewById(R.id.item_description_edittext);
        itemLocation = (EditText) findViewById(R.id.item_location_edittext);
        itemPicture = (ImageView) findViewById(R.id.item_image);
        costPerTimeSpinner = (Spinner) findViewById(R.id.cost_perTime_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.perTime_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        costPerTimeSpinner.setAdapter(adapter);


        Button attachPhotoButton = (Button) findViewById(R.id.upload_photo_button);
        attachPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent gallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, IMG_RESULT);
            }
        });

        Button postItem = (Button) findViewById(R.id.post_item_button);
        postItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                postItemToNeighbor();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {

                Uri imageChosen = data.getData();
                Bitmap pictureData = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageChosen);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                pictureData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                itemData = bos.toByteArray();
                itemPicture.setImageBitmap(BitmapFactory.decodeByteArray(itemData, 0, itemData.length));

            } else {
                Toast.makeText(this, "Image not selected.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Photo could not be selected.", Toast.LENGTH_LONG).show();
        }

    }

    protected void postItemToNeighbor() {

        currentUser = ParseUser.getCurrentUser();
        String postName = itemName.getText().toString().trim();
        String postLocation = itemLocation.getText().toString().trim();
        String postDescription = itemDescription.getText().toString().trim();
        String stringCost = itemCost.getText().toString().trim();
        String postPerTime = costPerTimeSpinner.getSelectedItem().toString();

        boolean valid_post = true;

        RentalItem newPost = new RentalItem();

        if (!stringCost.matches("")) {
            double cost = Double.parseDouble(stringCost);
            newPost.setCost(cost);
        } else {
            valid_post = false;
        }

        if (!postName.matches("")) {
            newPost.setName(postName);
        } else {
            valid_post = false;
        }

        if (!postLocation.matches("")) {
            newPost.setLocation(postLocation);
        } else {
            valid_post = false;
        }

        if (!postDescription.matches("")) {
            newPost.setDescription(postDescription);
        } else {
            valid_post = false;
        }

        if (!postPerTime.matches("")) {
            newPost.setPerTime(postPerTime);
        } else {
            valid_post = false;
        }

        if (itemData != null) {
            ParseFile postPicture = new ParseFile("itemPicture.jpg", itemData);
            newPost.setPhoto(postPicture);
        } else {
            valid_post = false;
        }

        if (currentUser == null) {
            valid_post = false;
        }
        newPost.setOwner(currentUser);


        if (valid_post == true) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Saving Post...");
            dialog.show();
            newPost.submitRentalItem();
            dialog.dismiss();
            Intent intent = new Intent(this, HomescreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(PostItemActivity.this, "Error: Missing Fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_item, menu);
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
