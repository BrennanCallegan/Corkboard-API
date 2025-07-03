package com.brennan.server.controller;

public class NotesNotFoundException extends RuntimeException {
    public NotesNotFoundException(String message){
        super(message);
    }
}
