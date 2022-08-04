/*
 *          StaticRVAdapter for reminders 
 *  
 *  Description: This class is used to create a recycler view adapter for the reminders.
 * 
 * updated: July 21, 2022
 * 
*/

package com.company.dementiacare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


// The class for array of item (Adapter)
public class StaticRVAdapter extends RecyclerView.Adapter<StaticRVAdapter.StaticRVViewHolder> {


    // The array of item
    public ArrayList<StaticRVModel> items;
    int row_index = -1;  //Check the item selected or not

    public StaticRVAdapter(){
        items = new ArrayList<StaticRVModel>();
        items.add(new StaticRVModel(R.drawable.inhaler, 1, R.color.light_blue_600, "patient",
                "Reminder 1", "Time", "Description", "dosage", "color", "type", "unit",
                new ArrayList<ArrayList<String>>() {
                    {
                        add(new ArrayList<String>() {
                            {
                                add("8:00");
                            }
                        });
                    }}
        ));
    }

    public StaticRVAdapter(ArrayList<StaticRVModel> items) {
        this.items = items;
    }

    public ArrayList<StaticRVModel> getItems() {
        return items;
    }

    @NonNull
    @Override
    //Set up the default background for item in recycle view
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_rv_item_layout,parent,false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    // Set the item that user clicks on the recycle views
    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder,final int position) {
        // Set the item that user clicks on the recycle views
        StaticRVModel currentItem = items.get(getItemViewType(position));
        holder.imageView.setImageResource(currentItem.getImage());
        // Set the text for the item
        holder.textView.setText(currentItem.getName());
        holder.desView.setText(currentItem.getDescription());
        String time = "";
        if (currentItem.getTagDaily() == 1 ){
            for (String _time:currentItem.getArrTime().get(8)){
                time +=_time + " ";
            }
        }
        else {
            time = currentItem.getTime();
        }
        holder.timeView.setText(time);
        holder.colorView.setText(currentItem.getColor());
        holder.typeView.setText(currentItem.getType());
        holder.patient.setText(currentItem.getPatient());
        if (currentItem.getDateVisible() == true){
            holder.dateView.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date objDate = null;
            try {
                objDate = dateFormat.parse(currentItem.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEEE dd-MM-yyyy");
            String finalDate = dateFormat2.format(objDate);
            holder.dateView.setText(finalDate);
        }
//        String date = "";
//        date = new SimpleDateFormat("EEEE dd - MM - yyyy").format(currentItem.getTime()).toString();

        // Set the listener for the item
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = getItemViewType(position);
                notifyDataSetChanged();
            }
        });

        //Check that the position the user click is on the recycle view and show checking layout
        if (row_index == getItemViewType(position)){
            // Show the checking layout
            currentItem.setVisibility(!currentItem.visibility);
            // Set the background for the item
            holder.expandedLayout.setVisibility(currentItem.isVisible() ?  View.VISIBLE : View.GONE);
            // if the item is visible, set a listener for the item
            if (currentItem.isVisible()) {
                // Set the listener for the item
                holder.finishedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show the dialog box to confirm that the user finished the item
                        currentItem.setVisibility(!currentItem.visibility);
                        // set the visibility of the item
                        holder.expandedLayout.setVisibility(currentItem.isVisible() ? View.VISIBLE : View.GONE);
                        holder.constraintLayout.setBackgroundResource(R.drawable.dynamic_rv_selected_bg);
                         // set the visibility of the checking layout
                        holder.checkImage.setVisibility(View.VISIBLE);
                    }
                });
                // if the item is not visible, set a listener for the item
                holder.unfinishedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show the dialog box to confirm that the user finished the item
                        currentItem.setVisibility(!currentItem.visibility);
                        // set the visibility of the item
                        holder.expandedLayout.setVisibility(currentItem.isVisible() ? View.VISIBLE : View.GONE);
                        holder.constraintLayout.setBackgroundResource(R.drawable.dynamic_rv_bg);
                        // set the visibility of the checking layout
                        holder.checkImage.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    // get the item view
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // get the number of item in the array
    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{

        //Variables
        TextView textView, desView, timeView, dosageView, colorView, typeView, patient;
        ImageView imageView, checkImage, finishedBtn, unfinishedBtn;
        ConstraintLayout constraintLayout, expandedLayout;
        CardView colorViewCard;
        TextView dateView;

        // The constructor for the view holder
        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.name);
            desView = itemView.findViewById(R.id.item_description);
            colorView = itemView.findViewById(R.id.item_colorText);
            typeView = itemView.findViewById(R.id.item_type);
            colorViewCard = itemView.findViewById(R.id.item_color);
            timeView = itemView.findViewById(R.id.item_time);
            checkImage = itemView.findViewById(R.id.checkImage);
            constraintLayout = itemView.findViewById(R.id.constraintLayout2);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);
            finishedBtn = itemView.findViewById(R.id.finishedBtn);
            unfinishedBtn = itemView.findViewById(R.id.unfinishedBtn);
            patient = itemView.findViewById(R.id.item_patient);
            dateView = itemView.findViewById(R.id.item_date);
        }
    }

}
