package de.progme.kassandra.data.record;

import de.progme.athena.db.serialization.annotation.Column;
import de.progme.athena.db.serialization.annotation.Table;
import de.progme.kassandra.data.KassandraMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by Marvin Erkes on 13.05.20.
 */
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kassandra_records_table", options = { Table.Option.CREATE_IF_NOT_EXISTS })
public class TableRecord extends Record {

    @Column(name = "metric" )
    @Getter
    private String metric;

    @Column(name = "timestamp", options = { Column.Option.PRIMARY_KEY })
    @Getter
    private long timestamp;

    @Column(name = "value")
    @Getter
    private float value;

    public TableRecord(KassandraMetric metric, long timestamp, float value) {
        this(metric.getName(), timestamp, value);
    }

    public TableRecord(KassandraMetric metric, Date timestamp, float value) {
        this(metric.getName(), timestamp.getTime(), value);
    }
}
