package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ValoracionDAOQuery
{
        String UUID = "select REPLACE(UUID(),'-','')";
        String CREATE_VALORACION = "insert into valoraciones_casals((id, loginid, casalid, valoracion) values (UNHEX(?), ?, ?, ?);";
        String UPDATE_VALORACION = "update valoraciones_casals set valoracion=? where id=unhex(?)";
        String GET_VALORACION_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.descripcion from users u where id=unhex(?)";
        String GET_VALORACION_BY_LOGINID = "select hex(ca.id) as id, ca.loginid, ca.valoracion from comments_casals ca where ca.loginid=?";
        String GET_VALORACION_BY_CASALID = "select hex(ca.id) as id, ca.casalid, ca.valoracion from comments_casals ca where ca.loginid=?";
        String DELETE_VALORACION = "delete from valoraciones where id=unhex(?)";
        String GET_ALL_VALORACIONES = "select *from valoraciones_casals";

}

/*
	id BINARY(16) NOT NULL,
  	loginid BINARY(16) NOT NULL,
  	casalid BINARY(16) NOT NULL,
  	valoracion BINARY NOT NULL,
 */