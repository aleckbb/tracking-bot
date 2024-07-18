package edu.java.scrapper.service.handlers;

public interface Handler<T> {
    String getData(T dto);

    T getInfo(String url);
}
