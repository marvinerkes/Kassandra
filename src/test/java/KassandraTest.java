import de.progme.kassandra.Kassandra;
import de.progme.kassandra.data.KassandraMetric;
import de.progme.kassandra.data.record.TimeserieRecord;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Date;

/**
 * Created by Marvin Erkes on 04.02.2016.
 */
@Slf4j
public class KassandraTest {

    public static void main(String[] args) throws Exception {

        Kassandra kassandra = new Kassandra.Builder()
                .setMysqlHost("vweb05.lumaserv.com")
                .setMysqlPort(3306)
                .setMysqlDatabase("m_erkes_school")
                .setMysqlUsername("m_erkes_school")
                .setMysqlPassword("48%tyC6w")
                .setRestHost("0.0.0.0")
                .setRestPort(4000)
                .build();
        kassandra.initialize();

        KassandraMetric catsMetric = new KassandraMetric("cats", KassandraMetric.Type.TIMESERIE);
        KassandraMetric dogsMetric = new KassandraMetric("dogs", KassandraMetric.Type.TIMESERIE);

        kassandra.createMetric(catsMetric);
        kassandra.createMetric(dogsMetric);

        log.info("Found metrics:");
        kassandra.getMetrics().forEach(metric -> log.info(" - {}", metric.getName()));

        log.info("Matched metric to \"{}\": {}", "cats", kassandra.getMetric("cats").getName());

        for (int i = 0; i < 200; i++) {
            int value = i <= 100 ? i : 100 - (i - 100);
            log.info("Inserting {}", value);
            kassandra.insertRecord(new TimeserieRecord(catsMetric, Instant.now().toEpochMilli() - (i * 1000*60*10), value));
            Thread.sleep(250);
        }
    }
}
