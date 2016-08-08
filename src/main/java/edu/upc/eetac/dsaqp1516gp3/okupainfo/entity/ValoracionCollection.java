package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoracionCollection {

    private List<Link> links;
    private List<Valoracion> Valoracion = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Valoracion> getValoraciones() {
        return Valoracion;
    }

    public void setValoraciones(List<Valoracion> valoracion) {
        Valoracion = valoracion;
    }
}
