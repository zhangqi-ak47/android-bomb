package com.goldze.mvvmhabit.game.store;

import me.goldze.mvvmhabit.http.BaseResponse;

public class CommonResponse<T> extends BaseResponse<T> {

    public int _type;

    public CommonResponse type(int _type) {
        this._type = _type;
        return this;
    }

    public boolean success() {
        return getCode() == 1 ? true : false;
    }

    public CommonResponse code(int code) {
        setCode(code);
        return this;
    }

//    public <S> void strongGo(S example) throws Exception {
//        if (getResult() == null) throw new Exception("getResult() == null");
//        example = (S) getResult();
//    }
}
