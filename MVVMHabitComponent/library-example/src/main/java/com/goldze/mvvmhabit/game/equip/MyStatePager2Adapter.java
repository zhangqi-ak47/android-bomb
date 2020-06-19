package com.goldze.mvvmhabit.game.equip;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.goldze.mvvmhabit.game.role.BaseF;

import java.util.List;

public class MyStatePager2Adapter extends FragmentStateAdapter {

    private List<BaseF> mFragments;

    public MyStatePager2Adapter(@NonNull FragmentActivity fragmentActivity, List<BaseF> fragments) {
        super(fragmentActivity);
        this.mFragments = fragments;
    }

//    public MyStatePager2Adapter(@NonNull Fragment fragment, List<BaseF> fragments) {
//        super(fragment);
//        this.mFragments = fragments;
//    }

//    public MyStatePager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<BaseF> fragments) {
//        super(fragmentManager, lifecycle);
//        this.mFragments = fragments;
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

}
