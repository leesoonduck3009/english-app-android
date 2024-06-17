package com.example.snaplearn.utils;

public class ExistedItemException extends Exception{
    public ExistedItemException(String message)
    {
        super(message);
    }
    public ExistedItemException()
    {
        super();
    }
}

