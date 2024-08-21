package com.callor.eartheden.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestroomVO {

	@JsonProperty("번호")
	private int number;

	@JsonProperty("구분")
	private String category;

	@JsonProperty("근거")
	private String basis;

	@JsonProperty("화장실명")
	private String toiletName;

	@JsonProperty("소재지도로명주소")
	private String roadAddress;

	@JsonProperty("소재지지번주소")
	private String parcelAddress;

	@JsonProperty("남성용-대변기수")
	private int maleToilets;

	@JsonProperty("남성용-소변기수")
	private int maleUrinals;

	@JsonProperty("남성용-장애인용대변기수")
	private int maleDisabledToilets;

	@JsonProperty("남성용-장애인용소변기수")
	private int maleDisabledUrinals;

	@JsonProperty("남성용-어린이용대변기수")
	private int maleChildToilets;

	@JsonProperty("남성용-어린이용소변기수")
	private int maleChildUrinals;

	@JsonProperty("여성용-대변기수")
	private int femaleToilets;

	@JsonProperty("여성용-장애인용대변기수")
	private int femaleDisabledToilets;

	@JsonProperty("여성용-어린이용대변기수")
	private int femaleChildToilets;

	@JsonProperty("관리기관명")
	private String managementAgency;

	@JsonProperty("전화번호")
	private String phoneNumber;

	@JsonProperty("개방시간")
	private String openHours;

	@JsonProperty("개방시간상세")
	private String detailedOpenHours;

	@JsonProperty("설치연월")
	private String installationDate;

	@JsonProperty("WGS84위도")
	private Double latitude;

	@JsonProperty("WGS84경도")
	private Double longitude;

	@JsonProperty("화장실소유구분")
	private String ownershipType;

	@JsonProperty("오물처리방식")
	private String wasteDisposalMethod;

	@JsonProperty("안전관리시설설치대상여부")
	private String safetyFacilityInstallation;

	@JsonProperty("비상벨설치여부")
	private String emergencyBellInstalled;

	@JsonProperty("비상벨설치장소")
	private String emergencyBellLocation;

	@JsonProperty("화장실입구CCTV설치유무")
	private String cctvAtEntrance;

	@JsonProperty("기저귀교환대유무")
	private String diaperChangingTable;

	@JsonProperty("기저귀교환대장소")
	private String diaperChangingTableLocation;

	@JsonProperty("리모델링연월")
	private String remodelingDate;

	@JsonProperty("데이터기준일자")
	private String dataReferenceDate;
}
