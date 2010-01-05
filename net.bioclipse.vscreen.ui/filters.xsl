<xsl:stylesheet 
version="1.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns="http://www.w3.org/TR/xhtml1/strict"> 
<xsl:template match="/"> 
<html> 
<head> 
<title>Filters</title> 
</head> 
<body> 
<h2>Filters:</h2> 
<table>
	<tr><td bgcolor="#dddddd">Name</td><td bgcolor="#dddddd">ID</td><td bgcolor="#dddddd">Description</td></tr>
<xsl:apply-templates select="/plugin/extension/screeningFilter"/> 
</table>
</body> 
</html> 
</xsl:template> 

<xsl:template match="screeningFilter"> 
	<tr>
	<td><xsl:value-of select="./@name"/><xsl:text> 
	</xsl:text></td>

	<td><xsl:value-of select="./@id"/><xsl:text> 
	</xsl:text></td>

	<td><xsl:value-of select="./@description"/></td>
	</tr>
</xsl:template>

</xsl:stylesheet> 
