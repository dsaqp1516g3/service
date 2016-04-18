package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_CASAL = "insert into casals (adminid, casalid, email, name, description, localization, latitude, longitude) values (UNHEX(REPLACE(UUID(),'-','')), UNHEX(?), ?, ?, ?, ?, ?, ?)";
    String UPDATE_CASAL = "update casals set email=?, name=?, description=? where casalid=unhex(?)";
    String UPDATE_LOCATION = "update casals set location=?, latitude=?, longitude=? where casalid=unhex(?)";
    String GET_CASAL_BY_CASALID = "select hex(c.casalid) as casalid, c.adminid, c.email, c.name, c.description from casals c where casalid=unhex(?)";
    String GET_CASAL_BY_EMAIL = "select hex(c.casalid) as casalid, c.adminid, c.email, c.name, c.description from casals c where c.email=?";
    String GET_VALIDATED_CASALS = "select casalsid, adminid, email, name, description, localization where validado=TRUE";
    String GET_NO_VALIDATED_CASALS = "select casalsid, adminid, email, name, description, localization where validado=FALSE";
    String DELETE_CASAL = "delete from casals where id=unhex(?)";
    String GET_ALL_CASALS = "select *from casals";
}