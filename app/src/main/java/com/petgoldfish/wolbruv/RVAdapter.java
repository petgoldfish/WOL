package com.petgoldfish.wolbruv;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<DeviceData> deviceDataList = Collections.emptyList();
    private WakeOnLan wakeOnLan;

    public RVAdapter(Context context, List<DeviceData> deviceData) {
        this.context = context;
        this.deviceDataList = deviceData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DeviceData current = deviceDataList.get(position);
        holder.aliasDisplay.setText(current.alias);
        holder.macDisplay.setText(current.MAC);
        holder.IPDisplay.setText(current.IP);
        holder.wakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wakeOnLan = new WakeOnLan(current.MAC, current.IP);
                new WOL().execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView macDisplay, IPDisplay, aliasDisplay;
        Button wakeBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            macDisplay = (TextView) itemView.findViewById(R.id.macDisplay);
            IPDisplay = (TextView) itemView.findViewById(R.id.IPDisplay);
            aliasDisplay = (TextView) itemView.findViewById(R.id.aliasDisplay);
            wakeBtn = (Button) itemView.findViewById(R.id.wakeBtn);

        }
    }

    class WOL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            wakeOnLan.run();

            return null;
        }

    }
}

