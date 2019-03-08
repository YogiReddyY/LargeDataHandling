<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="Results">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="30cm" page-width="30cm" margin-top="2cm"
                                       margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                  <!--<fo:block font-size="16pt" font-weight="bold" space-after="5mm">Company Name: <xsl:value-of select="companyname"/>
                  </fo:block>-->
                  <fo:block font-size="5pt">
                  <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                    <fo:table-column column-width="2cm"/>
                    <fo:table-column column-width="2cm"/>
                    <fo:table-column column-width="2cm"/>
                      <fo:table-column column-width="2cm"/>
                      <fo:table-column column-width="2cm"/>
                      <fo:table-column column-width="2cm"/>
                      <fo:table-column column-width="2cm"/>
                      <fo:table-column column-width="4cm"/>
                      <fo:table-column column-width="4cm"/>
                      <fo:table-column column-width="4cm"/>
                      <fo:table-column column-width="4cm"/>
                      <fo:table-column column-width="4cm"/>
                      <fo:table-column column-width="4cm"/>
                    <fo:table-body>
                      <xsl:apply-templates select="Row"/>
                    </fo:table-body>
                  </fo:table>
                  </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="Row">
        <fo:table-row>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="first_name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="middle_name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="last_name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="org_name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="org_id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="manager_name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="lead_name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="pin"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="org_id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="city"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="country"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="longlong"/>
                </fo:block>
            </fo:table-cell>

        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>