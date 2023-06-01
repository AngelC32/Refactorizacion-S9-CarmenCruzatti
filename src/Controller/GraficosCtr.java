package Controller;

import Connection.ConnectionPool;
import Interface.Graficos;
import Model.UserSession;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.security.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Clase controladora para la generación de gráficos.
 */
public class GraficosCtr implements ActionListener {

    public static String user_update = "";
    public Graficos graficosui;
    public DefaultTableModel model = new DefaultTableModel();
    public List<ModificateThread> hilos = new ArrayList<ModificateThread>();
    UserSession session;

    public int id_pc = -1;
    public String estado = null;
    public int id_labo = -1;
    public Timestamp mod = null;
    public String obs = null;
    public int numero1;
    
    public GraficosCtr() {
        graficosui = new Graficos();
        graficosui.botonGraficos.addActionListener(this);
        graficosui.tablaDatos = new JTable(model);
        graficosui.jScrollPane2.setViewportView(graficosui.tablaDatos);
        model.addColumn("Id PC");
        model.addColumn("Estado");
        model.addColumn("Id Laboratorio");
        model.addColumn("Fecha de Modificación");
        model.addColumn("Observación");
        numero1 = Integer.parseInt( JOptionPane.showInputDialog(
                null,"¿De qué año desea obtener data?",
                "Filtrado de información - años",
                JOptionPane.QUESTION_MESSAGE) );
    }

    public GraficosCtr(UserSession userSession) {
        this();
        this.session = userSession;
    }

    public void limpiarTabla(DefaultTableModel modelo, JTable tabla) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == graficosui.botonGraficos) {
            int operativo = 0;
            int defectuosa = 0;
            int mantenimiento = 0;
            limpiarTabla(model, graficosui.tablaDatos);

            try {
                List<Map<String, Object>> resultList = new ArrayList<>();
                String sql = String.format(
                        "select id_pc, estado, id_lab, fecha_mod,obs from computer where YEAR(fecha_mod) = " + numero1);
                //System.out.println(sql);

                try {
                    resultList = new ConnectionPool().makeConsult(sql);

                    for (int i = 0; i < resultList.size(); i++) {
                        model.addRow(new Object[]{
                            String.valueOf(resultList.get(i).get("id_pc")),
                            String.valueOf(resultList.get(i).get("estado")),
                            String.valueOf(resultList.get(i).get("id_lab")),
                            String.valueOf(resultList.get(i).get("fecha_mod")),
                            String.valueOf(resultList.get(i).get("obs"))
                        });
                    }
                } catch (SQLException ex) {
                    System.err.println("Error al llenar tabla" + ex);
                    JOptionPane.showMessageDialog(null, "Ocurrio un error al mostrar la información"
                    + ", intente nuevamente", "Mensaje del sistema", 0);
                    //System.out.println(ex);
                }

                graficosui.jPanel1.removeAll();
                graficosui.jPanel3.removeAll();
                graficosui.jPanel2.removeAll();
                GraphicPieThread pie = new GraphicPieThread(graficosui, resultList);
                pie.start();
                GraphicBarThreadCondtion bar = new GraphicBarThreadCondtion(graficosui, resultList);
                bar.start();
                GraphicBarThreadLab bar2 = new GraphicBarThreadLab(graficosui, resultList);
                bar2.start();
                graficosui.repaint();

                // Registrar la auditoría "Generacion de graficos"
                String sqlAudit = String.format("INSERT INTO `audit_admin`(`id_admin`, `description`) VALUES ('%d','%s')",
                        session.getId(), "Generacion de graficos");
                try {
                    new ConnectionPool().makeUpdate(sqlAudit);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } catch (Exception exc) {
                System.err.println(exc);
            }
        }
    }
}
