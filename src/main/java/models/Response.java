package models;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.HashMap;
import java.util.Map;

public class Response {

    public String message;
    public int status;
    public boolean ok;
    public Map<String, Object> body;

    public Response(String _message, int _status) {
        status = _status;
        message = _message;
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response(String _message, int _status,String key, Object _body) {
        status = _status;
        message = _message;
        body = new HashMap<>();
        body.put(key, _body);
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response(String _message, int _status, Map<String, Object> map){
        status = _status;
        message = _message;
        body = map;
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response() {
    };

}
