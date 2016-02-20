package com.petgoldfish.wolbruv;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    String[] names;
    private EditText macPrompt;
    private EditText IPPrompt;
    private EditText aliasPrompt;
    private ListView listView;
    private WakeOnLan wakeOnLan;
    private CoordinatorLayout coordinatorLayout;
    private List<DeviceData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataList = DeviceData.listAll(DeviceData.class, "alias");
        names = new String[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            names[i] = dataList.get(i).alias;
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wakeOnLan = new WakeOnLan(dataList.get(position).MAC, dataList.get(position).IP);
                new WOL().execute("");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.add_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setView(promptsView);

                macPrompt = (EditText) promptsView.findViewById(R.id.macPrompt);
                IPPrompt = (EditText) promptsView.findViewById(R.id.IPPrompt);
                aliasPrompt = (EditText) promptsView.findViewById(R.id.aliasPrompt);

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Add a device")
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        boolean exists = false;

                                        DeviceData deviceData = new DeviceData(macPrompt.getText().toString().trim(),
                                                IPPrompt.getText().toString().trim(),
                                                aliasPrompt.getText().toString().trim());

                                        for (int i = 0; i < dataList.size(); i++) {
                                            DeviceData temp = dataList.get(i);
                                            if (temp.MAC.equalsIgnoreCase(macPrompt.getText().toString().trim())) {
                                                exists = true;
                                            }
                                        }

                                        //Validation
                                        if (!exists) {
                                            deviceData.save();
                                        } else {
                                            Toast.makeText(context, "Device exists", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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
