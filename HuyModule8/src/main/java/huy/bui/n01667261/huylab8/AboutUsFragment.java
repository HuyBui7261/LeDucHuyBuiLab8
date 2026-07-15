// Huy Bui N01667261
package huy.bui.n01667261.huylab8;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

public class AboutUsFragment extends Fragment {

    private TextView prefsDisplayTxt;
    private ToggleButton orientationToggle;
    private SharedPreferences sharedPreferences;

    public AboutUsFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        //Bind layout views
        prefsDisplayTxt = view.findViewById(R.id.huyPrefsDisplayTxt);
        orientationToggle = view.findViewById(R.id.huyOrientationToggle);

        //Initialize SharedPreferences using the same preference filename as ShareFragment
        sharedPreferences = requireActivity().getSharedPreferences("HuyPrefs_N01667261", Context.MODE_PRIVATE);

        //Read and display values saved from SharedPreferences
        displaySavedData();

        //Set up ToggleButton listener for orientation changes
        orientationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //If toggle is ON, lock device orientation to portrait
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Toast.makeText(getContext(), "Orientation locked to Portrait", Toast.LENGTH_SHORT).show();
            } else {
                //If toggle is OFF, set to auto sensor orientation
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                Toast.makeText(getContext(), "Orientation set to Auto-Sensor", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Display visit counter in a short Toast every time the user accesses this screen
        handleVisitCounter();
    }

    private void handleVisitCounter() {
        //Retrieve current count, starting default value is 0 (so the first increment becomes 1)
        int currentCount = sharedPreferences.getInt("about_visit_count", 0);
        currentCount++;

        //Save updated count back to SharedPreferences
        sharedPreferences.edit().putInt("about_visit_count", currentCount).apply();

        //Show Toast: "Counter: X | Huy Bui"
        String toastMessage = "Counter: " + currentCount + " | Huy Bui";
        Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void displaySavedData() {
        // Check if the keys exist in SharedPreferences
        if (sharedPreferences.contains("email_val") && sharedPreferences.contains("id_val")) {
            boolean checkboxChecked = sharedPreferences.getBoolean("checkbox_val", false);
            String email = sharedPreferences.getString("email_val", "");
            int id = sharedPreferences.getInt("id_val", 0);

            // Format and display the values cleanly
            String formattedData = "CheckboxChecked: " + checkboxChecked + "\n" +
                    "Email: " + email + "\n" +
                    "Student ID: " + id;

            prefsDisplayTxt.setText(formattedData);
        } else {
            // If no data exists, display "NO DATA"
            prefsDisplayTxt.setText("NO DATA");
        }
    }
}