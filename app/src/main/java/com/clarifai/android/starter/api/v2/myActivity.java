package com.clarifai.android.starter.api.v2;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Dikshant on 2/11/2017.
 */

public class myActivity extends Activity implements myInterface {

    private Button buttons[] = null;

    @Override
    public void showTextView(){
        // set buttons
        findViewById(R.id.food_b0).setVisibility(View.VISIBLE);
        this.buttons[0] = (Button)findViewById(R.id.food_b0);

        findViewById(R.id.food_b1).setVisibility(View.VISIBLE);
        this.buttons[1] = (Button)findViewById(R.id.food_b1);

        findViewById(R.id.food_b2).setVisibility(View.VISIBLE);
        this.buttons[2] = (Button)findViewById(R.id.food_b2);

        findViewById(R.id.food_b3).setVisibility(View.VISIBLE);
        this.buttons[3] = (Button)findViewById(R.id.food_b3);

        findViewById(R.id.food_b4).setVisibility(View.VISIBLE);
        this.buttons[4] = (Button)findViewById(R.id.food_b4);

        this.buttons[0].setText("Russet Potato");
        this.buttons[1].setText("Red Potato");
        this.buttons[2].setText("White Potato");
        this.buttons[3].setText("Yellow Potato");
        this.buttons[4].setText("Fingerling Potato");

        for (int i = 0; i <= 4; i++){
            this.buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Log.d("Button Dialogue", "pressed");

                }
            });
        }
    }
}
