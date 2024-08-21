package com.callor.eartheden.service;

import java.io.IOException;
import java.util.List;

import com.callor.eartheden.models.WifiVO;

public interface WifiService {
	List<WifiVO> searchWifis(String query, String rootPath) throws IOException;

	List<WifiVO> getWifisByInstallationLocation(String location, String rootPath) throws IOException;
}