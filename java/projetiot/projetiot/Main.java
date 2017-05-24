package projetiot.projetiot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import projetiot.projetiot.fragments.Alerte;
import projetiot.projetiot.fragments.Camera;
import projetiot.projetiot.fragments.Gaz;
import projetiot.projetiot.fragments.HomeFragment;
import projetiot.projetiot.fragments.Lampe;
import projetiot.projetiot.fragments.Temperature;
import projetiot.projetiot.fragments.UltraSon;

public class Main extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        Alerte.OnFragmentInteractionListener,Camera.OnFragmentInteractionListener,
    Lampe.OnFragmentInteractionListener,Temperature.OnFragmentInteractionListener,UltraSon.OnFragmentInteractionListener, Gaz.OnFragmentInteractionListener{
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtNom, txtRealisePar;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private static final String TAG_ACCUEIL = "ACCUEIL";
    private static final String TAG_CAMERA = "Camera";
    private static final String TAG_LAMPES = "Lampes";
    private static final String TAG_ULTRASON = "Capteur ultra son";
    private static final String TAG_TEMPERATURE = "Température";
    private static final String TAG_ALERTE= "Alertes";
    private static final String TAG_GAZ = "Capteur de gaz";
    public static String CURRENT_TAG = TAG_ACCUEIL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtNom = (TextView) navHeader.findViewById(R.id.nom_app);
        txtRealisePar = (TextView) navHeader.findViewById(R.id.realise_par);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // load nav menu header data
      //  loadNavHeader();

        // initializing navigation menu
       setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_ACCUEIL;
            loadHomeFragment();
        }
    }
    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
   /* private void loadNavHeader() {
        // name, website
        txtNom.setText("Ma maison intelligent");
        txtRealisePar.setText("Réalisé par HANNACHI Samiha");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }*/
    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        Log.d("LoadHomeFragment", "LoadHomeFragment");
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
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

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        Log.d("GetHomeFragment", "Index est "+navItemIndex );
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // camera
                Camera cameraFragment = new Camera();
                return cameraFragment;

            case 2:
                // lampe

            Lampe lampeFragment = new Lampe();
            return lampeFragment;
            case 3:
                // ultrason
                UltraSon ultraSonFragment = new UltraSon();
                return ultraSonFragment;

            case 4:
                // Température
                Temperature temeratureFragment = new Temperature();
                return temeratureFragment;

            case 5:

            Gaz gazFragment = new Gaz();
            return gazFragment;
            case 6:

                Alerte alerteFragment = new Alerte();
                return alerteFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        Log.d("Nav item","valeur : "+navItemIndex);
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
                    case R.id.nav_accueil:
                        navItemIndex = 0;
                        CURRENT_TAG =TAG_ACCUEIL;
                        break;
                    case R.id.nav_camera:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CAMERA ;
                        break;
                    case R.id.nav_lampes:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_LAMPES;
                        break;
                    case R.id.nav_ultra_son:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ULTRASON;
                        break;
                    case R.id.nav_temperature:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_TEMPERATURE;
                        break;
                    case R.id.nav_gaz:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_GAZ;
                        break;
                    case R.id.nav_alertes:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_ALERTE;
                        break;
                    case R.id.nav_login:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(Main.this, Login.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_quitter:
                        // launch new intent instead of loading fragment
                        finish();
                        drawer.closeDrawers();
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
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
        // show or hide the fab
        private void toggleFab() {
            if (navItemIndex == 0)
                fab.show();
            else
                fab.hide();
        }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("Item cliqué", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;

            case R.id.nav_accueil:

                navItemIndex = 0;
                CURRENT_TAG =TAG_ACCUEIL;

                break;
            case R.id.nav_alertes:
                navItemIndex = 1;
                CURRENT_TAG = TAG_ALERTE;
                break;
            case R.id.nav_camera:
                navItemIndex = 2;
                CURRENT_TAG = TAG_CAMERA;
                break;
            case R.id.nav_lampes:
                navItemIndex = 3;
                CURRENT_TAG = TAG_LAMPES;
                break;
            case R.id.nav_temperature:
                navItemIndex = 4;
                CURRENT_TAG = TAG_TEMPERATURE;
                break;
            case R.id.nav_ultra_son:
                navItemIndex = 5;
                CURRENT_TAG = TAG_ULTRASON;
                break;
            case R.id.nav_gaz:
                navItemIndex = 6;
                CURRENT_TAG = TAG_GAZ;
                break;
            case R.id.nav_login:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Main.this, Login.class));
                drawer.closeDrawers();
                return true;
            case R.id.nav_quitter:
                // launch new intent instead of loading fragment
                finish();
                drawer.closeDrawers();
                return true;
            default:
                navItemIndex = 0;
        }
        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        item.setChecked(true);

        loadHomeFragment();

        return true;
    }

}
