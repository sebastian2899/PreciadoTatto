package com.mycompany.myapp.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "es";
    public static final String ORDENAR_CITAS_PORfECHA = "SELECT c FROM CitaTatto c  ORDER BY fecha_cita DESC";

    public static final String ORDENAR_CITASPERF_PORFECHA = "SELECT c FROM CitaPerforacion c  ORDER BY fecha_cita DESC";

    public static final String TOTAL_PRODUCTOS = "SELECT COUNT(*) FROM Producto p";
    public static final String TOTAL_VENTAS_HOY = "SELECT COUNT(*) FROM Ventas v WHERE TO_CHAR(v.fechaCreacion,'dd/MM/yyyy') = :fecha";
    public static final String TOTAL_COMPRAS_HOY = "SELECT COUNT(*) FROM Compras c WHERE TO_CHAR(c.fechaCreacion,'dd/MM/yyyy') = :fecha";

    private Constants() {}
}
