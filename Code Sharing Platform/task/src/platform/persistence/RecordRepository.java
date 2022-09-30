package platform.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.business.Record;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {
    Record findRecordByUUIDCode(String UUIDCode);
    boolean existsByUUIDCode(String UUIDCode);
    Iterable<Record> findAll();
}
