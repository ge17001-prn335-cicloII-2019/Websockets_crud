/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.kk.boundary;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;

/**
 *
 * @author rafael
 */
@LocalBean
public class GeneroDecoder implements Serializable {

    Jsonb jsonb = JsonbBuilder.create();

    public Genero jsonToGenero(String json) {
        if (json != null && !json.isEmpty() && !json.equals("")) {
            try {
                return jsonb.fromJson(json, Genero.class);
            } catch (JsonbException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }
}
