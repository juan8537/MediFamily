/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criterioc;

/**
 *
 * @author camey
 */
public class a {
    public static void main(String[] args) {
        
        recordatorio.startReminderService();
        
        
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Servicio interrumpido");
                break;
            }
        }
    }
    
}
