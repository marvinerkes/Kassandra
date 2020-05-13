package de.progme.kassandra;

import de.progme.athena.Athena;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.serialization.Order;
import de.progme.athena.db.settings.AthenaSettings;
import de.progme.hermes.server.HermesServer;
import de.progme.hermes.server.HermesServerFactory;
import de.progme.kassandra.data.KassandraMetric;
import de.progme.kassandra.data.record.Record;
import de.progme.kassandra.data.record.TableRecord;
import de.progme.kassandra.data.record.TimeserieRecord;
import de.progme.kassandra.rest.RestServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by Marvin Erkes on 12.05.20.
 */
@RequiredArgsConstructor
public class Kassandra {

    private static final DateTimeFormatter MYSQL_DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy/MM/dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);;

    @Getter
    private static Kassandra instance;

    private final String mysqlHost;
    private final int mysqlPort;
    private final String mysqlDatabase;
    private final String mysqlUsername;
    private final String mysqlPassword;

    private final String restHost;
    private final int restPort;

    private Athena athena;

    private RestServer restServer;

    public void initialize() {

        instance = this;

        athena = new Athena(new AthenaSettings.Builder()
                .host(this.mysqlHost)
                .port(this.mysqlPort)
                .user(this.mysqlUsername)
                .password(this.mysqlPassword)
                .database(this.mysqlDatabase)
                .poolSize(10)
                .poolName("Kassandra")
                .queryTimeout(1000)
                .build());
        athena.connect();
        athena.serializationManager().create(KassandraMetric.class);
        //athena.serializationManager().create(TableRecord.class);
        athena.serializationManager().create(TimeserieRecord.class);

        this.restServer = new RestServer(this.restHost, this.restPort);
        this.restServer.initialize();
    }

    public void createMetric(KassandraMetric metric) {
        this.athena.serializationManager().insert(metric);
    }

    public List<KassandraMetric> getMetrics() {
        return this.athena.serializationManager().select(KassandraMetric.class, -1);
    }

    public List<KassandraMetric> getMetrics(int limit) {
        return this.getMetrics(null, limit);
    }

    public List<KassandraMetric> getMetrics(String name, int limit) {
        return this.athena.serializationManager().select(
                KassandraMetric.class,
                limit,
                name != null ? new Condition("name", Condition.Operator.EQUAL, name) : null
        );
    }

    public KassandraMetric getMetric(String name) {
        List<KassandraMetric> matchedMetrics = this.getMetrics(name, 1);
        return matchedMetrics.isEmpty() ? null : matchedMetrics.get(0);
    }

    public void insertRecord(Record record) {
        this.athena.serializationManager().insert(record);
    }

    public List<TimeserieRecord> getTimeserieRecords(KassandraMetric metric) {
        return getTimeserieRecords(metric.getName(), -1, null, null);
    }

    public List<TimeserieRecord> getTimeserieRecords(String metric) {
        return getTimeserieRecords(metric, -1);
    }

    public List<TimeserieRecord> getTimeserieRecords(KassandraMetric metric, int limit) {
        return getTimeserieRecords(metric.getName(), limit, null, null);
    }

    public List<TimeserieRecord> getTimeserieRecords(String metric, int limit) {
        return getTimeserieRecords(metric, limit, null, null);
    }

    public List<TimeserieRecord> getTimeserieRecords(String metric, int limit, Instant start, Instant end) {

        return this.athena.serializationManager().select(
                TimeserieRecord.class,
                limit,
                new Order("timestamp", Order.Type.ASC),
                /*"ROUND(((60/30) * HOUR(timestamp) + FLOOR( MINUTE(timestamp) / 30)))"*/ null,
                new Condition("metric", Condition.Operator.EQUAL, metric),
                start != null ? new Condition("timestamp", Condition.Operator.GREATER_EQUAL, MYSQL_DATETIME_FORMATTER.format(start)) : null,
                end != null ? new Condition("timestamp", Condition.Operator.LESS_EQUAL, MYSQL_DATETIME_FORMATTER.format(end)) : null
        );
    }

    @Setter
    public static class Builder {

        private String mysqlHost;
        private int mysqlPort;
        private String mysqlDatabase;
        private String mysqlUsername;
        private String mysqlPassword;

        private String restHost;
        private int restPort;

        public Builder() {
        }

        public Kassandra build() {

            return new Kassandra(
                    this.mysqlHost,
                    this.mysqlPort,
                    this.mysqlDatabase,
                    this.mysqlUsername,
                    this.mysqlPassword,
                    this.restHost,
                    this.restPort
            );
        }
    }
}
