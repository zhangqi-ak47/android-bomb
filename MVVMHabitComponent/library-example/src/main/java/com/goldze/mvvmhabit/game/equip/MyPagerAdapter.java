package com.goldze.mvvmhabit.game.equip;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.goldze.mvvmhabit.game.role.BaseF;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<BaseF> fragmentList;

    public MyPagerAdapter(FragmentManager fm, Context context, List<BaseF> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        if (fragmentList!=null){
            return fragmentList.size();
        }
        return 0;
    }

    /**
     * //此方法用来显示tab上的名字
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (fragmentList!=null){
            return fragmentList.get(position).title;
        }
        return "";
    }
}