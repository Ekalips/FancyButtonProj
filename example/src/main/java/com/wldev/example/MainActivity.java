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

        FancyButton button1 = (FancyButton) findViewById(R.id.btn1);
        FancyButton button2 = (FancyButton) findViewById(R.id.btn2);
        FancyButton button3 = (FancyButton) findViewById(R.id.btn3);

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
    }
}
