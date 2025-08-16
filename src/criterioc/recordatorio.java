/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criterioc;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
/**
 *
 * @author camey
 */
public class recordatorio {
    private static final long CHECK_INTERVAL = 60000; // Revisar cada 1 minuto

    public static void startReminderService() {
        Timer timer = new Timer(true); 
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndSendReminders();
            }
        }, 0, CHECK_INTERVAL);
    }

    private static void checkAndSendReminders() {
        System.out.println("[" + new Date() + "] Verificando recordatorios...");
        ConeccionBD BD = null;
        ResultSet rs = null;
        
        try {
            BD = new ConeccionBD(
                "jdbc:mysql://localhost:3306/criterio c?useSSL=false",
                "root", "infobi");
            BD.getNewConnection();

            String query = "SELECT m.Nombre_medicamento, m.Dosis, m.Hora, m.Frecuencia, u.Nombre, u.Correo " +
          "FROM medicamentos m " +
          "JOIN usuario u ON m.Usuario_Nombre = u.Nombre " + 
          "WHERE TIMESTAMPDIFF(MINUTE, NOW(), m.Hora) BETWEEN 0 AND m.Frecuencia";
            
            System.out.println("Consulta SQL: " + query); // Para depuración

            Statement stmt = BD.getCurrentConnection().createStatement();
            rs = stmt.executeQuery(query);
            boolean hasReminders = false;

            while (rs.next()) {
                String medName = rs.getString("Nombre_medicamento");
                String dose = rs.getString("Dosis");
                String schedule = rs.getString("Hora");
                String patientName = rs.getString("Nombre");
                String email = rs.getString("Correo");

                Correo.sendMedicationReminder(email, patientName, medName, dose, schedule);
            }
            if(hasReminders) {
                System.out.println("Encontrado recordatorio para enviar");
            } else {
                System.out.println("No hay recordatorios pendientes");
            }
            
        } catch (Exception e) {
            System.err.println("Error en recordatorios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (BD != null) BD.closeConnection();
            } catch (Exception e) {
                System.err.println("Error al cerrar conexiones: " + e.getMessage());
            }
    }
    }
}
    

