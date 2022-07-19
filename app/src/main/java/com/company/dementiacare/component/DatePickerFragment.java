package com.company.dementiacare.component;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.company.dementiacare.ui.add.ReminderActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerListener mListener;

    public interface DatePickerListener{
        void OnDateSet(DatePicker dataPicker, int year, int month, int day);
    }

    ReminderActivity addNewMedicineActivityObj;

    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (DatePickerListener) context;
        } catch (Exception e){
            throw new ClassCastException(getActivity().toString()+ "Implement DatePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get((Calendar.DAY_OF_MONTH));
        return new DatePickerDialog(getActivity(), this, year,month,day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mListener.OnDateSet(datePicker, i, i1, i2);
    }
}
