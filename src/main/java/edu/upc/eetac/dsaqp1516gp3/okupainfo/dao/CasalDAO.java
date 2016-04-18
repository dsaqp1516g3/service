package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAO
{
    Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude,double longitude, boolean validadet) throws SQLException, CasalAlreadyExistsException;
    Casal updateProfile(String casalid, String email, String name, String description) throws SQLException;
    Casal updateLocation(String casalid, String localization, String latitude, String longitude) throws SQLException;
    Casal getCasalByCasalid(String casalid) throws SQLException;
    Casal getCasalByEmail(String email) throws SQLException;
    Casal getValidatedCasals() throws SQLException;
    Casal getNoValidatedCasals() throws SQLException;
    CasalCollection getAllCasals() throws SQLException;
    boolean deleteCasal(String casalid) throws SQLException;
}

/*
private List<Link> links;
private String casalid;
private String adminid;
private String email;
private String name;
private String description;
private Boolean validadet;
private String localization;
private double latitude;
private double longitude;

    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_CASAL = "insert into casals (casalsid, adminid, email, name, description, localization, latitud, longitud) values (UNHEX(REPLACE(UUID(),'-','')), UNHEX(?), ?, ?, ?, ?, ?, ?)";
    String UPDATE_CASAL = "update casals set email=?, fullname=?, description=? where casalsid=unhex(?)";
    String UPDATE_LOCATION = "update casals set location=?, latitud=?, longitud=? where casalsid=unhex(?)";
    String GET_CASAL_BY_CASALID = "select hex(c.casalid) as casalid, c.loginid, c.email, c.fullname, c.descripcion from casals c where casalid=unhex(?)";
    String GET_CASAL_BY_EMAIL = "select hex(c.casalid) as casalid, c.loginid, c.email, c.fullname, c.description from casals c where c.email=?";
    String GET_VALIDATED_CASALS = "select casalsid, adminid, email, name, description, localization where validado=TRUE";
    String GET_NO_VALIDATED_CASALS = "select casalsid, adminid, email, name, description, localization where validado=FALSE";
    String DELETE_CASAL = "delete from casals where id=unhex(?)";
    String GET_ALL_CASALS = "select *from casals";
 */