package de.progme.kassandra.rest.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Date;

/**
 * Created by Marvin Erkes on 12.05.20.
 */
@RequiredArgsConstructor
@Getter
public class AnnotationParser extends Parser {

    private final Range range;

    private final Annotation annotation;

    @RequiredArgsConstructor
    @Getter
    public static class Annotation {

        private final String name;

        private final String datasource;

        private final String iconColor;

        private final boolean enable;

        private final String query;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Range {

        private final Date from;
        private final Date to;

        public Range(String from, String to) {
            this(Date.from(Instant.parse(from)), Date.from(Instant.parse(to)));
        }
    }
}
