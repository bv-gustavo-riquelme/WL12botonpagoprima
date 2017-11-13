package cl.bice.vida.botonpago.common.dto.parametros;

import java.io.Serializable;

/**
 * Tabla :  (BICEVIDA.BPI_CEM_CODEMPMEDIO_TBL)
 */
public class CodEmpByMedio  implements Serializable {

    /**Map empresas <-> bicevida.datamodel.parametros.Empresas
     * @associates <{bicevida.datamodel.parametros.Empresas}>
     */
    private Empresas empresas;

    /**Map medioPago <-> bicevida.datamodel.parametros.MedioPago
     * @associates <{bicevida.datamodel.parametros.MedioPago}>
     */
    private MedioPago medioPago;
    private Integer codMedio;
    private Integer codEmpresa;
    private Long IDCOM;
    private Integer SRVREC;
    private String url;

    public CodEmpByMedio() {
        super();
        this.empresas = new Empresas();
        this.medioPago = new MedioPago();
    }

    public Integer getCodEmpresa() {
        return this.codEmpresa;
    }

    public Integer getCodMedio() {
        return this.codMedio;
    }


    public Empresas getEmpresas() {
        return (Empresas)this.empresas;
    }

    public MedioPago getMedioPago() {
        return (MedioPago)this.medioPago;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public void setCodMedio(Integer codMedio) {
        this.codMedio = codMedio;
    }


    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setIDCOM(Long iDCOM) {
        this.IDCOM = iDCOM;
    }

    public Long getIDCOM() {
        return IDCOM;
    }

    public void setSRVREC(Integer sRVREC) {
        this.SRVREC = sRVREC;
    }

    public Integer getSRVREC() {
        return SRVREC;
    }
}
