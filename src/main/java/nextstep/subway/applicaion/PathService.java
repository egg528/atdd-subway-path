package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    public PathResponse searchPath(PathRequest request) {
        return new PathResponse();
    }
}
