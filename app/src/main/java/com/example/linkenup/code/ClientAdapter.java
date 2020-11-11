package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.OpenClientActivity;
import com.example.linkenup.R;
import com.example.linkenup.system.Cliente;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.Holder>{

    Context context;
    LayoutInflater inflater;
    List<Cliente> list;

    public ClientAdapter(Context context, List<Cliente> list){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ClientAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_client, parent, false);
        ClientAdapter.Holder holder = new ClientAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenClientActivity.class);
                intent.putExtra("PK", list.get(position).primaryKey);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
