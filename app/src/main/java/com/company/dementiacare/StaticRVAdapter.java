/*
 *          StaticRVAdapter for reminders 
 *  
 *  Description: This class is used to create a recycler view adapter for the reminders.
 * 
 * updated: July 21, 2022
 * 
*/

package com.company.dementiacare;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


// The class for array of item (Adapter)
public class StaticRVAdapter extends RecyclerView.Adapter<StaticRVAdapter.StaticRVViewHolder> {


    // The array of item
    public List<StaticRVModel> items;
    int row_index = -1;  //Check the item selected or not

    public StaticRVAdapter(){
        items = new List<StaticRVModel>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<StaticRVModel> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(StaticRVModel staticRVModel) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends StaticRVModel> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends StaticRVModel> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public StaticRVModel get(int index) {
                return null;
            }

            @Override
            public StaticRVModel set(int index, StaticRVModel element) {
                return null;
            }

            @Override
            public void add(int index, StaticRVModel element) {

            }

            @Override
            public StaticRVModel remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<StaticRVModel> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<StaticRVModel> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<StaticRVModel> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        items.add(new StaticRVModel(R.drawable.inhaler, 1, R.color.light_blue_600, "patient",
                "Reminder 1", "Time", "Description", "dosage", "color", "type", "unit",
                new ArrayList<ArrayList<String>>() {
                    {
                        add(new ArrayList<String>() {
                            {
                                add("8:00");
                                add("8:30");
                                add("9:00");
                                add("9:30");
                                add("10:00");
                                add("10:30");
                                add("11:00");
                                add("11:30");
                                add("12:00");
                                add("12:30");
                                add("13:00");
                                add("13:30");
                                add("14:00");
                                add("14:30");
                                add("15:00");
                                add("15:30");
                                add("16:00");
                                add("16:30");
                                add("17:00");
                                add("17:30");
                                add("18:00");
                                add("18:30");
                                add("19:00");
                                add("19:30");
                                add("20:00");
                                add("20:30");
                                add("21:00");
                                add("21:30");
                                add("22:00");
                                add("22:30");
                                add("23:00");
                                add("23:30");
                            }
                        });
                    }}
        ));
    }

    public StaticRVAdapter(ArrayList<StaticRVModel> items) {
        this.items = items;
    }

    public List<StaticRVModel> getItems() {
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
//        String time = "";
//        for (String _time:currentItem.getArrTime().get(0)){
//            time +=_time + " ";
//        }
        String time = "";
        if (currentItem.getTagDaily() == 1 ){
            for (String _time:currentItem.getArrTime().get(0)){
                time +=_time + " ";
            }
        }
        else {
            time = currentItem.getTime();
        }
        holder.timeView.setText(time);
        holder.colorView.setText(currentItem.getColor());
        holder.typeView.setText(currentItem.getType());
        holder.colorViewCard.setCardBackgroundColor(currentItem.getCardColor());
        holder.patient.setText(currentItem.getPatient());
//        String date = "";
//        date = new SimpleDateFormat("EEEE dd - MM - yyyy").format(currentItem.getTime()).toString();
        holder.dateView.setText(currentItem.getTime());

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
