package com.kamranali.viewtobitmap;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int SELECTED_PICTURE = 1;
    private static final String IMAGES = "images";
    String downloadUrl;
    private Button mButton, downloadButton;
    private ImageView imageView;
    private FirebaseStorage storage;
    private TextView mTextView;
    private StorageReference storageRef, imageRef, folderRef;
    private String imgPath;
    private DatabaseReference database;
    private ProgressDialog progressDialog;
    private Bitmap mbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button1);
        downloadButton = (Button) findViewById(R.id.button2);
        imageView = (ImageView) findViewById(R.id.imageView);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenShot();
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = particularScreenShot();
            }
        });

    }



    public Bitmap particularScreenShot() {
        View view = findViewById(R.id.imageView);

        view.setDrawingCacheEnabled(true);
        int height = view.getHeight();
        int width = view.getWidth();
        view.layout(0,0,width,height);
        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        Log.d("Kamran"," "+b.toString());
        imageView.setImageBitmap(b);
        return b;
    }

    public void screenShot() {
        mbitmap = getBitmapOFRootView(mButton);
        imageView.setImageBitmap(mbitmap);
        createImage(mbitmap);
    }

    public Bitmap getBitmapOFRootView(View v) {
        View rootview = v.getRootView();
        rootview.setDrawingCacheEnabled(true);
        Bitmap bitmap1 = rootview.getDrawingCache();
        return bitmap1;
    }

    public void createImage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File file = new File(Environment.getExternalStorageDirectory() +
                "/capturedscreenandroid.jpg");
//        try {
//            file.createNewFile();
//            FileOutputStream outputStream = new FileOutputStream(file);
//            outputStream.write(bytes.toByteArray());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
