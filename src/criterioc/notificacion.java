/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criterioc;
import java.util.TimerTask;
import java.util.Date;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
/**
 *
 * @author camey
 */
public class notificacion extends TimerTask {
    private List<Medicamento_datosJava> medicamentos;

    public notificacion(List<Medicamento_datosJava> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public void run() {
        Instant ahora = Instant.now(); 
        
        for (Medicamento_datosJava med : medicamentos) {
            if (med == null || med.getHora() == null) {
                continue;
            }
            
            // Convertimos Date a Instant para usar la nueva API
            Instant horaMedicamento = med.getHora().toInstant();
            
            //  la diferencia usando Duration
            long diffInHours = Duration.between(horaMedicamento, ahora).toHours();
            
            if (med.getFrecuencia() > 0 && diffInHours % med.getFrecuencia() == 0) {
                Instant ultimaNotif = med.getUltimaNotificacion() != null ? 
                                    med.getUltimaNotificacion().toInstant() : null;
                
                if (ultimaNotif == null ||
                    Duration.between(ultimaNotif, ahora).toHours() >= med.getFrecuencia()) {
                    
                    System.out.println("¡Recordatorio! Toma: " + med.getNombre() + 
                                     " - Dosis: " + med.getDosis() + 
                                     " - Próxima dosis en: " + med.getFrecuencia() + " horas");
                    
                    med.setUltimaNotificacion(Date.from(ahora)); 
                }
            }
        }
    }
}
    
    

