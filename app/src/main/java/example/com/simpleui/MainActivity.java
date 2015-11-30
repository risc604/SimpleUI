package example.com.simpleui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText    inputText;
    //private Button      btnNext;
    private CheckBox    hideCheckBox;
    private SharedPreferences   sharedPreferences;
    private SharedPreferences.Editor    editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckBox", isChecked);
                editor.commit();
            }
        });
        hideCheckBox.setChecked(sharedPreferences.getBoolean("hideCheckBox", false));
    }

    public void submit(View view)       // button on Click action function.
    {
        String text = inputText.getText().toString();
        editor.putString("inputText", text);
        editor.commit();    // write to sharePreferce.
        Utils.writeFile(this, "history.txt", text + "\n");

        if(hideCheckBox.isChecked())
        {
            text = "*******************";
            inputText.setText("*******************");
        }
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        Toast.makeText(this, Utils.readFile(this, "history.txt"), Toast.LENGTH_LONG).show();
        //inputText.setText("");
    }


}
