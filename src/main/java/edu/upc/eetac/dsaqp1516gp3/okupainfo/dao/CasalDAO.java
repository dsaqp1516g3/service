package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAO
{
    Casal createCasal(String loginid, String password, String email, String fullname, String descripcion, String localization) throws SQLException, CasalAlreadyExistsException;
    Casal updateProfile(String id, String email, String fullname, String description) throws SQLException;
    Casal updateValoracion(String id, float valoracion) throws SQLException;
    Casal updateLocation(String id, String localization, String latitud, String longitud) throws SQLException;// ens cal la de UpdateLocation segur? Si perque hem de saber on estar situat per pasarla la direccio a la API de google maps
    Casal getCasalById(String id) throws SQLException;
    Casal getCasalByLoginid(String loginid) throws SQLException;
    Casal getAllCasals() throws SQLException;
    boolean deleteCasal(String id) throws SQLException;
    boolean checkPassword(String id, String password) throws SQLException;
}