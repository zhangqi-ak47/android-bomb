package com.goldze.mvvmhabit.game.store;

public class CommonUI {

    public int _code;
    public int _type;
    public String _tag;
    public Object _obj;

    public CommonUI() {
    }

    public CommonUI(int _code, int _type, String _tag) {
        this._code = _code;
        this._type = _type;
        this._tag = _tag;
    }

    public CommonUI(int _code, int _type, String _tag, Object _obj) {
        this._code = _code;
        this._type = _type;
        this._tag = _tag;
        this._obj = _obj;
    }
}
