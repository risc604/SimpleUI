package example.com.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity
{
    private final static int REQUEST_CODE_MENU_ACTIVITY = 1;
    private EditText inputText;
    //private Button      btnNext;
    private CheckBox hideCheckBox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ListView historyListView;
    private Spinner storeInfoSpinner;
    private String menuResult;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

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
        setHistory();
        setStoreInfo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setStoreInfo()
    {
        String[] stores = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stores);
        storeInfoSpinner.setAdapter(storeAdapter);
    }

    private void setHistory() {
        //String[] data = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] rawData = Utils.readFile(this, "history.txt").split("\n");

        // "name" -> "Tom"
        // "birth" -> "19000909"
        // "Sex" -> "M"
        List<Map<String, String>> data = new ArrayList<>();

        for (int i=0; i<rawData.length; i++)
        {
            try
            {
                JSONObject object = new JSONObject(rawData[i]);
                String note = object.getString("note");
                JSONArray array = object.getJSONArray("menu");

                Map<String, String> item = new HashMap<>();
                item.put("note", note);
                item.put("drinkNum", "15");
                item.put("storeInfo", "NTU Store");

                data.add(item);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
       }

        String[] from = {"note", "drinkNum", "storeInfo"};
        int[] to = {R.id.note, R.id.drinkNum, R.id.storeInfo};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_item, from, to);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        historyListView.setAdapter(adapter);
    }

    public void submit(View view)       // button on Click action function.
    {
        String text = inputText.getText().toString() ;
        editor.putString("inputText", text);
        editor.commit();    // write to sharePreferce.

        try {
            JSONObject orderData = new JSONObject();
            if (menuResult == null)
                menuResult = "[]";
            JSONArray array = new JSONArray(menuResult);
            orderData.put("note", text);
            orderData.put("menu", array);
            Utils.writeFile(this, "history.txt", orderData.toString() + "\n");

            ParseObject testObject = new ParseObject("Order");
            testObject.put("note", text);
            testObject.put("menu", array);
            testObject.saveInBackground();

        } catch (JSONException e) {
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

    public void goToMenu(View view)
    {
        //Intent  intent = new Intent();
        //intent.setClass(this, DrinkMenuActivity.class);
        //startActivity(intent);
        startActivityForResult(new Intent().setClass(this, DrinkMenuActivity.class), REQUEST_CODE_MENU_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_MENU_ACTIVITY)
        {
            if (resultCode == RESULT_OK)
            {
                //String result = data.getStringExtra("result");
                //Log.d("debug: ", result);
                menuResult = data.getStringExtra("result");
            }
            //super.onActivityResult(REQUEST_CODE_MENU_ACTIVITY, RESULT_OK, data);
        }
    }
}
