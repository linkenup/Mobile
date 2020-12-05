package com.example.linkenup.code;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkenup.activities.OpenScreenActivity;
import com.example.linkenup.R;
import com.example.linkenup.ScreenActivity;
import com.example.linkenup.system.Screen;

public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.Holder>{

    Context context;
    LayoutInflater inflater;
    public Screen[] screenList;
    int mode;
    public ScreenAdapter(Context context, Screen[] list,int mode){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.screenList = list;
        this.mode = mode;
    }
    @NonNull
    @Override
    public ScreenAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_screen, parent, false);
        ScreenAdapter.Holder holder = new ScreenAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenAdapter.Holder holder, int position) {
        holder.textName.setText(screenList[position].name);
        holder.textID.setText(String.valueOf(screenList[position].id));

        if(mode==ScreenActivity.OLDSOFTWARE_MODE)holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenScreenActivity.class);
                intent.putExtra(ScreenActivity.EXTRA_MODE,mode);
                intent.putExtra(OpenScreenActivity.EXTRA_SCREEN_ID, screenList[position].id);
                context.startActivity(intent);
            }
        });
        else if(mode==ScreenActivity.NEWSOFTWARE_MODE) holder.itemView.setOnClickListener((View view) -> {
            Intent intent = new Intent(context, OpenScreenActivity.class);
            intent.putExtra(ScreenActivity.EXTRA_MODE,mode);
            intent.putExtra(OpenScreenActivity.EXTRA_SCREEN_NOT_INSERTED, screenList[position]);
            intent.putExtra(OpenScreenActivity.EXTRA_SCREEN_POSITION,position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return screenList.length;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textName, textID;

        public Holder(@NonNull View itemView) {super(itemView);
            textName = (TextView) itemView.findViewById(R.id.itemscreen_text_name);
            textID = (TextView) itemView.findViewById(R.id.itemscreen_text_id);

        }
    }
}
