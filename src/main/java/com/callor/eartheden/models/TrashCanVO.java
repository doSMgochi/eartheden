package com.callor.eartheden.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrashCanVO {
	@JsonProperty("번호")
	private int id;

	@JsonProperty("위치")
	private String location;

	@JsonProperty("위도")
	private String latitude;

	@JsonProperty("경도")
	private String longitude;

	@JsonProperty("쓰레기통 종류")
	private String binType;
	
	private String region;
}
