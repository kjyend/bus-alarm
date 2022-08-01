package cooperation.bus.domain.repository;

import cooperation.bus.domain.entity.Bus;
import cooperation.bus.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    Bus findByMember(Member member);
}
