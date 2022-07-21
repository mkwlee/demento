/*
 *          Unused class
 * 
 *  Description: A recycler view adapter for the list of the types of medication.
 * 
 *  updated: July 20, 2022
*/

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

    // variables
    Context context;
    // ArrayList of the type of medication
    ArrayList<TypeModal> typeModals;

    // The constructor for the adapter
    public TM_RecyclerViewAdaptor(Context context, ArrayList<TypeModal> typeModals){

        this.context = context;
        this.typeModals = typeModals;
    }

    @NonNull
    @Override
    public TM_RecyclerViewAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate the layout for the recycler view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.type_list_view_item, parent, false);
        // create a new view holder
        return new TM_RecyclerViewAdaptor.MyViewHolder(view);
    }

    // bind the data to the view holder
    @Override
    public void onBindViewHolder(@NonNull TM_RecyclerViewAdaptor.MyViewHolder holder, int position) {

        // get the current item
        holder.typeName.setText(typeModals.get(position).getTypeName());
        // get the image for the current item
        holder.typeImage.setImageResource(typeModals.get(position).getImage());
    }

    // get the number of items in the list
    @Override
    public int getItemCount() {
        return typeModals.size();
    }

    // create a view holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // variables
        ImageView typeImage;
        TextView typeName;

        // constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            typeImage = itemView.findViewById(R.id.type_image);
            typeName = itemView.findViewById(R.id.type_name);

        }
    }
}
