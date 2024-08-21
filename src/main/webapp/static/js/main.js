document.addEventListener("DOMContentLoaded", () => {
  const ensureSlash = (path) => (path.endsWith("/") ? path : path + "/");
  const rootPathWithSlash = ensureSlash(rootPath);

  // 시군구 데이터
  const districts = {
    서울: [
      "종로구",
      "중구",
      "용산구",
      "성동구",
      "광진구",
      "동대문구",
      "중랑구",
      "성북구",
      "강북구",
      "도봉구",
      "노원구",
      "은평구",
      "서대문구",
      "마포구",
      "양천구",
      "강서구",
      "구로구",
      "금천구",
      "영등포구",
      "동작구",
      "관악구",
      "서초구",
      "강남구",
      "송파구",
      "강동구",
    ],
    대구: ["중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"],
    부산: [
      "중구",
      "서구",
      "동구",
      "영도구",
      "부산진구",
      "동래구",
      "남구",
      "북구",
      "해운대구",
      "사하구",
      "금정구",
      "강서구",
      "연제구",
      "수영구",
      "사상구",
      "기장군",
    ],
    대전: ["동구", "중구", "서구", "유성구", "대덕구"],
    광주: ["동구", "서구", "남구", "북구", "광산구"],
    인천: ["중구", "동구", "미추홀구", "연수구", "남동구", "부평구", "계양구", "서구", "강화군", "옹진군"],
    강원: [
      "춘천시",
      "원주시",
      "강릉시",
      "동해시",
      "태백시",
      "속초시",
      "삼척시",
      "홍천군",
      "횡성군",
      "영월군",
      "평창군",
      "정선군",
      "철원군",
      "화천군",
      "양구군",
      "인제군",
      "고성군",
      "양양군",
    ],
    경기: [
      "수원시",
      "고양시",
      "용인시",
      "성남시",
      "부천시",
      "안산시",
      "화성시",
      "남양주시",
      "평택시",
      "의정부시",
      "파주시",
      "시흥시",
      "김포시",
      "광명시",
      "군포시",
      "광주시",
      "이천시",
      "양주시",
      "오산시",
      "구리시",
      "안성시",
      "포천시",
      "의왕시",
      "하남시",
      "여주시",
      "양평군",
      "동두천시",
      "과천시",
      "연천군",
      "가평군",
    ],
    경남: [
      "창원시",
      "김해시",
      "진주시",
      "양산시",
      "거제시",
      "통영시",
      "사천시",
      "밀양시",
      "거창군",
      "함안군",
      "창녕군",
      "고성군",
      "하동군",
      "합천군",
      "함양군",
      "산청군",
      "의령군",
      "남해군",
    ],
    경북: [
      "포항시",
      "구미시",
      "경주시",
      "경산시",
      "안동시",
      "김천시",
      "영주시",
      "상주시",
      "문경시",
      "영천시",
      "의성군",
      "고령군",
      "성주군",
      "칠곡군",
      "청도군",
      "청송군",
      "영양군",
      "예천군",
      "봉화군",
      "울진군",
      "울릉군",
    ],
    세종: ["세종시"],
    울산: ["중구", "남구", "동구", "북구", "울주군"],
    전남: [
      "목포시",
      "여수시",
      "순천시",
      "나주시",
      "광양시",
      "담양군",
      "곡성군",
      "구례군",
      "고흥군",
      "보성군",
      "화순군",
      "장흥군",
      "강진군",
      "해남군",
      "영암군",
      "무안군",
      "함평군",
      "영광군",
      "장성군",
      "완도군",
      "진도군",
      "신안군",
    ],
    전북: [
      "전주시",
      "군산시",
      "익산시",
      "정읍시",
      "남원시",
      "김제시",
      "완주군",
      "진안군",
      "무주군",
      "장수군",
      "임실군",
      "순창군",
      "고창군",
      "부안군",
    ],
    제주: ["제주시", "서귀포시"],
    충남: [
      "천안시",
      "공주시",
      "보령시",
      "아산시",
      "서산시",
      "논산시",
      "계룡시",
      "당진시",
      "금산군",
      "부여군",
      "서천군",
      "청양군",
      "홍성군",
      "예산군",
      "태안군",
    ],
    충북: ["청주시", "충주시", "제천시", "보은군", "옥천군", "영동군", "증평군", "진천군", "괴산군", "음성군", "단양군"],
  };
  const mapContainer = document.createElement("div");
  mapContainer.style.width = "250px";
  mapContainer.style.height = "250px";
  mapContainer.style.position = "absolute";
  mapContainer.style.display = "none";
  mapContainer.style.zIndex = "1000";
  document.body.appendChild(mapContainer);

  let map = null;
  let marker = null;

  const showMap = (row, event) => {
    const lat = parseFloat(row.getAttribute("data-lat"));
    const lng = parseFloat(row.getAttribute("data-lng"));

    if (!isNaN(lat) && !isNaN(lng)) {
      const position = new kakao.maps.LatLng(lat, lng);

      mapContainer.style.left = `${event.pageX + 10}px`;
      mapContainer.style.top = `${event.pageY + 10}px`;
      mapContainer.style.display = "block";

      setTimeout(() => {
        if (!map) {
          map = new kakao.maps.Map(mapContainer, {
            center: position,
            level: 4,
          });

          marker = new kakao.maps.Marker({
            position: position,
            map: map,
          });
        } else {
          map.setCenter(position);
          marker.setPosition(position);
          map.relayout();
        }

        kakao.maps.event.trigger(map, "resize");
      }, 100);
    }
  };

  const hideMap = () => {
    mapContainer.style.display = "none";
  };

  document.addEventListener("mouseover", (event) => {
    const target = event.target.closest("tr[data-lat][data-lng]");
    if (target) {
      showMap(target, event);
    }
  });

  document.addEventListener("mousemove", (event) => {
    const target = event.target.closest("tr[data-lat][data-lng]");
    if (target) {
      mapContainer.style.left = `${event.pageX + 10}px`;
      mapContainer.style.top = `${event.pageY + 10}px`;
    }
  });

  document.addEventListener("mouseout", (event) => {
    const target = event.target.closest("tr[data-lat][data-lng]");
    if (target) {
      hideMap();
    }
  });

  document.addEventListener("click", (event) => {
    const target = event.target.closest("tr[data-lat][data-lng]");
    if (target) {
      const lat = parseFloat(target.getAttribute("data-lat"));
      const lng = parseFloat(target.getAttribute("data-lng"));

      if (!isNaN(lat) && !isNaN(lng)) {
        // 카카오 지도 URL 생성
        const kakaoMapUrl = `https://map.kakao.com/link/map/${lat},${lng}`;

        // 새 창으로 카카오 지도 열기
        window.open(kakaoMapUrl, "_blank");
      }
    }
  });

  // 커스텀 셀렉트박스
  document.querySelectorAll(".custom-select").forEach((selectElement) => {
    const trigger = selectElement.querySelector(".custom-select-trigger");
    const optionsContainer = selectElement.querySelector(".custom-options");
    const options = selectElement.querySelectorAll(".custom-option");
    const hiddenSelect = selectElement.nextElementSibling;

    // 셀렉트박스 열기
    trigger.addEventListener("click", () => {
      selectElement.classList.toggle("open");
    });

    options.forEach((option) => {
      option.addEventListener("click", () => {
        trigger.querySelector("span").textContent = option.textContent;

        hiddenSelect.value = option.getAttribute("data-value");

        selectElement.classList.remove("open");

        const event = new Event("change");
        hiddenSelect.dispatchEvent(event);
      });
    });
  });

  // 셀렉트박스 닫기
  window.addEventListener("click", (e) => {
    document.querySelectorAll(".custom-select").forEach((selectElement) => {
      if (!selectElement.contains(e.target)) {
        selectElement.classList.remove("open");
      }
    });
  });

  document.addEventListener("click", (event) => {
    const isOption = event.target.classList.contains("custom-option");
    const selectWrapper = event.target.closest(".custom-select");

    if (isOption && selectWrapper) {
      const triggerSpan = selectWrapper.querySelector(".custom-select-trigger span");
      const hiddenSelect = selectWrapper.nextElementSibling;

      triggerSpan.textContent = event.target.textContent;
      hiddenSelect.value = event.target.getAttribute("data-value");
      hiddenSelect.dispatchEvent(new Event("change"));

      selectWrapper.classList.remove("open");
    }

    document.querySelectorAll(".custom-select").forEach((selectElement) => {
      if (!selectElement.contains(event.target)) {
        selectElement.classList.remove("open");
      }
    });
  });

  // 스크롤 스냅
  const snapContainer = document.getElementById("snapContainer");
  const backgrounds = document.querySelectorAll(".background");

  let isScrolling = false;

  const disableScroll = () => {
    snapContainer.addEventListener("wheel", preventScroll, { passive: false });
  };

  const enableScroll = () => {
    snapContainer.removeEventListener("wheel", preventScroll);
  };

  const preventScroll = (event) => {
    event.preventDefault();
  };

  const handleScroll = (event) => {
    if (isScrolling) return;

    disableScroll();

    event.preventDefault();

    const delta = event.deltaY;
    let targetIndex;

    for (let i = 0; i < backgrounds.length; i++) {
      const rect = backgrounds[i].getBoundingClientRect();
      if (rect.top >= 0 && rect.top < window.innerHeight) {
        targetIndex = i;
        break;
      }
    }

    if (delta > 0 && targetIndex < backgrounds.length - 1) {
      targetIndex++;
    } else if (delta < 0 && targetIndex > 0) {
      targetIndex--;
    }

    if (targetIndex !== undefined) {
      isScrolling = true;
      backgrounds[targetIndex].scrollIntoView({ behavior: "smooth" });

      setTimeout(() => {
        isScrolling = false;
        enableScroll();
      }, 500);
    }
  };

  snapContainer.addEventListener("wheel", handleScroll);

  // 시군구 옵션 업데이트
  const updateDistrictOptions = (citySelect, districtSelect, districts) => {
    const city = citySelect.value;
    const customOptions = districtSelect.previousElementSibling.querySelector(".custom-options");
    const triggerSpan = districtSelect.previousElementSibling.querySelector(".custom-select-trigger span");

    districtSelect.innerHTML = '<option value="">시군구 선택</option>';
    customOptions.innerHTML = "";

    if (city && districts[city]) {
      const options = districts[city].map((district) => `<option value="${district}">${district}</option>`).join("");
      districtSelect.innerHTML += options;

      const customOptionsHtml = districts[city].map((district) => `<span class="custom-option" data-value="${district}">${district}</span>`).join("");
      customOptions.innerHTML += customOptionsHtml;
    } else {
      triggerSpan.textContent = "시군구 선택";
    }
  };

  // 모달 닫기
  const modalCloseHandler = (modal) => {
    modal.style.display = "none";
  };

  // 모달 바깥 클릭 시 닫기
  const modalOutsideClickHandler = (event, modal) => {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  };

  // 쓰레기통 검색
  const trashcanCitySelect = document.getElementById("trashcanCitySelect");
  const trashcanDistrictSelect = document.getElementById("trashcanDistrictSelect");
  const trashcanCityDistrictSearchButton = document.getElementById("trashcanCityDistrictSearchButton");
  const trashcanSearchButton = document.getElementById("trashcanSearchButton");
  const trashcanSearchInput = document.getElementById("trashcanSearchInput");
  const trashcanModal = document.getElementById("trashcanModal");
  const trashcanModalResultSection = document.getElementById("trashcanModalResultSection");
  const trashcanCloseModal = trashcanModal.getElementsByClassName("close")[0];

  trashcanCitySelect.addEventListener("change", () => {
    updateDistrictOptions(trashcanCitySelect, trashcanDistrictSelect, districts);
  });

  // 지역 검색
  trashcanCityDistrictSearchButton.addEventListener("click", () => {
    const city = trashcanCitySelect.value;
    const district = trashcanDistrictSelect.value;

    if (!city || !district) {
      alert("시/도 및 시/군/구를 선택해 주세요.");
      return;
    }

    let url = `${rootPathWithSlash}trashcan-search?region=${encodeURIComponent(city + " " + district)}`;

    fetch(url)
      .then((response) => response.text())
      .then((html) => {
        trashcanModalResultSection.innerHTML = html;
        trashcanModal.style.display = "block";
      })
      .catch((error) => {
        console.error("Error fetching trashcan data:", error);
        trashcanModalResultSection.innerHTML = "<p>검색 중 오류가 발생했습니다. 다시 시도해 주세요.</p>";
        trashcanModal.style.display = "block";
      });
  });

  // 검색어로 검색
  trashcanSearchButton.addEventListener("click", () => {
    const query = trashcanSearchInput.value.trim();

    if (!query) {
      alert("검색어를 입력해 주세요.");
      return;
    }

    let url = `${rootPathWithSlash}trashcan-search?search=${encodeURIComponent(query)}`;

    fetch(url)
      .then((response) => response.text())
      .then((html) => {
        trashcanModalResultSection.innerHTML = html;
        trashcanModal.style.display = "block";
      })
      .catch((error) => {
        console.error("Error fetching trashcan data:", error);
        trashcanModalResultSection.innerHTML = "<p>검색 중 오류가 발생했습니다. 다시 시도해 주세요.</p>";
        trashcanModal.style.display = "block";
      });
  });

  trashcanCloseModal.addEventListener("click", () => modalCloseHandler(trashcanModal));
  window.addEventListener("click", (event) => modalOutsideClickHandler(event, trashcanModal));

  // 화장실 검색
  const restroomCitySelect = document.getElementById("restroomCitySelect");
  const restroomDistrictSelect = document.getElementById("restroomDistrictSelect");
  const restroomCityDistrictSearchButton = document.getElementById("restroomCityDistrictSearchButton");
  const restroomSearchButton = document.getElementById("restroomSearchButton");
  const restroomSearchInput = document.getElementById("restroomSearchInput");
  const restroomModal = document.getElementById("restroomModal");
  const restroomModalResultSection = document.getElementById("restroomModalResultSection");
  const restroomCloseModal = restroomModal.getElementsByClassName("close")[0];

  restroomCitySelect.addEventListener("change", () => {
    updateDistrictOptions(restroomCitySelect, restroomDistrictSelect, districts);
  });

  // 지역 검색
  restroomCityDistrictSearchButton.addEventListener("click", () => {
    const city = restroomCitySelect.value;
    const district = restroomDistrictSelect.value;

    if (!city || !district) {
      alert("시/도 및 시/군/구를 선택해 주세요.");
      return;
    }

    let url = `${rootPathWithSlash}restroom-search?roadAddressPart1=${encodeURIComponent(city)}&roadAddressPart2=${encodeURIComponent(district)}`;

    fetch(url)
      .then((response) => response.text())
      .then((html) => {
        restroomModalResultSection.innerHTML = html;
        restroomModal.style.display = "block";
      })
      .catch((error) => {
        console.error("Error fetching restroom data:", error);
        restroomModalResultSection.innerHTML = "<p>검색 중 오류가 발생했습니다. 다시 시도해 주세요.</p>";
        restroomModal.style.display = "block";
      });
  });

  // 검색어로 검색
  restroomSearchButton.addEventListener("click", () => {
    const query = restroomSearchInput.value.trim();

    if (!query) {
      alert("검색어를 입력해 주세요.");
      return;
    }

    let url = `${rootPathWithSlash}restroom-search?search=${encodeURIComponent(query)}`;

    fetch(url)
      .then((response) => response.text())
      .then((html) => {
        restroomModalResultSection.innerHTML = html;
        restroomModal.style.display = "block";
      })
      .catch((error) => {
        console.error("Error fetching restroom data:", error);
        restroomModalResultSection.innerHTML = "<p>검색 중 오류가 발생했습니다. 다시 시도해 주세요.</p>";
        restroomModal.style.display = "block";
      });
  });

  restroomCloseModal.addEventListener("click", () => modalCloseHandler(restroomModal));
  window.addEventListener("click", (event) => modalOutsideClickHandler(event, restroomModal));

  // 와이파이 검색
  const wifiCitySelect = document.getElementById("wifiCitySelect");
  const wifiDistrictSelect = document.getElementById("wifiDistrictSelect");
  const wifiCityDistrictSearchButton = document.getElementById("wifiCityDistrictSearchButton");
  const wifiSearchButton = document.getElementById("wifiSearchButton");
  const wifiSearchInput = document.getElementById("wifiSearchInput");
  const wifiModal = document.getElementById("wifiModal");
  const wifiModalResultSection = document.getElementById("wifiModalResultSection");
  const wifiCloseModal = wifiModal.getElementsByClassName("close")[0];

  wifiCitySelect.addEventListener("change", () => {
    updateDistrictOptions(wifiCitySelect, wifiDistrictSelect, districts);
  });

  // 지역 검색
  wifiCityDistrictSearchButton.addEventListener("click", () => {
    const city = wifiCitySelect.value;
    const district = wifiDistrictSelect.value;

    if (!city || !district) {
      alert("시/도 및 시/군/구를 선택해 주세요.");
      return;
    }

    let url = `${rootPathWithSlash}wifi-search?installationProvince=${encodeURIComponent(city)}&installationCityCounty=${encodeURIComponent(
      district
    )}`;

    fetch(url)
      .then((response) => response.text())
      .then((html) => {
        wifiModalResultSection.innerHTML = html;
        wifiModal.style.display = "block";
      })
      .catch((error) => {
        console.error("Error fetching wifi data:", error);
        wifiModalResultSection.innerHTML = "<p>검색 중 오류가 발생했습니다. 다시 시도해 주세요.</p>";
        wifiModal.style.display = "block";
      });
  });

  // 검색어로 검색
  wifiSearchButton.addEventListener("click", () => {
    const query = wifiSearchInput.value.trim();

    if (!query) {
      alert("검색어를 입력해 주세요.");
      return;
    }

    let url = `${rootPathWithSlash}wifi-search?search=${encodeURIComponent(query)}`;

    fetch(url)
      .then((response) => response.text())
      .then((html) => {
        wifiModalResultSection.innerHTML = html;
        wifiModal.style.display = "block";
      })
      .catch((error) => {
        console.error("Error fetching wifi data:", error);
        wifiModalResultSection.innerHTML = "<p>검색 중 오류가 발생했습니다. 다시 시도해 주세요.</p>";
        wifiModal.style.display = "block";
      });
  });

  wifiCloseModal.addEventListener("click", () => modalCloseHandler(wifiModal));
  window.addEventListener("click", (event) => modalOutsideClickHandler(event, wifiModal));

  document.addEventListener("scroll", () => {
    const scrollIcon = document.getElementById("scrollIcon");
    const isLastPage = window.innerHeight + window.scrollY >= document.body.offsetHeight;

    if (isLastPage) {
      scrollIcon.classList.add("hidden");
    } else {
      scrollIcon.classList.remove("hidden");
    }
  });
});
