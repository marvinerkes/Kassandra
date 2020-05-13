package de.progme.kassandra.rest.resource;

import com.google.gson.*;
import de.progme.hermes.server.http.Request;
import de.progme.hermes.server.http.annotation.Path;
import de.progme.hermes.server.http.annotation.PathParam;
import de.progme.hermes.server.http.annotation.Produces;
import de.progme.hermes.server.http.annotation.method.GET;
import de.progme.hermes.server.http.annotation.method.POST;
import de.progme.hermes.shared.ContentType;
import de.progme.hermes.shared.Status;
import de.progme.hermes.shared.http.Response;
import de.progme.kassandra.Kassandra;
import de.progme.kassandra.rest.parser.AnnotationParser;
import de.progme.kassandra.rest.parser.QueryParser;
import de.progme.kassandra.rest.parser.SearchParser;
import de.progme.kassandra.rest.response.KassandraResponse;
import de.progme.kassandra.rest.response.RecordsResponse;
import de.progme.kassandra.rest.response.TimeserieRecordsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marvin Erkes on 12.05.20.
 */
@Path("")
public class GrafanaResource {

    private static Logger logger = LoggerFactory.getLogger(GrafanaResource.class);
    private static Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new JsonDeserializer<Instant>() {
        @Override
        public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return Instant.parse(json.getAsString());
        }
    }).create();

    @GET
    @Path("")
    @Produces(ContentType.APPLICATION_JSON)
    public Response status(Request httpRequest) {
        return Response
                .ok()
                .content(gson.toJson(new KassandraResponse()))
                .build();
    }

    @POST
    @Path("/search")
    @Produces(ContentType.APPLICATION_JSON)
    public Response search(Request httpRequest) {
        SearchParser search = this.gson.fromJson(httpRequest.body(), SearchParser.class);
        List<String> response = Kassandra.getInstance().getMetrics()
                .stream()
                .map(metric -> metric.getName())
                .collect(Collectors.toList());

        return Response
                .ok()
                .content(gson.toJson(response))
                .build();
    }

    @POST
    @Path("/query")
    @Produces(ContentType.APPLICATION_JSON)
    public Response query(Request httpRequest) {

        QueryParser query = this.gson.fromJson(httpRequest.body(), QueryParser.class);
        List<RecordsResponse> response = new ArrayList<>();
        query.getTargets().forEach(target -> {
            switch(target.getType()) {
                case TIMESERIE:
                    response.add(new TimeserieRecordsResponse(
                            target.getTarget(),
                            Kassandra.getInstance().getTimeserieRecords(
                                    target.getTarget(),
                                    query.getMaxDataPoints(),
                                    query.getRange().getFrom(),
                                    query.getRange().getTo()
                            )
                            .stream()
                            .map(record -> Arrays.asList(record.getValue(), (float) record.getTimestamp().getTime()))
                            .collect(Collectors.toList()))
                    );
                    break;
                case TABLE:

            }
        });

        return Response
                .ok()
                .content(gson.toJson(response))
                .build();
    }

    @POST
    @Path("/annotations")
    @Produces(ContentType.APPLICATION_JSON)
    public Response annotations(Request httpRequest) {

        AnnotationParser annotation = this.gson.fromJson(httpRequest.body(), AnnotationParser.class);

        System.out.println(this.gson.toJson(annotation));

        return Response
                .ok()
                .content(gson.toJson(new KassandraResponse()))
                .build();
    }

    @GET
    @Path("/tag-keys")
    @Produces(ContentType.APPLICATION_JSON)
    public Response tagKeys(Request httpRequest) {
        return Response
                .ok()
                .content(gson.toJson(new KassandraResponse()))
                .build();
    }

    @GET
    @Path("/tag-values")
    @Produces(ContentType.APPLICATION_JSON)
    public Response tagValues(Request httpRequest) {
        return Response
                .ok()
                .content(gson.toJson(new KassandraResponse()))
                .build();
    }
}
