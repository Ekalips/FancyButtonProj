package com.wldev.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ekalips.fancybuttonproj.FancyButton;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FancyButton button1 = findViewById(R.id.btn1);
        FancyButton button2 = findViewById(R.id.btn2);
        FancyButton button3 = findViewById(R.id.btn3);
        FancyButton button4 = findViewById(R.id.btn4);
        FancyButton button5 = findViewById(R.id.btn5);
        FancyButton button6 = findViewById(R.id.btn6);
        FancyButton button7 = findViewById(R.id.btn7);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof  FancyButton)
                {
                    if (((FancyButton)view).isExpanded())
                        ((FancyButton)view).collapse();
                    else
                        ((FancyButton)view).expand();
                }

            }
        };
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
    }
}
