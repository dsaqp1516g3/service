package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Comments_CasalsDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_COMMENT = "insert into comments_casals((id, creatorid, casalid, content) values (UNHEX(?), UNHEX(?), ?, ?)";
    String UPDATE_COMMENT = "update comments_casals set content=? where id=unhex(?) & creatorid=unhex(?)";
    String GET_COMMENT_BY_ID = "select hex(cc.id) as id, cc.creatorid, cc.casalid, cc.content from cc where id=unhex(?)";
    String GET_COMMENT_BY_CASALID = "select hex(cc.id) as id, cc.creatorid, cc.casalid, cc.content from cc where casalid=?";
    String GET_COMMENT_BY_CREATORID = "select hex(cc.id) as id, cc.creatorid, cc.casalid, cc.content from cc where creatorid=?";
    String GET_ALL_COMMENTS = "select *from comments_casals";
    String DELETE_COMMENT = "delete from comments_casals where id=unhex(?)";
}
