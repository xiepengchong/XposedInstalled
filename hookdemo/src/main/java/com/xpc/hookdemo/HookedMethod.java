package com.xpc.hookdemo;

import android.util.Log;

public class HookedMethod {


    public String test(String input){
        Log.v("HookedMethod","origin value:"+input);
        return "origin value:"+input;
    }
}
