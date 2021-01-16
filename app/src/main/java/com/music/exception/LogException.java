package com.music.exception;

public class LogException extends  RuntimeException{
    public LogException(Throwable cause,String msg,Object... args){
        super( String.format(msg,args),cause);
    }
}
