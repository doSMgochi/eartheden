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
public class WifiVO {

    @JsonProperty("번호")
    private int number;

    @JsonProperty("설치장소명")
    private String installationPlaceName;

    @JsonProperty("설치장소상세")
    private String installationPlaceDetail;

    @JsonProperty("설치시도명")
    private String installationProvince;

    @JsonProperty("설치시군구명")
    private String installationCityCounty;

    @JsonProperty("설치시설구분")
    private String facilityType;

    @JsonProperty("서비스제공사명")
    private String serviceProvider;

    @JsonProperty("와이파이SSID")
    private String wifiSSID;

    @JsonProperty("설치연월")
    private String installationDate;

    @JsonProperty("소재지도로명주소")
    private String roadAddress;

    @JsonProperty("소재지지번주소")
    private String parcelAddress;

    @JsonProperty("관리기관명")
    private String managementAgency;

    @JsonProperty("관리기관전화번호")
    private String managementPhoneNumber;

    @JsonProperty("WGS84위도")
    private Double latitude;

    @JsonProperty("WGS84경도")
    private Double longitude;

    @JsonProperty("데이터기준일자")
    private String dataReferenceDate;
}
