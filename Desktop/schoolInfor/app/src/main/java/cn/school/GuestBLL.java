package cn.school;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import cn.Common;
import cn.vipapps.AJAX;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;

/**
 * Created by wang on 2017/4/16.
 */

public class GuestBLL {

    public static void login(final String username, final String password, final CALLBACK<Boolean> callback){

        HashMap<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        SchoolinfoAJAX.getJSON("Guest","login",params,true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError){
//                    Log.e("GuestLogin1: ", "fail");
                    return;
                }
//                Log.e("GuestLogin1: ", "succ2");
                Boolean b = result.optBoolean("result");

                callback.run(b, b);
                CONFIG.setJSON(Common.CONFIG_DATA,result.optJSONObject("DATA"));
//                callback.run(false, true);
                Log.e( "GuestLogin1 ",result+"" );


            }
        });

    }

}
