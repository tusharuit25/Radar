package mr_immortalz.com.modelqq.utils;

import org.json.JSONObject;

/**
 * Created by DELL2 on 7/13/2016.
 */
public class Result {

    public RequestResponse getReqres() {
        return reqres;
    }

    public void setReqres(RequestResponse reqres) {
        this.reqres = reqres;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }

    public RequestError getReqErro() {
        return reqErro;
    }

    public void setReqErro(RequestError reqErro) {
        this.reqErro = reqErro;
    }

    RequestResponse reqres ;
    org.json.JSONObject obj;
    RequestError reqErro ;
}
