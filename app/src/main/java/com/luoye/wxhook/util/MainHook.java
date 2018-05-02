package com.luoye.wxhook.util;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by zyw on 18-4-28.
 */

public class MainHook implements IXposedHookLoadPackage {
    private  final  String PACKAGE_NAME="com.tencent.mm";
    private  final  String CLASS_NAME="com.tencent.mm.plugin.normsg.b";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.args[0];
                ClassLoader classLoader = context.getClassLoader();
                Class clazz=null;
                try {
                    clazz = classLoader.loadClass(CLASS_NAME);

                    hookWechat(clazz);
                }
                catch(Exception e){
                    e.printStackTrace();
                    return;
                }

            }
        });


    }


    private  void hookTest(ClassLoader classLoader){
        XposedHelpers.findAndHookMethod(CLASS_NAME,classLoader,"get",
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(110);
                    XposedBridge.log("----------------------->before wxHook get");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    XposedBridge.log("----------------------->after wxHook get");
                }
            });
    }

    private  void hookWechat(Class clazz)
    {
        XposedHelpers.findAndHookMethod(clazz,"uw",int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //String result=(String)param.getResult();
                        //XposedBridge.log("----------------------->before wxHook:"+result);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String result=(String)param.getResult();
                        XposedBridge.log("----------------------->after wxHook:"+result);
                    }
                });

    }
}
