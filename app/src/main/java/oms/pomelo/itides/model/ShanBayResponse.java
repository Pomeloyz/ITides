package oms.pomelo.itides.model;

import com.google.gson.annotations.SerializedName;

public class ShanBayResponse<T> {
    @SerializedName("msg")
    private String msg;
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("data")
    private T data;

    public ShanBayResponse(String msg, int statusCode, T data) {
        this.msg = msg;
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
