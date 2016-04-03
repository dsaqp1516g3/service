package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAO {
    Casal createCasal(String loginid, String password, String email, String fullname, String descripcion) throws SQLException, CasalAlreadyExistsException;
    Casal UpdateProfile(String id, String email, String fullname, String description, String localization, String latitud, String longitud) throws SQLException;
    Casal updateValoracion(String id, float valoracion) throws SQLException;
    Casal UpdateLocation(String id, String localization, String latitud, String longitud) throws SQLException;// ens cal la de UpdateLocation segur? Si perque hem de saber on estar situat per pasarla la direccio a la API de google maps
    Casal getCasalById(String id) throws SQLException;
    Casal getCasalByLoginid(String loginid) throws SQLException;
    Casal GetAllCasals() throws SQLException;
    public boolean deleteCasal(String id) throws SQLException;
    public boolean checkPassword(String id, String password) throws SQLException;
}