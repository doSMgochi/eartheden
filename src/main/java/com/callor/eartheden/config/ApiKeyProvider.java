package com.callor.eartheden.config;

public class ApiKeyProvider {
    
    // Kakao API Key를 저장
    private static final String KAKAO_API_KEY = "ee1d9d0c652e5a9a2f0fef6528481604";

    // Kakao API Key를 반환하는 메서드
    public static String getKakaoApiKey() {
        return KAKAO_API_KEY;
    }
}