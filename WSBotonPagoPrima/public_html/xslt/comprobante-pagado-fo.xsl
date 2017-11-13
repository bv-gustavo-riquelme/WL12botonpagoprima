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
        <fo:simple-page-master master-name="carta" 
            page-height="11in" 
            page-width="8.5in" 
            margin-left="0.1in" 
            margin-right="0.0in"
            margin-top="0.3cm" 
            margin-bottom="1cm">
            <fo:region-body background-color="#ffffff" 
                    region-name="xsl-region-body" 
                    margin-top="2.0cm" 
                    margin-bottom="1.5cm" 
                    margin-left="1.0cm" 
                    margin-right="1.0cm"/>
            <fo:region-before region-name="xsl-region-before" extent="100mm"/>
            <fo:region-after region-name="xsl-region-after" extent="30mm"/>
        </fo:simple-page-master>      
      </fo:layout-master-set>
      
      <fo:page-sequence master-reference="carta">
        <fo:static-content flow-name="xsl-region-before">
            <!-- barra de logo bice vida -->
          <fo:table table-layout="fixed" width="706px">           
            <fo:table-column column-width="600px"/>            
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell column-number="1"  padding="0" display-align="before">
                  <fo:block>
                    <fo:external-graphic width="600px" height="40px" src="url(cabecera_pp.jpg)"/>                    
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>          
        </fo:static-content>     
        
        
        <fo:static-content flow-name="xsl-region-after">
            <!-- barra de logo bice vida -->
          <!--
          <fo:table table-layout="fixed" width="706px">    
            <fo:table-column column-width="25px"/>   
            <fo:table-column column-width="575px"/>            
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell column-number="1" padding="0" display-align="before">
                  <fo:block>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell column-number="2" padding="0" display-align="before">
                  <fo:block>
                    <fo:external-graphic src="url(pie_pp.jpg)" />
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>      
          -->
          
            <fo:table table-layout="fixed" width="706px">    
            <fo:table-column column-width="80px"/>   
            <fo:table-column column-width="520px"/>            
            <fo:table-body start-indent="0pt">
              <fo:table-row>
                <fo:table-cell column-number="1" padding="0" display-align="before">
                  <fo:block>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell column-number="2" padding="0" display-align="before">
                  <fo:block>
                    <fo:external-graphic width="400px" height="40" src="url(pie_pp.jpg)" />                    
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
          
        </fo:static-content>     
      
        <fo:flow flow-name="xsl-region-body">
                  
            
            <!-- barra de titulo -->
            <fo:table table-layout="fixed" width="556px" >
            <!--<fo:table-column column-width="24px"/>-->
            <fo:table-column column-width="556px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row height="23px">
                <fo:table-cell display-align="center" color="#2a2565"
                   
                   font-size="11px"
                   font-weight="bold"
                   text-align="left">
                    <fo:block>
                        <fo:inline>Comprobante de Pago en L&#xED;nea</fo:inline>
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
            <fo:table table-layout="fixed" width="556px">
            <fo:table-column column-width="100px"/>
            <fo:table-column column-width="106px"/>
            <fo:table-column column-width="110px"/>
            <fo:table-column column-width="90px"/>
            <fo:table-column column-width="60px"/>
            <fo:table-column column-width="90px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row height="23px" background-color="#b4aec5">
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)" border-top-width="1px"
                               border-left-style="solid"
                               border-left-color="rgb(123,115,157)"
                               border-left-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      <fo:external-graphic src="url(mula_spacer.gif)"/>
                      Forma de Pago
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0" text-align="left"
                               display-align="center"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)"
                               border-top-width="1px">
                  <fo:block>
                    <fo:inline>
                      :
                      <xsl:value-of select="//header/formadepago"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)"
                               border-top-width="1px">
                  <fo:block>
                    <fo:inline>N&#xB0; de Transacci&#xF3;n</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0" text-align="left"
                               display-align="center"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)"
                               border-top-width="1px">
                  <fo:block>
                    <fo:inline>
                      :
                      <xsl:value-of select="//header/numerotransaccion"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)"
                               border-top-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Fecha</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0" text-align="left"
                               display-align="center"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)" border-top-width="1px"
                               border-right-style="solid"
                               border-right-color="rgb(123,115,157)"
                               border-right-width="1px">
                  <fo:block>
                    <fo:inline>
                      :
                      <xsl:value-of select="//header/fechaoperacion"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row height="23px">
                <fo:table-cell number-columns-spanned="4">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" 
                               background-color="#ffffff"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)" border-top-width="1px"
                               border-left-style="solid"
                               border-left-color="rgb(123,115,157)"
                               border-left-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Hora</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0" text-align="left"
                               display-align="center" 
                               background-color="#ffffff"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px"
                               border-top-style="solid"
                               border-top-color="rgb(123,115,157)" border-top-width="1px"
                               border-right-style="solid"
                               border-right-color="rgb(123,115,157)"
                               border-right-width="1px">
                  <fo:block>
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/horabanco"/>
                    </fo:inline>
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
          <fo:table table-layout="fixed" width="556px">
            <!--<fo:table-column column-width="24px"/>-->
            <fo:table-column column-width="556px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row height="23px">
                <!--
                <fo:table-cell display-align="center">
                  <fo:block>
                    <fo:external-graphic src="url(bg_datosclientes.gif)"/>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell >
                -->
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold"
                               text-align="left" 
                               display-align="center">
                  <fo:block>
                    <fo:inline>Datos del Cliente</fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>    
                   
            <!-- espacio -->        
            <fo:block line-height="5px">
                <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
            </fo:block>     
            
             <!-- barra de informacion del usuario -->           
            <fo:table table-layout="fixed" width="556px">
            <fo:table-column column-width="60px"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="60px"/>
            <fo:table-column column-width="90px"/>
            <fo:table-body start-indent="0pt">
              <!-- fila 1 -->
              <fo:table-row height="20px">
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" border="solid rgb(123,115,157) 1px"
                               background-color="#b4aec5"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      <fo:external-graphic src="url(mula_spacer.gif)"/>
                      Nombre
                    </fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0" text-align="left"
                               display-align="center" border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      <xsl:value-of select="//header/nombre"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" border="solid rgb(123,115,157) 1px"
                               background-color="#b4aec5"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline>Rut</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0" text-align="left"
                               display-align="center" border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      <xsl:value-of select="//header/rut"/>
                    </fo:inline>
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
        <fo:table table-layout="fixed" width="556px">
            <!--<fo:table-column column-width="24px"/>-->
            <fo:table-column column-width="556px"/>
            <fo:table-body start-indent="0pt">
              <fo:table-row height="23px">
                <!--
                <fo:table-cell display-align="center">
                  <fo:block>
                    <fo:external-graphic src="url(bg_infocliente.gif)"/>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                -->
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold"
                               text-align="left" 
                               display-align="center">
                  <fo:block>
                    <fo:inline>Detalle Pago en L&#xED;nea</fo:inline>
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
        <fo:table table-layout="fixed" width="556px" >        
        <fo:table-column column-width="91px"/>
        <fo:table-column column-width="90px"/>
        <fo:table-column column-width="75px"/>
        <fo:table-column column-width="75px"/>
        <fo:table-column column-width="75px"/>
        <fo:table-column column-width="75px"/>
        <fo:table-column column-width="75px"/>           
        <fo:table-body start-indent="0pt">
          <!-- cabecera -->  
           <fo:table-row height="30px">
           <!-- fila titulos -->
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center"
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>P&#xF3;liza</fo:inline>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center" 
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>Tipo</fo:inline>
                </fo:block>
            </fo:table-cell>                       
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center" 
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>Per&#xED;odo Cobertura</fo:inline>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center" 
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>Prima a Pagar (UF)</fo:inline>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center" 
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>Prima a Pagar ($)</fo:inline>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center" 
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>Saldo a Favor ($)</fo:inline>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell color="#2a2565"
                           
                           font-size="10px"
                           font-weight="bold"
                           padding="0"
                           display-align="center" 
                           text-align="center"
                           border="solid rgb(123,115,157) 1px" 
                           background-color="#b4aec5" 
                           border-collapse="collapse">
                <fo:block>
                    <fo:inline>Total ($)</fo:inline>
                </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <!-- iteracion de datos -->
          <xsl:for-each select="comprobante/detalle/row">  
              <fo:table-row height="30px">               
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"                               
                               padding="0"
                               display-align="center"
                               text-align="left"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./producto"/></fo:inline>
                  </fo:block>
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./numeropoliza"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"
                               padding="0"
                               display-align="center"
                               text-align="left"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./tipo"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"
                               padding="0"
                               display-align="center"
                               text-align="left"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./periodocobertura"/></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"
                               padding="0"
                               display-align="center"
                               text-align="right"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./primapagouf"/></fo:inline>
                  </fo:block>
                </fo:table-cell> 
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"
                               padding="0"
                               display-align="center"
                               text-align="right"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./primapagopesos"/></fo:inline>
                  </fo:block>
                </fo:table-cell> 
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"
                               padding="0"
                               display-align="center"
                               text-align="right"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./saldofavor"/></fo:inline>
                  </fo:block>
                </fo:table-cell> 
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px"
                               padding="0"
                               display-align="center"
                               text-align="center"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline><xsl:value-of select="./montopagado"/></fo:inline>
                  </fo:block>
                </fo:table-cell> 
                </fo:table-row>
            </xsl:for-each>
            <!-- fin iteracion de datos -->
            
            <!-- espacio -->
            <fo:table-row height="5px">
                <fo:table-cell number-columns-spanned="7" color="#ffffff"
                               border="solid #abcbe4 0px"
                               border-collapse="collapse">
                  <fo:block>
                    <fo:inline>
                      <fo:external-graphic src="url(mula_spacer.gif)"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <!-- Total -->
              <fo:table-row height="25px">
                <fo:table-cell number-columns-spanned="4">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565" number-columns-spanned="2"
                               
                               font-size="10px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block margin-left="5px">
                    <fo:inline>Total $</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" font-weight="bold" padding="0"
                               display-align="center" text-align="center"
                               border="solid rgb(123,115,157) 1px"
                               border-collapse="collapse">
                  <fo:block>
                    <fo:inline>
                      <xsl:value-of select="//footer/totalapagar"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            
           
            </fo:table-body>
        </fo:table>
           
        
        <!-- espacio -->        
        <fo:block line-height="15px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>          
        
        <!-- informacion del pago que se realizo con el Banco -->
          <fo:table table-layout="fixed" width="556px"
                    background-color="#ffffff" border="solid rgb(123,115,157) 1px"
                    border-collapse="collapse">
            <fo:table-column column-width="97px"/>
            <fo:table-column column-width="120px"/>
            <fo:table-column column-width="97px"/>
            <fo:table-column column-width="120px"/>
            <!--<fo:table-column column-width="84px"/>
            <fo:table-column column-width="80px"/>-->
            <fo:table-body start-indent="0pt">
            <!-- Fila numero 1 -->
              <fo:table-row height="20px" keep-with-next="always">
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>N&#xB0; Operaci&#xF3;n</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/numerooperacion"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>N&#xB0; Orden</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/numeroordencompra"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
             <!-- Fila numero 2 -->
              <fo:table-row height="20px" keep-with-next="always">
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>N&#xB0; Tarjeta</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/numerotarjeta"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Transacci&#xF3;n</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>: Pago</fo:inline>
                  </fo:block>
                </fo:table-cell>                
              </fo:table-row>
              <!-- Fila numero 3 -->
              <fo:table-row height="20px" keep-with-next="always">
              <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>N&#xB0; Autorizaci&#xF3;n</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/codigoautorizacion"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Moneda</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/moneda"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <!-- Fila numero 4 -->
              <fo:table-row height="20px" keep-with-next="always">
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Tipo Cuota</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/tipocuota"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Fecha</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/fechabanco"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <!-- Fila numero 5 -->
              <fo:table-row height="20px" keep-with-next="always">
		<fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>N&#xB0; Cuotas</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/numerocuotas"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>Hora</fo:inline>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left"
                               border-bottom-style="solid"
                               border-bottom-color="rgb(123,115,157)"
                               border-bottom-width="1px">
                  <fo:block margin-left="5px">
                    <fo:inline>
                      :
                      <xsl:value-of select="//footer/horabanco"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <!-- Fila numero 6 -->
              <fo:table-row height="20px" keep-with-next="always">
                <fo:table-cell color="#2a2565"
                               
                               font-size="11px" font-weight="bold" padding="0"
                               display-align="center" text-align="left">
                  <fo:block margin-left="5px">
                    <fo:inline>URL</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2a2565"
                               
                               font-size="10px" padding="0"
                               display-align="center" text-align="left">
                  <fo:block margin-left="5px">
                    <fo:inline>: www.bicevida.cl</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell color="#2A3547"
                               font-family="verdana , arial , helvetica , sans-serif"
                               font-size="10px" font-weight="bold"
                               text-align="left" padding="0">
                  <fo:block>
                    <fo:inline></fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>

        <!-- espacio -->            
        <fo:block line-height="10px">
            <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
        </fo:block>        
                   
        <!--informacion de mesa ayuda -->
        <fo:table table-layout="fixed" width="550px" >        
        <fo:table-column column-width="350px"/>
        <fo:table-column column-width="200px"/>
        <fo:table-body start-indent="0pt">
          <fo:table-row>            
             <fo:table-cell padding="0" text-align="left" >
              <fo:block line-height="20px">
                 <fo:inline><fo:external-graphic src="url(mula_spacer.gif)"/></fo:inline>
              </fo:block>   
              <fo:block margin-left="5px">
                
                <fo:inline></fo:inline>
              </fo:block>
             </fo:table-cell>
             <fo:table-cell padding="0" text-align="right">
              <fo:block margin-left="5px">
                <fo:external-graphic src="url(${timbre})" />
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