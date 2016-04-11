package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Comments_CasalsDAO
{
    Comments_Casals createComment(String creatorid, String casalid, String content) throws SQLException;
    Comments_Casals updateComment(String id, String content) throws SQLException;
    Comments_Casals getCommentById(String id) throws SQLException;
    Comments_Casals getCommentByCasalId(String casalid) throws SQLException;
    Comments_Casals getCommentByCreatorId(String creatorid) throws SQLException;
    Comments_Casals getAllComments() throws SQLException;
    boolean deleteComment(String id) throws SQLException;
}