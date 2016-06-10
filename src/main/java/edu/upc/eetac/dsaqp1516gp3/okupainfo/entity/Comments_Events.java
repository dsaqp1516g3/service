package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.CasalResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoMediaType;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoRootAPIResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_Events
{
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-commentevents", title = "Current commentevents", type = OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS_COLLECTION),
            @InjectLink(resource = CasalResource.class, method = "getCommentEvent", style = InjectLink.Style.ABSOLUTE, rel = "self", title = "CommentEvent profile", type = OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS, bindings = @Binding(name = "id", value = "${instance.id}"))
    })
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
