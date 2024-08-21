package com.callor.eartheden.config;

public class ApiKeyProviderSample {
    
    // Kakao API Key를 저장
    private static final String KAKAO_API_KEY = "YOUR_KAKAO_API_KEY";

    // Kakao API Key를 반환하는 메서드
    public static String getKakaoApiKey() {
        return KAKAO_API_KEY;
    }
}