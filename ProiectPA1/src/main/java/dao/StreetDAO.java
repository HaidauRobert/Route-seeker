package dao;

import database.Database;
import models.Node;
import models.Street;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StreetDAO {

    public boolean checkStreetExistence(Street street) throws SQLException {
        Connection con = Database.getConnection();


        try (Statement streetStmt = con.createStatement();
             ResultSet rs = streetStmt.executeQuery(
                     "select count(id) from streets where name ='" + street.getName() + "'"+ "and nr_map='" + street.getNrMap() + "'" ))  {

            int countStreets = 0;

            while (rs.next()) {
                countStreets = (rs.getInt(1));
            }
            if (countStreets == 0) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            con.close();
        }
        return false;
    }

    public void createStreet(Street newStreet) throws SQLException {

        Connection con = Database.getConnection();
        if(!checkStreetExistence(newStreet)) {
            try (PreparedStatement streetStmt = con.prepareStatement(
                    "insert into streets (name, id_node_1, id_node_2, length, nr_map) values (?, ?, ?, ?, ?)")) {
                streetStmt.setString(1, newStreet.getName());
                streetStmt.setInt(2, newStreet.getIdNodeStart());
                streetStmt.setInt(3, newStreet.getIdNodeEnd());
                streetStmt.setInt(4, newStreet.calculateLength(newStreet.getIdNodeStart(), newStreet.getIdNodeEnd()));
                streetStmt.setInt(5, newStreet.getNrMap());
                streetStmt.executeUpdate();
                con.commit();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                con.close();
            }
        }
    }

    public List<Street> generateStreets(int nrMap) throws SQLException {

        List<Street> streets = new ArrayList<>();
        Connection con = Database.getConnection();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select id, name, id_node_1, id_node_2, length, nr_map from streets where nr_map='" + nrMap + "'")) {
            while (rs.next()) {
                Street street = new Street(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6));
                streets.add(street);
            }

        }
        finally {
            con.close();
        }
        return streets;
    }

}
