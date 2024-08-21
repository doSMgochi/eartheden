package com.callor.eartheden.service;

import java.io.IOException;
import java.util.List;

import com.callor.eartheden.models.TrashCanVO;

public interface TrashCanService {
	List<TrashCanVO> getTrashCansByRegion(String region, String rootPath) throws IOException;

	List<TrashCanVO> searchTrashCans(String query, String rootPath) throws IOException;
}
