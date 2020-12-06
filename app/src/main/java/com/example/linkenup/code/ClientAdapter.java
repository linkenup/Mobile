package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.ClientActivity;
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
        else if(layoutNow == LAYOUT_MAP) view = inflater.inflate(R.layout.item_client_map,parent,false);
        else return null;
        ClientAdapter.Holder holder = new ClientAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.Holder holder, int position) {
        holder.textName.setText(clientList[position].name);
        if(layoutNow==LAYOUT_COMMON)holder.textCnpj.setText(context.getString(R.string.cnpj)+": "+clientList[position].cnpj);
        holder.textID.setText("ID: "+String.valueOf(clientList[position].id));

        if(layoutNow==LAYOUT_COMMON){
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
