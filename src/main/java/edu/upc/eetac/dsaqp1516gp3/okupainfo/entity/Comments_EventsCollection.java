package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.CasalResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoMediaType;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoRootAPIResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_EventsCollection
{
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-commentevents", title = "Current commentevents", type = OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS_COLLECTION),
            @InjectLink(resource = CasalResource.class, method = "getCommentEvent", style = InjectLink.Style.ABSOLUTE, rel = "self", title = "CommentEvent profile", type = OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS, bindings = @Binding(name = "id", value = "${instance.id}"))
    })
    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<Comments_Events> comments_events = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public List<Comments_Events> getComments_events() {
        return comments_events;
    }

    public void setComments_events(List<Comments_Events> comments_events) {
        this.comments_events = comments_events;
    }
}
