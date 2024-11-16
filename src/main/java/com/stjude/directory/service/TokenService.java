package com.stjude.directory.service;

public interface TokenService<T> {
    public String createToken(T input);
    public Boolean isTokenValid();
}
