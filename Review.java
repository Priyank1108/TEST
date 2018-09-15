package com.ndshah.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Review extends AppCompatActivity {

    //TextViews
    TextView mDateTextView, mEmployeeNameTextView, mCurrentLocationTextView, mDestinationTextView,
            mDescriptionTextView, mTotalAmountTextView, mSpecificInfoTextView, mDescriptionSubTextView;
    //Buttons
    Button mBackButton, mSubmitButton;

    //Firebase
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    //Strings
    String mDateString, mRadioButtonString, mRadioButtonString1, mEmployeeString, mCurrentLocationString,
            mDestinationString, mAmountString, mSpecificInfoString;

    //Firebase String data upload
    String mUserId, mName, mDate, mDescription, mSubDescription, mCurrentLocation, mDestination, mAmount,
            mSpecificInfo;

    String mNull = "";
    //String mConveyanceSheet = "Conveyance Sheet";
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        /**
        FirebaseApp.initializeApp(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        **/
        Bundle bundle = getIntent().getExtras();
        if(getIntent().getStringExtra("dateValue") != null ) {

            //Get String value of data passed on from MainActivity.java
            assert bundle != null;
            //Date
            mDateString = bundle.getString("dateValue");
            //Main radio Buttons
            mRadioButtonString = bundle.getString("radioButton");
            //Sub Radio Buttons
            mRadioButtonString1 = bundle.getString("radioButton1");
            //Employee Name
            mEmployeeString = bundle.getString("employee");
            //Current Location
            mCurrentLocationString = bundle.getString("location");
            //Destination
            mDestinationString = bundle.getString("destination");
            //Amount
            mAmountString = bundle.getString("amount");
            //Other text
            mSpecificInfoString = bundle.getString("other");

            //Set Date TextView
            mDateTextView = findViewById(R.id.date_text_view);
            mDateTextView.setText(mDateString);

            //Set Radio Button TextView
            mDescriptionTextView = findViewById(R.id.description_text);
            mDescriptionTextView.setText(mRadioButtonString1);
            mDescriptionTextView.setText(mRadioButtonString);

            //Set Employee Name TextView
            mEmployeeNameTextView = findViewById(R.id.employee_name);
            mEmployeeNameTextView.setText(mEmployeeString);

            //Set Total Amount TextView
            mTotalAmountTextView = findViewById(R.id.total_amount);
            mTotalAmountTextView.setText(mAmountString);

            //Set Current location TextView
            mCurrentLocationTextView = findViewById(R.id.current_location);
            mCurrentLocationTextView.setText(mCurrentLocationString);

            //Set Destination TextView
            mDestinationTextView = findViewById(R.id.destination);
            //mDestinationTextView.setText(mDestinationString);

            mDescriptionTextView = findViewById(R.id.description_text);
            assert mRadioButtonString != null;
            if (mRadioButtonString.equals("Other") && mSpecificInfoString != null) {
                mDescriptionTextView.setText(mRadioButtonString);
                mSpecificInfoTextView = findViewById(R.id.specific_description);
                mSpecificInfoTextView.setText(" - " + mSpecificInfoString);

            } else {
                if (mRadioButtonString.equals("Meal")) {
                    mDescriptionTextView.setText(mRadioButtonString);

                    Log.e("Radio Button Meal", mRadioButtonString);

                } else {
                    //if (mRadioButtonString1 != null) {
                    mDescriptionSubTextView = findViewById(R.id.description_sub_text);
                    mDescriptionSubTextView.setText("\r\n" + mRadioButtonString1);

                }
            }
        }

        Log.e("RadioButtonValue ", mRadioButtonString);

        //Back Button
        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mBackButtonIntent = new Intent(Review.this, MainActivity.class);
                mBackButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mBackButtonIntent);
            }
        });

        //Submit Button
        mSubmitButton = findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FirebaseApp.initializeApp(mContext);
                // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                //mFirebaseDatabase = FirebaseDatabase.getInstance();
                //mDatabaseReference = mFirebaseDatabase.getReference();

                mUserId = mDatabaseReference.push().getKey();
                assert mUserId != null;
                mName = mEmployeeNameTextView.getText().toString();
                mDate = mDateTextView.getText().toString();
                mDescription = mDescriptionTextView.getText().toString();

                if (mDescriptionSubTextView != null) {
                    mSubDescription = mDescriptionSubTextView.getText().toString();
                }

                //mDatabaseReference.child(mUserId).setValue(mConveyanceSheet);
                mCurrentLocation = mCurrentLocationTextView.getText().toString();
                mDestination = mDestinationTextView.getText().toString();
                mAmount = mTotalAmountTextView.getText().toString();

                //mDatabaseReference.child(mUserId).child("Full Name").setValue(mName);
                mDatabaseReference.child(mUserId).child(mName).child("Date").setValue(mDate);

                if(mSpecificInfoTextView !=null) {
                    mSpecificInfo = mSpecificInfoTextView.getText().toString();
                    mDatabaseReference.child(mUserId).child(mName).child("Description")
                            .setValue(mDescription + mSpecificInfo);
                    Log.e("SpecificInfo", mSpecificInfo);

                } else if(mSubDescription != null) {
                    mDatabaseReference.child(mUserId).child(mName).child("Description")
                            .setValue(mDescription + " - " + mSubDescription);

                }

                if (mRadioButtonString.equals("Meal")) {
                    mDatabaseReference.child(mUserId).child(mName).child("Description")
                            .setValue(mDescription);
                }

                mDatabaseReference.child(mUserId).child(mName).child("Amount").setValue(mAmount);
                mDatabaseReference.child(mUserId).child(mName).child("Location").setValue(mNull);
                mDatabaseReference.child(mUserId).child(mName).child("Destination").setValue(mNull);

                Intent mSubmitButtonIntent = new Intent(Review.this, MainActivity.class);
                mSubmitButtonIntent.addCategory( Intent.CATEGORY_HOME );
                mSubmitButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mSubmitButtonIntent);
                finish();
            }
        });
    }
}
