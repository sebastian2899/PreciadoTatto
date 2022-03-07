package com.mycompany.myapp.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "es";
    public static final String ORDENAR_CITAS_PORfECHA = " ORDER BY c.fechaCita DESC";

    public static final String ORDENAR_CITASPERF_PORFECHA = " ORDER BY fechaCita DESC";

    public static final String TOTAL_PRODUCTOS = "SELECT COUNT(*) FROM Producto p";
    public static final String TOTAL_VENTAS_HOY = "SELECT COUNT(*) FROM Ventas v WHERE TO_CHAR(v.fechaCreacion,'dd/MM/yyyy') = :fecha";
    public static final String TOTAL_COMPRAS_HOY = "SELECT COUNT(*) FROM Compras c WHERE TO_CHAR(c.fechaCreacion,'dd/MM/yyyy') = :fecha";

    public static final String CONSULTAR_PRODUCTOS_VENTAS = "SELECT COUNT(*) FROM ProductosVendidos p WHERE p.productoId = :productoId";

    public static final String PRODUCTO_BASE = "SELECT p FROM Producto p WHERE p.id IS NOT NULL";
    public static final String PRODUCTO_NOMBRE = " AND UPPER(p.nombre) LIKE :nombre";

    public static final String CITA_TATTO_BASE = "SELECT c FROM CitaTatto c WHERE c.id IS NOT NULL";
    public static final String CITA_TATTO_NOMBRE = " AND UPPER(c.infoCliente) LIKE :nombre";
    public static final String CITA_TATTO_HORA = " AND c.hora = :hora";
    public static final String CITA_TATTO_FECHA = "SELECT c FROM CitaTatto c WHERE TO_CHAR(fechaCita, 'yyyy-MM-dd')=:fechaCita";

    public static final String CITA_PERFORACION_BASE = "SELECT c FROM CitaPerforacion c WHERE c.id IS NOT NULL";
    public static final String CITA_PERFORACION_NOMBRE = " AND UPPER(c.nombreCliente) LIKE :nombre";
    public static final String CITA_PERFORACION_HORA = " AND (c.hora)=:hora";
    public static final String CITA_PERFORACION_FECHA =
        "SELECT c FROM CitaPerforacion c WHERE TO_CHAR(c.fechaCita,'yyyy-MM-dd')=:fechaCita";
    public static final String ELIMINAR_ABONOS_POR_CITA = "DELETE FROM Abono WHERE idCita = :idCita";

    public static final String ELIMINAR_VALORES_CAJA_POR_CITA =
        "UPDATE CajaTattos SET valorTattoDia = valorTattoDia-:valorDescontar " + "WHERE TO_CHAR(fechaCreacion,'dd/MM/yyyy')=:fechaAbono";

    public static final String ELIMINAR_VALORES_CAJA_POR_CITA_PERFO =
        "UPDATE CajaIngresos SET valorVendidoDia = valorVendidoDia-:descontar" + " WHERE TO_CHAR(fechaCreacion,'dd/MM/yyyy') =:fechaCita";

    // AND TO_CHAR(fechaCreacion,'dd/MM/yyyy') =:abonofechaPerfo

    public static final String ELIMINAR_VALORES_CAJA_POR_CITA_PERFO_ABONO = " AND TO_CHAR(fechaCreacion,'dd/MM/yyyy') =:abonofechaPerfo";

    public static final String PRODUCTOS_AGOTADOS = "SELECT p FROM Producto p WHERE p.cantidad = 0";

    public static final String FOTO_DISENIO_BASE = "SELECT g FROM GaleriaFotos g WHERE g.id IS NOT NULL";
    public static final String FOTO_DISENIO_NOMBRE = " AND UPPER(g.nombreDisenio) LIKE :nombre";
    public static final String FOTO_DISENIO_PRECIO = " AND g.precioDisenio = :precio";
    public static final String FOTO_DISENIO_MENOR_PRECIO = "SELECT g FROM GaleriaFotos g ORDER BY g.precioDisenio ASC";
    public static final String FOTO_DINSENIO_MAYOR_PRECIO = "SELECT g FROM GaleriaFotos g ORDER BY g.precioDisenio DESC ";

    private Constants() {}
}
