package com.thelkl321.ilejeszcze;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static android.content.res.Resources.*;

public class MainActivity extends AppCompatActivity {

    // THE LIST OF HOURS IN MILLIS + MINUTES IN MILLIS
    public static Map<Integer, Long> hoursInMillis = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showTimePickerDialog(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("buttonId", v.getId());

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        newFragment.setArguments(bundle);
    }

    // Called by the button
    public void pickTime(View v){
        showTimePickerDialog(v);
    }

    // Called by delete button
    public void deleteTime(View v){
        int deleteButtonNumber = Integer.parseInt((String) v.getTag());

        // stuff with time text
        int textID = getResources().getIdentifier("textView" + deleteButtonNumber, "id", "com.thelkl321.ilejeszcze");
        TextView text = (TextView) findViewById(textID);
        text.setText(getString(R.string._00_00));
        text.setVisibility(View.INVISIBLE);

        // stuff with next button
        if (deleteButtonNumber != 16) {
            int nextButtonId = getResources().getIdentifier("button" + (deleteButtonNumber+1), "id", "com.thelkl321.ilejeszcze");
            Button nextButton = (Button) findViewById(nextButtonId);
            nextButton.setVisibility(View.INVISIBLE);
        }

        // stuff with current button
        int buttonId = getResources().getIdentifier("button" + deleteButtonNumber, "id", "com.thelkl321.ilejeszcze");
        Button button = (Button) findViewById(buttonId);
        button.setVisibility(View.VISIBLE);

        // stuff with previous delete button
        if (deleteButtonNumber != 1) {
            int previousDeleteButtonId = getResources().getIdentifier("delete" + (deleteButtonNumber-1), "id", "com.thelkl321.ilejeszcze");
            Button previousDeleteButton = (Button) findViewById(previousDeleteButtonId);
            previousDeleteButton.setVisibility(View.VISIBLE);
        }

        // stuff with current delete button
        v.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //TODO: sort the array of hoursInMillis and update the widget when app paused
    }

    //TODO: Create desktop widget and on/off switch functionality
}
