package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.auth.UserInfo;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;

import java.sql.SQLException;

public interface AuthTokenDAO    {
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws SQLException;
}
