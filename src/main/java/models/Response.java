package models;

public class Response {

    public String message;
    public int status;
    public boolean ok;
    public Object body;

    public Response(String _message, int _status) {
        status = _status;
        message = _message;
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response(String _message, int _status, Object _body) {
        status = _status;
        message = _message;
        body = _body;
        if (_status < 0) {
            ok = false;
        } else {
            ok = true;
        }
    }

    public Response() {
    };

}
