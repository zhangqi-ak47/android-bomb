package com.goldze.mvvmhabit.app;

import com.goldze.mvvmhabit.data.MyRepository;
import com.goldze.mvvmhabit.data.source.MyHttpDataSource;
import com.goldze.mvvmhabit.data.source.MyLocalDataSource;
import com.goldze.mvvmhabit.data.source.http.MyHttpDataSourceImpl;
import com.goldze.mvvmhabit.data.source.http.service.MyApiService;
import com.goldze.mvvmhabit.data.source.local.MyLocalDataSourceImpl;
import com.goldze.mvvmhabit.utils.RetrofitClient;


/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 * Created by goldze on 2019/3/26.
 */
public class Injection {

    public static MyRepository provideMyRepository() {
        MyApiService apiService = RetrofitClient.getInstance().create(MyApiService.class);
        MyHttpDataSource httpDataSource = MyHttpDataSourceImpl.getInstance(apiService);
        MyLocalDataSource localDataSource = MyLocalDataSourceImpl.getInstance();
        return MyRepository.getInstance(httpDataSource, localDataSource);
    }
}
