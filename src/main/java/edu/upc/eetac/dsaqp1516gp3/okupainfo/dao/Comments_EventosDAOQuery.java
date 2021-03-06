package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Comments_EventosDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_COMMENT = "insert into comments_events (id, creatorid, eventoid, content) values (UNHEX(?), UNHEX(?), UNHEX(?), ?)";
    String UPDATE_COMMENT = "update comments_events set content=? where id=unhex(?)";
    String GET_COMMENT_BY_ID = "select hex(id) as id, hex(creatorid) as creatorid, hex(eventoid) as eventoid, content, last_modified, creation_timestamp from comments_events where id=unhex(?)";
    String GET_COMMENT_BY_EVENTID = "select hex(id) as id, hex(creatorid) as creatorid, hex(eventoid) as eventoid, content, last_modified, creation_timestamp from comments_events where eventoid=unhex(?)";
    String GET_COMMENT_BY_CREATORID = "select hex(id) as id, hex(creatorid) as creatorid, hex(eventoid) as eventoid, content, last_modified, creation_timestamp from comments_events where creatorid=unhex(?)";
    String GET_ALL_COMMENTS = "select hex(id) as id, hex(creatorid) as creatorid, hex(eventoid) as eventoid, content, last_modified, creation_timestamp from comments_events";
    String GET_COMMENT_EVENTS_AFTER = "select hex(id) as id, hex(creatorid) as creatorid, hex(casalid) as eventid, content, creation_timestamp, last_modified from comments_events where creation_timestamp > ? order by creation_timestamp desc limit 5";
    String DELETE_COMMENT = "delete from comments_events where id=unhex(?)";
}