package example.com.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText    inputText;
    //private Button      btnNext;
    private CheckBox    hideCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = (EditText)findViewById(R.id.inputText);
        //btnNext = (Button)findViewById(R.id.button);
        hideCheckBox = (CheckBox)findViewById(R.id.hideCheckBox);
        //hideCheckBox.setChecked(true);

        inputText.setText("1234");
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



        sdfgsgsdg
        dffqffq
    }

    public void submit(View view)       // button on Click action function.
    {
        String text = inputText.getText().toString();
        if(hideCheckBox.isChecked())
        {
            text = "*******************";
            inputText.setText(text);
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        inputText.setText("");
    }


}
