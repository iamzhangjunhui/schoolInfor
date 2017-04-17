package cn;

import android.content.Context;
import android.content.pm.PackageManager;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.vipapps.CONFIG;
import cn.vipapps.JSON;

public class Common {
    public static final boolean TEST = true;
    public static final String URL_BASE = "120.25.202.192";
    public static final String[] APIS_GUEST = new String[]{"Guest/login"};
    public static String[] APIS_TOKEN = new String[]{"Guest/login","Member/changePassword"};
    ////////////////////////////// CONFIG /////////////////////////////
    public static final String CONFIG_TOKEN = "TOKEN";
//    public static final String CONFIG_PROFILE = "PROFILE";
    public static final String CONFIG_PROFILE = "profile";
    public static final String CONFIG_PUSH_MESSAGE = "PUSH_MESSAGE";
    public static final String CONFIG_PUSH_EXTRA = "PUSH_EXTRA";
    ////////////////////////// MSG //////////////////////////////////////
    public static final String MSG_LOGIN = "LOGIN";
    public static final String MSG_CLEARIMG = "CLEARIMG";
    public static String MSG_DEPARTMENT = "DEPARTMENT";
    public static String CONFIG_DATA = "DATA";
    public static String MSG_UPDATEMEMBER = "UPDATEMEMBER";
    public static String MSG_TRIBLEMEMBER = "TRIBLEMEMBER";
    public static String MSG_ADDFRIEND = "ADDFREND";
    public static String MSG_CLOSSACTIVITY ="CLOSSACTIVITY";
    public static String MSG_PROGRESSBAR ="PROGRESSBAR";



    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }


    public static boolean isGuest() {
        return CONFIG.get(CONFIG_TOKEN) == null;
    }



    //判断用户是否拥有账号权限
    public static boolean hasUsers() {

        JSONObject profile = JSON.parse(CONFIG.get(CONFIG_PROFILE));
        JSONArray permisson = profile.optJSONArray("permissons");
        boolean role = JSON.contains(permisson, "users");

        return role;
    }






}
