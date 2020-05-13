package de.progme.kassandra.rest.parser;

import com.google.gson.annotations.SerializedName;
import de.progme.kassandra.data.KassandraMetric;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

/**
 * Created by Marvin Erkes on 12.05.20.
 */
@RequiredArgsConstructor
@Getter
public class QueryParser extends Parser {

    private final int panelId;

    private final Range range;

    private final int intervalMs;

    private final List<Target> targets;

    private final List<Filter> adhocFilters;

    private final int maxDataPoints;

    @RequiredArgsConstructor
    @Getter
    public static class Range {

        private final Instant from;
        private final Instant to;

        public Range(String from, String to) {
            this(Instant.parse(from), Instant.parse(to));
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Target {

        private final String target;

        private final String refId;

        private final KassandraMetric.Type type;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Filter {

        private final String key;

        private final Operator operator;

        private final String value;

        public enum Operator {
            @SerializedName("=")
            EQUALS("=");

            @Getter
            private final String value;

            Operator(String value) {
                this.value = value;
            }
        }
    }
}
