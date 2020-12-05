package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.activities.OpenWorkerActivity;
import com.example.linkenup.R;
import com.example.linkenup.WorkerActivity;
import com.example.linkenup.system.Worker;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.Holder> {
    public Worker[] workerList;
    Context context;
    LayoutInflater inflater;
    boolean resulting;

    public WorkerAdapter(Context context, Worker[] list) {
    this.context = context;
    this.workerList = list;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.resulting = false;
    }

    public WorkerAdapter(WorkerActivity context, Worker[] list, boolean resulting) {
        this.context = context;
        this.workerList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resulting = resulting;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_worker,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textID.setText("ID: "+String.valueOf(workerList[position].id));
        holder.textCPF.setText(context.getString(R.string.cpf)+": "+workerList[position].cpf);
        holder.textName.setText(workerList[position].name);
        if(!resulting)holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenWorkerActivity.class);
                intent.putExtra(OpenWorkerActivity.EXTRA_WORKER_ID, workerList[position].id);
                context.startActivity(intent);
            }
        });
        else holder.itemView.setOnClickListener((View view)->{
            ((WorkerActivity)context).resultWorker(workerList[position]);
        });

    }

    @Override
    public int getItemCount() {
        return workerList!=null?workerList.length:0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textID,textName,textCPF;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textID = itemView.findViewById(R.id.itemworker_text_id);
            textName = itemView.findViewById(R.id.itemworker_text_name);
            textCPF = itemView.findViewById(R.id.itemworker_text_cpf);
        }
    }
}
