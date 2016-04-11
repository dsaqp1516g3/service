package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_CasalsDAOImpl implements Comments_CasalsDAO{
    @Override
    public Comments_Casals createComment(String creatorid, String casalid, String content) throws SQLException {
        return null;
    }

    @Override
    public Comments_Casals updateComment(String id, String content) throws SQLException {
        return null;
    }

    @Override
    public Comments_Casals getCommentById(String id) throws SQLException {
        return null;
    }

    @Override
    public Comments_Casals getCommentByCasalId(String casalid) throws SQLException {
        return null;
    }

    @Override
    public Comments_Casals getCommentByCreatorId(String creatorid) throws SQLException {
        return null;
    }

    @Override
    public Comments_Casals getAllComments() throws SQLException {
        return null;
    }

    @Override
    public boolean deleteComment(String id) throws SQLException {
        return false;
    }
}
