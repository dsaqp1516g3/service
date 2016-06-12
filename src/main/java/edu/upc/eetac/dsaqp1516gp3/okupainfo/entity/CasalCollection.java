package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.CasalResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.EventResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoMediaType;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoRootAPIResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

public class CasalCollection
{
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-events", title = "Current Events", type = OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-casals", title = "Current Casals", type = OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION)
    })
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
