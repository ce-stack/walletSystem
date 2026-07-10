package com.system.wallet.config.enums;

public enum Auth {

    REGISTER("api/auth/register"),
    LOGIN("api/auth/login");
    private final String path;

    Auth(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static boolean isPublicPath(String path) {
        for (Auth authPath : values()) {
            if (path.startsWith(authPath.getPath())) {
                return true;
            }
        }
        return false;
    }

}
