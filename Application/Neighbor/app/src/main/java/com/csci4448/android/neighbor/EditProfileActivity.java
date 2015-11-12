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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    private int IMG_RESULT = 1;

    private byte[] profilePicture;
    ImageView userProfilePic;
    private EditText fullName;
    private EditText location;
    private EditText userBio;
    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        currentUser = ParseUser.getCurrentUser();

        fullName = (EditText) findViewById(R.id.fullname_edittext);
        location = (EditText) findViewById(R.id.location_edittext);
        userBio = (EditText) findViewById(R.id.user_bio_edittext);
        userProfilePic = (ImageView) findViewById(R.id.profileImage);

        fullName.setText(currentUser.getString("memberName"), TextView.BufferType.EDITABLE);
        location.setText(currentUser.getString("location"), TextView.BufferType.EDITABLE);
        userBio.setText(currentUser.getString("userBio"), TextView.BufferType.EDITABLE);

        ParseFile profilePictureParseFile = currentUser.getParseFile("userPicture");

        if (profilePictureParseFile != null) {
            profilePictureParseFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null) {
                        Bitmap pic = BitmapFactory.decodeByteArray(data, 0, data.length);

                        if (pic != null) {
                            userProfilePic.setImageBitmap(pic);
                        }
                    }
                }
            });
        }

        Button uploadUserPhotoButton = (Button) findViewById(R.id.upload_profile_pic);
        uploadUserPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent gallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, IMG_RESULT);
            }
        });

        Button submitChangesButton = (Button) findViewById(R.id.submit_profile_changes);
        submitChangesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateProfileChanges();
            }
        });
    }

    @Override
    /* Source ProgrammerGuru.com */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {

                Uri imageChosen = data.getData();
                Bitmap pictureData = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageChosen);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                pictureData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                profilePicture = bos.toByteArray();
                userProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length));

            } else {
                Toast.makeText(this, "Image not selected.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Photo could not be selected.", Toast.LENGTH_LONG).show();
        }

    }

    private void updateProfileChanges() {

        String userLocation = location.getText().toString().trim();
        String userFullname = fullName.getText().toString().trim();
        String userBioInfo = userBio.getText().toString().trim();

        currentUser.put("location", userLocation);
        currentUser.put("userBio", userBioInfo);
        currentUser.put("memberName", userFullname);

        ParseFile profilePicParseFile = new ParseFile("profilePic.jpg", profilePicture);
        currentUser.put("pictureFile", profilePicParseFile);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.show();

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Route back to main activity which should then lead to our homescreen activity.
                    dialog.dismiss();
                    Intent intent = new Intent(EditProfileActivity.this, HomescreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Error: Email Not Valid.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
