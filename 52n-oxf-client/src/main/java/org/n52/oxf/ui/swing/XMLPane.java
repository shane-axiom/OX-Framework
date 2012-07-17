/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package org.n52.oxf.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.n52.oxf.util.IOHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLPane extends JTextPane {

    private static final long serialVersionUID = -8800027402810462779L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLPane.class);
    
    MutableAttributeSet tagAttributes, elementAttributes, characterAttributes, cdataAttributes;
    Pattern partPattern, namePattern, attributePattern;
    Matcher partMatcher, nameMatcher, attributeMatcher;


    /**
     * 
     * @param xmlText
     */
    public XMLPane(String xmlText) {
        super();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("constructed XMLPane, formatting...");
        }

        this.setText(xmlText);

        initAttributeSets();
        initPatterns();

        nameMatcher = namePattern.matcher(xmlText);
        partMatcher = partPattern.matcher(xmlText);
        attributeMatcher = attributePattern.matcher(xmlText);

        while (partMatcher.find()) {
            formatPart(partMatcher);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("formatting finished");
        }
    }

    /**
     * initializes the AttributeSets for the differnet syntactic parts of the XML-Document.
     */
    private void initAttributeSets() {
        tagAttributes = new SimpleAttributeSet();
        StyleConstants.setForeground(tagAttributes, Color.blue);

        cdataAttributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(cdataAttributes, "Courier");

        characterAttributes = new SimpleAttributeSet();
        StyleConstants.setBold(characterAttributes, true);

        elementAttributes = new SimpleAttributeSet();
        StyleConstants.setForeground(elementAttributes, new Color(153, 0, 0));
    }

    /**
     * 
     * 
     */
    private void initPatterns() {
        // REX/Javascript 1.0
        // Robert D. Cameron "REX: XML Shallow Parsing with Regular Expressions",
        // Technical Report TR 1998-17, School of Computing Science, Simon Fraser
        // University, November, 1998.
        // Copyright (c) 1998, Robert D. Cameron.
        // The following code may be freely used and distributed provided that
        // this copyright and citation notice remains intact and that modifications
        // or additions are clearly identified.

        String TextSE = "[^<]+";
        String UntilHyphen = "[^-]*-";
        String Until2Hyphens = UntilHyphen + "([^-]" + UntilHyphen + ")*-";
        String CommentCE = Until2Hyphens + ">?";
        String UntilRSBs = "[^]]*]([^]]+])*]+";
        String CDATA_CE = UntilRSBs + "([^]>]" + UntilRSBs + ")*>";
        String S = "[ \\n\\t\\r]+";
        String NameStrt = "[A-Za-z_:]|[^\\x00-\\x7F]";
        String NameChar = "[A-Za-z0-9_:.-]|[^\\x00-\\x7F]";
        String Name = "(" + NameStrt + ")(" + NameChar + ")*";
        String QuoteSE = "\"[^\"]" + "*" + "\"" + "|'[^']*'";
        String DT_IdentSE = S + Name + "(" + S + "(" + Name + "|" + QuoteSE + "))*";
        String MarkupDeclCE = "([^]\"'><]+|" + QuoteSE + ")*>";
        String S1 = "[\\n\\r\\t ]";
        String UntilQMs = "[^?]*\\?+";
        String PI_Tail = "\\?>|" + S1 + UntilQMs + "([^>?]" + UntilQMs + ")*>";
        String DT_ItemSE = "<(!(--" + Until2Hyphens + ">|[^-]" + MarkupDeclCE + ")|\\?" + Name + "(" + PI_Tail + "))|%"
                + Name + ";|" + S;
        String DocTypeCE = DT_IdentSE + "(" + S + ")?(\\[(" + DT_ItemSE + ")*](" + S + ")?)?>?";
        String DeclCE = "--(" + CommentCE + ")?|\\[CDATA\\[(" + CDATA_CE + ")?|DOCTYPE(" + DocTypeCE + ")?";
        String PI_CE = Name + "(" + PI_Tail + ")?";
        String EndTagCE = Name + "(" + S + ")?>?";
        String AttValSE = "\"[^<\"]" + "*" + "\"" + "|'[^<']*'";
        String ElemTagCE = Name + "(" + S + Name + "(" + S + ")?=(" + S + ")?(" + AttValSE + "))*(" + S + ")?/?>?";
        String MarkupSPE = "<(!(" + DeclCE + ")?|\\?(" + PI_CE + ")?|/(" + EndTagCE + ")?|(" + ElemTagCE + ")?)";
        String XML_SPE = TextSE + "|" + MarkupSPE;

        partPattern = Pattern.compile(XML_SPE);
        namePattern = Pattern.compile(Name);
        attributePattern = Pattern.compile(AttValSE);
    }

    /**
     * 
     * @param partMatcherParam
     */
    private void formatPart(Matcher partMatcherParam) {
        String part = partMatcherParam.group();
        if ( !part.startsWith("<")) {
            applyAttributeSet(partMatcherParam, characterAttributes);
        }
        else {
            applyAttributeSet(partMatcherParam, tagAttributes);
            if ( !part.startsWith("<?") && !part.startsWith("<!")) {
                formatElement(partMatcherParam);
            }
            if (part.startsWith("<![CDATA[") && part.endsWith("]]>")) {
                applyAttributeSet(partMatcherParam, cdataAttributes, 9, 12);
            }
        }
    }

    /**
     * formats names and attributes of a XML-element.
     * 
     * @param partMatcherParam
     */
    private void formatElement(Matcher partMatcherParam) {
        // Alle Namen innerhalb des Elements werden gefunden und formatiert
        nameMatcher.find(partMatcherParam.start());
        do {
            applyAttributeSet(nameMatcher, elementAttributes);
            if ( !nameMatcher.find())
                break;
        } while (nameMatcher.end() < partMatcherParam.end());

        // Alle Attribute innerhalb des Elements werden gefunden und formatiert
        if ( !attributeMatcher.find(partMatcherParam.start()))
            return;
        do {
            applyAttributeSet(attributeMatcher, characterAttributes, 1, 1);
            if ( !attributeMatcher.find())
                break;
        } while (attributeMatcher.end() < partMatcherParam.end());
    }

    /**
     * formats the text of the actual Group of the Matcher with the specified AttributeSet.
     */
    private void applyAttributeSet(Matcher m, AttributeSet attributeSet) {
        applyAttributeSet(m, attributeSet, 0, 0);
    }

    /**
     * formats the text of the actual Group (minus specific margins) of the Matcher with the specified
     * AttributeSet.
     */
    private void applyAttributeSet(Matcher m, AttributeSet attributeSet, int additionalOffset, int lengthReduction) {
        int offset = m.start();
        int length = m.group().length();
        this.getStyledDocument().setCharacterAttributes(offset + additionalOffset,
                                                        length - lengthReduction,
                                                        attributeSet,
                                                        true);
    }
}