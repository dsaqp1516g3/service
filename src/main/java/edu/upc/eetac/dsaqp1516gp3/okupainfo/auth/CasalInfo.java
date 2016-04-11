package edu.upc.eetac.dsaqp1516gp3.okupainfo.auth;

import java.security.Principal;

/**
 * Created by Hermione on 11/04/2016.
 */
public class CasalInfo implements Principal
{

    public CasalInfo() {}

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

}
