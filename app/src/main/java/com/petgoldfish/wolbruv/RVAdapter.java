package com.petgoldfish.wolbruv;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<DeviceData> deviceDataList = Collections.emptyList();
    private WakeOnLan wakeOnLan;

    public RVAdapter(Context context, List<DeviceData> deviceData) {

        this.deviceDataList = deviceData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
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
                } else {
                    holder.macDisplay.setVisibility(View.GONE);
                    holder.IPDisplay.setVisibility(View.GONE);
                }
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
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            macDisplay = (TextView) itemView.findViewById(R.id.macDisplay);
            IPDisplay = (TextView) itemView.findViewById(R.id.IPDisplay);
            aliasDisplay = (TextView) itemView.findViewById(R.id.aliasDisplay);
            wakeBtn = (Button) itemView.findViewById(R.id.wakeBtn);
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

