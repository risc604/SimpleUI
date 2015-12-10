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
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    private final static int REQUEST_CODE_MENU_ACTIVITY = 1;
    private EditText    inputText;
    //private Button      btnNext;
    private CheckBox    hideCheckBox;
    private SharedPreferences   sharedPreferences;
    private SharedPreferences.Editor    editor;
    private ListView    historyListView;
    private Spinner     storeInfoSpinner;
    private String      menuResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storeInfoSpinner = (Spinner) findViewById(R.id.storeInfoSpinner);
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        inputText = (EditText)findViewById(R.id.inputText);
        //btnNext = (Button)findViewById(R.id.button);
        hideCheckBox = (CheckBox)findViewById(R.id.hideCheckBox);
        //hideCheckBox.setChecked(true);

        //inputText.setText("1234");
        inputText.setText(sharedPreferences.getString("inputText", ""));
        inputText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if(keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        submit(v);
                        return true;
                    }
                }
                return false;
            }
        });
        //btnNext.setText("Next");
        hideCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);
        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                        {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                            {
                                editor.putBoolean("hideCheckBox", isChecked);
                                editor.commit();
                            }
                        });
        hideCheckBox.setChecked(sharedPreferences.getBoolean("hideCheckBox", false));

        historyListView = (ListView) findViewById(R.id.historyListView);
        setHistory();
        setStoreInfo();
    }

    private void setStoreInfo()
    {
        String[] stores = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stores);
        storeInfoSpinner.setAdapter(storeAdapter);
    }

    private void setHistory()
    {
        //String[] data = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] data = Utils.readFile(this, "history.txt").split("\n");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        historyListView.setAdapter(adapter);
    }

    public void submit(View view)       // button on Click action function.
    {
        String text = inputText.getText().toString() + "," + menuResult;
        editor.putString("inputText", text);
        editor.commit();    // write to sharePreferce.

        try
        {
            JSONObject orderData = new JSONObject();
            JSONArray   array = new JSONArray(menuResult);
            orderData.put("note", text);
            orderData.put("menu", array);
            Utils.writeFile(this, "history.txt", text + "\n");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        if(hideCheckBox.isChecked())
        {
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
        if(requestCode == REQUEST_CODE_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //String result = data.getStringExtra("result");
                //Log.d("debug: ", result);
                menuResult = data.getStringExtra("result");
            }
            //super.onActivityResult(REQUEST_CODE_MENU_ACTIVITY, RESULT_OK, data);
        }
    }


}
