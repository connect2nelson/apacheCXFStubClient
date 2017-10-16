
package com.abm.weatherxnet;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetWeatherResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getWeatherResult"
})
@XmlRootElement(name = "GetWeatherResponse")
public class GetWeatherResponse {

    @XmlElement(name = "GetWeatherResult")
    protected String getWeatherResult;

    /**
     * Gets the value of the getWeatherResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGetWeatherResult() {
        return getWeatherResult;
    }

    /**
     * Sets the value of the getWeatherResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGetWeatherResult(String value) {
        this.getWeatherResult = value;
    }

}
