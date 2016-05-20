package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

public class CasalCollection
{
    @InjectLinks({})
    private List<Link> links;
    private List<Casal> Casals = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Casal> getCasals() {
        return Casals;
    }

    public void setCasals(List<Casal> casals) {
        Casals = casals;
    }
}
