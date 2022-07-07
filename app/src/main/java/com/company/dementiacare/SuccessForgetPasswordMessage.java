/*
 * 
 *   SuccessForgetPasswordMessage
 * 
 *  Description:
 *  This activity is used to display the success message after the user has successfully reset the password.
 * 
 *  Updated:
 *  July 07, 2022
 * 
*/

package com.company.dementiacare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SuccessForgetPasswordMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_forget_password_message);
    }
}