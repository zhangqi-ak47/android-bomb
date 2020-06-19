package com.goldze.mvvmhabit.game.store;

import java.util.List;

import lombok.Data;

@Data
public class PagingResponse<T> {

    private int current;
    private int size;
    private int total;
    private List<T> records;
    private List<String> orders;
    private boolean searchCount;
    private int pages;

}
