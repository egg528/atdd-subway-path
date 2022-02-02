package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.utils.RestAssuredCRUD;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssuredCRUD.get("/lines");
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        return RestAssured
                .given().log().all()
                .when().get(createResponse.header("location"))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssuredCRUD.get("lines/{id}", id);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssuredCRUD.postRequest("/lines", lineRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssuredCRUD.postRequest("/lines/{lineId}/sections", sectionRequest, lineId);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_수정한다(Long lineId, LineRequest lineRequest) {
        return RestAssuredCRUD.putRequest("/lines/{lineId}", lineRequest, lineId);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_역으로_구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssuredCRUD.delete("/lines/"+lineId+"/sections", "stationId", stationId);
    }
}