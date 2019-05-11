package cn.hd.utils;

public class ServerResponse<T> {

    public final static int OK = 0;
    public final static int ERROR = 500;
    public final static int FAILURE = -500;

    private int code;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return OK == code;
    }

    private ServerResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private ServerResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    private ServerResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        ServerResponse<T> response = new ServerResponse<>(OK, data);
        return response;
    }

    public static <T> ServerResponse<T> createByError(String msg) {
        ServerResponse<T> response = new ServerResponse<T>(ERROR, msg);
        return response;
    }

    public static <T> ServerResponse<T> createByFailure(String msg) {
        ServerResponse<T> response = new ServerResponse<T>(FAILURE, msg);
        return response;
    }

    public static <T> ServerResponse<T> createByFailure(T data, String msg) {
        return new ServerResponse<>(FAILURE, data, msg);
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(OK);
    }

    public String toJSONString() {
        return "{\"code\":\"" + code + "\",\"msg\":\"" + msg + "\",\"data\":\"" + data + "\"}";
    }

}
