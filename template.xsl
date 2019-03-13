<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" indent="no" />
    <xsl:template match="/">

        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master
                        master-name="A4-Landscape" page-height="21cm" page-width="29.7cm"
                        margin-top="1cm" margin-bottom="1cm" margin-left="1cm"
                        margin-right="2cm">
                    <fo:region-body />
                    <fo:region-before extent="1.5cm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-Landscape">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:table table-layout="fixed" width="100%"
                                  border-collapse="separate">
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-column column-width="10%" />
                            <fo:table-header>
                                <fo:table-row>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">ID</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">first name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">middle name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">last name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">org name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">org id</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">manager name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">lead name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">pin</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid .5px black"
                                                   text-align="center" wrap-option="wrap">
                                        <fo:block font-weight="bold">city</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:apply-templates select="data/row" />
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="data/row">
        <fo:table-row>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="id" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="first_name" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="middle_name" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="last_name" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="org_name" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="org_id" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="manager_name" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="lead_name" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block>
                        <xsl:value-of select="pin" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
            <fo:table-cell border="solid .5px black"
                           text-align="center" wrap-option="wrap">
                <fo:block-container overflow="hidden">
                    <fo:block wrap-option="wrap">
                        <xsl:value-of select="city" />
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>