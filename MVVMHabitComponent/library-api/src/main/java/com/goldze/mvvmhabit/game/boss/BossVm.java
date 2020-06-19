package com.goldze.mvvmhabit.game.boss;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.goldze.mvvmhabit.data.MyRepository;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.role.BaseVM;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;

public class BossVm extends BaseVM {

    public ObservableField<String> stringData = new ObservableField<>("");
    public ObservableInt visibilityData = new ObservableInt();

    public BindingCommand btnCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            GM.Log.d("btnCommand->");
//            addReuest(1, true, true, model.login(setParams(
//                    "mobile", "admin@qq.com",
//                    "password", "admin"
//            )));
        }
    });

    public void testRequest(){
//        addReuest(2, true, true, model.project(setParams(
//                "currPage", "1",
//                "pageSize", "2"
//        )));
    }

    public TextWatcher textWatcher=new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            GM.Log.d("textWatcher->");
        }
    };

    public BindingCommand<Boolean> focusCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            GM.Log.d("focusCommand->");
        }
    });

    public BossVm(@NonNull Application application) {
        super(application);
    }

    public BossVm(@NonNull Application application, MyRepository model) {
        super(application, model);
    }

    @Override
    public void initActType(int actType) {
        this.actType=actType;
        if (actType==1){

        }
    }
}
