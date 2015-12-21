package example.com.simpleui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {

    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String note = getIntent().getStringExtra("note");
        String storeInfo = getIntent().getStringExtra("storeInfo");
        address = storeInfo.split(",")[1];

        Log.d("debug", note);
        Log.d("debug", storeInfo);

        //String
        //Thread thread = new Thread(new Runnable() {
        //    @Override
        //    public void run()
        //    {
        //
        //        //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=taipei101";
        //        String url = Utils.getGeoCodingUrl(address);
        //        byte[] bytes= Utils.urlToByte(url);
        //        String result = new String(bytes);
        //        double[] latlng = Utils.getLatLngFormJsonString(result);
        //        Log.d("debug", result);
        //        Log.d("debug", latlng[0] + "," + latlng[1]);
        //
        //    }
        //});
        //thread.start();

        AsyncTask task = new AsyncTask()
        {
            @Override
            protected Object doInBackground(Object[] params)
            {
                Utils.addressToLatLng(address);
                return null;
            }

            protected void onPostExecute(Objects o)
            {}

        };

        task.execute();
    }

}
