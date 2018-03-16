package com.camerawork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private CameraView cameraView;
    CircleImageView btn_take_photo;
    CircleImageView profile_image1, profile_image2,
            profile_image3, profile_image4, profile_image5;
    TextView cancle_tv;
    private static final String TAG = "MainActivity";

    private CameraKitImage kitImage;
    private ArrayList<CameraKitImage> imagesArray = new ArrayList<>();
    private int count =0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btn_take_photo = findViewById(R.id.capture_button);
        profile_image1 = findViewById(R.id.profile_image1);
        profile_image2 = findViewById(R.id.profile_image2);
        profile_image3 = findViewById(R.id.profile_image3);
        profile_image4 = findViewById(R.id.profile_image4);
        profile_image5 = findViewById(R.id.profile_image5);

        cancle_tv = findViewById(R.id.cancle_tv);
        cameraView = findViewById(R.id.camera);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                if (imagesArray.size() > 4){
                    imagesArray.add(4,cameraKitImage);

                }else {
                    imagesArray.add(cameraKitImage);
                }
                setimage(cameraKitImage);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.captureImage();
            }

        });

    }

    private void setimage(CameraKitImage cameraKitImage) {

        if (count==5&&imagesArray.size()<5){
            count =0;
        }
        if (count == 0){
            Glide.with(MainActivity.this).load(imagesArray.get(0).getBitmap()).into(profile_image1);

        } else if (count == 1 ){
            Glide.with(MainActivity.this).load(imagesArray.get(1).getBitmap()).into(profile_image2);

        } else if (count == 2 ){
            Glide.with(MainActivity.this).load(imagesArray.get(2).getBitmap()).into(profile_image3);

        } else if (count == 3 ){
            Glide.with(MainActivity.this).load(imagesArray.get(3).getBitmap()).into(profile_image4);

        } else  {
            Glide.with(MainActivity.this).load(imagesArray.get(4).getBitmap()).into(profile_image5);
            for (int i = 0; i < imagesArray.size(); i++) {
                Log.d(TAG, "setimage: "+i+" val "+imagesArray.get(i).getBitmap());
            }
        }
        count++;

    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }


}
