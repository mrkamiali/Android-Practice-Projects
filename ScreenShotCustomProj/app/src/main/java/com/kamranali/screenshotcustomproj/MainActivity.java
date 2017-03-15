package com.kamranali.screenshotcustomproj;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    Bitmap mbitmap;
    Button captureScreenShot,particularImge ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Your ScreenShot Image:");

        captureScreenShot = (Button) findViewById(R.id.capture_screen_shot);
        particularImge = (Button) findViewById(R.id.particularImge);
        imageView = (ImageView) findViewById(R.id.imageView);

        particularImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                particularScreenShot();
            }
        });

    }

    private void particularScreenShot() {
        View u = findViewById(R.id.imageView);
        u.setDrawingCacheEnabled(true);

        int totalHeight = imageView.getHeight();
        int totalWidth = imageView.getWidth();
        u.layout(0, 0, totalWidth, totalHeight);
        u.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);

//        //Save bitmap
//        String extr = Environment.getExternalStorageDirectory().toString() +   File.separator + "Folder";
//        String fileName = new SimpleDateFormat("yyyyMMddhhmm'_report.jpg'").format(new Date());
//        File myPath = new File(extr, fileName);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(myPath);
//            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
            imageView.setImageBitmap(b);
//        }catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


    }

    public void screenShot(View view) {
        mbitmap = getBitmapOFRootView(captureScreenShot);
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
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
