package cn.school;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ProgressBar;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.Common;
import cn.vipapps.AJAX;
import cn.vipapps.ARRAY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;

@SuppressWarnings("deprecation")
public class SchoolinfoAJAX {
    static String getUrl(String api, String method) {
//        return String.format("%s/API/%sService.asmx/%s", Common.URL_BASE, api, method);
        return String.format("%s/%s/%s", Common.URL_BASE, api, method);
    }


    public static void getJSON(final String api, final String method, Map<String, Object> params, final boolean isSilence, AJAX.Mode mode,
                               final CALLBACK<JSONObject> callback) {

        String url = getUrl(api, method);
        Log.e("=====================", url);
        Map<String, Object> headers = new HashMap<String, Object>();
        if (!ARRAY.contains(Common.APIS_GUEST, String.format("%s/%s", api, method))) {
            headers.put("COOKIE", CONFIG.get(Common.CONFIG_TOKEN));
        }
//		headers.put("content-type", "application/json");
        AJAX.setHeaders(headers);
//		if (!isSilence) {
//			DIALOG.loading();
//		}

        AJAX.getJSON(url, params, mode, new CALLBACK<JSONObject>() {

            @Override
            public void run(boolean isError, JSONObject json) {
                if (!isSilence) {
                    DIALOG.done();

                }
                if (isError) {
                    callback.run(true, null);
                    return;
                }
                if (ARRAY.contains(Common.APIS_TOKEN, String.format("%s/%s", api, method))) {
                    Header[] allHeaderFields = AJAX.HEADERS;
                    for (Header header : allHeaderFields) {
                        if (header.getName().equals("Set-Cookie")) {
                            CONFIG.set(Common.CONFIG_TOKEN, header.getValue());
                        }
                    }
                } else {

                }
                if (json.optInt("code") != 0) {
//                    DIALOG.toast(json.optInt("code")+json.optJSONObject("data").optBoolean("result"));
                    return;
                }
                if (callback != null) {
                    callback.run(false, json.optJSONObject("data"));
                }

            }

        });
    }

    public static void upload(final String api, final String method, Map<String, Object> params, Map<String, java.io.InputStream> files,
                              AJAX.Mode mode, final CALLBACK<JSONObject> callback) {
        String url = getUrl(api, method);
        Map<String, Object> headers = new HashMap<String, Object>();
        if (!ARRAY.contains(Common.APIS_GUEST, api)) {
            headers.put("COOKIE", CONFIG.get(Common.CONFIG_TOKEN));
        }
//		headers.put("content-type", "application/json");
        AJAX.setHeaders(headers);


        AJAX.upload(url, params, files, mode, new CALLBACK<JSONObject>() {

            @Override
            public void run(boolean isError, JSONObject json) {

                if (isError) {
                    callback.run(true, null);
                    return;
                }
                if (ARRAY.contains(Common.APIS_TOKEN, api)) {
                    Map<String, Object> allHeaderFields = AJAX.getHeaders();
                    String Set_Cookie = (String) allHeaderFields.get("Set-Cookie");
                    CONFIG.set(Common.CONFIG_TOKEN, Set_Cookie);
                } else {

                }
                if (callback != null) {
                    callback.run(false, json);
                }

            }

        });
    }

    public static void downLoad(final String name, final String api, final String method, Map<String, Object> params,
                                final boolean isSilence, AJAX.Mode mode, ProgressBar pb,
                                final CALLBACK<Boolean> callback) {

        String url = getUrl(api, method);
        Map<String, Object> headers = new HashMap<String, Object>();
        if (!ARRAY.contains(Common.APIS_GUEST, String.format("%s/%s", api, method))) {
            headers.put("COOKIE", CONFIG.get(Common.CONFIG_TOKEN));
        }
//		headers.put("content-type", "application/json");
        AJAX.setHeaders(headers);
//		if (!isSilence) {
//			DIALOG.loading();
//		}

        AJAX.downLoad(name, url, params, mode, new CALLBACK<Boolean>() {

            @Override
            public void run(boolean isError, Boolean json) {
                if (!isSilence) {
                    DIALOG.done();

                }
                if (isError) {
                    callback.run(true, null);
                    return;
                }
//				if (json.optInt("code") != 0){
////                    DIALOG.toast(json.optInt("code")+json.optJSONObject("data").optBoolean("result"));
//					return;
//				}
                if (callback != null) {
                    callback.run(false, json);
                }

            }

        }, pb);
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    /*public static void getVoid(final String api,final String method, Map<String, Object> params, final boolean isSilence, AJAX.Mode mode,
			final CALLBACK callback) {
		String url = getUrl(api,method);
		Map<String, Object> headers = new HashMap<String, Object>();
//		if (!ARRAY.contains(Common.APIS_GUEST, api)) {
//			headers.put("COOKIE", CONFIG.get(Common.CONFIG_TOKEN));
//		}
//		headers.put("content-type", "application/json");
		AJAX.setHeaders(headers);
		if (!isSilence) {
			DIALOG.loading();
		}

		AJAX.getVoid(url, params, mode, new CALLBACK() {

			@Override
			public void run(boolean isError, Object json) {
				if (!isSilence) {
					DIALOG.done();

				}
				if (isError) {
					callback.run(true, null);
					return;
				}
//				if (ARRAY.contains(Common.APIS_TOKEN, api)) {
//					Map<String, Object> allHeaderFields = AJAX.getHeaders();
//					String Set_Cookie = (String) allHeaderFields.get("Set-Cookie");
//					CONFIG.set(Common.CONFIG_TOKEN, Set_Cookie);
//				} else {
//
//				}
				if (callback != null) {
					callback.run(false, null);
				}

			}

		});
	}*/
    public static void getAvatar(final String username, final CALLBACK<Bitmap> callback) {

//		if (Common.TEST){
//			url =  String.format("%s/data/avatar/%s.jpg",Common.URL_BASE ,username);
//		}else {
//			url =  String.format("%s/data/avatar/%s.jpg",Common.URL_BASE2 ,username);
//		}
        String url;
        url = CONFIG.getJSON(Common.CONFIG_DATA).optJSONObject("users_").optJSONObject(username).optString("avatar");
//		url = CONFIG.getJSON(Common.CONFIG_PROFILE).optJSONObject("data").optString("avatar");
        Log.e("getAvatar: ", CONFIG.getJSON(Common.CONFIG_DATA) + "");
//        Log.e("getAvatar: ", url);
        AJAX.getImage(url, new CALLBACK<Bitmap>() {

            @Override
            public void run(boolean isError, Bitmap image) {
                if (isError) {
                    callback.run(true, null);
                    return;
                }
                callback.run(false, image);

            }

        });
    }


}
