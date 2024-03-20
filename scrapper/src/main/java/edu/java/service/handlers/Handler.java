package edu.java.service.handlers;

public interface Handler<T> {
    String getData(T dto);

    T getInfo(String url);
}
