package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.fake.FakeLineRepository;
import nextstep.subway.fake.FakeStationRepository;
import nextstep.subway.fixture.LineFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
public class LineServiceFakeTest {
    private StationRepository stationRepository = new FakeStationRepository();
    private LineRepository lineRepository = new FakeLineRepository();
    private StationService stationService = new StationService(stationRepository);
    private LineService lineService = new LineService(lineRepository, stationService);


    private Station 강남역;
    private Station 역삼역;
    private Station 잠실역;
    private Station 판교역;
    private Line 신분당선;
    private Line 분당선;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        잠실역 = stationRepository.save(new Station("잠실역"));
        판교역 = stationRepository.save(new Station("판교역"));
        신분당선 = lineRepository.save(new Line("신분당선", "red", new Section(강남역, 역삼역, 10)));
        분당선 = lineRepository.save(new Line("분당선", "red", new Section(역삼역, 강남역, 10)));
    }


    @Test
    @DisplayName("노선을 생성한다.")
    void saveLine() {
        // given
        Station 강남역 = stationRepository.save(LineFixture.강남역);
        Station 역삼역 = stationRepository.save(LineFixture.역삼역);
        String 신분당선 = "신분당선";
        String 색 = "red";

        LineRequest request = new LineRequest(신분당선, 색, 강남역.getId(), 역삼역.getId(), 10);
        LineResponse line = lineService.saveLine(request);

        assertAll(
            () -> assertThat(line.getName()).isEqualTo(신분당선),
            () -> assertThat(line.getColor()).isEqualTo(색),
            () -> assertThat(line.getStations()).hasSize(2)
        );
    }


    @Test
    @DisplayName("역 목록을 보여준다.")
    void showLines() {
        // when
        List<LineResponse> response = lineService.showLines();

        // then
        assertAll(
            () -> assertThat(response).hasSize(lineRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("식별자를 이용해 노선을 조회한다.")
    void findById() {
        // when
        LineResponse response = lineService.findById(신분당선.getId());

        // then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(신분당선.getName()),
            () -> assertThat(response.getColor()).isEqualTo(신분당선.getColor())
        );
    }

    @Test
    @DisplayName("식별자로 노선을 조회하지 못하는 경우 예외가 발생한다.")
    void findByIdValidation1() {
        // given
        long 존재하지않는_ID = 3000L;

        // when & then
        assertThatThrownBy(
            () -> lineService.findById(존재하지않는_ID)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선의 이름과 색을 수정한다.")
    void updateLine() {
        // given
        String 초당선 = "초당선";
        String black = "black";
        lineService.updateLine(신분당선.getId(), new LineRequest(초당선, black, null, null, 0));

        // when
        LineResponse response = lineService.findById(신분당선.getId());

        // then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(초당선),
            () -> assertThat(response.getColor()).isEqualTo(black)
        );
    }

    @Test
    @DisplayName("노선의 이름과 색을 수정할 때, 식별자로 노선을 조회하지 못하는 경우 예외가 발생한다.")
    void updateLineValidation1() {
        // given
        long 존재하지않는_ID = 3000L;
        String 초당선 = "초당선";
        String black = "black";

        // when & then
        assertThatThrownBy(
            () -> lineService.updateLine(존재하지않는_ID, new LineRequest(초당선, black, null, null, 0))
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void deleteLine() {
        // when
        lineService.deleteLine(신분당선.getId());

        // then
        assertThat(lineRepository.findById(신분당선.getId())).isEmpty();
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void addSection1() {
        // given
        int 신분당선_구간_개수 = 신분당선.getSections().getSections().size();

        // when
        SectionRequest 구간_등록_요청 = new SectionRequest(역삼역.getId(), 잠실역.getId(), 10);
        lineService.addSection(신분당선.getId(), 구간_등록_요청);

        // then
        Line line = lineRepository.findById(신분당선.getId()).get();
        assertAll(
            () -> assertThat(line.getSections().getSections()).hasSize(신분당선_구간_개수 + 1)
        );
    }

    @Test
    @DisplayName("중간 구간을 추가한다.")
    void addSection2() {
        // given
        Section 역삼역_잠실역 = new Section(역삼역, 잠실역, 10);
        신분당선.getSections().addSection(역삼역_잠실역);
        int 신분당선_구간_개수 = 신분당선.getSections().getSections().size();
        // when
        SectionRequest 구간_등록_요청 = new SectionRequest(판교역.getId(), 역삼역.getId(), 3);
        lineService.addSection(신분당선.getId(), 구간_등록_요청);

        // then
        Line line = lineRepository.findById(신분당선.getId()).orElseThrow(IllegalArgumentException::new);

        assertAll(
            () -> assertThat(line.getSections().getSections()).hasSize(신분당선_구간_개수 + 1)
        );
    }

    @Test
    @DisplayName("상행 구간을 추가한다.")
    void addSection3() {
        // given
        Section 역삼역_잠실역 = new Section(역삼역, 잠실역, 10);
        신분당선.getSections().addSection(역삼역_잠실역);
        int 신분당선_구간_개수 = 신분당선.getSections().getSections().size();
        // when
        SectionRequest 구간_등록_요청 = new SectionRequest(판교역.getId(), 강남역.getId(), 3);
        lineService.addSection(신분당선.getId(), 구간_등록_요청);

        // then
        Line line = lineRepository.findById(신분당선.getId()).orElseThrow(IllegalArgumentException::new);

        assertAll(
            () -> assertThat(line.getSections().getSections()).hasSize(신분당선_구간_개수 + 1)
        );
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void deleteSection() {
        // given
        신분당선.getSections().addSection(new Section(역삼역, 잠실역, 10));
        int 신분당선_구간_개수 = 신분당선.getSections().getSections().size();

        // when
        lineService.deleteSection(신분당선.getId(), 잠실역.getId());

        // then
        Line line = lineRepository.findById(신분당선.getId()).get();
        assertAll(
            () -> assertThat(line.getSections().getSections()).hasSize(신분당선_구간_개수 - 1)
        );
    }

    @Test
    @DisplayName("구간을_순서대로_조회한다")
    void getSections() {
        // given
        Section 역삼역_잠실역 = new Section(역삼역, 잠실역, 10);
        Section 판교역_역삼역 = new Section(판교역, 역삼역, 3);
        신분당선.getSections().addSection(역삼역_잠실역);
        신분당선.getSections().addSection(판교역_역삼역);

        // when
        List<String> 역_이름_목록 = lineService.findById(신분당선.getId()).getStations().stream().map(StationResponse::getName).collect(Collectors.toList());

        // then
        assertAll(
            () -> assertThat(역_이름_목록).containsExactly("강남역", "판교역", "역삼역", "잠실역")
        );
    }
}