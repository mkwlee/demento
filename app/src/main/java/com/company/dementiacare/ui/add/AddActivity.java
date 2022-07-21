/*
*               Add Activity Class
*   
*     Description: This class is used to display and add a new medicine for a patient.
*
*      Updated: July 17, 2022  
* */

package com.company.dementiacare.ui.add;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dementiacare.R;
import com.company.dementiacare.TM_RecyclerViewAdaptor;
import com.company.dementiacare.component.Colors;
import com.company.dementiacare.component.Type;
import com.company.dementiacare.component.TypeModal;
import com.company.dementiacare.component.WeekDay;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    // Declare variables
    Bitmap bitmap;
    private static final int REQUEST_CAMERA_CODE = 100;

    // Card view for colors
    public static CardView[] weekDayButtons = new CardView[7];
    public static CardView[] weekDayButtons1 = new CardView[3];

    private HashMap<String, Colors> buttonIdStrToWeekDayMap = new HashMap<>();
    private HashMap<String, Type> buttonIdStrToWeekDayMap1 = new HashMap<>();

    private Colors currentDay;
    private CardView currentDayButton;

    // create a list of types of medicine
    ArrayList<TypeModal> typeModals = new ArrayList<>();

    // image buttons
    ImageButton closeButton, scannerButton;

    // medicine input
    TextInputLayout medicineLayout, dosageLayout;

    // text inside the input
    TextInputEditText medicineName;

    // dropdown menu for dosage of medicine
    Spinner unitSpinner, patientSpinner;
    // list of dosage of medicine
    private static final String[] units = {"g", "IU", "mcg", "mcg/hr", "mcg/ml", "mEq", "mg", "mg/cm2", "mg/g", "mg/ml", "ml", "%"};

    // list of patients with random ame
    private static final String[] patients = {"John", "Jane", "Jack", "Jill", "Joe", "Juan", "Jenny", "Juanita", "Juanito", "Juanita", "Juanito"};

    // list of the colors
    private final String[] colors = {"red", "green", "blue", "white", "black", "orange", "yellow"};

    private final String[] types = {"drop", "tablet", "capsule"};


    // list of the images of the types of medicine
    int [] typesImages = {R.drawable.outline_medication_black_24dp, R.drawable.outline_medication_black_24dp, R.drawable.outline_water_drop_white_24dp};

    // next button
    View nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // find the medicine name input field
        medicineLayout = findViewById(R.id.medicine_name_input);

        // find the dosage input field
        dosageLayout = findViewById(R.id.medicine_dosage);

        // Camera permissions
        if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        scannerButton = findViewById(R.id.scanner);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddActivity.this);
            }
        });

        // setup the types modals
//        RecyclerView recyclerView = findViewById(R.id.medicine_type_list_recyclerView);

//        setUpTypeModals();
//
//        TM_RecyclerViewAdaptor adaptor = new TM_RecyclerViewAdaptor(this, typeModals);
//        recyclerView.setAdapter(adaptor);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // find the close button by id
        closeButton = findViewById(R.id.add_close);
        // close activity when close button is clicked
        closeButton.setOnClickListener(v -> finish());

        // Dropdown for patients picker and also patient name change
//        patientName = findViewById(R.id.patient_name);
        patientSpinner = findViewById(R.id.patient_picker);
        //create an adapter to describe how the units are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(this, R.layout.patient_spinner_item, patients);
        // set the patient name to the first patient in the list and the patient spinner to the adapter
//        patientName.setText(patients[0]);
        patientSpinner.setAdapter(patientAdapter);
        // set the patient name to the patient name in the spinner when the spinner is changed
//        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                patientName.setText(patients[i]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                patientName.setText(patients[0]);
//            }
//        });


        // Dropdown menu for units picker
        unitSpinner = findViewById(R.id.unit_picker);
        //create an adapter to describe how the units are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units);
        //set the spinners adapter to the previously created one.
        unitSpinner.setAdapter(adapter);

        // find the next button by id
        nextButton = findViewById(R.id.next_button);
        // handle the next button actions and animations
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the medicine name
                boolean validMedicine = validateMedicineInput();
                boolean validDosage = validateDosageInput();
                if (validMedicine && validDosage){
                    // if the medicine name is valid, then move to the next activity
                    Intent i = new Intent(AddActivity.this, ReminderActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        for (int weekDayIndex = 0; weekDayIndex < colors.length; ++weekDayIndex) {
            int weekDayId = getResources().getIdentifier(colors[weekDayIndex], "id",
                    getApplicationContext().getPackageName());

            buttonIdStrToWeekDayMap.put(Integer.toString(weekDayId), new Colors(colors[weekDayIndex]));

            final CardView currentDayButton = findViewById(weekDayId);
            weekDayButtons[weekDayIndex] = currentDayButton;
            currentDayButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (buttonNotInFocus(view, event)) {
                                return false;
                            }
                            setCurrentColor(currentDayButton);
                            AddActivity.this.currentDayButton = currentDayButton;
                            // Add Time button should be active only when week mode selected
                            // or specific day selected
                            int buttonId = currentDayButton.getId();
                            currentDay = buttonIdStrToWeekDayMap.get(Integer.toString(buttonId));
                            return false;
                    }
                    return false;
                }
            });
        }

        for (int weekDayIndex = 0; weekDayIndex < types.length; ++weekDayIndex) {
            int weekDayId = getResources().getIdentifier(types[weekDayIndex], "id",
                    getApplicationContext().getPackageName());

            buttonIdStrToWeekDayMap1.put(Integer.toString(weekDayId), new Type(types[weekDayIndex]));

            final CardView currentDayButton = findViewById(weekDayId);
            weekDayButtons1[weekDayIndex] = currentDayButton;
            currentDayButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (buttonNotInFocus(view, event)) {
                                return false;
                            }
                            setCurrentType(currentDayButton);
                            AddActivity.this.currentDayButton = currentDayButton;
                            // Add Time button should be active only when week mode selected
                            // or specific day selected
                            int buttonId = currentDayButton.getId();
                            currentDay = buttonIdStrToWeekDayMap.get(Integer.toString(buttonId));
                            return false;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTextFromImage (Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()){
            Toast.makeText(AddActivity.this, "Error while scanning!", Toast.LENGTH_LONG).show();
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i =0; i < textBlockSparseArray.size(); i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            medicineName.setText(stringBuilder.toString());
        }
    }

    // Description: This method is used to set up the type of medicine that the user can add.
    private void setUpTypeModals() {
        // get the string array of type of medicine
        String [] typesName = getResources().getStringArray(R.array.medicine_types);

        for (int i =0; i < typesName.length; i++) {
            typeModals.add(new TypeModal(typesName[i], typesImages[i]));
        }
    }

    // check if the medicine name input is empty
    private boolean validateMedicineInput (){
        String val = medicineLayout.getEditText().getText().toString();
        if (val.isEmpty()){
            medicineLayout.setError("Please type in a medicine name");
            return false;
        }
        else{
            medicineLayout.setError(null);
            medicineLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDosageInput(){
        String val = dosageLayout.getEditText().getText().toString();
        if(val.isEmpty()){
            dosageLayout.setError("Dosage is required");
            return false;
        }
        else{
            dosageLayout.setError(null);
            dosageLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void setCurrentColor (CardView currentColorButton){
        for (CardView weekDayButton : weekDayButtons) {
            if (weekDayButton == currentColorButton) {
                weekDayButton.getChildAt(0).setVisibility(View.VISIBLE);
            } else {
                weekDayButton.getChildAt(0).setVisibility(View.GONE);
            }
        }
    }

    private void setCurrentType (CardView currentColorButton){
        for (CardView weekDayButton : weekDayButtons1) {
            if (weekDayButton == currentColorButton) {
                weekDayButton.setCardBackgroundColor(getResources().getColor(R.color.light_blue));
            } else {
                weekDayButton.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
            }
        }
    }

    private boolean buttonNotInFocus(View view, MotionEvent event) {
        Rect rect = new Rect();
        // get the View's (Button's) Rect relative to its parent
        view.getHitRect(rect);
        // offset the touch coordinates with the values from r
        // to obtain meaningful coordinates
        final float x = event.getX() + rect.left;
        final float y = event.getY() + rect.top;
        if (!rect.contains((int) x, (int) y)) {
            return true;
        }
        return false;
    }

    // Description: This method is used to handle the back/close button press animation.
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }
}