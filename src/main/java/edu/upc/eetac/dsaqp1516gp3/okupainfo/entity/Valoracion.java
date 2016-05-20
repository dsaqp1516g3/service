package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Valoracion {

    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String loginid;
    private String casalid;
    private String valoracion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getCasalid() {
        return casalid;
    }

    public void setCasalid(String casalid) {
        this.casalid = casalid;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
