package com.petgoldfish.wolbruv;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<DeviceData> deviceDataList = Collections.emptyList();
    private WakeOnLan wakeOnLan;
    private EditText macPrompt;
    private EditText IPPrompt;
    private EditText aliasPrompt;
    private Context mContext;

    public RVAdapter(Context context, List<DeviceData> deviceData) {

        this.deviceDataList = deviceData;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DeviceData current = deviceDataList.get(position);
        String macText, IPText;
        macText = "MAC - " + current.MAC;
        IPText = "IP - " + current.IP;
        holder.aliasDisplay.setText(current.alias);
        holder.macDisplay.setText(macText);
        holder.IPDisplay.setText(IPText);
        holder.wakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wakeOnLan = new WakeOnLan(current.MAC, current.IP);
                new WOL().execute();
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.macDisplay.getVisibility() == View.GONE) {
                    holder.macDisplay.setVisibility(View.VISIBLE);
                    holder.IPDisplay.setVisibility(View.VISIBLE);
                    holder.editBtn.setVisibility(View.VISIBLE);
                } else {
                    holder.macDisplay.setVisibility(View.GONE);
                    holder.IPDisplay.setVisibility(View.GONE);
                    holder.editBtn.setVisibility(View.GONE);
                }
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.add_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);
                alertDialogBuilder.setView(promptsView);

                macPrompt = (EditText) promptsView.findViewById(R.id.macPrompt);
                IPPrompt = (EditText) promptsView.findViewById(R.id.IPPrompt);
                aliasPrompt = (EditText) promptsView.findViewById(R.id.aliasPrompt);

                macPrompt.setText(current.MAC);
                IPPrompt.setText(current.IP);
                aliasPrompt.setText(current.alias);

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Edit")
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        boolean exists = false;

                                        for (int i = 0; i < deviceDataList.size(); i++) {
                                            DeviceData temp = deviceDataList.get(i);
                                            if (temp.MAC.equalsIgnoreCase(macPrompt.getText().toString().trim())) {
                                                exists = true;
                                            }
                                        }

                                        //Validation
                                        if (!exists) {
                                            DeviceData sav = DeviceData.findById(DeviceData.class, current.getId());
                                            sav.MAC = macPrompt.getText().toString().trim();
                                            sav.IP = IPPrompt.getText().toString().trim();
                                            sav.alias = aliasPrompt.getText().toString().trim();
                                            sav.save();

                                        } else {
                                            Toast.makeText(mContext, "Device exists", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                        .setNeutralButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DeviceData del = DeviceData.findById(DeviceData.class, current.getId());
                                        del.delete();
                                        removeItem(position);
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder delDialogBuilder = new AlertDialog.Builder(mContext);
                delDialogBuilder.setTitle("Delete " + current.alias + " ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(position);
                            }
                        })
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = delDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceDataList.size();
    }

    public void addItem() {
        notifyItemInserted(deviceDataList.size());
    }

    public void removeItem(int position) {
        deviceDataList.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView macDisplay, IPDisplay, aliasDisplay;
        Button wakeBtn;
        ImageButton editBtn;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            macDisplay = (TextView) itemView.findViewById(R.id.macDisplay);
            IPDisplay = (TextView) itemView.findViewById(R.id.IPDisplay);
            aliasDisplay = (TextView) itemView.findViewById(R.id.aliasDisplay);
            wakeBtn = (Button) itemView.findViewById(R.id.wakeBtn);
            editBtn = (ImageButton) itemView.findViewById(R.id.editBtn);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.cardLayout);

        }
    }

    class WOL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            wakeOnLan.run();
            wakeOnLan.run();
            wakeOnLan.run();

            return null;
        }

    }
}

