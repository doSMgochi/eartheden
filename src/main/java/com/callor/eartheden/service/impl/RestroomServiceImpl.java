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

import com.callor.eartheden.models.RestroomVO;
import com.callor.eartheden.service.RestroomService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestroomServiceImpl implements RestroomService {

	private final Map<String, List<RestroomVO>> cachedRestrooms = new HashMap<>();
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

	public RestroomServiceImpl(ResourceLoader resourceLoader, ServletContext servletContext) throws IOException {
		this.resourceLoader = resourceLoader;
		this.mapper = new ObjectMapper();
		this.servletContext = servletContext;
		loadAllRestrooms();
	}

	private void loadAllRestrooms() throws IOException {
		String[] fileNames = { "공중화장실정보강원.json", "공중화장실정보경기.json", "공중화장실정보경남.json", "공중화장실정보경북.json", "공중화장실정보광주.json",
				"공중화장실정보대구.json", "공중화장실정보대전.json", "공중화장실정보부산.json", "공중화장실정보서울.json", "공중화장실정보세종.json",
				"공중화장실정보울산.json", "공중화장실정보인천.json", "공중화장실정보전남.json", "공중화장실정보전북.json", "공중화장실정보제주.json",
				"공중화장실정보충남.json", "공중화장실정보충북.json" };

		for (String fileName : fileNames) {
			List<RestroomVO> restrooms = loadRestroomsFromFile(fileName);
			cachedRestrooms.put(fileName, restrooms);
		}
	}

	private List<RestroomVO> loadRestroomsFromFile(String fileName) {
		try {
			String resourcePath = "file:" + servletContext.getRealPath("/static/json/") + fileName;
			Resource resource = resourceLoader.getResource(resourcePath);
			return mapper.readValue(resource.getInputStream(), new TypeReference<List<RestroomVO>>() {
			});
		} catch (IOException e) {
			return List.of();
		}
	}

	@Override
	public List<RestroomVO> getRestroomsByRoadAddress(String roadAddressPart1, String roadAddressPart2,
			String rootPath) {
		final String normalizedRoadAddressPart1 = normalizeAddressPart(roadAddressPart1);
		final String normalizedRoadAddressPart2 = normalizeAddressPart(roadAddressPart2);

		return cachedRestrooms.values().stream().flatMap(List::stream).filter(restroom -> {
			boolean matches = false;

			if (normalizedRoadAddressPart1.equals("세종")) {
				matches = restroom.getRoadAddress() != null
						&& normalizeAddressPart(restroom.getRoadAddress()).startsWith("세종");
				if (!matches) {
					matches = restroom.getParcelAddress() != null
							&& normalizeAddressPart(restroom.getParcelAddress()).startsWith("세종");
				}
			} else {
				if (restroom.getRoadAddress() != null) {
					String[] roadAddressParts = normalizeAddressPart(restroom.getRoadAddress()).split("\\s+");
					if (roadAddressParts.length >= 2) {
						matches = roadAddressParts[0].equalsIgnoreCase(normalizedRoadAddressPart1)
								&& roadAddressParts[1].equalsIgnoreCase(normalizedRoadAddressPart2);
					}
				}

				if (!matches && restroom.getParcelAddress() != null) {
					String[] parcelAddressParts = normalizeAddressPart(restroom.getParcelAddress()).split("\\s+");
					if (parcelAddressParts.length >= 2) {
						matches = parcelAddressParts[0].equalsIgnoreCase(normalizedRoadAddressPart1)
								&& parcelAddressParts[1].equalsIgnoreCase(normalizedRoadAddressPart2);
					}
				}
			}

			return matches;
		}).collect(Collectors.toList());
	}

	@Override
	public List<RestroomVO> searchRestrooms(String query, String rootPath) {
		return cachedRestrooms.values().stream().flatMap(List::stream)
				.filter(restroom -> restroom.getToiletName() != null && restroom.getToiletName().contains(query))
				.collect(Collectors.toList());
	}
}
