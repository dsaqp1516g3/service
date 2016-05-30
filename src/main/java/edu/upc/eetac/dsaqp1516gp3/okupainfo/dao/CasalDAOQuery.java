package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_CASAL = "insert into casals (adminid, casalid, email, name, description, localization, latitude, longitude, validado) values (UNHEX(?), UNHEX(?), ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_CASAL = "update casals set email=?, name=?, description=? where casalid=unhex(?)";
    String GET_CASAL_BY_CASALID = "select hex(c.casalid) as casalid, hex(c.adminid) as adminid, c.email, c.name, c.description, c.localization, c.latitude, c.longitude from casals c where casalid=unhex(?)";
    String GET_CASAL_BY_EMAIL = "select hex(c.casalid) as casalid, c.adminid, c.email, c.name, c.description from casals c where c.email=?";
    String GET_VALIDATED_CASALS = "select casalid, adminid, email, name, description, localization where validado=TRUE";
    String GET_NO_VALIDATED_CASALS = "select casalid, adminid, email, name, description, localization where validado=FALSE";
    String DELETE_CASAL = "delete from casals where id=unhex(?)";
    String GET_ALL_CASALS = "select hex(casalid) as casalid, hex(adminid) as adminid, email, name, description, localization, latitude, longitude from casals";
}