package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;


public class TransaccionMedioByEmpresaByMedio2 implements Serializable {
    public TransaccionMedioByEmpresaByMedio2() {
    }

    private Long codEmpresa;
    private Long codMedio;
    private String nommedio;
    private Long codMedio2;
    private String nommedio2;
    private String rut;
    private String nombre;
    private String nomproducto;
    private Long producto;
    private Long idtransaccion;
    private Long idcuadratura;
    private Date fechacuadratura;
    private Date turno;
    private Long monto_total;
    
    private Integer numeroPropuesta;


    public void setCodEmpresa(Long codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Long getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodMedio(Long codMedio) {
        this.codMedio = codMedio;
    }

    public Long getCodMedio() {
        return codMedio;
    }

    public void setNommedio(String nommedio) {
        this.nommedio = nommedio;
    }

    public String getNommedio() {
        return nommedio;
    }

    public void setCodMedio2(Long codMedio2) {
        this.codMedio2 = codMedio2;
    }

    public Long getCodMedio2() {
        return codMedio2;
    }

    public void setNommedio2(String nommedio2) {
        this.nommedio2 = nommedio2;
    }

    public String getNommedio2() {
        return nommedio2;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getRut() {
        return rut;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNomproducto(String nomproducto) {
        this.nomproducto = nomproducto;
    }

    public String getNomproducto() {
        return nomproducto;
    }

    public void setProducto(Long producto) {
        this.producto = producto;
    }

    public Long getProducto() {
        return producto;
    }

    public void setIdtransaccion(Long idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public Long getIdtransaccion() {
        return idtransaccion;
    }

    public void setIdcuadratura(Long idcuadratura) {
        this.idcuadratura = idcuadratura;
    }

    public Long getIdcuadratura() {
        return idcuadratura;
    }

    public void setFechacuadratura(Date fechacuadratura) {
        this.fechacuadratura = fechacuadratura;
    }

    public Date getFechacuadratura() {
        return fechacuadratura;
    }

    public void setTurno(Date turno) {
        this.turno = turno;
    }

    public Date getTurno() {
        return turno;
    }

    public void setMonto_total(Long monto_total) {
        this.monto_total = monto_total;
    }

    public Long getMonto_total() {
        return monto_total;
    }

    public void setNumeroPropuesta(Integer numeroPropuesta) {
        this.numeroPropuesta = numeroPropuesta;
    }

    public Integer getNumeroPropuesta() {
        return numeroPropuesta;
    }
}


/*


 SELECT  tt.COD_EMPRESA, tt.COD_MEDIO, tt.NOMBRE, tt.COD_MEDIO2, tt.DESCRIPCION, tt.RUT_PERSONA, 
         tt.NOMBRE_PERSONA, tt.NOM_PRODUCTO, tt.NUM_PRODUCTO, tt.CUOTA, tt.ID_TRANSACCION,
         tt.ID_CUADRATURA, tt.FECHA_PAGO, tt.TURNO, tt.MONTO_TOTAL      
 FROM
 (
     SELECT  t0.ID_TRANSACCION, t0.ID_CUADRATURA, t0.COD_EMPRESA, t0.COD_MEDIO, t1.NOMBRE,
             t0.COD_MEDIO2, t2.DESCRIPCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,
             t0.NOMBRE_PERSONA, t0.NOMBRE AS NOM_PRODUCTO, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO,
             t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO
     FROM    BPI_DTC_DETTRANCUAD_VW t0,
             BPI_MPG_MEDIOPAGO_TBL t1,
             BPI_BCO_BANCOS_TBL t2
     WHERE   t1.COD_MEDIO = t0.COD_MEDIO
     AND     t2.CODIGO = t0.COD_MEDIO2
     AND     t0.COD_MEDIO = 3
     UNION
     SELECT  t0.ID_TRANSACCION, t0.ID_CUADRATURA, t0.COD_EMPRESA, t0.COD_MEDIO, t1.NOMBRE,
             t0.COD_MEDIO2, t1.NOMBRE AS DESCRIPCION, t0.NUM_PRODUCTO, t0.CUOTA, t0.MONTO_TOTAL,
             t0.NOMBRE_PERSONA, t0.NOMBRE AS NOM_PRODUCTO, t0.FECHA_HORA, t0.NUM_TRANSACCION_MEDIO,
             t0.COD_PRODUCTO, t0.RUT_PERSONA, t0.FECHA_PAGO, t0.TURNO
     FROM    BPI_DTC_DETTRANCUAD_VW t0,
             BPI_MPG_MEDIOPAGO_TBL t1
     WHERE   t1.COD_MEDIO = t0.COD_MEDIO
     AND     t0.COD_MEDIO2 IS NULL
     AND     t0.COD_MEDIO <> 3
 ) tt
 WHERE     tt.COD_EMPRESA = 1
 AND   tt.ID_CUADRATURA =
 AND   tt.FECHA_HORA BETWEEN to_date('01/02/2008 00:00:00','DD/MM/YYYY HH24:MI:SS') AND to_date('10/07/2009 23:59:59','DD/MM/YYYY HH24:MI:SS')
 ORDER BY  tt.COD_MEDIO ASC, tt.COD_MEDIO2 ASC, tt.ID_TRANSACCION DESC

;

*/