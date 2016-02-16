package com.petgoldfish.wolbruv;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText macText;
    EditText ipText;
    Button wolButton;
    WakeOnLan wakeOnLan;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        macText = (EditText) findViewById(R.id.macText);
        ipText = (EditText) findViewById(R.id.IPText);
        wolButton = (Button) findViewById(R.id.wolButton);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        wolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wakeOnLan = new WakeOnLan(macText.getText().toString().trim(), ipText.getText().toString().trim());
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Snackbar sb = Snackbar.make(coordinatorLayout, "Woken, bruv!", Snackbar.LENGTH_LONG);
            sb.show();
        }
    }
}


