// Huy Bui N01667261
package huy.bui.n01667261.huylab8;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private ImageView imageView;
    private int clickCounter = 0;

    private final int[] images = {
            R.drawable.mc1,
            R.drawable.mc2,
            R.drawable.mc3,
            R.drawable.mc4
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = view.findViewById(R.id.imageView);
        Button actionButton = view.findViewById(R.id.button);

        actionButton.setOnClickListener(v -> {
            clickCounter++;

            // Cycle through images safely using remainder
            int currentImage = images[clickCounter % images.length];
            imageView.setImageResource(currentImage);

            // Create and show a material Snackbar
            String snackbarMessage = "Huy Bui | Clicks: " + clickCounter;
            Snackbar.make(view, snackbarMessage, Snackbar.LENGTH_SHORT).show();
        });

        return view;
    }
}