/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects;

import connections.DBConnection;
import dialogs.Dialogs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.TextArea;
import main.Main;

/**
 *
 * @author cmeehan
 */
public class Projects {

    public void getMemo(String id, TextArea memo, String projectId) {

    }

    public void setMemo(String id, String memo, String projectId) {

    }

    public void saveProject(String id, String version, int clientId, String projectName, String projectType, String projectManagers, boolean inBid, boolean pending, boolean inProgress, boolean complete, String employees, String notes) {
        Connection conn = new DBConnection().connect();
        String sql;
        // If we are updating a quote we are first going to archive the previous quote
        if (id != null) {
            archiveProject(id);
            sql = "UPDATE PROJECTS SET VERSION = ? CLIENT_ID = ?, PROJECT_NAME = ?, PROJECT_TYPE = ?, PROJECT_MANAGERS = ?, IN_BID = ?, PENDING = ?, IN_PROGRESS, COMPLETE = ?, EMPLOYEES = ?, NOTES = ?, UPDATED_BY = ?";

        } else {
            sql = "INSERT INTO PROJECTS(VERSION, CLIENT_ID, PROJECT_NAME, PROJECT_TYPE, PROJECT_MANAGERS, IN_BID, PENDING, IN_PROGRESS, COMPLETE, EMPLOYEES, NOTES, UPDATED_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, version);
            ps.setInt(2, clientId);
            ps.setString(3, projectName);
            ps.setString(4, projectType);
            ps.setString(5, projectManagers);
            ps.setBoolean(6, inBid);
            ps.setBoolean(7, pending);
            ps.setBoolean(8, inProgress);
            ps.setBoolean(9, complete);
            ps.setString(10, employees);
            ps.setString(11, notes);
            ps.setString(12, Main.USER_ID);
            ps.executeUpdate();
            ps.closeOnCompletion();
            ResultSet key = ps.getGeneratedKeys();
            if (key.next()) {
                Dialogs dialogs = new Dialogs();
                dialogs.notificationDialog("Save Project", "Project Saved", "Project " + String.valueOf(key.getInt(1)) + " successfully saved.").showAndWait();
            }
        } catch (SQLException ex) {
            ex.getMessage();
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.getMessage();
            }
        }
    }
    
    private void archiveProject(String id){
        Connection conn = new DBConnection().connect();
        String sql = "INSERT INTO PROJECT_ARCHIVES (PROJECT_ID, VERSION, CLIENT_ID, PROJECT_NAME, PROJECT_TYPE, PROJECT_MANAGERS, IN_BID, PENDING, IN_PROGRESS, COMPLETE, EMPLOYEES, NOTES, DATE_UPDATED, UPDATED_BY) VALUES(SELECT ID, VERSION, CLIENT_ID, PROJECT_NAME, PROJECT_TYPE, PROJECT_MANAGERS, IN_BID, PENDING, IN_PROGRESS, COMPLETE, EMPLOYEES, NOTES, DATE_UPDATED, UPDATED_BY FROM PROJECTS WHERE ID = ?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally{
            try{
                conn.close();
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
