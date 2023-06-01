/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Connection.ConnectionPool;
import Interface.AuditAdmin;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class AuditAdminCtr {

    private AuditAdmin auditAdminUI;
    private DefaultTableModel model;

    public AuditAdminCtr() {
        auditAdminUI = new AuditAdmin();
        model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Admin");
        model.addColumn("Descripcion");
        model.addColumn("Fecha");

        auditAdminUI.jTableModifications.setModel(model);
        fetchData();
    }

    private void clearTable() {
        model.setRowCount(0);
    }

    private void fetchData() {
        try {
            List<Map<String, Object>> resultList = new ConnectionPool().makeConsult("SELECT audit_admin.id_audit, audit_admin.timestamp, audit_admin.description, admins.name_admin FROM audit_admin JOIN admins ON audit_admin.id_admin = admins.id_admin");
            for (Map<String, Object> result : resultList) {
                int id = (int) result.get("id_audit");
                String description = (String) result.get("description");
                String timestamp = "";
                Object timestampObj = result.get("timestamp");
                if (timestampObj != null) {
                    timestamp = timestampObj.toString();
                }
                String nameAdmin = (String) result.get("name_admin");
                model.addRow(new Object[]{id, nameAdmin, description, timestamp});
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener datos de auditoría de administradores: " + ex.getMessage());
        }
    }

}
