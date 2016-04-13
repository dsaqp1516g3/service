package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Comments_EventosDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_COMMENT = "insert into comments_events((id, creatorid, eventoid, content) values (UNHEX(?), UNHEX(?), ?, ?)";
    String UPDATE_COMMENT = "update comments_events set content=? where id=unhex(?) & creatorid=unhex(?)";
    String GET_COMMENT_BY_ID = "select hex(ce.id) as id, ce.creatorid, ce.eventoid, ce.content from ce where id=unhex(?)";
    String GET_COMMENT_BY_EVENTID = "select hex(ce.id) as id, ce.creatorid, ce.eventoid, ce.content from ce where eventoid=?";
    String GET_COMMENT_BY_CREATORID = "select hex(ce.id) as id, ce.creatorid, ce.eventoid, ce.content from ce where creatorid=?";
    String GET_ALL_COMMENTS = "select *from comments_events";
    String DELETE_COMMENT = "delete from comments_events where id=unhex(?)";
}