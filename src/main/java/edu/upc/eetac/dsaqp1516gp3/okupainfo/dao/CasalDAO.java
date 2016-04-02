package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import java.sql.SQLException;

public interface CasalDAO {
    public Casal createCasal(String loginid, String password, String email, String fullname) throws SQLException, CasalAlreadyExistsException;

    public Casal updateProfile(String id, String email, String fullname) throws SQLException;

    public Casal getCasalById(String id) throws SQLException;

    public Casal getCasalByLoginid(String loginid) throws SQLException;

    public boolean deleteCasal(String id) throws SQLException;

    public boolean checkPassword(String id, String password) throws SQLException;
}
