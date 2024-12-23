package com.example.loaescuela.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.loaescuela.fragment.BaseFragment;
import com.example.loaescuela.fragment.BoxBeachFragment;
import com.example.loaescuela.fragment.BoxLocalFragment;
import com.example.loaescuela.fragment.IncomesBeachFragment;
import com.example.loaescuela.fragment.IncomesLocalFragment;

import java.util.ArrayList;

public class PageIncomesAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<BaseFragment> mFragments;


    public PageIncomesAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mFragments = new ArrayList<>();

        mFragments.add(new IncomesBeachFragment());
        mFragments.add(new IncomesLocalFragment());
        mFragments.add(new BoxBeachFragment());
        mFragments.add(new BoxLocalFragment());

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position ==0){
            return "ESCUELA";
        }else if(position == 1){
            return "NEGOCIO";
        }else if(position == 2){
            return "Caja esc";
        }else{
            return "Caja esc";
        }
    }
}