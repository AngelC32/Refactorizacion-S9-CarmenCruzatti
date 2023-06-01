package Controller;

import Connection.ConnectionPool;
import Interface.AuditUser;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class AuditUserCtr {

    private AuditUser auditUserUI;
    private DefaultTableModel model;

    public AuditUserCtr() {
        auditUserUI = new AuditUser();
        model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Usuario");
        model.addColumn("Descripcion");
        model.addColumn("Fecha");

        auditUserUI.jTableModifications.setModel(model);
        fetchData();
    }

    private void clearTable() {
        model.setRowCount(0);
    }

    private void fetchData() {
        try {
            List<Map<String, Object>> resultList = new ConnectionPool().makeConsult("SELECT audit_user.id_audit, audit_user.timestamp, audit_user.description, users.name_user FROM audit_user JOIN users ON audit_user.id_user = users.id_user");
            for (Map<String, Object> result : resultList) {
                int id = (int) result.get("id_audit");
                String description = (String) result.get("description");
                String timestamp = "";
                Object timestampObj = result.get("timestamp");
                if (timestampObj != null) {
                    timestamp = timestampObj.toString();
                }
                String nameUser = (String) result.get("name_user");
                model.addRow(new Object[]{id, nameUser, description, timestamp});
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener datos de auditoría de usuarios: " + ex.getMessage());
        }
    }

}
