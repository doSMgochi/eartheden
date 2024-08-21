package com.callor.eartheden.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callor.eartheden.config.ApiKeyProvider;
import com.callor.eartheden.models.RestroomVO;
import com.callor.eartheden.models.TrashCanVO;
import com.callor.eartheden.models.WifiVO;
import com.callor.eartheden.service.RestroomService;
import com.callor.eartheden.service.TrashCanService;
import com.callor.eartheden.service.WifiService;

@Controller
public class HomeController {

	private final TrashCanService trashCanService;
	private final RestroomService restroomService;
	private final WifiService wifiService;
	private final ServletContext servletContext;

	public HomeController(TrashCanService trashCanService, RestroomService restroomService, WifiService wifiService,
			ServletContext servletContext) {
		this.trashCanService = trashCanService;
		this.restroomService = restroomService;
		this.wifiService = wifiService;
		this.servletContext = servletContext;
	}

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("region", "서울");
		model.addAttribute("rootPath", servletContext.getContextPath());
		model.addAttribute("kakaoApiKey", ApiKeyProvider.getKakaoApiKey());
		return "home";
	}

	@RequestMapping("/trashcan-search")
	public String trashcanSearch(@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "search", required = false) String search, Model model) {

		List<TrashCanVO> trashCans = new ArrayList<>();

		try {
			if (search != null && !search.isEmpty()) {
				trashCans = trashCanService.searchTrashCans(search, servletContext.getContextPath());
				model.addAttribute("searchKeyword", search);
			} else if (region != null && !region.isEmpty()) {
				trashCans = trashCanService.getTrashCansByRegion(region, servletContext.getContextPath());
			}
		} catch (IOException e) {
			model.addAttribute("errorMessage", "데이터를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
			return "fragments/trashcans";
		}

		if (trashCans.isEmpty()) {
			model.addAttribute("noResults", true);
		} else {
			model.addAttribute("noResults", false);
			model.addAttribute("trashCans", trashCans);
		}
		model.addAttribute("region", region);
		return "fragments/trashcans";
	}

	@RequestMapping("/restroom-search")
	public String restroomSearch(@RequestParam(value = "roadAddressPart1", required = false) String roadAddressPart1,
			@RequestParam(value = "roadAddressPart2", required = false) String roadAddressPart2,
			@RequestParam(value = "search", required = false) String search, Model model) {

		List<RestroomVO> restrooms = new ArrayList<>();

		try {
			if (search != null && !search.isEmpty()) {
				restrooms = restroomService.searchRestrooms(search, servletContext.getContextPath());
				model.addAttribute("searchKeyword", search);
			} else if (roadAddressPart1 != null && !roadAddressPart1.isEmpty() && roadAddressPart2 != null
					&& !roadAddressPart2.isEmpty()) {
				restrooms = restroomService.getRestroomsByRoadAddress(roadAddressPart1, roadAddressPart2,
						servletContext.getContextPath());
			}
		} catch (IOException e) {
			model.addAttribute("errorMessage", "데이터를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
			return "fragments/restrooms";
		}

		if (restrooms.isEmpty()) {
			model.addAttribute("noResults", true);
		} else {
			model.addAttribute("noResults", false);
			model.addAttribute("restrooms", restrooms);
		}
		model.addAttribute("roadAddressPart1", roadAddressPart1);
		model.addAttribute("roadAddressPart2", roadAddressPart2);
		return "fragments/restrooms";
	}

	@RequestMapping("/wifi-search")
	public String wifiSearch(
			@RequestParam(value = "installationProvince", required = false) String installationProvince,
			@RequestParam(value = "installationCityCounty", required = false) String installationCityCounty,
			@RequestParam(value = "search", required = false) String search, Model model) {

		List<WifiVO> wifis = new ArrayList<>();

		try {
			if (search != null && !search.isEmpty()) {
				wifis = wifiService.searchWifis(search, servletContext.getContextPath());
				model.addAttribute("searchKeyword", search);
			} else if (installationProvince != null && !installationProvince.isEmpty() && installationCityCounty != null
					&& !installationCityCounty.isEmpty()) {
				String location = installationProvince + " " + installationCityCounty;
				wifis = wifiService.getWifisByInstallationLocation(location, servletContext.getContextPath());
			}
		} catch (IOException e) {
			model.addAttribute("errorMessage", "데이터를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
			return "fragments/wifis";
		}

		if (wifis.isEmpty()) {
			model.addAttribute("noResults", true);
		} else {
			model.addAttribute("noResults", false);
			model.addAttribute("wifis", wifis);
		}

		model.addAttribute("installationProvince", installationProvince);
		model.addAttribute("installationCityCounty", installationCityCounty);

		return "fragments/wifis";
	}
}
