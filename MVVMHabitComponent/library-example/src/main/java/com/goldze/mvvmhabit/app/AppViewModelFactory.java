package com.goldze.mvvmhabit.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.goldze.mvvmhabit.data.MyRepository;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2019/3/26.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final MyRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideMyRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, MyRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            Constructor constructor1 = modelClass.getDeclaredConstructor(Application.class,MyRepository.class);
//            constructor1.setAccessible(true);
            return (T) constructor1.newInstance(mApplication, mRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return (T) new BaseViewModel(mApplication, mRepository);
        return null;
    }
}
