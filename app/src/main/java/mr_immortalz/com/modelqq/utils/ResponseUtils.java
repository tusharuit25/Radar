package mr_immortalz.com.modelqq.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android1 on 10/19/2016.
 */
public class ResponseUtils {

    public static Result parseJsonArrayResponseCheck(JSONObject JsonData){
        try {
            Result requestResult = new Result();
            RequestResponse reqRes = new RequestResponse();
            org.json.JSONObject Requestobj = JsonData.getJSONObject("Request");
            reqRes.setStatus(Requestobj.getInt("Status"));
            reqRes.setHasError(Requestobj.getBoolean("HasError"));
            reqRes.setHasResponse(Requestobj.getBoolean("HasResponse"));
            reqRes.setMessage(Requestobj.getString("Message"));
            reqRes.setMethod(Requestobj.getString("Method"));
            requestResult.setReqres(reqRes);
            RequestError Err = new RequestError();
            org.json.JSONObject Errobj = JsonData.getJSONObject("Error");
            Err.setCode(Errobj.getString("Code"));
            Err.setMessage(Errobj.getString("Message"));
            requestResult.setReqErro(Err);
            org.json.JSONObject ResponseObj = JsonData.getJSONObject("Response");
            requestResult.setObj(ResponseObj);
            return  requestResult;
        } catch (JSONException e) {
            Log.d("log", e.getMessage());

            return  null;

        }
    }
}
