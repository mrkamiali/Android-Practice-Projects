package com.android.imageselector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final int SELECTED_PICTURE = 1;
    private static final String IMAGES = "images";
    String downloadUrl;
    private Button mButton, downloadButton;
    private ImageView mImageView;
    private FirebaseStorage storage;
    private TextView mTextView;
    private StorageReference storageRef, imageRef, folderRef;
    private String imgPath;
    private DatabaseReference database;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        mButton = (Button) findViewById(R.id.button);
        mImageView = (ImageView) findViewById(R.id.imageview);
        downloadButton = (Button) findViewById(R.id.downloadImg);

        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        mTextView = (TextView) findViewById(R.id.textView);

        storageRef = storage.getReferenceFromUrl("gs://imagedb-fc793.appspot.com");
        folderRef = storageRef.child("images");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(MainActivity.this, "Downloading Image", "loading...", true, false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMax(100);
                database.child("ImgURl").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String fileURl = dataSnapshot.getValue().toString();
                        Log.d("File URL", "" + dataSnapshot.getValue().toString());
                        String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(fileURl);
                        String name = URLUtil.guessFileName(fileURl, null, fileExtenstion);

                        File outPutDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGES);
                        if (!outPutDir.mkdirs()) {
                            Log.d("LOG", "Directory not created");
                        }
                        try {
                            File imageFileRaw = File.createTempFile(name, ".jpg");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                progressDialog.dismiss();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


            if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
                Uri uri = data.getData();
                String[] imgHolder = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, imgHolder, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(imgHolder[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                Log.d("Image Path", imgPath);
                mImageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));

                try {

                    UploadTask uploadTask;

                    Uri file = Uri.fromFile(new File(imgPath));
                    imageRef = folderRef.child(file.getLastPathSegment());
                    uploadTask = imageRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                            downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            Log.e("URL", "" + downloadUrl);
                            mTextView.setText(downloadUrl);
                            database.child("ImgURl").setValue(downloadUrl);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
