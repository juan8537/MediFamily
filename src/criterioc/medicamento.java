/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criterioc;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.util.Date;
import java.sql.PreparedStatement;

/**
 *
 * @author camey
 */
public class medicamento extends javax.swing.JFrame {
    
    
    private static String basededatos = "criterio c";
    private static String contrasena = "infobi";
    private static String servidor = "localhost";
    private static String usuario = "root";
    
    private String normalizarRutaImagen(String ruta) {
    if (ruta == null) return "";
    
    ruta = ruta.replace("\\", "/");
    
    if (ruta.startsWith("C:Users")) {
        ruta = "C:/Users/" + ruta.substring(7);
    }
    if (ruta.startsWith("C:/Userscamey")) {
        ruta = "C:/Users/camey/" + ruta.substring(13);
    }
    
    if (ruta.contains("cameyDownloads")) {
        ruta = ruta.replace("cameyDownloads", "camey/Downloads");
    }
    
    return ruta.trim();
}
    
    private Date ultimaNotificacion;
    public Date getUltimaNotificacion() {
        return ultimaNotificacion;
    }
    public void setUltimaNotificacion(Date ultimaNotificacion) {
        this.ultimaNotificacion = ultimaNotificacion;
    }
    public medicamento(int idMedicamento) {
        initComponents();
        this.setUltimaNotificacion(new Date());
        mostrarMedicamentoPorId(idMedicamento); //  método
        setLocationRelativeTo(null);
        setTitle("Detalles del Medicamento");
    }
    
    private void mostrarUltimoMedicamento() {
    try {
        
        
        java.sql.Connection conexion = java.sql.DriverManager.getConnection(
    "jdbc:mysql://" + servidor + "/" + basededatos + "?useSSL=false&allowPublicKeyRetrieval=true", 
    usuario, 
    contrasena
);
        
        String sql = "SELECT * FROM medicamentos ORDER BY ID_Medicamento DESC LIMIT 1";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        if (rs.next()) {
            txt_nombre.setText(rs.getString("Nombre_medicamento"));
            txt_dosis.setText(rs.getString("dosis"));
            txt_horario.setText(rs.getString("hora"));
            txt_paciente.setText(rs.getString("Usuario_nombre"));

            String rutaImagen = rs.getString("imagen");
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                ImageIcon imagenOriginal = new ImageIcon(rutaImagen);
                Image imagenRedimensionada = imagenOriginal.getImage().getScaledInstance(
                    lbl_imagen.getWidth(), lbl_imagen.getHeight(), Image.SCALE_SMOOTH);
                lbl_imagen.setIcon(new ImageIcon(imagenRedimensionada));
            } else {
                lbl_imagen.setIcon(null);
            }
            this.setUltimaNotificacion(new Date());
        } else {
            JOptionPane.showMessageDialog(this, "No hay medicamentos registrados.");
        }

        rs.close();
        stmt.close();
        conexion.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al mostrar el medicamento: " + e.getMessage());
    }
}
    
    private void mostrarMedicamentoPorId(int idMedicamento) {
        java.sql.Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
        conexion = java.sql.DriverManager.getConnection(
            "jdbc:mysql://" + servidor + "/" + basededatos + 
            "?useSSL=false&allowPublicKeyRetrieval=true", 
            usuario, contrasena);
        
        String sql = "SELECT * FROM medicamentos WHERE ID_Medicamento = ?";
        stmt = conexion.prepareStatement(sql);
        stmt.setInt(1, idMedicamento);
        
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            txt_id.setText(String.valueOf(rs.getInt("ID_Medicamento"))); 
            txt_nombre.setText(rs.getString("Nombre_medicamento"));
            txt_dosis.setText(rs.getString("dosis"));
            txt_horario.setText(rs.getString("hora"));
            txt_paciente.setText(rs.getString("Usuario_nombre"));
            txt_frecuencia.setText(rs.getString("Frecuencia"));
            
            String rutaImagen = rs.getString("imagen");
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                rutaImagen = normalizarRutaImagen(rutaImagen);
                System.out.println("Ruta final normalizada: " + rutaImagen);
                
                File archivoImagen = new File(rutaImagen);
                if (archivoImagen.exists()) {
                    cargarImagenDesdeArchivo(rutaImagen);
                } else {
                    System.out.println("El archivo no existe en: " + rutaImagen);
                    cargarImagenPorDefecto();
                }
            } else {
                cargarImagenPorDefecto();
            }
            
            this.setUltimaNotificacion(new Date());
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró medicamento con ID: " + idMedicamento);
            this.dispose();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al mostrar el medicamento: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        try { if (conexion != null) conexion.close(); } catch (Exception e) {}
    }
    }
    
    private void cargarImagenPorDefecto() {
    try {
        java.net.URL imgUrl = getClass().getResource("/img/default.png");
        if (imgUrl != null) {
            ImageIcon imagenDefault = new ImageIcon(imgUrl);
            Image img = imagenDefault.getImage().getScaledInstance(
                lbl_imagen.getWidth(), lbl_imagen.getHeight(), Image.SCALE_SMOOTH);
            lbl_imagen.setIcon(new ImageIcon(img));
        } else {
            ImageIcon imagenDefault = new ImageIcon("src/main/resources/img/default.png");
            Image img = imagenDefault.getImage().getScaledInstance(
                lbl_imagen.getWidth(), lbl_imagen.getHeight(), Image.SCALE_SMOOTH);
            lbl_imagen.setIcon(new ImageIcon(img));
        }
    } catch (Exception e) {
        lbl_imagen.setText("Imagen no disponible");
        System.out.println("Error cargando imagen por defecto: " + e.getMessage());
    }
}
    private void cargarImagenDesdeArchivo(String ruta) {
    try {
        ImageIcon imagenOriginal = new ImageIcon(ruta);
        Image imagenRedimensionada = imagenOriginal.getImage()
            .getScaledInstance(lbl_imagen.getWidth(), lbl_imagen.getHeight(), Image.SCALE_SMOOTH);
        lbl_imagen.setIcon(new ImageIcon(imagenRedimensionada));
    } catch (Exception e) {
        System.out.println("Error al cargar imagen: " + e.getMessage());
        cargarImagenPorDefecto();
    }
}
    /**
     * Creates new form medicamento
     */
    public medicamento() {
        initComponents();
        this.setUltimaNotificacion(new Date());
        mostrarUltimoMedicamento();
        setLocationRelativeTo(null);
        setTitle("Medicamentos");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_tt = new javax.swing.JLabel();
        lbl_imagen = new javax.swing.JLabel();
        lbl_tt2 = new javax.swing.JLabel();
        lbl_nombre = new javax.swing.JLabel();
        lbl_dosis = new javax.swing.JLabel();
        lbl_hora = new javax.swing.JLabel();
        lbl_paciente = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        txt_dosis = new javax.swing.JTextField();
        txt_horario = new javax.swing.JTextField();
        txt_paciente = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        lbl_frecuencia = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        txt_frecuencia = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbl_tt.setFont(new java.awt.Font("Trebuchet MS", 3, 18)); // NOI18N
        lbl_tt.setText("Medicamentos");

        lbl_imagen.setText("Imagen");

        lbl_tt2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        lbl_tt2.setText("Datos:");

        lbl_nombre.setText("Nombre Medicamento");

        lbl_dosis.setText("Dosis:");

        lbl_hora.setText("Horario:");

        lbl_paciente.setText("Pacientes:");

        lbl_id.setText("ID Medicamento:");

        lbl_frecuencia.setText("Frecuencia");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_tt, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(lbl_imagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_tt2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_nombre)
                            .addComponent(lbl_dosis)
                            .addComponent(lbl_hora)
                            .addComponent(lbl_paciente)
                            .addComponent(lbl_id)
                            .addComponent(lbl_frecuencia))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addComponent(txt_dosis)
                            .addComponent(txt_horario)
                            .addComponent(txt_paciente)
                            .addComponent(txt_id)
                            .addComponent(txt_frecuencia))))
                .addContainerGap(160, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_tt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_tt2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_nombre)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_dosis)
                            .addComponent(txt_dosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_hora)
                            .addComponent(txt_horario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_paciente)
                            .addComponent(txt_paciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_id)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_frecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_frecuencia))))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(medicamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(medicamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(medicamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(medicamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new medicamento().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbl_dosis;
    private javax.swing.JLabel lbl_frecuencia;
    private javax.swing.JLabel lbl_hora;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_imagen;
    private javax.swing.JLabel lbl_nombre;
    private javax.swing.JLabel lbl_paciente;
    private javax.swing.JLabel lbl_tt;
    private javax.swing.JLabel lbl_tt2;
    private javax.swing.JTextField txt_dosis;
    private javax.swing.JTextField txt_frecuencia;
    private javax.swing.JTextField txt_horario;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_paciente;
    // End of variables declaration//GEN-END:variables
}

