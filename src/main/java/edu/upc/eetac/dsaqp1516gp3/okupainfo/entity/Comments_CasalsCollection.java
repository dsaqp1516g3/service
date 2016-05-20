package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_CasalsCollection
{
    @InjectLinks({})
    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<Comments_Casals> comments_casals = new ArrayList<>();

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

    public List<Comments_Casals> getComments_casals() {
        return comments_casals;
    }

    public void setComments_casals(List<Comments_Casals> comments_casals) {
        this.comments_casals = comments_casals;
    }
}
