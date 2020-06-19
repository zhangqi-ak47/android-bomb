package com.goldze.mvvmhabit.game.boss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentBossBinding;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.role.BaseF;
import com.goldze.mvvmhabit.game.role.BaseLF;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.CommonUI;

public class BossFragment extends BaseF<FragmentBossBinding, BossVm> {

    public static BossFragment newInstance() {
        BossFragment fragment = new BossFragment();
//        Bundle bundle=new Bundle();
//        bundle.put
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_boss;
    }

    @Override
    public int initArgument() {
        return 0;
    }

    @Override
    public int initFlow() {
        return 0;
    }

    @Override
    public int initLayoutUpdate(CommonUI common) {
        if (NPC.LAYOUT_UPDATE_TAG.equals(common._tag)) {

        }
        return 0;
    }

    @Override
    public int initServerResponse(CommonResponse common) {
        return 0;
    }

    @Override
    public Class<BossVm> initVM() {
        return BossVm.class;
    }

    @Override
    public void onClickView(View view) {
        super.onClickView(view);
    }
}
