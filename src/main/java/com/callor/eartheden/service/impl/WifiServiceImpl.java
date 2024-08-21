package com.callor.eartheden.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.callor.eartheden.models.WifiVO;
import com.callor.eartheden.service.WifiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WifiServiceImpl implements WifiService {

    private final Map<String, List<WifiVO>> cachedWifis = new HashMap<>();
    private final ResourceLoader resourceLoader;
    private final ObjectMapper mapper;
    private final ServletContext servletContext;

    private String normalizeAddressPart(String addressPart) {
        if (addressPart == null)
            return "";
        return addressPart.replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
                .replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산")
                .replace("서울특별시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천")
                .replace("전라남도", "전남").replace("전라북도", "전북").replace("제주특별자치도", "제주").replace("충청남도", "충남")
                .replace("충청북도", "충북").trim();
    }

    public WifiServiceImpl(ResourceLoader resourceLoader, ServletContext servletContext) throws IOException {
        this.resourceLoader = resourceLoader;
        this.mapper = new ObjectMapper();
        this.servletContext = servletContext;
        loadAllWifis();
    }

    private void loadAllWifis() throws IOException {
        String[] fileNames = { 
            "무료와이파이정보강원.json", "무료와이파이정보경기.json", "무료와이파이정보경남.json", 
            "무료와이파이정보경북.json", "무료와이파이정보광주.json", "무료와이파이정보대구.json", 
            "무료와이파이정보대전.json", "무료와이파이정보부산.json", "무료와이파이정보서울.json",
            "무료와이파이정보세종.json", "무료와이파이정보울산.json", "무료와이파이정보인천.json", 
            "무료와이파이정보전남.json", "무료와이파이정보전북.json", "무료와이파이정보제주.json", 
            "무료와이파이정보충남.json", "무료와이파이정보충북.json" 
        };

        for (String fileName : fileNames) {
            List<WifiVO> wifis = loadWifisFromFile(fileName);
            cachedWifis.put(fileName, wifis);
        }
    }

    private List<WifiVO> loadWifisFromFile(String fileName) {
        try {
            String resourcePath = "file:" + servletContext.getRealPath("/static/json/") + fileName;
            Resource resource = resourceLoader.getResource(resourcePath);
            return mapper.readValue(resource.getInputStream(), new TypeReference<List<WifiVO>>() {
            });
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public List<WifiVO> getWifisByInstallationLocation(String location, String rootPath) throws IOException {
        final String normalizedLocation = normalizeAddressPart(location);
        return cachedWifis.values().stream().flatMap(List::stream)
            .filter(wifi -> normalizeAddressPart(wifi.getInstallationProvince() + " " + wifi.getInstallationCityCounty())
                .equalsIgnoreCase(normalizedLocation))
            .collect(Collectors.toList());
    }

    @Override
    public List<WifiVO> searchWifis(String query, String rootPath) throws IOException {
        return cachedWifis.values().stream().flatMap(List::stream)
            .filter(wifi -> wifi.getInstallationPlaceName().contains(query))
            .collect(Collectors.toList());
    }
}
