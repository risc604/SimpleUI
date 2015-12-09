package example.com.simpleui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DrinkMenuActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
    }

    public void add(View view)
    {
        Button  button = (Button) view;
        int number = Integer.parseInt(button.getText().toString());
        number++;
        button.setText(String.valueOf(number));
    }

    /*  JSON format
    [   {"name":"black tea", "1":2, "m":10},
        {"name":"milk tea", "1":10, "m":10},
        {"name":"green tea", "1":5, "m":10} ]
     */

    public void getData()
    {
        LinearLayout    rooLinearLayout = (LinearLayout) findViewById(R.id.root);
        int count = rooLinearLayout.getChildCount();
        for (int i=0; i<(count-1); i++) {
            LinearLayout ll = (LinearLayout) rooLinearLayout.getChildAt(i);

            TextView drinkNameTextView = (TextView) ll.getChildAt(0);
            Button  lButton = (Button) ll.getChildAt(1);
            Button  mButton = (Button) ll.getChildAt(2);

            String drinkName = drinkNameTextView.getText().toString();
            int lNumber = Integer.parseInt(lButton.getText().toString());
            int mNumber = Integer.parseInt(mButton.getText().toString());
        }

    }
}

