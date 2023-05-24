package com.example.bookshare_project.security;

public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/auth/*";
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_DATE = 3_600_000; //1 hour
}
