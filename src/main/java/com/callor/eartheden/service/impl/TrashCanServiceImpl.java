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

import com.callor.eartheden.models.TrashCanVO;
import com.callor.eartheden.service.TrashCanService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TrashCanServiceImpl implements TrashCanService {
	
	private final Map<String, List<TrashCanVO>> cachedTrashCans = new HashMap<>();
	private final ResourceLoader resourceLoader;
	private final ObjectMapper mapper;
	private final ServletContext servletContext;

	public TrashCanServiceImpl(ResourceLoader resourceLoader, ServletContext servletContext) throws IOException {
		this.resourceLoader = resourceLoader;
		this.mapper = new ObjectMapper();
		this.servletContext = servletContext;
		loadAllTrashCans();
	}

	private void loadAllTrashCans() throws IOException {
		String[] fileNames = { "쓰레기통설치정보서울.json", "쓰레기통설치정보광주.json", "쓰레기통설치정보대구남구.json", "쓰레기통설치정보대구달서구.json",
				"쓰레기통설치정보대구달성군.json", "쓰레기통설치정보대구동구.json", "쓰레기통설치정보대구서구.json", "쓰레기통설치정보대구수성구.json",
				"쓰레기통설치정보대구중구.json", "쓰레기통설치정보대전대덕구.json", "쓰레기통설치정보대전동구.json", "쓰레기통설치정보대전서구.json",
				"쓰레기통설치정보대전중구.json", "쓰레기통설치정보부산금정구.json", "쓰레기통설치정보부산남구.json", "쓰레기통설치정보부산사상구.json",
				"쓰레기통설치정보부산서구.json", "쓰레기통설치정보부산수영구.json", "쓰레기통설치정보부산연제구.json", "쓰레기통설치정보부산해운대구.json",
				"쓰레기통설치정보인천남동구.json" };

		for (String fileName : fileNames) {
			List<TrashCanVO> trashCans = loadTrashCansFromFile(fileName);
			cachedTrashCans.put(fileName, trashCans);
		}
	}

	private List<TrashCanVO> loadTrashCansFromFile(String fileName) {
		try {
			String resourcePath = "file:" + servletContext.getRealPath("/static/json/") + fileName;

			Resource resource = resourceLoader.getResource(resourcePath);
			List<TrashCanVO> trashCans = mapper.readValue(resource.getInputStream(),
					new TypeReference<List<TrashCanVO>>() {
					});

			return trashCans;
		} catch (IOException e) {
			System.err.println("Error loading file: " + fileName + " - " + e.getMessage());
			return List.of(); 
		}
	}

	@Override
	public List<TrashCanVO> getTrashCansByRegion(String region, String rootPath) {

	    List<TrashCanVO> filteredList;
	    
	    if ("서울 전체".equalsIgnoreCase(region) || "광주 전체".equalsIgnoreCase(region)) {
	        filteredList = cachedTrashCans.values().stream()
	                                      .flatMap(List::stream)
	                                      .filter(trashCan -> trashCan.getRegion() != null && trashCan.getRegion().startsWith(region.replace(" 전체", "")))
	                                      .collect(Collectors.toList());
	    } else if (region.endsWith("전체")) {
	        String city = region.replace(" 전체", "");
	        filteredList = cachedTrashCans.values().stream()
	                                      .flatMap(List::stream)
	                                      .filter(trashCan -> trashCan.getRegion() != null && trashCan.getRegion().startsWith(city))
	                                      .collect(Collectors.toList());
	    } else {
	        filteredList = cachedTrashCans.values().stream()
	                                      .flatMap(List::stream)
	                                      .filter(trashCan -> trashCan.getRegion() != null && trashCan.getRegion().equalsIgnoreCase(region))
	                                      .collect(Collectors.toList());
	    }

	    return filteredList;
	}

	@Override
	public List<TrashCanVO> searchTrashCans(String query, String rootPath) {
		List<TrashCanVO> filteredList = cachedTrashCans.values().stream().flatMap(List::stream).filter(trashCan -> {
			boolean matches = trashCan.getLocation().contains(query);
			return matches;
		}).collect(Collectors.toList());
		return filteredList;
	}
}
