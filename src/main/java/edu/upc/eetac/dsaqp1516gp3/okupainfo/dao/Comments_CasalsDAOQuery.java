package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Comments_CasalsDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_COMMENT = "insert into comments_casals((id, creatorid, casalid, content) values (UNHEX(?), UNHEX(?), UNHEX(?), ?)";
    String UPDATE_COMMENT = "update comments_casals set content=? where id=unhex(?) & creatorid=unhex(?)";
    String GET_COMMENT_BY_ID = "select hex(id) as id, hex(creatorid) as creatorid, hex(casalid) as casalid, content from comments_casals where id=unhex(?)";
    String GET_COMMENTS_BY_CASALID = "select hex(id) as id, hex(creatorid) as creatorid, hex(casalid) as casalid, content, creation_timestamp from comments_casals where casalid=unhex(?)";
    String GET_COMMENT_BY_CREATORID = "select hex(id) as id, hex(creatorid) as creatorid, hex(casalid) as casalid, content from comments_casals  where creatorid=unhex(?)";
    String GET_ALL_COMMENTS = "select hex(id) as id, hex(creatorid) as creatorid, hex(casalid) as casalid, content, creation_timestamp from comments_casals";
    String GET_COMMENT_CASALS_AFTER = "select hex(id) as id, hex(creatorid) as creatorid, hex(casalid) as casalid, cc.content, cc.creation_timestamp from comments_casals cc where creation_timestamp > ? order by creation_timestamp desc limit 5";
    String DELETE_COMMENT = "delete from comments_casals where id=unhex(?)";
}
