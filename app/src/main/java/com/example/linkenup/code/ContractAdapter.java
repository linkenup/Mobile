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
import com.example.linkenup.activities.OpenContractActivity;
import com.example.linkenup.system.Contract;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.Holder>{

    DatabaseHelper db;
    Context context;
    LayoutInflater inflater;
    public Contract[] contractList;

    public ContractAdapter(Context context, Contract[] list){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contractList = list;
        db = new DatabaseHelper(context);
    }
    @NonNull
    @Override
    public ContractAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_contract, parent, false);
        ContractAdapter.Holder holder = new ContractAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContractAdapter.Holder holder, int position) {
        holder.clientText.setText(db.getClient(contractList[position].fkClient).name);
        holder.softwareText.setText(db.getSoftware(contractList[position].fkSoftware).name);
        holder.idText.setText("ID: "+String.valueOf(contractList[position].id));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenContractActivity.class);
                intent.putExtra(OpenContractActivity.EXTRA_CONTRACT_ID, contractList[position].id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contractList!=null?contractList.length:0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView clientText, softwareText, idText;

        public Holder(@NonNull View itemView) {super(itemView);
            clientText = itemView.findViewById(R.id.itemcontract_text_clientname);
            softwareText = itemView.findViewById(R.id.itemcontract_text_softwarename);
            idText = itemView.findViewById(R.id.itemcontract_text_id);
        }
    }
}
