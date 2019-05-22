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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="players" type="{}player" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="history" type="{}history" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="updatedTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "players",
    "history"
})
@XmlRootElement(name = "aoeLeague")
public class AoeLeague
    implements Cloneable, CopyTo, Equals, HashCode
{

    protected List<Player> players;
    protected History history;
    @XmlAttribute(name = "updatedTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updatedTime;

    /**
     * Gets the value of the players property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the players property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlayers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Player }
     * 
     * 
     */
    public List<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<Player>();
        }
        return this.players;
    }

    /**
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link History }
     *     
     */
    public History getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link History }
     *     
     */
    public void setHistory(History value) {
        this.history = value;
    }

    /**
     * Gets the value of the updatedTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdatedTime() {
        return updatedTime;
    }

    /**
     * Sets the value of the updatedTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdatedTime(XMLGregorianCalendar value) {
        this.updatedTime = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof AoeLeague)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final AoeLeague that = ((AoeLeague) object);
        {
            List<Player> lhsPlayers;
            lhsPlayers = (((this.players!= null)&&(!this.players.isEmpty()))?this.getPlayers():null);
            List<Player> rhsPlayers;
            rhsPlayers = (((that.players!= null)&&(!that.players.isEmpty()))?that.getPlayers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "players", lhsPlayers), LocatorUtils.property(thatLocator, "players", rhsPlayers), lhsPlayers, rhsPlayers)) {
                return false;
            }
        }
        {
            History lhsHistory;
            lhsHistory = this.getHistory();
            History rhsHistory;
            rhsHistory = that.getHistory();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "history", lhsHistory), LocatorUtils.property(thatLocator, "history", rhsHistory), lhsHistory, rhsHistory)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsUpdatedTime;
            lhsUpdatedTime = this.getUpdatedTime();
            XMLGregorianCalendar rhsUpdatedTime;
            rhsUpdatedTime = that.getUpdatedTime();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "updatedTime", lhsUpdatedTime), LocatorUtils.property(thatLocator, "updatedTime", rhsUpdatedTime), lhsUpdatedTime, rhsUpdatedTime)) {
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
            List<Player> thePlayers;
            thePlayers = (((this.players!= null)&&(!this.players.isEmpty()))?this.getPlayers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "players", thePlayers), currentHashCode, thePlayers);
        }
        {
            History theHistory;
            theHistory = this.getHistory();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "history", theHistory), currentHashCode, theHistory);
        }
        {
            XMLGregorianCalendar theUpdatedTime;
            theUpdatedTime = this.getUpdatedTime();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "updatedTime", theUpdatedTime), currentHashCode, theUpdatedTime);
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
        if (draftCopy instanceof AoeLeague) {
            final AoeLeague copy = ((AoeLeague) draftCopy);
            if ((this.players!= null)&&(!this.players.isEmpty())) {
                List<Player> sourcePlayers;
                sourcePlayers = (((this.players!= null)&&(!this.players.isEmpty()))?this.getPlayers():null);
                @SuppressWarnings("unchecked")
                List<Player> copyPlayers = ((List<Player> ) strategy.copy(LocatorUtils.property(locator, "players", sourcePlayers), sourcePlayers));
                copy.players = null;
                if (copyPlayers!= null) {
                    List<Player> uniquePlayersl = copy.getPlayers();
                    uniquePlayersl.addAll(copyPlayers);
                }
            } else {
                copy.players = null;
            }
            if (this.history!= null) {
                History sourceHistory;
                sourceHistory = this.getHistory();
                History copyHistory = ((History) strategy.copy(LocatorUtils.property(locator, "history", sourceHistory), sourceHistory));
                copy.setHistory(copyHistory);
            } else {
                copy.history = null;
            }
            if (this.updatedTime!= null) {
                XMLGregorianCalendar sourceUpdatedTime;
                sourceUpdatedTime = this.getUpdatedTime();
                XMLGregorianCalendar copyUpdatedTime = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "updatedTime", sourceUpdatedTime), sourceUpdatedTime));
                copy.setUpdatedTime(copyUpdatedTime);
            } else {
                copy.updatedTime = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new AoeLeague();
    }

}
