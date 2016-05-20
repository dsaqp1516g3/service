package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_Events
{
    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String creatorid;
    private String eventoid;
    private String Content;
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

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getEventoid() {
        return eventoid;
    }

    public void setEventoid(String eventoid) {
        this.eventoid = eventoid;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
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
