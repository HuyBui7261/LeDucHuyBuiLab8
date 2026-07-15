// Huy Bui N01667261
package huy.bui.n01667261.huylab8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private ImageView imageView;
    private TextView titleTextView;

    // Launchers for System Picker and Permission Requests
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Initialize Photo Picker Launcher (To select image from Device Storage)
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Successfully picked a real image from device photos!
                        imageView.setImageURI(uri);
                        Toast.makeText(getContext(), R.string.huy_bui_image_loaded_successfully, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 2. Initialize Permission Request Launcher
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean allGranted = true;
                    for (Boolean granted : result.values()) {
                        if (!granted) {
                            allGranted = false;
                            break;
                        }
                    }

                    if (allGranted) {
                        // Option A: Permission Allowed
                        Toast.makeText(getContext(), R.string.huy_bui_permission_allowed, Toast.LENGTH_SHORT).show();
                        openDeviceGallery();
                    } else {
                        // Option B: Permission Denied - Show detailed OS toast
                        String sdkMessage;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            sdkMessage = getString(R.string.huy_bui_permission_denied_on_api_33_android_13);
                        } else {
                            sdkMessage = getString(R.string.huy_bui_permission_denied_on_api_32_or_lower);
                        }
                        Toast.makeText(getContext(), sdkMessage, Toast.LENGTH_LONG).show();

                        // Check if denied permanently (User selected "Don't ask again" or denied twice)
                        if (!shouldShowRequestPermissionRationale(getRequiredPermissions()[0])) {
                            showSettingsDialog();
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Bind layout views
        titleTextView = view.findViewById(R.id.textView2);
        imageView = view.findViewById(R.id.imageView2);
        Button btnAccessPhotos = view.findViewById(R.id.button2);

        // Click Action
        btnAccessPhotos.setOnClickListener(v -> checkAndRequestPermissions());

        return view;
    }

    // Determine which permissions to check depending on the system API version
    private String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) or higher
            return new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            };
        } else {
            // Android 12 (API 32) or lower
            return new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }
    }

    private void checkAndRequestPermissions() {
        String[] permissions = getRequiredPermissions();
        boolean isGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        if (isGranted) {
            Toast.makeText(getContext(), R.string.huy_bui_permission_already_allowed, Toast.LENGTH_SHORT).show();
            openDeviceGallery();
        } else {
            // Launch the dynamic system permissions flow
            permissionLauncher.launch(permissions);
        }
    }

    private void openDeviceGallery() {
        // Launches the native media visual file systems picker
        galleryLauncher.launch("image/*");
    }

    // Guides users to application settings screen to resolve double-denials manually
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.permissions_required)
                .setMessage(R.string.you_have_denied_this_permission_twice_please_enable_storage_media_permission_in_app_settings_to_pick_device_photos)
                .setPositiveButton(R.string.go_to_settings, (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }
}