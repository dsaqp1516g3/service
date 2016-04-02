package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import java.sql.SQLException;

public interface CasalDAO
{
    Casal createCasal(String loginid, String password, String email, String fullname, String description, float valoracion, String localization, String latitud, String longitud) throws SQLException, CasalAlreadyExistsException;
    Casal UpdateProfile(String id, String email, String fullname, String description, String localization, String latitud, String longitud) throws SQLException;
    Casal UpdateValoracion(String id, float valoracion) throws SQLException;
    Casal UpdtateLocation(String id, String localization, String latitud, String longitud) throws SQLException;
    Casal GetCasalById(String id) throws SQLException;
    Casal GetCasalByLoginId(String loginid) throws SQLException;
    Casal GetAllCasals() throws SQLException;
    boolean deleteCasal(String id) throws SQLException;
    boolean checkPassword(String id, String password) throws SQLException;
}
