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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static android.content.res.Resources.*;

public class MainActivity extends AppCompatActivity {

    static Calendar c = Calendar.getInstance();

    // THE LIST OF HOURS IN MILLIS + MINUTES IN MILLIS
    //public static Map<Integer, Long> hoursInMillis = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
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

        // delete the file of corresponding hour
        deleteFile("hour" + deleteButtonNumber);
    }

    public void loadData(){
        int i=1;
        Map<Integer, Long> hoursInMillis = new TreeMap<>();

        // Filling hoursInMillis with data from file
        while (i<17) {
            try {
                FileInputStream fIn = openFileInput("hour"+String.valueOf(i));
                int c;
                String hour = "";
                while ((c = fIn.read()) != -1) {
                    hour = hour + Character.toString((char) c);
                }
                fIn.close();

                hoursInMillis.put(i, Long.valueOf(hour));
                i++;

            } catch (java.io.IOException e) {
                break;
            }
        }

        // Changing the UI accordingly
        i--; // Because i was number of a file that did not exist - we need to step back to previous one
        for(int j=1; j<=i; j++){

            // Get hour and minutes
            c.setTimeInMillis(hoursInMillis.get(j));
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String minutes = String.valueOf(c.get(Calendar.MINUTE));


            // Make the button invisible
            int buttonId = getResources().getIdentifier("button" + j, "id", "com.thelkl321.ilejeszcze");
            Button button = (Button) findViewById(buttonId);
            button.setVisibility(View.INVISIBLE);

            // Make the textView visible and set it
            int textID = getResources().getIdentifier("textView" + j, "id", "com.thelkl321.ilejeszcze");
            TextView text = (TextView) findViewById(textID);
            if(Integer.parseInt(minutes)<10) minutes = "0" + minutes;
            text.setText(hour + getString(R.string.colon) + minutes);
            text.setVisibility(View.VISIBLE);

            // Make the next button visible
            Button nextButton;
            if (j != 16) {
                int nextButtonId = getResources().getIdentifier("button" + (j+1), "id", "com.thelkl321.ilejeszcze");
                nextButton = (Button) findViewById(nextButtonId);
                nextButton.setVisibility(View.VISIBLE);
            }

            // Make the previous delete button invisible
            if(j !=1) {
                int previousDeleteButtonId = getResources().getIdentifier("delete" + (j-1), "id", "com.thelkl321.ilejeszcze");
                Button previousDeleteButton = (Button) findViewById(previousDeleteButtonId);
                previousDeleteButton.setVisibility(View.INVISIBLE);
            }

            // Make the delete button visible
            int deleteButtonId = getResources().getIdentifier("delete" + j, "id", "com.thelkl321.ilejeszcze");
            Button deleteButton = (Button) findViewById(deleteButtonId);
            deleteButton.setVisibility(View.VISIBLE);

        }
    }

    //TODO: Create on/off switch functionality
}
