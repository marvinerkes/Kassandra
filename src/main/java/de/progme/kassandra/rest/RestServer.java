package de.progme.kassandra.rest;

import de.progme.hermes.server.HermesServer;
import de.progme.hermes.server.HermesServerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marvin Erkes on 04.02.2020.
 */
@RequiredArgsConstructor
public class RestServer {

    private static Logger logger = LoggerFactory.getLogger(RestServer.class);

    private final String host;
    private final int port;

    private HermesServer hermesServer;

    public void initialize() {

        this.hermesServer = HermesServerFactory.create(new KassandraRestConfig(this.host, this.port));
        this.hermesServer.start();

        logger.info("RESTful API listening on {}:{}", this.host, this.port);
    }

    public void stop() {

        this.hermesServer.stop();
    }
}
