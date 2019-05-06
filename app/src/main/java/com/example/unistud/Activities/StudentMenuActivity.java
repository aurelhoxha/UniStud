package com.example.unistud.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.Fragments.StudentEventFragment;
import com.example.unistud.Fragments.StudentHomeFragment;
import com.example.unistud.Fragments.StudentInternshipFragment;
import com.example.unistud.Fragments.StudentProfileFragment;
import com.example.unistud.Fragments.StudentTradeFragment;
import com.example.unistud.Fragments.StudentTutorialFragment;
import com.example.unistud.Fragments.StudentUniversityFragment;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;

public class StudentMenuActivity  extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Home";
    private static final String TAG_UNIVERSITY = "University";
    private static final String TAG_INTERNSHIP = "Internship";
    private static final String TAG_TUTORIAL = "Tutorial";
    private static final String TAG_TRADE = "Trade";
    private static final String TAG_EVENT = "Events";
    private static final String TAG_PROFILE = "Profile";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.student_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.student_nav_view);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_student_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("UniStud");
        txtWebsite.setText("");

        // showing dot next to Share Bilkent label
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Schedule
                StudentHomeFragment homeFragment = new StudentHomeFragment();
                return homeFragment;
            case 1:
                // Share
                StudentUniversityFragment universityFragment = new StudentUniversityFragment();
                return universityFragment;
            case 2:
                // Share
                StudentInternshipFragment internshipFragment = new StudentInternshipFragment();
                return internshipFragment;
            case 3:
                // Share
                StudentTutorialFragment tutorialFragment = new StudentTutorialFragment();
                return tutorialFragment;
            case 4:
                // Calendar fragment
                StudentTradeFragment tradeFragment = new StudentTradeFragment();
                return tradeFragment;
            case 5:
                // Task fragment
                StudentEventFragment eventFragment = new StudentEventFragment();
                return eventFragment;
            case 6:
                // Task fragment
                StudentProfileFragment profileFragment = new StudentProfileFragment();
                return profileFragment;

            default:
                return new StudentHomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_student_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_student_university:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_UNIVERSITY;
                        break;
                    case R.id.nav_student_interships:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_INTERNSHIP;
                        break;
                    case R.id.nav_student_tutorials:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_TUTORIAL;
                        break;
                    case R.id.nav_student_trade:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_TRADE;
                        break;
                    case R.id.nav_student_events:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_EVENT;
                        break;
                    case R.id.nav_student_profile:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(StudentMenuActivity.this, AboutUs.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_log_out:
                        // launch new intent instead of loading fragment
                        FirebaseAuth user = FirebaseAuth.getInstance();
                        user.signOut();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        // show menu only when Internship fragment is selected
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.action_bar_tutorials, menu);
        }

        if (navItemIndex == 1) {
            getMenuInflater().inflate(R.menu.action_bar_university, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //ADD EVENT
        if (id == R.id.action_add_tutorial) {
            addTutorialMethod();

        }

        if (id == R.id.action_add_university) {
            addUniversityMethod();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addTutorialMethod() {
        Intent myIntent = new Intent(getApplicationContext(), StudentAddTutorial.class);
        startActivityForResult(myIntent,1);
    }

    private void addUniversityMethod() {
        Intent myIntent = new Intent(getApplicationContext(), AddUniversity.class);
        startActivityForResult(myIntent,1);
    }


}
