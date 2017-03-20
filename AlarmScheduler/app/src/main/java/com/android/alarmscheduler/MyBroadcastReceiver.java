package com.android.alarmscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by Kamran ALi on 9/8/2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "MyBroadCastReceiver";
    public static final String URL = "http://d.cdnpk.eu/pk-mp3/mehmet-erarabac%C4%B1-mehmet-erarabac%C4%B1-ezan-yar%C4%B1%C5%9Fmas%C4%B1-most-beautiful-azan-from-turkey/v544414114_068018161.mp3";
    MediaPlayer mMediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i = new Intent(context,MyServices.class);

        i.putExtra("URL", URL);
//        i.setClassName(context,MyServices.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        context.startActivity(intent);
        context.startService(i);

//        String urlString = URL;
//        String result = "";
       /* try {
            Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(URL);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            Log.d("Error on execute", "" + e.getMessage());
            e.printStackTrace();

        }*/
        /*try {
            result = loadFromNetwork(urlString);
        } catch (IOException e) {
            Log.i(TAG, "" + R.string.connection_error);
        }*/
    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

//    public String loadFromNetwork(final String urlString) throws IOException {
//        InputStream stream = null;
//        String str = "";
//
//
////        new DownloadFilesTask().execute();
////        try {
////            stream = downloadUrl(urlString);
////            str = readIt(stream);
////        } catch (IOException e) {
////            e.printStackTrace();
////        } finally {
////            if (stream != null) {
////                try {
////                    stream.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//        return str;
//    }

//    public String readIt(InputStream stream) throws IOException {
//
//        StringBuilder builder = new StringBuilder();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//
//        reader.close();
//        return builder.toString();
//    }

//    public InputStream downloadUrl() throws IOException {
//
//        URL url = new URL(URL);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(10000 /* milliseconds */);
//        conn.setConnectTimeout(15000 /* milliseconds */);
//        conn.setRequestMethod("GET");
//        conn.setDoInput(true);
//        // Start the query
//        conn.connect();
//        InputStream stream = conn.getInputStream();
//        return stream;
//    }

}

//    class DownloadFilesTask extends AsyncTask<URL, Void, String> {
//    InputStream stream = null;
//    String str = "";
//        MyBroadcastReceiver my = new MyBroadcastReceiver();
//
//    @Override
//    protected String doInBackground(URL... params) {
//        try {
//
//            stream = my.downloadUrl();
//            str = my.readIt(stream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        return str;
//    }
//}
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }