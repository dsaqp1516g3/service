package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ValoracionDAOQuery
{
        String UUID = "select REPLACE(UUID(),'-','')";
        String CREATE_VALORACION = "insert into valoraciones_casals (id, loginid, casalid, valoracion) values (UNHEX(?), UNHEX(?), UNHEX(?), ?)";
        String UPDATE_VALORACION = "update valoraciones_casals set valoracion=? where id=unhex(?)";
        String GET_VALORACION_BY_ID = "select hex(vc.id) as id, hex(vc.loginid) as loginid, hex(vc.casalid) as casalid,  vc.valoracion from valoraciones_casals vc where vc.id=unhex(?)";
        String GET_VALORACION_BY_USERID = "select hex(id) as id, hex(loginid) as loginid, hex(casalid) as casalid,  valoracion from valoraciones_casals where loginid=unhex(?)";
        String GET_VALORACION_BY_CASALID = "select hex(id) as id, hex(loginid) as loginid, hex(casalid) as casalid, valoracion from valoraciones_casals where casalid=unhex(?)";
        String DELETE_VALORACION = "delete from valoraciones_casals where id=unhex(?)";
}
