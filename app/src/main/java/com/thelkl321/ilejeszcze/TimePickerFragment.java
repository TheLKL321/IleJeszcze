package com.thelkl321.ilejeszcze;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    // Called when time is picked
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // get button id and create button
        Bundle bundle = getArguments();
        int buttonId = (int) bundle.get("buttonId");
        Button button = (Button) getActivity().findViewById(buttonId);
        button.setVisibility(View.INVISIBLE);

        // get text id and create textView
        int buttonNumber = Integer.parseInt((String) button.getTag());   //Buttons have tags 1,2,3... and each has corresponding textView
        int textID = getResources().getIdentifier("textView" + buttonNumber, "id", "com.thelkl321.ilejeszcze");
        TextView text = (TextView) getActivity().findViewById(textID);

        String minutesOnClock = Integer.toString(minute);
        if (minute < 10) minutesOnClock = "0" + minutesOnClock;
        text.setText(hourOfDay + ":" + minutesOnClock);
        text.setVisibility(View.VISIBLE);

        // pass hour to MainActivity
        MainActivity.hoursInMillis.put(buttonNumber, TimeUnit.HOURS.toMillis(hourOfDay)+TimeUnit.MINUTES.toMillis(minute));

        // create next button
        Button nextButton = null;
        if (buttonNumber != 16) {
            int nextButtonId = getResources().getIdentifier("button" + (buttonNumber+1), "id", "com.thelkl321.ilejeszcze");
            nextButton = (Button) getActivity().findViewById(nextButtonId);
            nextButton.setVisibility(View.VISIBLE);
        }

        // create previous delete button
        if(buttonNumber !=1) {
            int previousDeleteButtonId = getResources().getIdentifier("delete" + (buttonNumber - 1), "id", "com.thelkl321.ilejeszcze");
            Button previousDeleteButton = (Button) getActivity().findViewById(previousDeleteButtonId);
            previousDeleteButton.setVisibility(View.INVISIBLE);
        }

        // create delete button
        int deleteButtonId = getResources().getIdentifier("delete" + (buttonNumber), "id", "com.thelkl321.ilejeszcze");
        Button deleteButton = (Button) getActivity().findViewById(deleteButtonId);
        deleteButton.setVisibility(View.VISIBLE);

    }
}