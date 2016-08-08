package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Valoracion;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.ValoracionCollection;

import java.sql.SQLException;


@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ValoracionDAO {
    Valoracion createValoracion(String loginid, String casalid, boolean valoracion) throws SQLException;
    Valoracion updateValoracion(String id, String loginid, String casalid, boolean valoracion) throws SQLException;
    Valoracion getValoracionById(String id) throws SQLException;
    ValoracionCollection getValoracionesByCasalId(String casalid) throws SQLException;
    ValoracionCollection getValoracionesByUserId(String loginid) throws SQLException;
    boolean deleteValoracion(String id) throws SQLException;
}
