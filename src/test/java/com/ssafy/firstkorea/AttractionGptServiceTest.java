package com.ssafy.firstkorea;

import com.ssafy.firskorea.FirstTimeInKoreaApplication;
import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.service.AttractionGptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;


@SpringBootTest(classes = FirstTimeInKoreaApplication.class)
@Slf4j
class AttractionGptServiceTest {
    private final int MAX_CONTENT_ID = 5015034;

    @Autowired
    AttractionGptServiceImpl attractionGptService;

    @Test
    void notNullTest() {
        Assertions.assertThat(attractionGptService).isNotNull();
    }

    @Test
    void simpleTest() throws SQLException {

        //kcurture의 contentId를 5000000 ~ 5015034까지 본다.
        // 2972 이전에는 이상한 번역이 많아서 확인해보기
        for (int idx = 5000001; idx < MAX_CONTENT_ID; idx++) {
            try {
                attractionGptService.getAttractionDetailWithGptApi(new MemberContentDto("qwer", String.valueOf(idx)));
            } catch (Exception e) {
                log.error("Error processing attraction: {}", e.getMessage(), e);
            }
        }

    }

    boolean checkIdValid(String contentId, int idx) {
        return (Integer.parseInt(contentId) + idx) <= MAX_CONTENT_ID;
    }


}
