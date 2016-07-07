package com.petgoldfish.wolbruv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.petgoldfish.wolbruv.DB.DBController;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    final Context mContext = this;
    int defaultTheme = R.style.AppTheme;
    int darkTheme = R.style.AppTheme_Dark;
    int theme = defaultTheme;
    private EditText macPrompt;
    private EditText IPPrompt;
    private EditText aliasPrompt;
    private RecyclerView recyclerView;
    private RVAdapter adapter;
    private List<DeviceData> dataList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences.Editor editor;
    private DBController dbController;

    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean dark = sharedPreferences.getBoolean("dark", false);
        if (dark) {
            theme = darkTheme;
        } else {
            theme = defaultTheme;
        }

        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setDistanceToTriggerSync(50);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        dbController = new DBController(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(linearLayoutManager);

        FadeInRightAnimator animator = new FadeInRightAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.add_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);
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

                                        if (!Utils.validateInput(IPPrompt.getText().toString().trim(), macPrompt.getText().toString().trim())) {
                                            Toast.makeText(mContext, "Invalid IP and/or MAC", Toast.LENGTH_LONG).show();
                                        } else {

                                            DeviceData deviceData = new DeviceData(macPrompt.getText().toString().trim(),
                                                    IPPrompt.getText().toString().trim(),
                                                    aliasPrompt.getText().toString().trim());

                                            dataList = adapter.addItem(deviceData, dataList.size());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        switch (item.getItemId()) {
            case R.id.lightTheme:
                if (!(theme == defaultTheme)) {
                    editor.putBoolean("dark", false);
                    editor.apply();
                    restartActivity();
                }
                break;
            case R.id.darkTheme:
                if (!(theme == darkTheme)) {
                    editor.putBoolean("dark", true);
                    editor.apply();
                    restartActivity();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void restartActivity() {

        finish();
        final Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void refreshList() {

        //dataList = DeviceData.listAll(DeviceData.class, "id");
        dataList = dbController.getAllDeviceDatas();
        adapter = new RVAdapter(this, dataList);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
        Log.v("REFRESH", "Refreshed");

    }
}
