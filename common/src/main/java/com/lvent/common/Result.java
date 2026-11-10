package com.lvent.common;


import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result(){

    }

    public Result(T data){
        this.data = data;
    }

    public Result(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Result(Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(){
        return new Result(200,"成功");
    }

    public static <V> Result success(V data){
        return new Result(200,"成功",data);
    }

    public static <V> Result success(String msg,V data){
        return new Result(200,msg,data);
    }

    public static <V> Result error(Integer code,String msg,V data){
        return new Result(code,msg,data);
    }

    public static <V> Result error(){
        return new Result(500,null,null);
    }
 }
