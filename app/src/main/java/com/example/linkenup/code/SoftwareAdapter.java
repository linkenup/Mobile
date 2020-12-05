package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.R;
import com.example.linkenup.activities.OpenSoftwareActivity;
import com.example.linkenup.SoftwareActivity;
import com.example.linkenup.system.Software;

public class SoftwareAdapter extends RecyclerView.Adapter<SoftwareAdapter.Holder>{

    Context context;
    LayoutInflater inflater;
    public Software[] softwareList;
    boolean resulting;

    public SoftwareAdapter(Context context, Software[] list){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.softwareList = list;
        resulting = true;
    }

    public SoftwareAdapter(SoftwareActivity context, Software[] list, boolean resulting) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.softwareList = list;
        this.resulting = resulting;
    }

    @NonNull
    @Override
    public SoftwareAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_software, parent, false);
        SoftwareAdapter.Holder holder = new SoftwareAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SoftwareAdapter.Holder holder, int position) {
        holder.textName.setText(softwareList[position].name);
        holder.textID.setText("ID: "+String.valueOf(softwareList[position].id));
        holder.textScreens.setText(context.getString(R.string.screens)+": "+String.valueOf(new DatabaseHelper(context).countScreen(softwareList[position].id)));

        if(!resulting)holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenSoftwareActivity.class);
                intent.putExtra(OpenSoftwareActivity.EXTRA_SOFTWARE_ID, softwareList[position].id);
                context.startActivity(intent);
            }
        });
        else holder.itemView.setOnClickListener((View view)->{
            ((SoftwareActivity)context).resultSoftware(softwareList[position]);
        });
    }

    @Override
    public int getItemCount() {
        return softwareList!=null?softwareList.length:0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textName, textID, textScreens;

        public Holder(@NonNull View itemView) {super(itemView);
            textName = (TextView) itemView.findViewById(R.id.itemsoftware_text_name);
            textID = (TextView) itemView.findViewById(R.id.itemsoftware_text_id);
            textScreens =(TextView) itemView.findViewById(R.id.itemsoftware_text_screens);
        }
    }
}
