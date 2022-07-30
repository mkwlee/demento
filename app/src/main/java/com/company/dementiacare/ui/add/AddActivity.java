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
import com.company.dementiacare.component.MedicineReminder;
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

    // call medicineReminder to set the reminder
    private MedicineReminder medicineReminder;
    // camera variables
    Bitmap bitmap;
    // camera Request code
    private static final int REQUEST_CAMERA_CODE = 100;

    // Card view list for colors
    public static CardView[] colorCard = new CardView[7];
    // Card view list for types
    public static CardView[] typeCard = new CardView[3];

    // hash map for colors
    private HashMap<String, Colors> colorIdStrMap = new HashMap<>();
    // hash map for types
    private HashMap<String, Type> typeIdStrMap = new HashMap<>();

    // current color selected
    private Colors currentColor;
    private CardView currentColorCard;

    // current type selected
    private Type currentType;
    private CardView currentTypeCard;

    // create a list of types of medicine
    ArrayList<TypeModal> typeModals = new ArrayList<>();

    // image buttons
    ImageButton closeButton, scannerButton;

    // medicine input
    TextInputLayout medicineLayout, dosageLayout, descriptionLayout;
    String selectedType, selectedColor;

    // dropdown menu for dosage of medicine
    Spinner unitSpinner, patientSpinner;
    // list of dosage of medicine
    private static final String[] units = {"g", "IU", "mcg", "mcg/hr", "mcg/ml", "mEq", "mg", "mg/cm2", "mg/g", "mg/ml", "ml", "%"};
    String selectedUnit;

    // list of patients with random ame
    private static final String[] patients = {"John", "Jane", "Jack", "Jill", "Joe", "Juan", "Jenny", "Juanita", "Juanito", "Juanita", "Juanito"};
    String selectedPatient;

    // list of the colors
    private final String[] colors = {"red", "green", "blue", "white", "black", "orange", "yellow"};

    private final String[] types = {"drop", "tablet", "capsule"};


    // list of the images of the types of medicine
    int [] typesImages = {R.drawable.outline_medication_black_24dp, R.drawable.outline_medication_black_24dp, R.drawable.outline_water_drop_white_24dp};

    // next button
    MaterialButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        medicineReminder = new MedicineReminder();

        // find the medicine name input field
        medicineLayout = findViewById(R.id.medicine_name_input);

        // find the dosage input field
        dosageLayout = findViewById(R.id.medicine_dosage);

        // find the description layout by id
        descriptionLayout = findViewById(R.id.medicine_description);

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

        // set the selected patient to the patient that is selected in the spinner
        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPatient = patients[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedPatient = patients[0];                
            }
        });

        // Dropdown menu for units picker
        unitSpinner = findViewById(R.id.unit_picker);
        //create an adapter to describe how the units are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units);
        //set the spinners adapter to the previously created one.
        unitSpinner.setAdapter(adapter);

        // set the selected unit to the unit that is selected in the spinner
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUnit = units[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedUnit = units[0];
            }
        });

        // get the ids from the colors list and add them to the hash map
        for (int colorIndex = 0; colorIndex < colors.length; ++colorIndex) {
            int colorId = getResources().getIdentifier(colors[colorIndex], "id",
                    getApplicationContext().getPackageName());
            colorIdStrMap.put(Integer.toString(colorId), new Colors(colors[colorIndex]));

            // find the current color button and set the color to the color in the list
            final CardView currentColorButton = findViewById(colorId);
            colorCard[colorIndex] = currentColorButton;
            // set a listener to the current color button
            currentColorButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    // motion event is a motion event that is triggered by the user touching the screen
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return false;
                        case MotionEvent.ACTION_UP:
                        // if the color is not already selected, then select it
                            if (buttonNotInFocus(view, event)) {
                                return false;
                            }
                            // set the color to the color in the list
                            setCurrentColor(currentColorButton);
                            // set the color to the color in the list
                            AddActivity.this.currentColorCard = currentColorButton;
                            // Add Time button should be active only when week mode selected
                            // or specific day selected
                            int buttonId = currentColorButton.getId();
                            currentColor = colorIdStrMap.get(Integer.toString(buttonId));
                            return false;
                    }
                    return false;
                }
            });
        }

        // find the add time button by id and map it to the hash map
        for (int typeIndex = 0; typeIndex < types.length; ++typeIndex) {
            int typeId = getResources().getIdentifier(types[typeIndex], "id",
                    getApplicationContext().getPackageName());
            typeIdStrMap.put(Integer.toString(typeId), new Type(types[typeIndex]));

            // find the current color button and set the color to the color in the list
            final CardView currentTypeButton = findViewById(typeId);
            typeCard[typeIndex] = currentTypeButton;
            currentTypeButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (buttonNotInFocus(view, event)) {
                                return false;
                            }
                            // set the color to the color in the list
                            setCurrentType(currentTypeButton);
                            AddActivity.this.currentTypeCard = currentTypeButton;
                            // Add Time button should be active only when week mode selected
                            // or specific day selected
                            int buttonId = currentTypeButton.getId();
                            currentType = typeIdStrMap.get(Integer.toString(buttonId));
                            return false;
                    }
                    return false;
                }
            });
        }

        // find the next button by id
        nextButton = findViewById(R.id.next_button);
        // handle the next button actions and animations
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all the inputs and boolean that has been entered or checked

                // get the medicine name
                String medicineName = medicineLayout.getEditText().getText().toString();
                // validate medicine name
                boolean validMedicine = validateMedicineInput();
                // get the dosage amount
                String dosage = dosageLayout.getEditText().getText().toString();

                // get the description text
                String description = descriptionLayout.getEditText().getText().toString();

                // validate the dosage amount
                boolean validDosage = validateDosageInput();
                // get the type selected
                if (currentType != null){
                    selectedType = currentType.getTypeString();
                }
                // get the color selected
                if (currentColor != null) {
                    selectedColor = currentColor.getColorString();
                }

                if (!validMedicine){
                    Toast.makeText(getApplicationContext(), "Please enter medicine name", Toast.LENGTH_LONG).show();
                }
                else if (!validDosage){
                    Toast.makeText(getApplicationContext(), "Please enter dosage", Toast.LENGTH_LONG).show();
                }
                else if (selectedType == null){
                    Toast.makeText(getApplicationContext(), "Please choose a type", Toast.LENGTH_LONG).show();
                }
                else if (selectedColor == null){
                    Toast.makeText(getApplicationContext(), "Please choose a color", Toast.LENGTH_LONG).show();
                }
                else {
                    medicineReminder.setPatient(selectedPatient);
                    medicineReminder.setName(medicineName);
                    medicineReminder.setType(selectedType);
                    medicineReminder.setColor(selectedColor);
                    medicineReminder.setDosage(dosage);
                    medicineReminder.setUnit(selectedUnit);
                    Intent i = new Intent(AddActivity.this, ReminderActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    // on activity result method for the camera intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the request code is the camera intent and the result is ok
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            // get the result from the camera intent
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            // if the result is ok
            if(resultCode == RESULT_OK){
                // get the image uri from the result
                Uri resultUri = result.getUri();
                try {
                    // get the bitmap from the image uri
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                    // catch the exception if the bitmap is null
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // method to get the text from the image
    private void getTextFromImage (Bitmap bitmap){
        // create a new text recognizer
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        // if the recognizer is not null
        if (!recognizer.isOperational()){
            Toast.makeText(AddActivity.this, "Error while scanning!", Toast.LENGTH_LONG).show();
        }
        else {
            // get the bitmap from the image
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            // get the text from the image
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            // make  a string builder to store the text
            StringBuilder stringBuilder = new StringBuilder();
            // for each text block in the sparse array
            for (int i =0; i < textBlockSparseArray.size(); i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            // set the medicine name to the string builder
//            medicineName.setText(stringBuilder.toString());
            medicineLayout.getEditText().setText(stringBuilder.toString());
        }
    }

//    // Description: This method is used to set up the type of medicine that the user can add.
//    private void setUpTypeModals() {
//        // get the string array of type of medicine
//        String [] typesName = getResources().getStringArray(R.array.medicine_types);
//
//        // for each type of medicine
//        for (int i =0; i < typesName.length; i++) {
//            typeModals.add(new TypeModal(typesName[i], typesImages[i]));
//        }
//    }

    // check if the medicine name input is empty
    private boolean validateMedicineInput (){
        String val = medicineLayout.getEditText().getText().toString();
        // if the medicine name is empty
        if (val.isEmpty()){
            // set the error message
            medicineLayout.setError("Please type in a medicine name");
            return false;
        }
        else{
            // set the error message to null
            medicineLayout.setError(null);
            // return true
            medicineLayout.setErrorEnabled(false);
            return true;
        }
    }

    // check if the dosage input is empty
    private boolean validateDosageInput(){
        String val = dosageLayout.getEditText().getText().toString();

        // if the dosage is empty
        if(val.isEmpty()){
            // set the error message
            dosageLayout.setError("Dosage is required");
            return false;
        }
        else{
            // set the error message to null
            dosageLayout.setError(null);
            dosageLayout.setErrorEnabled(false);
            return true;
        }
    }

    // set the current color to the color in the list
    private void setCurrentColor (CardView currentColorButton){
        for (CardView colorButton : colorCard) {
            if (colorButton == currentColorButton) {
                colorButton.getChildAt(0).setVisibility(View.VISIBLE);
            } else {
                colorButton.getChildAt(0).setVisibility(View.GONE);
            }
        }
    }

    // set the current type to the type in the list
    private void setCurrentType (CardView currentTypeButton){
        for (CardView typButton : typeCard) {
            if (typButton == currentTypeButton) {
                typButton.setCardBackgroundColor(getResources().getColor(R.color.light_blue));
            } else {
                typButton.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
            }
        }
    }

    // check if the button is not in focus
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