package com.fzy.project.common;

public class UserHolder {

    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    public static void save(String tokenKey) {
        tl.set(tokenKey);
    }

    public static String get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }

}