package platform.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.persistence.RecordRepository;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Record findRecordByUUIDCode(String UUIDCode) {
        return recordRepository.findRecordByUUIDCode(UUIDCode);
    }

    public Record save(Record toSave) {
        return recordRepository.save(toSave);
    }

    public boolean existsByUUIDCode(String UUIDCode) {
        return recordRepository.existsByUUIDCode(UUIDCode);
    }

    public Iterable<Record> findAll() {
        return recordRepository.findAll();
    }
}
