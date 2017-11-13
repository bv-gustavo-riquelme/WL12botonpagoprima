<?xml version="1.0" encoding="UTF-8"?>
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
        <fo:simple-page-master master-name="carta" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
            <fo:region-body margin-top="0.79in" margin-bottom="0.79in"/>
        </fo:simple-page-master>      
      </fo:layout-master-set>  
      <fo:page-sequence master-reference="carta">
        <fo:flow flow-name="xsl-region-body">
        
            <!-- barra de logo bice vida -->
            <fo:table table-layout="fixed" width="500px">
            <fo:table-column column-width="170px"/>
            <fo:table-body start-indent="0pt">
            <fo:table-row>
              <fo:table-cell padding="0" display-align="before">
                <fo:block>
                  <fo:external-graphic src="url(logo_bice_vida.jpg)"/>
                  <fo:inline></fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            </fo:table-body>
            </fo:table>
          
            <!-- barra de titulo -->
            <fo:table table-layout="fixed" width="500px" >
            <fo:table-column column-width="78px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="14px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell background-image="url(barra_left.jpg)" 
                                height="32px" 
                                padding="0" 
                                display-align="center">
                  <fo:block><fo:inline></fo:inline></fo:block>                   
                </fo:table-cell>
                <fo:table-cell background-image="url(barra_relleno.jpg)" height="32px">
                  <fo:block>
                      <fo:table table-layout="fixed">
                        <fo:table-column column-width="408px"/>
                        <fo:table-body start-indent="0pt">
                           <fo:table-row>
                            <fo:table-cell line-height="10px">
                                <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                            </fo:table-cell>
                           </fo:table-row>
                           <fo:table-row>
                            <fo:table-cell color="white"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="15px"
                               font-weight="bold"
                               text-align="right">
                                <fo:block>
                                    <fo:inline>Comprobante de Pago</fo:inline>
                                </fo:block>
                            </fo:table-cell>
                           </fo:table-row>                           
                        </fo:table-body>
                      </fo:table>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell background-image="url(barra_right.jpg)" 
                               height="32px" 
                               padding="0" 
                               display-align="center">
                  <fo:block><fo:inline></fo:inline></fo:block>                   
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>            
            </fo:table>    
            
            <!-- espacio -->            
            <fo:block line-height="10px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>
            
            <!-- barra de subtitulo -->           
            <fo:table table-layout="fixed" width="500px" >
            <fo:table-column column-width="100px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="110px"/>
            <fo:table-column column-width="80px"/>
            <fo:table-column column-width="40px"/>
            <fo:table-column column-width="70px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Forma de Pago</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//header/formadepago"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>N de Transaccion</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//header/numerotransaccion"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Fecha</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
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
            <fo:block line-height="10px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>          
            
            
            <!-- barra de subtitulo -->
            <fo:table table-layout="fixed" width="500px" >
            <fo:table-column column-width="8px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="8px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell background-image="url(barrtitulo_left.jpg)" 
                                height="27px" 
                                padding="0" 
                                display-align="center">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell background-image="url(barrtitulo_relleno.jpg)" height="27px">
                  <fo:block>

                      <fo:table table-layout="fixed">
                        <fo:table-column column-width="484px"/>
                        <fo:table-body start-indent="0pt">
                           <fo:table-row>
                            <fo:table-cell line-height="8px">
                                <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                            </fo:table-cell>
                           </fo:table-row>
                           <fo:table-row>
                            <fo:table-cell color="white"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="12px"
                               font-weight="bold"                               
                               padding="0"
                               display-align="center">
                                <fo:block>
                                    <fo:inline>Datos del Cliente</fo:inline>
                                </fo:block>
                            </fo:table-cell>
                           </fo:table-row>                           
                        </fo:table-body>
                      </fo:table>
                  
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell background-image="url(barrtitulo_right.jpg)"
                               height="27px"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>                
            </fo:table>
            
            <!-- espacio -->        
            <fo:block line-height="10px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>                      
            
            <!-- barra de informacion del usuario -->           
            <fo:table table-layout="fixed" width="500px"  border-collapse="separate">
            <fo:table-column column-width="70px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="40px"/>
            <fo:table-column column-width="100px"/>
            <fo:table-body start-indent="0pt">
              <!-- fila 1 -->
              <fo:table-row>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Nombre</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/nombre"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               text-align="left"
                               display-align="after">
                  <fo:block>
                    <fo:inline>Rut</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/rut"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>

              <!-- fila 2 -->
              <fo:table-row>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Direccion</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/direccion"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              
              <!-- fila 3 -->
              <fo:table-row>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Comuna</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/comuna"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"
                               padding="0"
                               text-align="left"
                               display-align="after">
                  <fo:block>
                    <fo:inline>Ciudad</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               padding="0"
                               text-align="left"
                               display-align="center">
                  <fo:block>
                    <fo:inline>: <xsl:value-of select="//header/ciudad"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>              
              
            </fo:table-body>
            </fo:table>
            
        <!-- espacio -->
        <fo:block line-height="10px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>         
            
        <!-- barra informacion seguro -->
        <fo:table table-layout="fixed" width="500px" >
        <fo:table-column column-width="6px"/>
        <fo:table-column column-width="proportional-column-width(1)"/>
        <fo:table-column column-width="6px"/>
        <fo:table-body start-indent="0pt">
            <fo:table-row>
            <fo:table-cell background-image="url(barrsubtitulo_left.jpg)"
                   height="23px"
                   padding="0"
                   display-align="center">
            <fo:block>
            <fo:inline></fo:inline>
            </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barrsubtitulo_relleno.jpg)" height="23px">
            <fo:block>
            
              <fo:table table-layout="fixed">
                <fo:table-column column-width="484px"/>
                <fo:table-body start-indent="0pt">
                   <fo:table-row>
                    <fo:table-cell line-height="5px">
                        <fo:block><fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline></fo:block>
                    </fo:table-cell>
                   </fo:table-row>
                   <fo:table-row>
                    <fo:table-cell color="#2a3547"
                           font-family="verdana , arial , helvetica , sans-serif"
                           font-size="12px"
                           font-weight="bold"                                   
                           padding="0"
                           display-align="center">
                        <fo:block>
                            <fo:inline>Informacion del Seguro</fo:inline>
                        </fo:block>
                    </fo:table-cell>
                   </fo:table-row>                           
                </fo:table-body>
              </fo:table>
                        
            </fo:block>
            </fo:table-cell>
            <fo:table-cell background-image="url(barrsubtitulo_right.jpg)"
                   height="23px"
                   padding="0"
                   display-align="center">
            <fo:block>
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
        
        <!-- barra de contenido de detalle poliza -->
        <fo:table table-layout="fixed" width="500px" >
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
                        <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="left"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>Poliza</fo:inline>
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
                        <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="center"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>Num. Poliza</fo:inline>
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
                        <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="center"
                               padding="0"
                               display-align="center">
                            <fo:block>
                                <fo:inline>Período Cobertura</fo:inline>
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
                        <fo:table-cell color="#2a3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="11px"
                               font-weight="bold"                           
                               text-align="right"
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
          
          <!-- contenido -->
          <xsl:for-each select="comprobante/detalle/row">
          <fo:table-row>
            <fo:table-cell border-bottom="#cbd1dd 1px solid"
                           border-left="#cbd1dd 1px solid"
                           padding="0"
                           height="15px"
                           display-align="center">
              <fo:block>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom="#cbd1dd 1px solid"
                           color="black"
                           font-family="verdana , arial , helvetica , sans-serif"
                           font-size="11px"
                           padding="0"
                           height="15px"
                           display-align="center">
              <fo:block>
                <fo:inline><xsl:value-of select="./producto"/></fo:inline>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom="#cbd1dd 1px solid"
                           color="black"
                           font-family="verdana , arial , helvetica , sans-serif"
                           font-size="11px"
                           padding="0"
                           height="15px"
                           text-align="center"
                           display-align="center">
              <fo:block>
                <fo:inline><xsl:value-of select="./numeropoliza"/></fo:inline>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom="#cbd1dd 1px solid"
                           color="black"
                           font-family="verdana , arial , helvetica , sans-serif"
                           font-size="11px"
                           padding="0"
                           height="15px"
                           text-align="center"
                           display-align="center">
              <fo:block>
                <fo:inline><xsl:value-of select="./periodocobertura"/></fo:inline>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom="#cbd1dd 1px solid"
                           color="black"
                           font-family="verdana , arial , helvetica , sans-serif"
                           font-size="11px"
                           padding="0"
                           height="15px"
                           text-align="right"
                           display-align="center">
              <fo:block>
                <fo:inline><xsl:value-of select="./montopagado"/></fo:inline>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom="#cbd1dd 1px solid"
                           border-right="#cbd1dd 1px solid"
                           padding="0"
                           height="15px"
                           display-align="center">
              <fo:block>
                <fo:inline></fo:inline>
              </fo:block>
            </fo:table-cell>
            </fo:table-row>
            </xsl:for-each>
            
            <!-- totalizador -->
            <fo:table-row height="35px">
                <fo:table-cell background-image="url(box_relleno_color2.jpg)"
                               border-bottom="#cbd1dd 1px solid"
                               color="white"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="12px"
                               font-weight="bold"
                               text-align="center"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell background-image="url(box_relleno_color2.jpg)"
                               border-bottom="#cbd1dd 1px solid"
                               color="white"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="12px"
                               font-weight="bold"
                               text-align="center"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Fecha Vencimiento</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="#cbd1dd 1px solid"
                               color="black"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="12px"
                               padding="0"
                               text-align="center"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//footer/fechavencimiento"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell background-image="url(box_relleno_color2.jpg)"
                               border-bottom="#cbd1dd 1px solid"
                               color="white"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="12px"
                               font-weight="bold"
                               text-align="center"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline>Total a Pagar</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="#cbd1dd 1px solid"
                               color="black"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="12px"
                               padding="0"
                               text-align="center"
                               display-align="center">
                  <fo:block>
                    <fo:inline><xsl:value-of select="//footer/totalapagar"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom="#cbd1dd 1px solid"
                               border-right="#cbd1dd 1px solid"
                               padding="0"
                               display-align="center">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>          
        </fo:table-body>
        </fo:table>
        
        <!--timbre pagado -->
        <fo:table table-layout="fixed" width="500px" >
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
                <fo:external-graphic src="url(transaccion_pagada.jpg)" content-width="100px" content-height="100px" />
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