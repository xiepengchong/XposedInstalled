package com.xposedinstalled.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

internal class HookMonitor : IXposedHookLoadPackage {
    private val TARGET_PACKAGE = "com.xpc.hookdemo"


    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if(TARGET_PACKAGE.equals(lpparam.packageName)){
            hookDemo(lpparam.classLoader)
        }
    }

    fun hookDemo(cl:ClassLoader){
        XposedHelpers.findAndHookMethod("com.xpc.hookdemo.HookedMethod",cl,"test", String::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                XposedBridge.log("before hooked method called")
                super.beforeHookedMethod(param)
            }

            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                XposedBridge.log("after hooked method called")
                super.afterHookedMethod(param)
            }
        })
    }

}