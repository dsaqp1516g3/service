package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.auth.UserInfo;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AuthTokenDAO
{
    UserInfo getUserByAuthToken(String token) throws SQLException;
    AuthToken createAuthToken(String userid) throws SQLException;
    void deleteToken(String userid) throws SQLException;
}
