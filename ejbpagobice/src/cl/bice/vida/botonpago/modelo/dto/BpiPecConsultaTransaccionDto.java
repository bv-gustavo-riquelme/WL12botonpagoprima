package cl.bice.vida.botonpago.modelo.dto;

import java.io.Serializable;

import java.util.Date;


public class BpiPecConsultaTransaccionDto implements Serializable
{
	/** 
	 * This attribute maps to the column ID_CONSULTA in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Long idConsulta;

	/** 
	 * This attribute maps to the column ID_TRANSACCION_BOTON in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Long idTransaccionBoton;

	/** 
	 * This attribute maps to the column ID_NAVEGACION_BOTON in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Long idNavegacionBoton;

	/** 
	 * This attribute maps to the column ID_LINEA_NEGOCIO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Integer idLineaNegocio;

	/** 
	 * This attribute maps to the column FECHA_DOCUMENTO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Date fechaDocumento;

	/** 
	 * This attribute maps to the column NUMERO_DOCUMENTO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected String numeroDocumento;

	/** 
	 * This attribute maps to the column NUMERO_CONTRATO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected String numeroContrato;

	/** 
	 * This attribute maps to the column MONTO_MINIMO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Double montoMinimo;

	/** 
	 * This attribute maps to the column MONTO_MAXIMO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Double montoMaximo;

	/** 
	 * This attribute maps to the column ID_ESTADO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Integer idEstado;

	/** 
	 * This attribute maps to the column MONTO_PAGADO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Double montoPagado;

	/** 
	 * This attribute maps to the column FECHA_HORA_PAGO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected Date fechaHoraPago;

	/** 
	 * This attribute maps to the column DESCRIPCION_PRODUCTO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected String descripcionProducto;

	/** 
	 * This attribute maps to the column CODIGO_AUTORIZACION_MEDIO in the BPI_PEC_CONSULTA_TRANSACCION table.
	 */
	protected String codigoAutorizacionMedio;
        
        
        protected String instrumentoCaja;
        
        

	/**
	 * Method 'BpiPecConsultaTransaccion'
	 * 
	 */
	public BpiPecConsultaTransaccionDto()
	{
	}

	/**
	 * Method 'getIdConsulta'
	 * 
	 * @return Long
	 */
	public Long getIdConsulta()
	{
		return idConsulta;
	}

	/**
	 * Method 'setIdConsulta'
	 * 
	 * @param idConsulta
	 */
	public void setIdConsulta(Long idConsulta)
	{
		this.idConsulta = idConsulta;
	}

	/**
	 * Method 'getIdTransaccionBoton'
	 * 
	 * @return Long
	 */
	public Long getIdTransaccionBoton()
	{
		return idTransaccionBoton;
	}

	/**
	 * Method 'setIdTransaccionBoton'
	 * 
	 * @param idTransaccionBoton
	 */
	public void setIdTransaccionBoton(Long idTransaccionBoton)
	{
		this.idTransaccionBoton = idTransaccionBoton;
	}

	/**
	 * Method 'getIdNavegacionBoton'
	 * 
	 * @return Long
	 */
	public Long getIdNavegacionBoton()
	{
		return idNavegacionBoton;
	}

	/**
	 * Method 'setIdNavegacionBoton'
	 * 
	 * @param idNavegacionBoton
	 */
	public void setIdNavegacionBoton(Long idNavegacionBoton)
	{
		this.idNavegacionBoton = idNavegacionBoton;
	}

	/**
	 * Method 'getIdLineaNegocio'
	 * 
	 * @return Integer
	 */
	public Integer getIdLineaNegocio()
	{
		return idLineaNegocio;
	}

	/**
	 * Method 'setIdLineaNegocio'
	 * 
	 * @param idLineaNegocio
	 */
	public void setIdLineaNegocio(Integer idLineaNegocio)
	{
		this.idLineaNegocio = idLineaNegocio;
	}

	/**
	 * Method 'getFechaDocumento'
	 * 
	 * @return Date
	 */
	public Date getFechaDocumento()
	{
		return fechaDocumento;
	}

	/**
	 * Method 'setFechaDocumento'
	 * 
	 * @param fechaDocumento
	 */
	public void setFechaDocumento(Date fechaDocumento)
	{
		this.fechaDocumento = fechaDocumento;
	}

	/**
	 * Method 'getNumeroDocumento'
	 * 
	 * @return String
	 */
	public String getNumeroDocumento()
	{
		return numeroDocumento;
	}

	/**
	 * Method 'setNumeroDocumento'
	 * 
	 * @param numeroDocumento
	 */
	public void setNumeroDocumento(String numeroDocumento)
	{
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * Method 'getNumeroContrato'
	 * 
	 * @return String
	 */
	public String getNumeroContrato()
	{
		return numeroContrato;
	}

	/**
	 * Method 'setNumeroContrato'
	 * 
	 * @param numeroContrato
	 */
	public void setNumeroContrato(String numeroContrato)
	{
		this.numeroContrato = numeroContrato;
	}

	/**
	 * Method 'getMontoMinimo'
	 * 
	 * @return Double
	 */
	public Double getMontoMinimo()
	{
		return montoMinimo;
	}

	/**
	 * Method 'setMontoMinimo'
	 * 
	 * @param montoMinimo
	 */
	public void setMontoMinimo(Double montoMinimo)
	{
		this.montoMinimo = montoMinimo;
	}

	/**
	 * Method 'getMontoMaximo'
	 * 
	 * @return Double
	 */
	public Double getMontoMaximo()
	{
		return montoMaximo;
	}

	/**
	 * Method 'setMontoMaximo'
	 * 
	 * @param montoMaximo
	 */
	public void setMontoMaximo(Double montoMaximo)
	{
		this.montoMaximo = montoMaximo;
	}

	/**
	 * Method 'getIdEstado'
	 * 
	 * @return Integer
	 */
	public Integer getIdEstado()
	{
		return idEstado;
	}

	/**
	 * Method 'setIdEstado'
	 * 
	 * @param idEstado
	 */
	public void setIdEstado(Integer idEstado)
	{
		this.idEstado = idEstado;
	}

	/**
	 * Method 'getMontoPagado'
	 * 
	 * @return Double
	 */
	public Double getMontoPagado()
	{
		return montoPagado;
	}

	/**
	 * Method 'setMontoPagado'
	 * 
	 * @param montoPagado
	 */
	public void setMontoPagado(Double montoPagado)
	{
		this.montoPagado = montoPagado;
	}

	/**
	 * Method 'getFechaHoraPago'
	 * 
	 * @return Date
	 */
	public Date getFechaHoraPago()
	{
		return fechaHoraPago;
	}

	/**
	 * Method 'setFechaHoraPago'
	 * 
	 * @param fechaHoraPago
	 */
	public void setFechaHoraPago(Date fechaHoraPago)
	{
		this.fechaHoraPago = fechaHoraPago;
	}

	/**
	 * Method 'getDescripcionProducto'
	 * 
	 * @return String
	 */
	public String getDescripcionProducto()
	{
		return descripcionProducto;
	}

	/**
	 * Method 'setDescripcionProducto'
	 * 
	 * @param descripcionProducto
	 */
	public void setDescripcionProducto(String descripcionProducto)
	{
		this.descripcionProducto = descripcionProducto;
	}

	/**
	 * Method 'getCodigoAutorizacionMedio'
	 * 
	 * @return String
	 */
	public String getCodigoAutorizacionMedio()
	{
		return codigoAutorizacionMedio;
	}

	/**
	 * Method 'setCodigoAutorizacionMedio'
	 * 
	 * @param codigoAutorizacionMedio
	 */
	public void setCodigoAutorizacionMedio(String codigoAutorizacionMedio)
	{
		this.codigoAutorizacionMedio = codigoAutorizacionMedio;
	}



	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		if (idConsulta != null) {
			_hashCode = 29 * _hashCode + idConsulta.hashCode();
		}
		
		if (idTransaccionBoton != null) {
			_hashCode = 29 * _hashCode + idTransaccionBoton.hashCode();
		}
		
		if (idNavegacionBoton != null) {
			_hashCode = 29 * _hashCode + idNavegacionBoton.hashCode();
		}
		
		if (idLineaNegocio != null) {
			_hashCode = 29 * _hashCode + idLineaNegocio.hashCode();
		}
		
		if (fechaDocumento != null) {
			_hashCode = 29 * _hashCode + fechaDocumento.hashCode();
		}
		
		if (numeroDocumento != null) {
			_hashCode = 29 * _hashCode + numeroDocumento.hashCode();
		}
		
		if (numeroContrato != null) {
			_hashCode = 29 * _hashCode + numeroContrato.hashCode();
		}
		
		if (montoMinimo != null) {
			_hashCode = 29 * _hashCode + montoMinimo.hashCode();
		}
		
		if (montoMaximo != null) {
			_hashCode = 29 * _hashCode + montoMaximo.hashCode();
		}
		
		if (idEstado != null) {
			_hashCode = 29 * _hashCode + idEstado.hashCode();
		}
		
		if (montoPagado != null) {
			_hashCode = 29 * _hashCode + montoPagado.hashCode();
		}
		
		if (fechaHoraPago != null) {
			_hashCode = 29 * _hashCode + fechaHoraPago.hashCode();
		}
		
		if (descripcionProducto != null) {
			_hashCode = 29 * _hashCode + descripcionProducto.hashCode();
		}
		
		if (codigoAutorizacionMedio != null) {
			_hashCode = 29 * _hashCode + codigoAutorizacionMedio.hashCode();
		}
		
		return _hashCode;
	}

	
	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "cl.bice.vida.restfull.dto.BpiPecConsultaTransaccion: " );
		ret.append( "idConsulta=" + idConsulta );
		ret.append( ", idTransaccionBoton=" + idTransaccionBoton );
		ret.append( ", idNavegacionBoton=" + idNavegacionBoton );
		ret.append( ", idLineaNegocio=" + idLineaNegocio );
		ret.append( ", fechaDocumento=" + fechaDocumento );
		ret.append( ", numeroDocumento=" + numeroDocumento );
		ret.append( ", numeroContrato=" + numeroContrato );
		ret.append( ", montoMinimo=" + montoMinimo );
		ret.append( ", montoMaximo=" + montoMaximo );
		ret.append( ", idEstado=" + idEstado );
		ret.append( ", montoPagado=" + montoPagado );
		ret.append( ", fechaHoraPago=" + fechaHoraPago );
		ret.append( ", descripcionProducto=" + descripcionProducto );
		ret.append( ", codigoAutorizacionMedio=" + codigoAutorizacionMedio );
		return ret.toString();
	}

    public void setInstrumentoCaja(String instrumentoCaja) {
        this.instrumentoCaja = instrumentoCaja;
    }

    public String getInstrumentoCaja() {
        return instrumentoCaja;
    }
}
