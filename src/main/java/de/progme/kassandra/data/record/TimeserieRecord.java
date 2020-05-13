package de.progme.kassandra.data.record;

import de.progme.athena.db.serialization.annotation.Column;
import de.progme.athena.db.serialization.annotation.Table;
import de.progme.kassandra.data.KassandraMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

/**
 * Created by Marvin Erkes on 13.05.20.
 */
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kassandra_records_timeserie", options = { Table.Option.CREATE_IF_NOT_EXISTS })
public class TimeserieRecord extends Record {

    @Column(name = "metric" )
    @Getter
    private String metric;

    @Column(name = "timestamp" )
    @Getter
    private Timestamp timestamp;

    @Column(name = "value")
    @Getter
    private float value;

    public TimeserieRecord(KassandraMetric metric, long timestamp, float value) {
        this(metric.getName(), new Timestamp(timestamp), value);
    }
}
