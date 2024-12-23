package com.example.loaescuela.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.loaescuela.R;
import com.example.loaescuela.adapters.PageIncomesAdapter;
import com.example.loaescuela.fragment.BaseFragment;
import com.example.loaescuela.fragment.BoxBeachFragment;
import com.example.loaescuela.fragment.BoxLocalFragment;
import com.example.loaescuela.fragment.HighschoolAssistsFragment;
import com.example.loaescuela.fragment.IncomesBeachFragment;
import com.example.loaescuela.fragment.IncomesLocalFragment;
import com.example.loaescuela.fragment.SchoolAssistsFragment;
import com.example.loaescuela.types.Constants;
import com.google.android.material.tabs.TabLayout;

public class IncomesActivity extends BaseActivity{


    private PageIncomesAdapter mAdapter;
    private TabLayout mTabLayout;
    private LinearLayout button;
    private ImageView image_button;
    private ImageView returnn;

    private ViewPager viewPager;

    private TextView title;

    private static final int ADD_STUDENT_ACTIVITY = 1021;
    private static final int CREATE_BOX_ACTIVITY = 1321;
    private Long student_id;
    private String payment_place;


    public void startAddStudentActivity(String place) {

        Intent i = new Intent(this, SelectStudentForAssistActivity.class);
        i.putExtra("TITLE", "Seleccionar alumno para crear pago");
        i.putExtra("ID", -1l);
        i.putExtra("CATEGORIA", "");
        i.putExtra("SUBCATEGORIA", "");
        i.putExtra("TYPE", "PAGOS");
        i.putExtra("PAYMENTPLACE", this.payment_place);
       // i.putExtra("CAMEFROM", this.payment_place);
        startActivityForResult(i, ADD_STUDENT_ACTIVITY);
    }

    public void startCreateBoxActivity(String place) {

        Intent i = new Intent(this, CreateBoxActivity.class);
        i.putExtra("PAYMENTPLACE", this.payment_place);
        //i.putExtra("PAYMENTPLACE", place);
        startActivityForResult(i, CREATE_BOX_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CREATE_BOX_ACTIVITY ){
            String place = data.getStringExtra("PAYMENTPLACE");
            Fragment f;

           // if(payment_place.equals("escuela")){
            if(place.equals("escuela")){
                selectFragment(2);
                f = mAdapter.getItem(2);
                if (f instanceof BaseFragment) {
                    ((BoxBeachFragment) f).refreshList(-1l);
                }

            }else{
                selectFragment(3);
                f = mAdapter.getItem(3);
                if(f instanceof BaseFragment) {
                    ((BoxLocalFragment) f).refreshList(-1l);
                }
            }
        }

        if (requestCode == ADD_STUDENT_ACTIVITY) {

            System.out.println("entra aca pagos");
            System.out.println(payment_place);

            if (resultCode == RESULT_OK) {
                this.student_id = data.getLongExtra("STUDENTID",-1l);

               // payment_place = data.getStringExtra("CAMEFROM");
               // System.out.println("asd"+data.getStringExtra("CAMEFROM"));
                Fragment f;

                if(payment_place.equals("escuela")){
                    System.out.println("entra aca pagos esc");
                    selectFragment(0);
                    f = mAdapter.getItem(0);
                    if (f instanceof BaseFragment) {
                        ((IncomesBeachFragment) f).refreshList(this.student_id);
                    }

                }else{

                    System.out.println("entra aca nego");
                    selectFragment(1);
                    f = mAdapter.getItem(1);
                    if(f instanceof BaseFragment) {
                        ((IncomesLocalFragment) f).refreshList(this.student_id);
                    }
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout home= this.findViewById(R.id.home);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);

        viewPager =  findViewById(R.id.viewpager);

        mAdapter = new PageIncomesAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        mTabLayout =  findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setSelectedTabIndicatorHeight(15);

        button= findViewById(R.id.fab);
        //image_button= findViewById(R.id.image_button);

        setTitle("Pagos ");
        actionFloatingButton();
        setPaymentPlaceByPosition(0);

        for (int i = 0; i < mAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_text);
                View v= tab.getCustomView();
                TextView t= v.findViewById(R.id.text1);

                setTextByPosition(t,i);
            }
        }

         viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //setImageButton();
                actionFloatingButton();
                setVisibilityButton();
                setPaymentPlaceByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View im=tab.getCustomView();
                TextView t=im.findViewById(R.id.text1);
                t.setTextColor(getResources().getColor(R.color.white));

                setPaymentPlaceByPosition(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View im=tab.getCustomView();
                TextView t=im.findViewById(R.id.text1);
                t.setTextColor(getResources().getColor(R.color.coloRose_soft_2));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



     /*   String name=getIntent().getStringExtra("NAMEFRAGMENT2");
        if(name.equals("special")){
            selectFragment(1);
        }


        if(SessionPrefs.get(getBaseContext()).getLevel() != null && !SessionPrefs.get(this).getLevel().equals("admin")){
            ((LinearLayout) mTabLayout.getTabAt(1).view).setVisibility(View.GONE);
        }*/

    }

    public void selectFragment(int position){
        viewPager.setCurrentItem(position, true);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_incomes;
    }

    private void setTextByPosition(TextView t, Integer i) {
        if (i == 0) {
            t.setText("Escuela");
            t.setTextColor(getResources().getColor(R.color.white));
        } else if (i == 1){
            t.setText("Nego");
            t.setTextColor(getResources().getColor(R.color.coloRose_soft_2));
        } else if( i == 2){
            t.setText("Caja playa");
            t.setTextColor(getResources().getColor(R.color.coloRose_soft_2));
        } else{
            t.setText("Caja nego");
            t.setTextColor(getResources().getColor(R.color.coloRose_soft_2));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setPaymentPlaceByPosition(Integer i) {
        if (i == 0 || i == 2) {
            this.payment_place = "escuela";
        } else  {
            this.payment_place = "negocio";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setVisibilityButton(){
        int position = mTabLayout.getSelectedTabPosition();
        Fragment f = mAdapter.getItem(position);

        if( f instanceof BaseFragment){
            button.setVisibility(((BaseFragment)f).getVisibility());
        }
    }

    public void actionFloatingButton(){
        int position = mTabLayout.getSelectedTabPosition();
        final Fragment f = mAdapter.getItem(position);

        if(f instanceof BaseFragment){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseFragment)f).onClickButton();
                }
            });
        }
    }

}
