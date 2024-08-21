package com.callor.eartheden.service;

import java.io.IOException;
import java.util.List;

import com.callor.eartheden.models.RestroomVO;

public interface RestroomService {
	public List<RestroomVO> getRestroomsByRoadAddress(String roadAddressPart1, String roadAddressPart2, String rootPath) throws IOException;
    List<RestroomVO> searchRestrooms(String query, String rootPath) throws IOException;
}