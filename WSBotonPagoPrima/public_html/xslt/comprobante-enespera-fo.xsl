<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet 
    version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:s="http://www.stylusstudio.com/xquery" 
    xmlns:fn="http://www.w3.org/2005/xpath-functions" 
    xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
  <xsl:decimal-format name="Chile" decimal-separator="," grouping-separator="."/>
  <xsl:template match="/">
     <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
     <fo:layout-master-set>
        <fo:simple-page-master master-name="carta" page-height="11in" page-width="8.5in" margin-left="0.1in" margin-right="0.0in">
            <fo:region-body margin-top="0.0in" margin-bottom="0.0in"/>
        </fo:simple-page-master>      
      </fo:layout-master-set>  
      <fo:page-sequence master-reference="carta">
        <fo:flow flow-name="xsl-region-body">
        
            <!-- barra de logo bice vida -->
            <fo:table table-layout="fixed" width="700px">
            <fo:table-column column-width="85px"/>
            <fo:table-body start-indent="0pt">
            <fo:table-row>
              <fo:table-cell padding="0" display-align="before">
                <fo:block>
                  <fo:external-graphic src="url(cabecera.jpg)"/>
                  <fo:inline></fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            </fo:table-body>
            </fo:table>
          
            <!-- espacio -->            
            <fo:block line-height="8px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>
            
            <!-- barra de titulo -->
            <fo:table table-layout="fixed" width="600px" >
            <fo:table-column column-width="30px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell>
                  <fo:block>
                   <fo:external-graphic src="url(bg_bullet.gif)"/>
                   <fo:inline></fo:inline>
                  </fo:block>                   
                </fo:table-cell>
                <fo:table-cell color="#5b5b5b"
                   font-family="Arial, Helvetica, sans-serif"
                   font-size="14px"
                   font-weight="bold"
                   text-align="left">
                    <fo:block>
                        <fo:inline>COMPROBANTE DE PAGO EN L&#xCD;NEA</fo:inline>
                    </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>            
            </fo:table>    
            
            <!-- espacio -->            
            <fo:block line-height="8px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>
            
            <!-- barra de subtitulo -->           
            <fo:table table-layout="fixed" width="600px" border="solid #E2E2E2 1px" background-color="#ECECEC">
            <fo:table-column column-width="100px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="140px"/>
            <fo:table-column column-width="120px"/>
            <fo:table-column column-width="60px"/>
            <fo:table-column column-width="90px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row height="23px">
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/>Forma de Pago :</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//header/formadepago"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>N&#xBA; de Transacci&#xF3;n :</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//header/numerotransaccion"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Fecha :</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//header/fechaoperacion"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
            </fo:table>
            
            <!-- espacio -->        
            <fo:block line-height="8px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>          
            
            <!-- barra de subtitulo -->
            <fo:table table-layout="fixed" width="600px" >
            <fo:table-column column-width="30px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell>
                  <fo:block>
                   <fo:external-graphic src="url(bg_datosclientes.gif)"/>
                   <fo:inline></fo:inline>
                  </fo:block>                   
                </fo:table-cell>
                <fo:table-cell color="#5b5b5b"
                   font-family="Arial, Helvetica, sans-serif"
                   font-size="14px"
                   font-weight="bold"
                   text-align="left">
                    <fo:block>
                        <fo:inline>Datos del Cliente</fo:inline>
                    </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>            
            </fo:table>                
            
     
            <!-- espacio -->        
            <fo:block line-height="3px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>                      
            
            <!-- barra de informacion del usuario -->           
            <fo:table table-layout="fixed" width="600px" border="solid #E2E2E2 1px" border-collapse="separate">
            <fo:table-column column-width="60px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="60px"/>
            <fo:table-column column-width="90px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row height="10px">
                <fo:table-cell>
                 <fo:inline></fo:inline>
                </fo:table-cell>
              </fo:table-row>
                
              <!-- fila 1 -->
              <fo:table-row height="18px">
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="before">
                  <fo:block>
                    <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/>Nombre</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="before">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/nombre"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               text-align="left"
                               display-align="before">
                  <fo:block>
                    <fo:inline>Rut :</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="before">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//header/rut"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>

              <!-- fila 2 -->
              <fo:table-row height="18px">
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="before">
                  <fo:block>
                    <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/>Direcci&#xF3;n</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="before">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/direccion"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              
              <!-- fila 3 -->
              <fo:table-row height="18px">
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="before">
                  <fo:block>
                    <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/>Comuna</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="before">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/comuna"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>              

              <!-- fila 4 -->
              <fo:table-row height="18px">
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               font-weight="bold"
                               padding="0"
                               display-align="before">
                  <fo:block>
                    <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/>Regi&#xF3;n</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="10px"
                               padding="0"
                               text-align="left"
                               display-align="before">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/ciudad"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>              

              <fo:table-row height="2px">
                <fo:table-cell>
                 <fo:inline></fo:inline>
                </fo:table-cell>
              </fo:table-row>
              
            </fo:table-body>
            </fo:table>
            
        <!-- espacio -->
        <fo:block line-height="8px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>         

        <!-- barra informacion del seguro -->
        <fo:table table-layout="fixed" width="600px" >
        <fo:table-column column-width="30px"/>
        <fo:table-column column-width="proportional-column-width(1)"/>
        <fo:table-body start-indent="0pt">
          <fo:table-row>
            <fo:table-cell>
              <fo:block>
               <fo:external-graphic src="url(bg_infocliente.gif)"/>
               <fo:inline></fo:inline>
              </fo:block>                   
            </fo:table-cell>
            <fo:table-cell color="#5b5b5b"
               font-family="Arial, Helvetica, sans-serif"
               font-size="14px"
               font-weight="bold"
               text-align="left">
                <fo:block>
                    <fo:inline>Informaci&#xF3;n del Seguro</fo:inline>
                </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>            
        </fo:table>                
            
        
        <!-- espacio -->        
        <fo:block line-height="8px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>                   
        
        <!-- barra de contenido de detalle poliza -->
        <fo:table table-layout="fixed" width="600px" >
        <fo:table-column column-width="8px"/>
        <fo:table-column column-width="proportional-column-width(1)"/>
        <fo:table-column column-width="100px"/>
        <fo:table-column column-width="100px"/>
        <fo:table-column column-width="100px"/>
        <fo:table-column column-width="8px"/>
        <fo:table-body start-indent="0pt">
          <!-- cabecera -->
          <fo:table-row>
            <fo:table-cell background-image="url(barhead_left.jpg)"
                           height="38px"
                           padding="0"
                           display-align="center">
              <fo:block>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barhead_relleno.jpg)" height="38px">
              <fo:block>
              
                  <fo:table table-layout="fixed">
                    <fo:table-column/>
                    <fo:table-body start-indent="0pt">
                       <fo:table-row>
                        <fo:table-cell line-height="15px">
                            <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                        </fo:table-cell>
                       </fo:table-row>
                       <fo:table-row>
                        <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="center"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>P&#xF3;liza (s)</fo:inline>
                            </fo:block>
                        </fo:table-cell>
                       </fo:table-row>                           
                    </fo:table-body>
                  </fo:table>              
                
              </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barhead_relleno.jpg)" height="38px">
              <fo:block>

                  <fo:table table-layout="fixed">
                    <fo:table-column/>
                    <fo:table-body start-indent="0pt">
                       <fo:table-row>
                        <fo:table-cell line-height="15px">
                            <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                        </fo:table-cell>
                       </fo:table-row>
                       <fo:table-row>
                        <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="center"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>N&#xBA; P&#xF3;liza</fo:inline>
                            </fo:block>
                        </fo:table-cell>
                       </fo:table-row>                           
                    </fo:table-body>
                  </fo:table>   
                  
              </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barhead_relleno.jpg)" height="38px">
              <fo:block>

                  <fo:table table-layout="fixed">
                    <fo:table-column/>
                    <fo:table-body start-indent="0pt">
                       <fo:table-row>
                        <fo:table-cell line-height="15px">
                            <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                        </fo:table-cell>
                       </fo:table-row>
                       <fo:table-row>
                        <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="center"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>Per&#xED;odo Cobertura</fo:inline>
                            </fo:block>
                        </fo:table-cell>
                       </fo:table-row>                           
                    </fo:table-body>
                  </fo:table>   

              </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barhead_relleno.jpg)" height="38px">
              <fo:block>

                  <fo:table table-layout="fixed">
                    <fo:table-column/>
                    <fo:table-body start-indent="0pt">
                       <fo:table-row>
                        <fo:table-cell line-height="15px">
                            <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                        </fo:table-cell>
                       </fo:table-row>
                       <fo:table-row>
                        <fo:table-cell color="#05036F"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="center"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>Monto Pago</fo:inline>
                            </fo:block>
                        </fo:table-cell>
                       </fo:table-row>                           
                    </fo:table-body>
                  </fo:table>   
                  
                </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barhead_right.jpg)"
                           height="38px"
                           padding="0"
                           display-align="center">
              <fo:block>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <!-- iteracion de datos -->
          <xsl:for-each select="comprobante/detalle/row">
          <!-- iteracion de datos -->
          
              <fo:table-row>
                <fo:table-cell border-bottom="solid #D7D7D7 0px"
                               border-top="solid #FFFFFF 0px"
                               border-left="solid #D7D7D7 0px"
                               background-color="#E5EEF2"
                               height="25px"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               text-align="center">
                  <fo:block line-height="25px">
                    <fo:inline  alignment-adjust="baseline"></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="solid #D7D7D7 0px"
                               border-top="solid #FFFFFF 0px"
                               background-color="#E5EEF2"
                               height="25px"
                               color="#5b5b5b"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               text-align="center">
                  <fo:block line-height="25px">
                    <fo:inline alignment-adjust="baseline"><xsl:value-of select="./producto"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="solid #D7D7D7 0px"
                               border-top="solid #FFFFFF 0px"
                               background-color="#E5EEF2"
                               height="25px"
                               color="#5b5b5b"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               text-align="center">
                  <fo:block line-height="25px">
                    <fo:inline  alignment-adjust="baseline"><xsl:value-of select="./numeropoliza"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="solid #D7D7D7 0px"
                               border-top="solid #FFFFFF 0px"
                               background-color="#E5EEF2"
                               height="25px"
                               color="#5b5b5b"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               text-align="center">
                  <fo:block line-height="25px">
                    <fo:inline  alignment-adjust="baseline"><xsl:value-of select="./periodocobertura"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="solid #D7D7D7 0px"
                               border-top="solid #FFFFFF 0px"
                               background-color="#E5EEF2"
                               height="25px"
                               color="#5b5b5b"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               text-align="center">
                  <fo:block line-height="25px">
                    <fo:inline alignment-adjust="baseline"><xsl:value-of select="./montopagado"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="solid #D7D7D7 0px"
                               border-top="solid #FFFFFF 0px"
                               border-right="solid #D7D7D7 0px"
                               background-color="#E5EEF2"
                               height="25px"
                               font-family="Arial, Helvetica, sans-serif"
                               font-size="11px">
                  <fo:block line-height="25px">
                    <fo:inline  alignment-adjust="baseline"></fo:inline>
                  </fo:block>
                </fo:table-cell>
                </fo:table-row>
                
            <!-- iteracion de datos -->
            </xsl:for-each>
            <!-- iteracion de datos -->
           
            </fo:table-body>
        </fo:table>
           
        
        <!-- espacio -->        
        <fo:block line-height="8px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>                
        
        <!-- totalizador -->
        <fo:table table-layout="fixed" width="600px" border="solid #E2E2E2 1px" border-collapse="separate">
        <fo:table-column column-width="8px"/>
        <fo:table-column column-width="100px"/>
        <fo:table-column column-width="100px"/>
        <fo:table-column column-width="proportional-column-width(3)"/>
        <fo:table-column column-width="115px"/>
        <fo:table-body start-indent="0pt">            
            <fo:table-row height="25px">
                <fo:table-cell>
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               color="#70AF00"
                               text-align="left"
                               padding="0">
                  <fo:block line-height="25px">
                    <fo:inline></fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               text-align="left"
                               color="#5b5b5b"
                               font-weight="bold"
                               padding="0">
                  <fo:block line-height="25px">
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               color="#70AF00"
                               text-align="center"
                               padding="0">
                  <fo:block line-height="25px">
                    <fo:inline>Total a Pagar $</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell font-family="Arial, Helvetica, sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               color="#5b5b5b"
                               text-align="center"
                               padding="0">
                  <fo:block line-height="25px">
                    <fo:inline><xsl:value-of select="//footer/totalapagar"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>          
        </fo:table-body>
        </fo:table>
        
        <!-- espacio -->        
        <fo:block line-height="15px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>          
        
        <!--timbre en espera de pago -->
        <fo:table table-layout="fixed" width="600px" >
        <fo:table-column column-width="400px"/>
        <fo:table-column column-width="proportional-column-width(1)"/>
        <fo:table-body start-indent="0pt">
          <fo:table-row>
            <fo:table-cell padding="0" text-align="right" display-align="before">
              <fo:block>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding="0" text-align="right" display-align="center">
              <fo:block>
                <fo:external-graphic src="url(btn_transaccion.jpg)" content-width="151px" content-height="55px" />
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
        </fo:table>
        
   
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  
</xsl:stylesheet>
