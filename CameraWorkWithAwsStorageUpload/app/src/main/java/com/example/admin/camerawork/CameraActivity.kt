package com.example.admin.camerawork

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException


class CameraActivity : AppCompatActivity() {
    private val PICK_IMAGES = 6069
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1001
    //    private CameraView cameraView;
    private lateinit var btn_take_photo: CircleImageView
    private lateinit var profile_image0: CircleImageView
    private lateinit var profile_image1: CircleImageView
    private lateinit var profile_image2: CircleImageView
    private lateinit var profile_image3: CircleImageView
    private lateinit var cancle_tv: TextView
    private lateinit var done_TV: TextView
    var filterVariable = ""
    var imgBitmpas = arrayOfNulls<Bitmap>(4)
    private var count = 0
    private val TAG = "CameraActivity"
    private lateinit var camera_Preview_Frame: FrameLayout
    private var camera: Camera? = null
    private var cameraPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_camera)
        initilizeViews()

        if (ContextCompat.checkSelfPermission(this@CameraActivity,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initCamera()
        } else {
            askForCameraPermission()
            Toast.makeText(this, "Please Allow app to Use Camera of your Device. Thanks", Toast.LENGTH_SHORT).show()
        }


    }

    private fun askForCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@CameraActivity, Manifest.permission.CAMERA)) {
            Snackbar.make(findViewById<View>(android.R.id.content), "Need permission for loading data", Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    View.OnClickListener {
                        //asking all required permissions
                        ActivityCompat.requestPermissions(this@CameraActivity, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                    }).show()
        } else {
            ActivityCompat.requestPermissions(this@CameraActivity, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }

    private fun initCamera() {
        if (checkCameraHardware()) {
            camera = getCameraInstance()
            cameraPreview = CameraPreview(this, camera)
            //            CameraPreview.setCameraDisplayOrientation(CameraActivity.this,camera);
            camera_Preview_Frame.addView(cameraPreview)
            setFocus()

        } else {
            Toast.makeText(applicationContext, "Device not support camera feature", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFocus() {
        val params = camera?.getParameters()
        if (params?.supportedFocusModes!!.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else {
            params?.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }
        camera?.parameters = params
    }

    private fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open()
        } catch (e: Exception) {
            Log.e("TAG", "getCameraInstance: No Camera Found")
        }

        return c
    }

    class RetrieveFeedTask : AsyncTask<Bitmap, Void, String>() {
        override fun doInBackground(vararg bitmap: Bitmap?): String {
            var bis: ByteArrayInputStream? = null
            var bos: ByteArrayOutputStream = ByteArrayOutputStream()
            try {
                val credentials = BasicAWSCredentials("ACCESSKEYHERE", "SECRETKEYHERE")
                var s3Client: AmazonS3 = AmazonS3Client(credentials)

                bitmap[0]?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                val bImageData = bos.toByteArray()
                bis = ByteArrayInputStream(bImageData)
                // upload to s3 bucket
                var s3Put: PutObjectRequest = PutObjectRequest("joviparks/govt_id", "MYPICCCCC", bis, null)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                s3Client.putObject(s3Put)

                return s3Client.getUrl("joviparks/govt_id", "MYPICCCCC").toString()
            } finally {
                try {
                    bos.close();
                    if (bis != null) bis.close();
                } catch (e: IOException) {
                    Log.d("LOG", "Error while closing stream for writing an image for MyPIC . png caused by " + e.message);
                }
            }
        }


    }

    private fun checkCameraHardware(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    var myShutterCallback: Camera.ShutterCallback = Camera.ShutterCallback {
    }
    private val pictureCallback = Camera.PictureCallback { data, camera ->
        try {
            val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
            val out = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, out)
            val decoded:Bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
            setimage(decoded)

            if (decoded != null)
                RetrieveFeedTask().execute(decoded)

            camera.startPreview()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            camera.startPreview()

        }
    }

    private fun setimage(bmp: Bitmap) {

        if (count == 4) {
            count = 0
        }

        if (count == 0) {

            Glide.with(applicationContext).load(bmp).into(profile_image0)
            imgBitmpas[0] = bmp
        } else if (count == 1) {
            Glide.with(applicationContext).load(bmp).into(profile_image1)
            imgBitmpas[1] = bmp

        } else if (count == 2) {
            Glide.with(applicationContext).load(bmp).into(profile_image2)
            imgBitmpas[2] = bmp

        } else {
            imgBitmpas[3] = bmp

            Glide.with(applicationContext).load(bmp).into(profile_image3)

        }
        count++

    }

    private fun initilizeViews() {

        btn_take_photo = findViewById(R.id.capture_button)
        profile_image0 = findViewById(R.id.profile_image1)
        profile_image1 = findViewById(R.id.profile_image2)
        profile_image2 = findViewById(R.id.profile_image3)
        profile_image3 = findViewById(R.id.profile_image4)
        cancle_tv = findViewById(R.id.cancle_tv)
        done_TV = findViewById(R.id.next_tv)
        camera_Preview_Frame = findViewById(R.id.camera)

        done_TV.setOnClickListener {
            if (imgBitmpas[0] != null) {
                finish()
            } else {
                Toast.makeText(this@CameraActivity, "Please Load atleast 1 image.", Toast.LENGTH_SHORT).show()
            }

        }

        btn_take_photo.setOnClickListener {
            try {
                camera?.takePicture(myShutterCallback, null, pictureCallback)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        cancle_tv.setOnClickListener {
            if (imgBitmpas[0] != null) {
                finish()
            } else {
                Toast.makeText(this@CameraActivity, "Please Load atleast 1 image.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
