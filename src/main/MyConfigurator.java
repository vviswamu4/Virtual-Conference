package main;

import javax.websocket.server.ServerEndpointConfig;
public class MyConfigurator extends ServerEndpointConfig.Configurator {

    private static final MediaServer ECHOSERVER = new MediaServer();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (MediaServer.class.equals(endpointClass)) {
            return (T) ECHOSERVER;
        } else {
            throw new InstantiationException();
        }
    }

}
