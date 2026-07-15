// Huy Bui N01667261
package huy.bui.n01667261.huylab8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ShareFragment extends Fragment {

    private CheckBox checkBox;
    private EditText emailInput;
    private EditText idInput;
    private ImageButton shareBtn;
    private SharedPreferences sharedPreferences;

    public ShareFragment() {
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        //Bind layout views using your XML IDs
        checkBox = view.findViewById(R.id.checkBox);
        emailInput = view.findViewById(R.id.editTextTextEmailAddress);
        idInput = view.findViewById(R.id.editTextNumber);
        shareBtn = view.findViewById(R.id.imageButton);

        //Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("HuyPrefs_N01667261", Context.MODE_PRIVATE);

        //Handle submissions on ImageButton click
        shareBtn.setOnClickListener(v -> validateAndSaveData());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Everytime user accesses this screen, display the custom AlertDialog
        showGmtDialog();
    }

    private void showGmtDialog() {
        // b.iii. Current date and time in GMT format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String gmtTime = sdf.format(new Date());

        //Create Dialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Huy Bui")
                .setMessage("Current GMT Time: " + gmtTime)
                .setIcon(R.drawable.catdroid)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()); // b.iv. OK button dismisses

        AlertDialog alertDialog = builder.create();

        //Design custom layouts with light green color background
        alertDialog.setOnShowListener(dialogInterface -> {
            if (alertDialog.getWindow() != null) {
                // Set light green background
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D4EDDA")));
            }

            //Stylize title color to dark green
            int titleId = requireContext().getResources().getIdentifier("alertTitle", "id", "android");
            if (titleId > 0) {
                TextView titleText = alertDialog.findViewById(titleId);
                if (titleText != null) {
                    titleText.setTextColor(Color.parseColor("#155724"));
                }
            }

            //Stylize message body
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            if (messageText != null) {
                messageText.setTextColor(Color.parseColor("#155724"));
                messageText.setTextSize(18);
            }

            //Stylize button action text
            Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (okButton != null) {
                okButton.setTextColor(Color.parseColor("#155724"));
            }
        });

        alertDialog.show();
    }

    private void validateAndSaveData() {
        String email = emailInput.getText().toString().trim();
        String idText = idInput.getText().toString().trim();

        boolean isValid = true;

        //Validate valid email format
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address!");
            isValid = false;
        }

        //Validate student ID has at least 6 digits
        if (idText.length() < 6) {
            idInput.setError("Student ID must be at least 6 digits!");
            isValid = false;
        }

        if (isValid) {
            boolean isChecked = checkBox.isChecked();
            int studentId = Integer.parseInt(idText); // j.iv.2.c. Must store as integer

            //Save data using SharedPreference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("checkbox_val", isChecked);
            editor.putString("email_val", email);
            editor.putInt("id_val", studentId);
            editor.apply();

            //Format user info text to span across multiple lines
            String snackbarText = "checkbox: " + isChecked + ",\nemail: " + email + ",\nid: " + studentId;

            //Display snackbar with indefinite length and a Dismiss option
            Snackbar snackbar = Snackbar.make(requireView(), snackbarText, Snackbar.LENGTH_INDEFINITE);

            // Set snackbar view to handle multi-line outputs
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            if (textView != null) {
                textView.setMaxLines(3);
            }

            snackbar.setAction("DISMISS", v -> snackbar.dismiss());
            snackbar.show();

            //Clear all input fields and focus
            checkBox.setChecked(false);
            emailInput.setText("");
            idInput.setText("");
            emailInput.clearFocus();
            idInput.clearFocus();
        }
    }
}