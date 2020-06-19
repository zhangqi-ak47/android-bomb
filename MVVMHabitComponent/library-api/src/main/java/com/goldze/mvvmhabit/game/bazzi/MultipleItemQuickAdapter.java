package com.goldze.mvvmhabit.game.bazzi;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.R;

import java.util.List;

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<QuickMultipleEntity, BaseViewHolder> implements LoadMoreModule {

    public MultipleItemQuickAdapter(List<QuickMultipleEntity> data) {
        super(data);
        // 绑定 layout 对应的 type
//        addItemType(QuickMultipleEntity.TYPE_LAYOUT1, R.layout.layout_type1);
//        addItemType(QuickMultipleEntity.TYPE_LAYOUT2, R.layout.layout_type2);
//        addItemType(QuickMultipleEntity.TYPE_LAYOUT3, R.layout.layout_type3);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuickMultipleEntity item) {
        // 根据返回的 type 分别设置数据
        switch (helper.getItemViewType()) {
            case QuickMultipleEntity.TYPE_LAYOUT1:
//                helper.setText(R.id.tv, item.getContent());
                break;
            case QuickMultipleEntity.TYPE_LAYOUT2:
                break;
            default:
                break;
        }
    }
}