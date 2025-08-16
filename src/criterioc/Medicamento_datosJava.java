/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criterioc;
import java.util.Date;
/**
 *
 * @author camey
 */
public class Medicamento_datosJava {
    private String nombre;
    private String dosis;
    private Date hora;
    private int frecuencia; 
    private Date ultimaNotificacion;
    
    
    public Medicamento_datosJava(String nombre, Date hora, int frecuencia) {
        this.nombre = nombre;
        this.hora = hora;
        this.frecuencia = frecuencia;
        this.ultimaNotificacion = null;
    }

    
    public String getNombre() {
        return nombre;
    }

    public Date getHora() {
        return hora;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Date getUltimaNotificacion() {
        return ultimaNotificacion;
    }

    public void setUltimaNotificacion(Date ultimaNotificacion) {
        this.ultimaNotificacion = ultimaNotificacion;
    }
    
    
    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }
}
    

