package models;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.HashMap;
import java.util.Map;

public class Response {

    public String message;
    public int status;
    public boolean ok;
    public Exception exception;
    public Map<String, Object> body;

    public Response(String _message, int _status) {
        status = _status;
        message = _message;
        exception = null;
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response(String _message, int _status, Exception e) {
        status = _status;
        message = _message;
        exception = e;
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response(String _message, int _status,String key, Object _body) {
        status = _status;
        message = _message;
        exception = null;
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
        exception = null;
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
