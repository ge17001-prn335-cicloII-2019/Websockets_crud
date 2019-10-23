/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.kk.boundary;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.Session;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;
import ues.occ.edu.sv.ingenieria.prn335.kk.controller.GeneroFacade;

/**
 *
 * @author rafael
 */
@ServerEndpoint(value = "/genero")
public class WebSocketServer implements Serializable {

    @Inject
    private GeneroDecoder decoderGenero;
    @Inject
    private GeneroEncoder encoderGenero;
    @Inject
    private GeneroFacade facade;

    public Session session;
    private static final Set<WebSocketServer> clientsEndpoints = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        clientsEndpoints.add(this);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            List<Genero> lista = facade.findAll();
            String jsonMessage = encoderGenero.generoToJson(lista);
            broadcast(jsonMessage);
        } catch (EncodeException ex) {
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        try {
            clientsEndpoints.remove(this);
            String message = "Adios";
            broadcast(message);
        } catch (EncodeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private void broadcast(String message) throws IOException, EncodeException {
        clientsEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
                
            }
        });
    }

}
