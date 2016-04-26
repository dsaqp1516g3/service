package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Valoracion;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.ValoracionCollection;

import java.sql.SQLException;

/**
 * Created by Guillermo on 26/04/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ValoracionDAO {

    Valoracion createValoracion(String loginid, String casalid, Boolean valoracion) throws SQLException, UserAlreadyExistsException;
    Valoracion updateValoracion(String id, String loginid, String casalid, Boolean valoracion) throws SQLException;
    Valoracion getValoracionById(String id) throws SQLException;

}
