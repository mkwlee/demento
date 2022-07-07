package com.company.dementiacare.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.dementiacare.R;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private TextView editText;
    private TextInputLayout nameField, phoneField, addressField, mailField;

    private boolean editing = false;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = getView().findViewById(R.id.edit_profile);
        nameField = getView().findViewById(R.id.name_field);
        phoneField = getView().findViewById(R.id.phone_field);
        addressField = getView().findViewById(R.id.address_field);
        mailField = getView().findViewById(R.id.mail_field);

        // when editText is clicked, it will change editing to true and show the editable fields
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editing) {
                    editing = true;
                    editText.setText("Done");
                    nameField.setEnabled(true);
                    phoneField.setEnabled(true);
                    addressField.setEnabled(true);
                    mailField.setEnabled(true);
                } else {
                    if (!validateName() | !validatePhone() | !validateAddress() | !validateMail()) {
                        return;
                    }
                    editing = false;
                    editText.setText("Edit");
                    nameField.setEnabled(false);
                    phoneField.setEnabled(false);
                    addressField.setEnabled(false);
                    mailField.setEnabled(false);
                }
            }
        });
    }

    private boolean validateName () {
        if (nameField.getEditText().getText().toString().isEmpty()) {
            nameField.setError("Name is required");
            return false;
        } else {
            nameField.setError(null);
            return true;
        }
    }

    private boolean validatePhone () {
        if (phoneField.getEditText().getText().toString().isEmpty()) {
            phoneField.setError("Phone is required");
            return false;
        }
            // check if the phone number is valid and if it is, show error message
        else if (!phoneField.getEditText().getText().toString().matches("^[0-9]{10}$")) {
            phoneField.setError("Phone is invalid");
            return false;
        }
        else {
            phoneField.setError(null);
            return true;
        }
    }

    private boolean validateAddress () {
        if (addressField.getEditText().getText().toString().isEmpty()) {
            addressField.setError("Address is required");
            return false;
        } else {
            addressField.setError(null);
            return true;
        }
    }

    private boolean validateMail () {
        if (mailField.getEditText().getText().toString().isEmpty()) {
            mailField.setError("Mail is required");
            return false;
        } else if (!mailField.getEditText().getText().toString().matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
            mailField.setError("Mail is invalid");
            return false;
        } else {
            mailField.setError(null);
            return true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}