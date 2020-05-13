package de.progme.kassandra.rest.parser;

import com.google.gson.annotations.SerializedName;
import de.progme.kassandra.data.KassandraMetric;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Created by Marvin Erkes on 12.05.20.
 */
@RequiredArgsConstructor
@Getter
public class SearchParser extends Parser {

    private final String target;
}
