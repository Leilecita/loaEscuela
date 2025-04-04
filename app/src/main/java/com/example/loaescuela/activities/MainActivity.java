package com.example.loaescuela.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.R;
import com.example.loaescuela.activities.noseusa.ListIncomesDayActivity;
import com.example.loaescuela.adapters.assistsResumByDay.AssistResumAdapter;
import com.example.loaescuela.adapters.assistsResumByDay.PlanillaResumAdapter;
import com.example.loaescuela.data.SessionPrefs;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.ReportResumAsist;
import com.example.loaescuela.network.models.ReportResumPlanilla;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.AccessNetworkConstants;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    PlanillaResumAdapter adapterIncomeInfo;
    GridLayoutManager gridlayoutmanager;
    public RecyclerView mRecyclerView;

    Toolbar myToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    CoordinatorLayout coordinatorLayout;
    FrameLayout frameLayout;

    ImageView payments;
    ImageView assists;
    ImageView students;
    ImageView spendings;
    ImageView outcomes;

    public TextView name_day;
    public TextView number_day;
    public TextView month;
    public TextView year;
    public LinearLayout dayResum;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        String level = SessionPrefs.get(this).getLevel();

        if (id == R.id.nav_assists) {
            startActivity(new Intent(this, GeneralAssistActivity.class));
        } else if (id == R.id.nav_payments) {
            if(level.equals("admin")){
                startActivity(new Intent(this, IncomesActivity.class));
            }

        } else if (id == R.id.nav_create_student) {
            startActivity(new Intent(this, CreateStudentActivity.class));
        } else if (id == R.id.nav_resum) {
            if(level.equals("admin")) {
                startActivity(new Intent(getBaseContext(), AssistsResumByDayActivity.class));
            }
        } else if (id == R.id.nav_planillas) {
            if(level.equals("admin")) {
                startActivity(new Intent(this, PlanillasActivity.class));
            }
        }  else if (id == R.id.nav_students) {
            startActivity(new Intent(this, StudentsListActivity.class));
        } else if (id == R.id.nav_session) {
            signOut();
        }else if( id == R.id.nav_home){
           // selectFragment(0);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 1 - Configure Toolbar
    private void configureToolBar(){
        this.myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerLayout.setScrimColor(getResources().getColor(R.color.shadownav));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.nav_view);

        View headerLayout = navigationView.getHeaderView(0);
        TextView name = headerLayout.findViewById(R.id.user_name);
        //name.setText(SessionPrefs.get(this).getName());
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
      /*  MenuItem liquidations = menu.findItem(R.id.nav_liquidations);
        MenuItem workers = menu.findItem(R.id.nav_workers);
        MenuItem prices = menu.findItem(R.id.nav_prices);
        MenuItem allOrders = menu.findItem(R.id.nav_orders);
        MenuItem zones = menu.findItem(R.id.nav_zones);
        MenuItem home = menu.findItem(R.id.nav_home);*/


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        getWindow().setStatusBarColor(Color.parseColor("#95215f"));

        frameLayout =  findViewById(R.id.main_content);
        coordinatorLayout=findViewById(R.id.rl);

        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assists = findViewById(R.id.asistencia);
        students = findViewById(R.id.students);
        payments = findViewById(R.id.payments);
        outcomes = findViewById(R.id.spendings);
        dayResum = findViewById(R.id.start_resum);


        String level = SessionPrefs.get(this).getLevel();
        String name = SessionPrefs.get(this).getName();

        if(level != null && !level.equals("") && level.equals("admin")) {

            dayResum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(), AssistsResumByDayActivity.class));
                }
            });

            outcomes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(), OutcomesActivity.class));
                }
            });

            payments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(), IncomesActivity.class);
                    // i.putExtra("NAMEFRAGMENT", "lei");
                    startActivity(i);
                }
            });
        }else{
            outcomes.setAlpha(0.7f);
            payments.setAlpha(0.7f);
        }

        assists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( name.equals("cami") || name.equals("jose") || name.equals("flor")){
                    Intent i = new Intent(getBaseContext(), GeneralAssistActivity.class);
                    i.putExtra("NAMEFRAGMENT", "mini");
                    startActivity(i);
                }else if(name.equals("sole") ){
                    Intent i = new Intent(getBaseContext(), GeneralAssistActivity.class);
                    i.putExtra("NAMEFRAGMENT", "high");
                    startActivity(i);

                }else if(name.equals("gucho") || name.equals("flor")){
                    Intent i = new Intent(getBaseContext(), GeneralAssistActivity.class);
                    i.putExtra("NAMEFRAGMENT", "kids");
                    startActivity(i);

                }else{
                    Intent i = new Intent(getBaseContext(), GeneralAssistActivity.class);
                    i.putExtra("NAMEFRAGMENT", "s");
                    startActivity(i);

                   // startActivity(new Intent(getBaseContext(), GeneralAssistActivity.class));
                }

            }
        });

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), StudentsListActivity.class));
            }
        });

        name_day = findViewById(R.id.name_day);
        number_day = findViewById(R.id.number_day);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);

        mRecyclerView = findViewById(R.id.list_planillas);
        gridlayoutmanager = new GridLayoutManager(this ,2   );
       // gridlayoutmanager = new LinearLayoutManager(this  );
        mRecyclerView.setLayoutManager(gridlayoutmanager);
        adapterIncomeInfo = new PlanillaResumAdapter(this, new ArrayList<ReportResumPlanilla>(),"main");
        mRecyclerView.setAdapter(adapterIncomeInfo);

        listPlanillas();

    }

    @Override
    public void onResume() {
        super.onResume();
        listPlanillas();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main_drawer;
    }

    public void signOut(){
        SessionPrefs.get(getBaseContext()).logOut();
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
    }

  /*  @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/


    public void listPlanillas(){
        ApiClient.get().getAssistsResumByDay(0 , new GenericCallback<List<ReportResumAsist>>() {
            @Override
            public void onSuccess(List<ReportResumAsist> data) {

                if(data.size() > 0){
                    String date = DateHelper.get().onlyDate(data.get(0).day);
                    String day = DateHelper.get().getNameDay(date);
                    String number = DateHelper.get().getDay(date);
                    String month2 = DateHelper.get().getNameMonth(date);
                    String year2 = DateHelper.get().getYear(date);
                    name_day.setText(day);
                    number_day.setText(number);
                    month.setText(month2.substring(0,3));
                    year.setText(year2);

                    adapterIncomeInfo.setItems(data.get(0).planillas);
                }


            }

            @Override
            public void onError(Error error) {
                checkValidSession(error);
            }
        });

    }

    private void checkValidSession(Error error){
        System.out.println(error.message);
        System.out.println(error.result);
        if(error.message.equals("Session invalida") ){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }


}