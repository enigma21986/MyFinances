package com.mokin.myfinances.app.utility;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        long milliseconds = getArguments().getLong("transactionDateTime");

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        MyDateSetCallbacks activity = (MyDateSetCallbacks) getActivity();
        activity.onDateSet(year, month, day);
    }

    public interface MyDateSetCallbacks {
        void onDateSet(int year, int month, int day);
    }
}
