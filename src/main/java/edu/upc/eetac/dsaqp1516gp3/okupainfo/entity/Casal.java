package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Casal
{
    private List<Link> links;
    private String casalsid;
    private String adminid;
    private String email;
    private String name;
    private String description;
    private Boolean validadet;
    private String localization;
    private double latitude;
    private double longitude;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getCasalsid() {
        return casalsid;
    }

    public void setCasalsid(String casalsid) {
        this.casalsid = casalsid;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getValidadet() {
        return validadet;
    }

    public void setValidadet(Boolean validadet) {
        this.validadet = validadet;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
