<?xml version="1.0"?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="text" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:for-each select="/operators/operator">
            public static final org.apache.vxquery.functions.Operator <xsl:value-of select="translate(substring-after(@name, ':'), 'abcdefghijklmnopqrstuvwxyz-:', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ__')"/>
                = new org.apache.vxquery.functions.Operator(<xsl:value-of select="translate(substring-after(@name, ':'), 'abcdefghijklmnopqrstuvwxyz-:', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ__')"/>_QNAME,
                new org.apache.vxquery.functions.Signature(
                createSequenceType("<xsl:value-of select="return/@type"/>")
                <xsl:if test="@varargs = 'true'">
                    , true
                </xsl:if>
                <xsl:for-each select="param">
                    ,
                    org.apache.commons.lang3.tuple.Pair.&lt;javax.xml.namespace.QName, org.apache.vxquery.types.SequenceType&gt;of(
                        new javax.xml.namespace.QName("<xsl:value-of select="@name"/>"),
                        createSequenceType("<xsl:value-of select="@type"/>")
                    )
                </xsl:for-each>
                )
                );
        </xsl:for-each>
    </xsl:template>
</xsl:transform>