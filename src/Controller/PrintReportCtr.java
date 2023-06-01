package Controller;

import Interface.PrintReport;
import Connection.ConnectionPool;
import Model.UserSession;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class PrintReportCtr implements ActionListener {
    PrintReport printnui;
    UserSession session;

    public PrintReportCtr() {
        printnui = new PrintReport();
        printnui.btnImprimir.addActionListener(this);
    }

    public PrintReportCtr(UserSession session) {
        this.session = session;
        printnui = new PrintReport();
        printnui.btnImprimir.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.printnui.btnImprimir) {
            StringBuilder reportTypes = new StringBuilder();
            try {
                int currentAdminID = session.getId(); // Obtener el ID del administrador desde la sesión

                if (this.printnui.jCheckBox1.isSelected()) {
                    reportTypes.append("1 ");
                    WriterThread hilo1 = new WriterThread("1");
                    hilo1.start();
                }
                if (this.printnui.jCheckBox2.isSelected()) {
                    reportTypes.append("2 ");
                    WriterThread hilo2 = new WriterThread("2");
                    hilo2.start();
                }
                if (this.printnui.jCheckBox3.isSelected()) {
                    reportTypes.append("3 ");
                    WriterThread hilo3 = new WriterThread("3");
                    hilo3.start();
                }
                if (this.printnui.jCheckBox4.isSelected()) {
                    reportTypes.append("4 ");
                    WriterThread hilo4 = new WriterThread("4");
                    hilo4.start();
                }
                if (this.printnui.jCheckBox5.isSelected()) {
                    reportTypes.append("5 ");
                    WriterThread hilo5 = new WriterThread("5");
                    hilo5.start();
                }

                String sqlAudit = String.format("INSERT INTO `audit_admin`(`id_admin`, `description`) VALUES ('%d','%s')",
                        currentAdminID, "Impresión de reporte: " + reportTypes.toString());
                new ConnectionPool().makeUpdate(sqlAudit);
            } catch (SQLException ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(null, "Ocurrio un error al generar el reporte"
                    + ", intente nuevamente", "Mensaje del sistema", 0);
            }
        }
    }
}
