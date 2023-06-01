/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import java.sql.SQLException;

import Connection.ConnectionPool;
import Interface.ModificationReport;
import Model.User;
import Model.UserSession;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mildr
 */
public class UserReadFileCtr {
    ModificationReport reporte;
    String acumulador = "";
    public User usuario = Model.User.usuario;
    public DefaultTableModel model = new DefaultTableModel();
    public UserSession userSession;

    public UserReadFileCtr(UserSession userSession) {
        this.userSession = userSession;
        reporte = new ModificationReport();
        model.addColumn("Id PC");
        model.addColumn("Estado");
        model.addColumn("Modificacion");
        model.addColumn("Laboratorio");
        reporte.jTableModifications.setModel(model);
    }

    public void getData() {
        try {
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 7777);
            RMI interfaz = (RMI) registro.lookup("RemotoRMI");

            List<String> respuesta = interfaz.leerModificaciones(usuario.getName_user());
            for (String string : respuesta) {
                String[] datos = string.split(",");
                model.addRow(new Object[]{datos[1], datos[2], datos[3], datos[4]});
            }

            // Registrar en la tabla de auditoría
            int userId = userSession.getId();
            String description = "Lectura de reporte";
            insertAuditLog(userId, description);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void insertAuditLog(int userId, String description) {
        String sql = String.format("INSERT INTO `audit_user` (`id_user`, `description`) VALUES (%d, '%s')", userId, description);
        try {
            new ConnectionPool().makeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}