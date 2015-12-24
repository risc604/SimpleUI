package example.com.simpleui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView addressTextView;
    private ImageView staticMapImage;
    private Switch mapSwitch;
    private WebView staticMapWebView;

    //private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        addressTextView = (TextView) findViewById(R.id.address);
        staticMapImage = (ImageView) findViewById(R.id.staticMapImage);
        staticMapWebView = (WebView) findViewById(R.id.webView);
        staticMapWebView.setVisibility(View.GONE);  //init

        mapSwitch = (Switch) findViewById(R.id.mapSwitch);
        mapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    staticMapImage.setVisibility(View.GONE);
                    staticMapWebView.setVisibility(View.VISIBLE);
                }
                else
                {
                    staticMapWebView.setVisibility(View.GONE);
                    staticMapImage.setVisibility(View.VISIBLE);
                }
            }
        });

        String note = getIntent().getStringExtra("note");
        String storeInfo = getIntent().getStringExtra("storeInfo");
        String address = storeInfo.split(",")[1];
        addressTextView.setText(address);


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

        //AsyncTask task = new AsyncTask()
        //{
        //    @Override
        //    protected Object doInBackground(Object[] params)
        //    {
        //        Utils.addressToLatLng(address);
        //        return null;
        //    }
        //
        //    protected void onPostExecute(Objects o)
        //    {}
        //
        //};

        GeoCodingTask task = new GeoCodingTask();
        task.execute(address);
    }

    class  GeoCodingTask extends AsyncTask<String, Void, byte[]>
    {
        String url;
        @Override
        protected byte[] doInBackground(String... params) //connect network not in main thread.
        {
            String address = params[0];
            double[] latLng = Utils.addressToLatLng(address);
            url=Utils.getStaticMapUrl(latLng, 18);
            return Utils.urlToByte(url);
        }

        protected void onPostExecute(byte[] bytes)
        {
            staticMapWebView.loadUrl(url);
            //addressTextView.setText(latLng[0] + "," + latLng[1]);
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            staticMapImage.setImageBitmap(bm);
        }
    }
}
