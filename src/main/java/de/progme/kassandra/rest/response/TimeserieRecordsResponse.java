package de.progme.kassandra.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Created by Marvin Erkes on 13.05.20.
 */
@AllArgsConstructor
@Getter
public class TimeserieRecordsResponse extends RecordsResponse {

    private final String target;

    private final List<List<Float>> datapoints;
}
