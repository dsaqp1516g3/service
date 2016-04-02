package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import java.sql.SQLException;

public class CasalDAOImpl implements CasalDAO{
    @Override
    public Casal createCasal(String loginid, String password, String email, String fullname, String description, float valoracion, String localization, String latitud, String longitud) throws SQLException, CasalAlreadyExistsException {
        return null;
    }

    @Override
    public Casal UpdateProfile(String id, String email, String fullname, String description, String localization, String latitud, String longitud) throws SQLException {
        return null;
    }

    @Override
    public Casal UpdateValoracion(String id, float valoracion) throws SQLException {
        return null;
    }

    @Override
    public Casal UpdtateLocation(String id, String localization, String latitud, String longitud) throws SQLException {
        return null;
    }

    @Override
    public Casal GetCasalById(String id) throws SQLException {
        return null;
    }

    @Override
    public Casal GetCasalByLoginId(String loginid) throws SQLException {
        return null;
    }

    @Override
    public Casal GetAllCasals() throws SQLException {
        return null;
    }

    @Override
    public boolean deleteCasal(String id) throws SQLException {
        return false;
    }

    @Override
    public boolean checkPassword(String id, String password) throws SQLException {
        return false;
    }
}
