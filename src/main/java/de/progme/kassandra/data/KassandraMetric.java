package de.progme.kassandra.data;

import com.google.gson.annotations.SerializedName;
import de.progme.athena.db.serialization.annotation.Column;
import de.progme.athena.db.serialization.annotation.Table;
import de.progme.kassandra.Kassandra;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marvin Erkes on 12.05.20.
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kassandra_metrics", options = { Table.Option.CREATE_IF_NOT_EXISTS })
public class KassandraMetric {

    @Column(name = "name", options = { Column.Option.PRIMARY_KEY })
    @Getter
    private String name;

    @Getter
    @Column(name = "type")
    private int type;

    public KassandraMetric(String name, Type type) {
        this(name, type.getValue());
    }

    public enum Type {
        @SerializedName("timeserie")
        TIMESERIE(0),
        @SerializedName("table")
        TABLE(1);

        private final int value;
        private static Map<Integer, Type> map = new HashMap<>();

        Type(int value) {
            this.value = value;
        }

        static {
            for (Type type : Type.values()) {
                map.put(type.value, type);
            }
        }

        public static Type valueOf(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }
    }
}
