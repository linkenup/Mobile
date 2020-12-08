package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.ClientActivity;
import com.example.linkenup.MapActivity;
import com.example.linkenup.activities.OpenClientActivity;
import com.example.linkenup.R;
import com.example.linkenup.system.Client;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.Holder>{

    public static int LAYOUT_COMMON = 23, LAYOUT_MAP = 26;

    Context context;
    LayoutInflater inflater;
    public Client[] clientList;
    boolean resulting;
    int layoutNow;
    private Double latitude,longitude;

    public ClientAdapter(Context context, Client[] list){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.clientList = list;
        resulting = false;
        layoutNow = LAYOUT_COMMON;
    }

    public ClientAdapter(Context context, Client[] list,int layout){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.clientList = list;
        resulting = false;
        layoutNow = layout;

        if(layoutNow == LAYOUT_MAP){
            latitude = ((MapActivity)context).lastLatitude;
            longitude = ((MapActivity)context).lastLongitude;
        }
    }

    public ClientAdapter(ClientActivity context, Client[] list, boolean resulting){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.clientList = list;
        this.resulting = resulting;
        layoutNow = LAYOUT_COMMON;
    }
    @NonNull
    @Override
    public ClientAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(layoutNow==LAYOUT_COMMON) view = inflater.inflate(R.layout.item_client, parent, false);
        else if(layoutNow == LAYOUT_MAP) try{view = inflater.inflate(R.layout.item_client_map,parent,false);}catch (Exception e){return null;}
        else return null;
        ClientAdapter.Holder holder = new ClientAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.Holder holder, int position) {
        holder.textName.setText(clientList[position].name);
        holder.textID.setText("ID: "+String.valueOf(clientList[position].id));

        if(layoutNow==LAYOUT_COMMON){
            holder.textCnpj.setText(context.getString(R.string.cnpj)+": "+clientList[position].cnpj);

            if(!resulting)holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OpenClientActivity.class);
                    intent.putExtra(OpenClientActivity.EXTRA_CLIENT_ID, clientList[position].id);
                    context.startActivity(intent);
                }
            });
            else holder.itemView.setOnClickListener((View view)->{
                    ((ClientActivity)context).resultClient(clientList[position]);
            });
        }
        if(layoutNow==LAYOUT_MAP){
            if(clientList[position].getLastDistance()!=null)
                holder.textID.setText(String.format("%.02fKm",clientList[position].getLastDistance() / 1000));
            else{
                if(latitude!=null&&longitude!=null) {
                    Float distance = clientList[position].getDistance(context, latitude, longitude);
                    if (distance != null)
                        holder.textID.setText(String.format("%.02fKm",distance / 1000));
                }
            }
            holder.itemView.setOnClickListener((View view)->{
                ((MapActivity)context).clickClient(clientList[position]);
            });
        }
    }

    @Override
    public int getItemCount() {
        return clientList!=null?clientList.length:0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textName, textCnpj, textID, textContracts;

        public Holder(@NonNull View itemView) {super(itemView);
            textName = (TextView) itemView.findViewById(R.id.itemclient_text_name);
            if(layoutNow==LAYOUT_COMMON)textCnpj = (TextView) itemView.findViewById(R.id.itemclient_text_cnpj);
            textID = (TextView) itemView.findViewById(R.id.itemclient_text_id);

        }
    }
}
