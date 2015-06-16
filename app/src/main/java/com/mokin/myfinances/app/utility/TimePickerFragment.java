package com.mokin.myfinances.app.utility;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        long milliseconds = getArguments().getLong("transactionDateTime");

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        MyTimeSetCallbacks activity = (MyTimeSetCallbacks) getActivity();
        activity.onTimeSet(hourOfDay, minute);
    }

    public interface MyTimeSetCallbacks {
        void onTimeSet(int hourOfDay, int minute);
    }
}
