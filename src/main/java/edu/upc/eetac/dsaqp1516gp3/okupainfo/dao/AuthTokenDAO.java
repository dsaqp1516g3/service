package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.auth.UserInfo;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;

import java.sql.SQLException;

public interface AuthTokenDAO
{
    UserInfo getUserByAuthToken(String token) throws SQLException;
    AuthToken createAuthToken(String userid) throws SQLException;
    void deleteToken(String userid) throws SQLException;
}
