package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.DirectorActivity;
import com.example.linkenup.activities.OpenDirectorActivity;
import com.example.linkenup.R;
import com.example.linkenup.system.Director;

public class DirectorAdapter extends RecyclerView.Adapter<DirectorAdapter.Holder>{

    Context context;
    LayoutInflater inflater;
    public Director[] directorList;
    boolean resulting;

    public DirectorAdapter(Context context, Director[] list){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.directorList = list;
        resulting = false;
    }

    public DirectorAdapter(DirectorActivity context, Director[] list, boolean b) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.directorList = list;
        resulting = b;
    }

    @NonNull
    @Override
    public DirectorAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_director, parent, false);
        DirectorAdapter.Holder holder = new DirectorAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DirectorAdapter.Holder holder, int position) {
        holder.textName.setText(directorList[position].name);
        holder.cpfText.setText(context.getString(R.string.cpf)+": "+directorList[position].cpf);
        holder.textID.setText("ID: "+String.valueOf(directorList[position].id));

        if(!resulting)holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenDirectorActivity.class);
                intent.putExtra(OpenDirectorActivity.EXTRA_DIRECTOR_ID, directorList[position].id);
                context.startActivity(intent);
            }
        });
        else holder.itemView.setOnClickListener((View view)->{
            ((DirectorActivity)context).resultDirector(directorList[position]);
        });
    }

    @Override
    public int getItemCount() {
        return directorList!=null?directorList.length:0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textName, cpfText, textID;

        public Holder(@NonNull View itemView) {super(itemView);
            textName = (TextView) itemView.findViewById(R.id.itemdirector_text_name);
            textID = (TextView) itemView.findViewById(R.id.itemdirector_text_id);
            cpfText = (TextView) itemView.findViewById(R.id.itemdirector_text_cpf);
        }
    }
}
