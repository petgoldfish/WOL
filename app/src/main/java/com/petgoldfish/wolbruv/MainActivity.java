package com.petgoldfish.wolbruv;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText macText;
    EditText ipText;
    Button wolButton;
    WakeOnLan wakeOnLan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        macText = (EditText) findViewById(R.id.macText);
        ipText = (EditText) findViewById(R.id.IPText);
        wolButton = (Button) findViewById(R.id.wolButton);

        wolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wakeOnLan = new WakeOnLan(macText.getText().toString(), ipText.getText().toString());
                new WOL().execute("");
            }
        });


    }

    class WOL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            wakeOnLan.run();

            return null;
        }
    }
}


