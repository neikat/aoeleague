//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.07 at 05:24:21 PM ICT 
//


package com.sonic.aoeleague.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for history complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="history">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="matches" type="{}match" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "history", propOrder = {
    "matches"
})
public class History
    implements Cloneable, CopyTo, Equals, HashCode
{

    protected List<Match> matches;

    /**
     * Gets the value of the matches property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the matches property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMatches().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Match }
     * 
     * 
     */
    public List<Match> getMatches() {
        if (matches == null) {
            matches = new ArrayList<Match>();
        }
        return this.matches;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof History)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final History that = ((History) object);
        {
            List<Match> lhsMatches;
            lhsMatches = (((this.matches!= null)&&(!this.matches.isEmpty()))?this.getMatches():null);
            List<Match> rhsMatches;
            rhsMatches = (((that.matches!= null)&&(!that.matches.isEmpty()))?that.getMatches():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "matches", lhsMatches), LocatorUtils.property(thatLocator, "matches", rhsMatches), lhsMatches, rhsMatches)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<Match> theMatches;
            theMatches = (((this.matches!= null)&&(!this.matches.isEmpty()))?this.getMatches():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "matches", theMatches), currentHashCode, theMatches);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(Object target) {
        final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
        final Object draftCopy = ((target == null)?createNewInstance():target);
        if (draftCopy instanceof History) {
            final History copy = ((History) draftCopy);
            if ((this.matches!= null)&&(!this.matches.isEmpty())) {
                List<Match> sourceMatches;
                sourceMatches = (((this.matches!= null)&&(!this.matches.isEmpty()))?this.getMatches():null);
                @SuppressWarnings("unchecked")
                List<Match> copyMatches = ((List<Match> ) strategy.copy(LocatorUtils.property(locator, "matches", sourceMatches), sourceMatches));
                copy.matches = null;
                if (copyMatches!= null) {
                    List<Match> uniqueMatchesl = copy.getMatches();
                    uniqueMatchesl.addAll(copyMatches);
                }
            } else {
                copy.matches = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new History();
    }

}
