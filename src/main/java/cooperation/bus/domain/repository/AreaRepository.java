package cooperation.bus.domain.repository;

import cooperation.bus.domain.entity.Area;
import cooperation.bus.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, String> {
    Area findByMember(Member member);
}
