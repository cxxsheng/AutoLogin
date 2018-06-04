package com.h3hz.thl.util;

public class ResponData {
    private int retCode=0;
    private String retData=null;
    public ResponData(int code, String data){
        retCode = code;
        retData = data;
    }

    public int getRetCode() {
        return retCode;
    }

    public String getRetData() {
        return retData;
    }
}
