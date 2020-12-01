package com.demo.application.home.data.datasource.database;

import com.demo.application.home.data.repository.HomeRepo;

import io.reactivex.Single;

public class HomeDbDataSourceImp implements HomeRepo.DashboardDbData {
    @Override
    public Single<Integer> getStoreDataCount() {
        return null;
    }
}
