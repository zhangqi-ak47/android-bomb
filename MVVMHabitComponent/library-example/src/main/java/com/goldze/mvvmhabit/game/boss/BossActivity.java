package com.goldze.mvvmhabit.game.boss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.ActivityBossBinding;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.mounts.DialogFragmentConvert;
import com.goldze.mvvmhabit.game.mounts.RvAdapterConvert;
import com.goldze.mvvmhabit.game.role.BaseA;
import com.goldze.mvvmhabit.game.role.BaseDF;
import com.goldze.mvvmhabit.game.role.BaseM;
import com.goldze.mvvmhabit.game.store.CommonDialog;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.CommonUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BossActivity extends BaseA<ActivityBossBinding, BossVm> {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, BossActivity.class);
        return intent;
    }

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_boss;
    }

    @Override
    public int initArgument() {
        aIntent.getStringExtra(NPC.ARG_PARAM1);
        return 0;
    }

    @Override
    public int initFlow() {
        setNaviTitle(0, "boss");

        List<String> list=new ArrayList<>();
        list.add("abc");
        list.add("abc");
        list.add("abc");
        list.add("abc");
        binding.rv.setAdapter(initRvAdapter(R.layout.activity_boss, list, new RvAdapterConvert<String>() {
            @Override
            public void convert(BaseViewHolder viewHolder, String item) {
                viewHolder.setText(R.id.tv, item);
            }
        }));


        initOnRefresh();
//        initOnLoadMore();

        return 0;
    }

    @Override
    public int initLayoutUpdate(CommonUI common) {
        if (NPC.LAYOUT_UPDATE_TAG.equals(common._tag)) {
            //更新界面UI
        }
        return 0;
    }

    @Override
    public int initServerResponse(CommonResponse common) {
        if (common.success()) {
            switch (common._type) {
                case 1:
                    break;
            }
        } else if (common.getCode() > 0) {
            //服务器返回的问题
        } else {
            //自身的问题
        }
        return 0;
    }

    @Override
    public Class<BossVm> initVM() {
        return BossVm.class;
    }

    @Override
    public void onClickView(View view) {
        if (view.getId() == R.id.tv) {
            CommonDialog commonDialog = new CommonDialog(R.layout.activity_boss)
                    .setmGravity(Gravity.BOTTOM)
                    .setConvert(new DialogFragmentConvert() {
                        @Override
                        public void viewCreate(BaseViewHolder viewHolder, BaseDF dialog, FragmentManager fm) {
                            viewHolder.setText(R.id.tv, "cccc");

                        }
                    })
                    .show(getSupportFragmentManager());

            viewModel.testRequest();
        }
    }

    @Override
    public String onCommon(String tag, int type, Object obj) {
        super.onCommon(tag, type, obj);
        if (NPC.FRAGMENT_CALL_ACTIVITY_TAG.equals(tag)){
            switch (type){
                case 1:
                    break;
            }
        }

        return tag;
    }
}
