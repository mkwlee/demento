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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.dementiacare.R;
import com.company.dementiacare.TM_RecyclerViewAdaptor;
import com.company.dementiacare.component.TypeModal;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    // Declare variables

    Bitmap bitmap;
    private static final int REQUEST_CAMERA_CODE = 100;

    // create a list of types of medicine
    ArrayList<TypeModal> typeModals = new ArrayList<>();

    // image buttons
    ImageButton closeButton, scannerButton;

    // textInput
    TextInputEditText medicineName;

    // dropdown menu for dosage of medicine
    Spinner spinner;
    // list of dosage of medicine
    private static final String[] items = {"g", "IU", "mcg", "mcg/hr", "mcg/ml", "mEq", "mg", "mg/cm2", "mg/g", "mg/ml", "ml", "%"};

    int [] typesImages = {R.drawable.outline_medication_black_24dp, R.drawable.outline_medication_black_24dp, R.drawable.outline_water_drop_white_24dp};

    // next button
    View nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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
        RecyclerView recyclerView = findViewById(R.id.medicine_type_list_recyclerView);

        setUpTypeModals();

        TM_RecyclerViewAdaptor adaptor = new TM_RecyclerViewAdaptor(this, typeModals);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // find the close button by id
        closeButton = findViewById(R.id.add_close);
        // close activity when close button is clicked
        closeButton.setOnClickListener(v -> finish());

        // Dropdown menu
        spinner = findViewById(R.id.unit_picker);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);

        // find the next button by id
        nextButton = findViewById(R.id.next_button);
        // handle the next button actions and animations
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to add activity screen
                Intent i = new Intent(AddActivity.this, ReminderActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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

    // Description: This method is used to handle the back/close button press animation.
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }
}