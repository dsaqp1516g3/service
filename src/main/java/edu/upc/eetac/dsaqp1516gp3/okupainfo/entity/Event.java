package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.CasalResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.EventResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoMediaType;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoRootAPIResource;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-events", title = "Current Events", type = OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-casals", title = "Current Casals", type = OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION),
    })
    private List<Link> links;
    private String id;
    private String casalid;
    private String title;
    private String description;
    private String localization;
    private double latitude;
    private double longitude;
    private long eventdate;
    private long creationTimestamp;
    private long lastModified;


    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCasalid() {
        return casalid;
    }

    public void setCasalid(String casalid) {
        this.casalid = casalid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getEventdate() {
        return eventdate;
    }

    public void setEventdate(long eventdate) {
        this.eventdate = eventdate;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

}
