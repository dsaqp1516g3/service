package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CasalDAOImpl implements CasalDAO {
    @Override
    public Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude, double longitude, boolean validated, InputStream image) throws SQLException, CasalAlreadyExistsException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String uuid = writeAndConvertImage(image);
        String casalid = null;
        try {
            Casal casal = getCasalByEmail(email);
            if (casal != null)
                throw new CasalAlreadyExistsException();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                casalid = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(CasalDAOQuery.CREATE_CASAL);

            stmt.setString(1, adminid);
            stmt.setString(2, casalid);
            stmt.setString(3, email);
            stmt.setString(4, name);
            stmt.setString(5, description);
            stmt.setString(6, localization);
            stmt.setDouble(7, latitude);
            stmt.setDouble(8, longitude);
            stmt.setBoolean(9, false);
            stmt.setString(10, uuid);
            stmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getCasalByCasalid(casalid);
    }

    @Override
    public Casal createAndroidCasal(String adminid, String email, String name, String description, String localization, double latitude, double longitude, boolean validated) throws SQLException, CasalAlreadyExistsException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String casalid = null;
        try {
            Casal casal = getCasalByEmail(email);
            if (casal != null)
                throw new CasalAlreadyExistsException();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                casalid = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(CasalDAOQuery.CREATE_CASAL);

            stmt.setString(1, adminid);
            stmt.setString(2, casalid);
            stmt.setString(3, email);
            stmt.setString(4, name);
            stmt.setString(5, description);
            stmt.setString(6, localization);
            stmt.setDouble(7, latitude);
            stmt.setDouble(8, longitude);
            stmt.setString(9, String.valueOf(false));
            stmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getCasalByCasalid(casalid);
    }


    private String writeAndConvertImage(InputStream file){
        BufferedImage image;
        try{
            image = ImageIO.read(file);
        }catch(IOException E){
            throw new InternalServerErrorException("error");
        }
        if(image==null){
            return null;
        }
        else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid.toString() + ".png";
            try {
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("okupainfo");

                    ImageIO.write(image, "png", new File(prb.getString("uploadFolder") + filename));

            } catch (IOException e) {
                throw new InternalServerErrorException("error");
            }
            String respuesta = uuid.toString();
            return respuesta;
        }
    }

    @Override
    public Casal updateProfile(String casalid, String email, String name, String description, String localization, double latitude, double longitude, boolean validated) throws SQLException {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_CASAL);
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, localization);
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, longitude);
            stmt.setBoolean(7, validated);
            stmt.setString(8, casalid);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                casal = getCasalByCasalid(casalid);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return casal;
    }

    @Override
    public Casal getCasalByCasalid(String casalid) throws SQLException {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_CASAL_BY_CASALID);
            stmt.setString(1, casalid);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casal.setLocalization(rs.getString("localization"));
                casal.setLatitude(rs.getDouble("latitude"));
                casal.setLongitude(rs.getDouble("longitude"));
                casal.setValidated(rs.getBoolean("validado"));
                casal.setImage(rs.getString("image"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return casal;
    }

    @Override
    public Casal getCasalByEmail(String email) throws SQLException {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_CASAL_BY_EMAIL);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return casal;
    }

    @Override
    public CasalCollection getValidatedCasals() throws SQLException {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_VALIDATED_CASALS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casal.setLocalization(rs.getString("localization"));
                casal.setValidated(rs.getBoolean("validado"));
                casalCollection.getCasals().add(casal);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return casalCollection;
    }

    @Override
    public CasalCollection getNoValidatedCasals() throws SQLException {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_NO_VALIDATED_CASALS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casal.setLocalization(rs.getString("localization"));
                casal.setValidated(rs.getBoolean("validado"));
                casalCollection.getCasals().add(casal);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return casalCollection;
    }

    @Override
    public CasalCollection getAllCasals() throws SQLException {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_ALL_CASALS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casal.setLatitude(rs.getDouble("latitude"));
                casal.setLongitude(rs.getDouble("longitude"));
                casal.setImage(rs.getString("image"));
                casalCollection.getCasals().add(casal);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return casalCollection;
    }

    @Override
    public boolean deleteCasal(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.DELETE_CASAL);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public String getAdminId(String casalid) throws SQLException {
        String adminid = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_ADMINID);
            stmt.setString(1, casalid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                adminid = rs.getString(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return adminid;
    }
}