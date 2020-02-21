package atdd.station.domain;

import atdd.line.domain.Line;
import atdd.line.domain.LineTest;
import atdd.line.domain.TimeTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StationTest {

    private final String name = "name!!";
    private final Line line = LineTest.create(654L, "line!!!", TimeTable.MAX_INTERVAL_TIME_TABLE, 0);

    private final Station station1 = create(143L, "name11");
    private final Station station2 = create(144L, "name22");
    private final Station station3 = create(145L, "name33");

    private final Duration duration = new Duration(LocalTime.MAX);
    private final double distance = 1.5;

    @Test
    void create()  {
        final Station station = Station.create(name);

        assertThat(station.getName()).isEqualTo(name);
    }

    @DisplayName("name 은 빈값이나 null 일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNullAndEmptyName(String name) {
        assertThatThrownBy(() -> Station.create(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name은 필수 입니다.");
    }

    @Test
    void addLine() {
        final Station station = create(143L, name);

        station.addLine(line);

        final List<Line> lines = station.getLines();

        assertThat(lines).hasSize(1);

        final Line addedLine = lines.get(0);
        assertThat(addedLine.getId()).isEqualTo(line.getId());
        assertThat(addedLine.getName()).isEqualTo(line.getName());
    }

    @DisplayName("addLine - 동일한 이름의 line 을 중복 추가시 에러")
    @Test
    void addLineBySameName() {

        final Station station = create(143L, name);

        station.addLine(line);

        assertThatThrownBy(() -> station.addLine(line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 line 입니다. lineName : [line!!!]");
    }

    @Test
    void isEqualStation() throws Exception {
        final Station station = create(143L, name);

        assertThat(station.isEqualStation(station.getId())).isTrue();
        assertThat(station.isEqualStation(station.getId() + 1)).isFalse();
    }

    @Test
    void addNextStation() throws Exception {
        final Station station = create(143L, name);
        final Station nextStation = create(143L, "name2222");
        final Duration duration = new Duration(LocalTime.MAX);
        final double distance = 1;

        station.addNextStation(line, nextStation, duration, distance);

        final Set<Station> nextStations = station.getSameLineNextStations(line);
        assertThat(nextStations).hasSize(1);
        assertThat(CollectionUtils.firstElement(nextStations)).isEqualTo(nextStation);
    }

    @DisplayName("addNextStation - 다음역이 이미 등록되어 있으면 에러")
    @Test
    void addNextStationExistNextStation() throws Exception {
        final Station station = create(143L, name);
        final Station nextStation = create(143L, "name2222");
        final Duration duration = new Duration(LocalTime.MAX);
        final double distance = 1;

        station.addNextStation(line, nextStation, duration, distance);


        assertThatThrownBy(() -> station.addNextStation(line, nextStation, duration, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 다음역이 존재합니다. lineName : [line!!!], nextStationName : [name2222]");
    }

    @DisplayName("getNextStation - 입력받은 line 과 같은 nextStation 이 없으면 empty")
    @Test
    void getNextStations() throws Exception {
        final Station station = create(143L, name);
        final Line line1 = LineTest.create(654L, "line!!!", TimeTable.MAX_INTERVAL_TIME_TABLE, 0);
        final Line line2 = LineTest.create(655L, "line222", TimeTable.MAX_INTERVAL_TIME_TABLE, 0);
        final Station nextStation = create(143L, "name2222");
        final Duration duration = new Duration(LocalTime.MAX);
        final double distance = 1;
        station.addNextStation(line1, nextStation, duration, distance);


        final Set<Station> nextStations = station.getSameLineNextStations(line2);


        assertThat(nextStations).isEmpty();
    }

    @Test
    void deleteSection() throws Exception {
        station1.addNextStation(line, station2, duration, distance);
        station1.addNextStation(line, station3, duration, distance);

        station1.deleteSection(line, station2);

        final Set<Station> nextStations = station1.getSameLineNextStations(line);
        assertThat(nextStations).hasSize(1);
        final Station nextStation = CollectionUtils.firstElement(nextStations);
        assertThat(nextStation).isEqualTo(station3);
    }

    @DisplayName("delete - 존재하지 않는 역을 삭제하면 에러")
    @Test
    void deleteSectionNotExist() throws Exception {
        assertThatThrownBy(() -> station1.deleteSection(line, station3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 다음역이 없습니다. currentStation : [name11], lineName : [line!!!], stationName : [name33]");
    }

    @Test
    void getDuration() throws Exception {
        station1.addNextStation(line, station2, duration, distance);

        final Duration result = station1.getDuration(line, station2);

        assertThat(result).isEqualTo(duration);
    }

    @DisplayName("getDuration - 등록된 구간이 없으면 에러")
    @Test
    void getDurationNotExist() throws Exception {
        assertThatThrownBy(() -> station1.getDuration(line, station3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 구간이 없습니다. currentStation : [name11], lineName : [line!!!], stationName : [name33]");
    }

    @Test
    void getDistance() throws Exception {
        station1.addNextStation(line, station2, duration, distance);

        final double result = station1.getDistance(line, station2);

        assertThat(result).isEqualTo(distance);
    }

    @DisplayName("getDistance - 등록된 구간이 없으면 에러")
    @Test
    void getDistanceNotExist() throws Exception {
        assertThatThrownBy(() -> station1.getDistance(line, station3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 구간이 없습니다. currentStation : [name11], lineName : [line!!!], stationName : [name33]");
    }

    public static Station create(Long id, String name) {
        Station station = new Station();
        ReflectionTestUtils.setField(station, "id", id);
        ReflectionTestUtils.setField(station, "name", name);
        return station;
    }

}