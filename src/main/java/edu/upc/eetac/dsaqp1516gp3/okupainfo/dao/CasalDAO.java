package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAO
{
    Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude,double longitude, boolean validadted) throws SQLException, CasalAlreadyExistsException;
    Casal updateProfile(String casalid, String email, String name, String description) throws SQLException;
    Casal updateLocation(String casalid, String localization, Double latitude, Double longitude) throws SQLException;
    Casal getCasalByCasalid(String casalid) throws SQLException;
    Casal getCasalByEmail(String email) throws SQLException;
    Casal getValidatedCasals() throws SQLException;
    Casal getNoValidatedCasals() throws SQLException;
    CasalCollection getAllCasals() throws SQLException;
    boolean deleteCasal(String casalid) throws SQLException;
}