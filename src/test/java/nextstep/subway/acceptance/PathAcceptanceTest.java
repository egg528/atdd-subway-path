package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 조회 기능")
public class PathAcceptanceTest extends AcceptanceTest{


    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 범계역;

    /**
     * Given
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        범계역 = 지하철역_생성_요청("범계역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "red").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "orange").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(교대역, 남부터미널역, 4));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 5));
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 강남역, 10));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 교대역, 2));
    }

    /**
     * Given - 지하철 노선/구간/역을 생성하고
     * When - 출발역, 도착역을 지정하여 경로를 조회하면
     * Then - 경로 사이의 역들과 경로 거리를 반환 받을 수 있다.
     */
    @DisplayName("지하철 경로 조회")
    @Test
    void searchPath(){
        // when

        // then

    }

    /**
     * Given - 지하철 노선/구간/역을 생성하고
     * When - 츨빌역과 도착역을 같게 지정하여 경로를 조회하면
     * Then - 예외를 반환한다.
     */
    @DisplayName("지하철 경로 조회: 출발역 = 도착역")
    @Test
    void searchPathWithSourceSameAsTarget(){
        // when

        // then

    }

    /**
     * Given - 지하철 노선/구간/역을 생성하고
     * When - 연결되지 않은 출발역과 도착역을 지정하고 경로를 조회하면
     * Then - 예외를 반환한다.
     */
    @DisplayName("지하철 경로 조회: 출발역과 도착역 연결 X")
    @Test
    void searchPathWithSourceTargetDisconnected(){
        // when

        // then

    }

    /**
     * Given - 지하철 노선/구간/역을 생성하고
     * When - 존재하지 않는 역을 지정하고 경로를 조회하면
     * Then - 예외를 반환한다.
     */
    @DisplayName("지하철 경로 조회: 출발역이 존재하지 않음")
    @Test
    void searchPathFailWithNotExistStation(){
        // when

        // then

    }

    private Map<String, String> createSectionCreateParams(
            Long upStationId, Long downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
