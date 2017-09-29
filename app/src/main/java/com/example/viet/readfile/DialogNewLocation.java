package com.example.viet.readfile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.regex.Pattern;

/**
 * Created by Viet on 9/29/2017.
 */

public class DialogNewLocation extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_location, null);

        final ClearableEditText editCity = dialogView.findViewById(R.id.edit_city);
        final ClearableEditText editLat = dialogView.findViewById(R.id.edit_lat);
        final ClearableEditText editLong = dialogView.findViewById(R.id.edit_long);
        final ClearableEditText editTimezone = dialogView.findViewById(R.id.edit_timezone);
        final TextView error_message = dialogView.findViewById(R.id.errMsg);

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnOK = dialogView.findViewById(R.id.btn_ok);

        builder.setView(dialogView).setMessage("Add new location");

        /* Handle Cancel button */
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /* Handle OK button */
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean a = !Pattern.matches("^[A-Za-z ]*$", editCity.getText().toString());
                boolean b = !Pattern.matches("^Australia/[A-Za-z]*$", editTimezone.getText().toString());
                MainActivity callingMain = (MainActivity) getActivity();

                String new_location_string = editCity.getText().toString() + "," +
                        editLat.getText().toString() + "," + editLong.getText().toString() + "," +
                        editTimezone.getText().toString() + "\n";

                if (editCity.getText().toString().matches("") ||
                    editLat.getText().toString().matches("") ||
                    editLong.getText().toString().matches("") ||
                    editTimezone.getText().toString().matches("")) {
                    error_message.setText(R.string.err_message);
                } else if (a || b) {
                    error_message.setText(R.string.err_message);
                } else {
                    callingMain.file_append(new_location_string);
                    dismiss();
                    Toast.makeText(getActivity(), "New location added.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return builder.create();
    }


}
