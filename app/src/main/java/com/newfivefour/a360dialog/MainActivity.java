package com.newfivefour.a360dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.newfivefour.d360analyticslibrary.Analytics;

import java.util.HashMap;

/**
 * Quick, simple, dumb.
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final TextView key = (TextView) findViewById(R.id.analyticskey);
    Analytics.init(getApplicationContext(), key.getText().toString());
    Button binit = (Button) findViewById(R.id.buttoninit);
    binit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if(key.getText().length()==0) {
          key.setText("blank key");
        }
        Analytics.init(getApplicationContext(), key.getText().toString());
      }
    });

    final TextView name = (TextView) findViewById(R.id.name);
    final TextView tv1 = (TextView) findViewById(R.id.key);
    final TextView tv2 = (TextView) findViewById(R.id.value);
    final TextView tv3 = (TextView) findViewById(R.id.key1);
    final TextView tv4 = (TextView) findViewById(R.id.value1);
    Button b = (Button) findViewById(R.id.button);
    b.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Analytics.instance().send(name.getText().toString(),
                new HashMap<String, String>() {{
                  put(tv1.getText().toString(), tv2.getText().toString());
                  put(tv3.getText().toString(),tv4.getText().toString());
                }});
      }
    });
  }
}
