package cooperation.bus.domain.repository;

import cooperation.bus.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, String> {
    //service에서 createareamember을 사용해야한다. 또한 member를 통해서 정류소 번호얻기
}
