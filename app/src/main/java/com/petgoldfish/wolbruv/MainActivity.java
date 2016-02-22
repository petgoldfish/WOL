package com.petgoldfish.wolbruv;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private EditText macPrompt;
    private EditText IPPrompt;
    private EditText aliasPrompt;
    private RecyclerView recyclerView;
    private RVAdapter adapter;
    private List<DeviceData> dataList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(linearLayoutManager);

        FadeInRightAnimator animator = new FadeInRightAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

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
                                            //deviceData.save();
                                            dataList.add(deviceData);
                                            adapter.addItem();

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

    public void refreshList() {

        dataList = DeviceData.listAll(DeviceData.class, "alias");

        adapter = new RVAdapter(this, dataList);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

    }
}
