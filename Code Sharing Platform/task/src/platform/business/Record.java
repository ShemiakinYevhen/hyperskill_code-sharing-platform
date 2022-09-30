package platform.business;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "records")
@Data
@NoArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(unique = true, name = "UUID", nullable = false)
    private String UUIDCode = UUID.randomUUID().toString();

    @Column(name = "time")
    private long time;

    @Column(name = "views")
    private long views;

    public Record(String code, LocalDateTime date, long time, long views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
    }
}
