package com.swiggy.userservice.common.exceptions;

public class NoSuchRoleException extends Exception{
    public NoSuchRoleException() {
        super("No such role exists");
    }
}
