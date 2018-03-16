package com.camerawork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    Camera camera;
    CircleImageView btn_take_photo;
    CircleImageView profile_image1;
    CircleImageView profile_image2;
    CircleImageView profile_image3;
    TextView cancle_tv;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;
    Camera.ShutterCallback shutterCallback;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);
        surfaceView = findViewById(R.id.surfaceView);
        btn_take_photo = findViewById(R.id.capture_button);
        profile_image1 = findViewById(R.id.profile_image1);
        profile_image2 = findViewById(R.id.profile_image2);
        profile_image3 = findViewById(R.id.profile_image3);
        cancle_tv = findViewById(R.id.cancle_tv);
        surfaceHolder = surfaceView.getHolder();

        //install a surface holder.callback so we get notified when underlying surface is created and destroyed
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraImage();
            }
        });
        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);


                FileOutputStream outputStream = null;
                File file_image = getDir();
                if (!file_image.exists() && !file_image.mkdirs()) {
                    Toast.makeText(CameraActivity.this, "Cant create directory", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = simpleDateFormat.format(new Date());
                String pohtofile = "Cam_Demo" + date + ".jpg";
                String file_name = file_image.getAbsolutePath() + "/" + pohtofile;
                File picFile = new File(file_name);
                try {
                    outputStream = new FileOutputStream(picFile);
                    outputStream.write(data);
                    outputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException ex) {

                } finally {

                }
                Toast.makeText(CameraActivity.this, "Picture saved", Toast.LENGTH_SHORT).show();

                refreshCamera();
                refreshGallery(file_image);

            }
        };
    }

    //refresh gallery
    void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            //preview surface doesnot exist
            return;
        }
        //stop preview before making any changes
        try {
            camera.stopPreview();
        } catch (Exception e) {

        }

        //set preview sie and make any resize rotate or
        //reformating the changes here
        //start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }

    }

    private File getDir() {
        File dics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(dics, "Camera Demo");
    }

    private void cameraImage() {
        //take picture
        camera.takePicture(null, null, jpegCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //open camera
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {

        }
        Camera.Parameters parameters;
        parameters = camera.getParameters();
        //modify parameters
        parameters.setPreviewFrameRate(30);
        parameters.setPreviewSize(352, 288);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        for(int i=0;i<sizes.size();i++) {
            if(sizes.get(i).width > size.width)
                size = sizes.get(i);
        }
        parameters.setPictureSize(size.width, size.height);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            //The surface has been created, now tell the camera where to draw
            camera.setPreviewDisplay(surfaceHolder);

        } catch (Exception e) {

        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    };

    private void onPictureTake(byte[] data, Camera camera) {

    }
}
