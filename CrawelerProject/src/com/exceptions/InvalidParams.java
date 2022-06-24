package com.exceptions;

public class InvalidParams extends Exception {

    public InvalidParams(String url, String number) {
        super(String.format("Url - %s , number - %s are not valid", url, number));
    }

}
