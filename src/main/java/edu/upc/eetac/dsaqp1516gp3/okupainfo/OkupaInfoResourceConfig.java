package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class OkupaInfoResourceConfig extends ResourceConfig {
    public OkupaInfoResourceConfig() {
        packages("edu.upc.eetac.dsaqp1516gp3.okupainfo");
        packages("edu.upc.eetac.dsaqp1516gp3.okupainfo.auth");
        register(RolesAllowedDynamicFeature.class);
    }
}
