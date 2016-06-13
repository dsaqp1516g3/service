package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

import java.io.InputStream;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAO
{
    Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude, double longitude, boolean validated, InputStream image) throws SQLException, CasalAlreadyExistsException;
    Casal createAndroidCasal(String adminid, String email, String name, String description, String localization, double latitude, double longitude, boolean validated) throws SQLException, CasalAlreadyExistsException;
    Casal updateProfile(String casalid, String email, String name, String description, String localization, double latitude,double longitude, boolean validated) throws SQLException;
    Casal getCasalByCasalid(String casalid) throws SQLException;
    Casal getCasalByEmail(String email) throws SQLException;
    CasalCollection getValidatedCasals() throws SQLException;
    CasalCollection getNoValidatedCasals() throws SQLException;
    CasalCollection getAllCasals() throws SQLException;
    boolean deleteCasal(String casalid) throws SQLException;
    String getAdminId(String casalid) throws SQLException;
}