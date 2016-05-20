package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoracionCollection {
    @InjectLinks({})
    private List<Link> links;
    private List<Valoracion> Valoracion = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Valoracion> getValoracion() {
        return Valoracion;
    }

    public void setValoracion(List<edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Valoracion> valoracion) {
        Valoracion = valoracion;
    }
}
