package example.com.simpleui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_MENU_ACTIVITY = 1;
    private final static int REQUEST_TAKE_PHOTO = 2;

    private EditText inputText;
    //private Button      btnNext;
    private CheckBox hideCheckBox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ListView historyListView;
    private Spinner storeInfoSpinner;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private String menuResult;
    private ImageView photoImageView;
    private boolean hasPhoto = false;
    private List<ParseObject> queryResult;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ParseObject testObject = new ParseObject("People");
        //testObject.put("name", "Tom");
        //testObject.put("age", "23");
        //testObject.saveInBackground();

        setContentView(R.layout.activity_main);

        storeInfoSpinner = (Spinner) findViewById(R.id.storeInfoSpinner);
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        inputText = (EditText) findViewById(R.id.inputText);
        //btnNext = (Button)findViewById(R.id.button);
        // hideCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);
        //hideCheckBox.setChecked(true);
        photoImageView = (ImageView) findViewById(R.id.photo);

        //inputText.setText("1234");
        inputText.setText(sharedPreferences.getString("inputText", ""));
        inputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        submit(v);
                        return true;
                    }
                }
                return false;
            }
        });
        //btnNext.setText("Next");
        hideCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);
        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckBox", isChecked);
                editor.commit();
            }
        });
        hideCheckBox.setChecked(sharedPreferences.getBoolean("hideCheckBox", false));

        historyListView = (ListView) findViewById(R.id.historyListView);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOrderDetail(position);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setHistory();
        setStoreInfo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void goToOrderDetail(int position) {
        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);
        ParseObject object = queryResult.get(position);
        intent.putExtra("note", object.getString("note"));
        startActivity(intent);
    }

    private void setStoreInfo() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                //make objects to array.
                String[] stores = new String[objects.size()];
                for (int i = 0; i < stores.length; i++) {
                    ParseObject object = objects.get(i);
                    stores[i] = "[" + object.getString("name") + "], " + object.getString("address");
                }
                ArrayAdapter<String> storeAdapter = new
                        ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, stores);
                storeInfoSpinner.setAdapter(storeAdapter);
            }
        });


        //String[] stores = getResources().getStringArray(R.array.storeInfo);
        //ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stores);
        //storeInfoSpinner.setAdapter(storeAdapter);
    }

    private void setHistory() {
        //String[] data = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                queryResult = objects;
                List<Map<String, String>> data = new ArrayList<>();

                for (int i = 0; i < objects.size(); i++) {
                    ParseObject object = objects.get(i); //JSON ojgect package.
                    String note = object.getString("note");
                    JSONArray array = object.getJSONArray("menu");

                    Map<String, String> item = new HashMap<>();
                    item.put("note", note);
                    item.put("drinkNum", "15");
                    item.put("storeInfo", "NTU Store");

                    data.add(item);
                }

                String[] from = {"note", "drinkNum", "storeInfo"};
                int[] to = {R.id.note, R.id.drinkNum, R.id.storeInfo};
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listview_item, from, to);
                historyListView.setAdapter(adapter);
                historyListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        //String[] rawData = Utils.readFile(this, "history.txt").split("\n");

        // "name" -> "Tom"
        // "birth" -> "19000909"
        // "Sex" -> "M"
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
    }

    public void submit(View view)       // button on Click action function.
    {
        progressDialog.setTitle("Loading ...");
        progressDialog.show();

        String text = inputText.getText().toString();
        editor.putString("inputText", text);
        editor.commit();    // write to sharePreferce.

        try
        {
            JSONObject orderData = new JSONObject();
            if (menuResult == null)
                menuResult = "[]";
            JSONArray array = new JSONArray(menuResult);
            orderData.put("note", text);    //make data set.
            orderData.put("menu", array);
            Utils.writeFile(this, "history.txt", orderData.toString() + "\n");

            ParseObject orderObject = new ParseObject("Order");
            orderObject.put("note", text);
            orderObject.put("menu", array);
            if (hasPhoto == true)
            {
                Uri uri = Utils.getPhotoUri();
                ParseFile parseFile = new ParseFile("photo.png", Utils.uriToByte(this, uri));
                orderObject.put("photo", parseFile);
            }

            orderObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e)
                {
                    progressDialog.dismiss();
                    //Log.d("debug", "line: 179");
                    if (e == null)
                    {
                        Toast.makeText(MainActivity.this, "[SaveCallback] ok.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "[SaveCallback] fail.", Toast.LENGTH_SHORT).show();
                    }
                }
            });  // the other thread.
            //Log.d("debug", "line: 186");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        if (hideCheckBox.isChecked()) {
            text = "*******************";
            inputText.setText("*******************");
        }
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, Utils.readFile(this, "history.txt"), Toast.LENGTH_LONG).show();
        //inputText.setText("");
        setHistory();  //reload list view.
    }

    public void goToMenu(View view) {
        //Intent  intent = new Intent();
        //intent.setClass(this, DrinkMenuActivity.class);
        //startActivity(intent);
        startActivityForResult(new Intent().setClass(this, DrinkMenuActivity.class), REQUEST_CODE_MENU_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //String result = data.getStringExtra("result");
                //Log.d("debug: ", result);
                menuResult = data.getStringExtra("result");
            }
            //super.onActivityResult(REQUEST_CODE_MENU_ACTIVITY, RESULT_OK, data);
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                //Bitmap bm = data.getParcelableExtra("data"); //raw picture data.
                //photoImageView.setImageBitmap(bm);

                Uri uri = Utils.getPhotoUri();
                photoImageView.setImageURI(uri);
                hasPhoto = true;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_take_photo) {
            //Toast.makeText(this, "take photo", Toast.LENGTH_SHORT).show();
            goToCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoUri());  // call Utils.java
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }
}
