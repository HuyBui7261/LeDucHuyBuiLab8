// Huy Bui N01667261
package huy.bui.n01667261.huylab8;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

public class BuiActivity8 extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Set up Custom Toolbar
        Toolbar toolbar = findViewById(R.id.huyToolbar);
        setSupportActionBar(toolbar);

        // 2. Reference Views
        drawerLayout = findViewById(R.id.huyDrawerLayout);
        NavigationView navigationView = findViewById(R.id.huyNavigationView);

        // 3. Setup Navigation Controller & Top-Level Configurations
        NavController navController = Navigation.findNavController(this, R.id.huyNavHostFragment);

        // This ensures the Hamburger Icon displays instead of a back arrow on these screens
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_share, R.id.nav_about)
                .setOpenableLayout(drawerLayout)
                .build();

        // 4. Connect everything to the Jetpack Controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 5. Connect the Animated Drawer Toggle to the Toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Connects Toolbar Navigation back actions
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.huyNavHostFragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}