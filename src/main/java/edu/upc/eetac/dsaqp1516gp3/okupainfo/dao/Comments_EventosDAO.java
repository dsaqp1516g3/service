package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Events;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Comments_EventosDAO
{
    Comments_Events createComment(String creatorid, String eventoid, String content) throws SQLException;
    Comments_Events updateComment(String id, String creatorid, String content) throws SQLException;
    Comments_Events getCommentById(String id) throws SQLException;
    Comments_Events getCommentByEventoId(String eventoid) throws SQLException;
    Comments_Events getCommentByCreatorId(String creatorid) throws SQLException;
    Comments_Events getAllComments() throws SQLException;
    boolean deleteComment(String id) throws SQLException;
}

