package com.company.dementiacare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.dementiacare.component.TypeModal;

import java.util.ArrayList;

public class TM_RecyclerViewAdaptor extends RecyclerView.Adapter<TM_RecyclerViewAdaptor.MyViewHolder> {

    Context context;
    ArrayList<TypeModal> typeModals;

    public TM_RecyclerViewAdaptor(Context context, ArrayList<TypeModal> typeModals){

        this.context = context;
        this.typeModals = typeModals;
    }

    @NonNull
    @Override
    public TM_RecyclerViewAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.type_list_view_item, parent, false);
        return new TM_RecyclerViewAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TM_RecyclerViewAdaptor.MyViewHolder holder, int position) {

        holder.typeName.setText(typeModals.get(position).getTypeName());
        holder.typeImage.setImageResource(typeModals.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return typeModals.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView typeImage;
        TextView typeName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            typeImage = itemView.findViewById(R.id.type_image);
            typeName = itemView.findViewById(R.id.type_name);


        }
    }
}
