package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_Events
{
    private List<Link> links;
    private String id;
    private String creatorid;
    private String eventoid;
    private String Content;
    private long creationTimestamp;
}
