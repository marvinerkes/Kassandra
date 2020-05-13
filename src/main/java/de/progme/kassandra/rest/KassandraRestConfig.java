package de.progme.kassandra.rest;

import de.progme.hermes.server.impl.HermesConfig;
import de.progme.kassandra.rest.resource.GrafanaResource;

/**
 * Created by Marvin Erkes on 04.02.2020.
 */
public class KassandraRestConfig extends HermesConfig {

    public KassandraRestConfig(String host, int port) {

        host(host);
        port(port);
        corePoolSize(2);
        maxPoolSize(4);
        backLog(50);
        register(GrafanaResource.class);
    }
}
