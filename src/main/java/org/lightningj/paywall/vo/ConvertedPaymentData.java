/*
 *************************************************************************
 *                                                                       *
 *  LightningJ                                                           *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public License   *
 *  (LGPL-3.0-or-later)                                                  *
 *  License as published by the Free Software Foundation; either         *
 *  version 3 of the License, or any later version.                      *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/
package org.lightningj.paywall.vo;

import org.lightningj.paywall.vo.amount.CryptoAmount;

import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Extention of PaymentData also containg the amount generated
 * by the CurrencyConverter in order to determine which amount
 * the LightningHandler should use to create the invoice.
 *
 * Created by Philip Vendil on 2018-11-11.
 */
public class ConvertedPaymentData extends PaymentData{

    protected CryptoAmount convertedAmount;

    /**
     * Empty Constructor
     */
    public ConvertedPaymentData() {
    }

    /**
     * Default Constructor.
     *
     * @param paymentData    the original payment data.
     * @param convertedAmount the amount generated by the currency converter.
     */
    public ConvertedPaymentData(PaymentData paymentData, CryptoAmount convertedAmount) {
        super(paymentData.preImageHash, paymentData.description, paymentData.requestedAmount, paymentData.expireDate);
        this.convertedAmount = convertedAmount;
    }

    /**
     * JSON Parseable constructor
     *
     * @param jsonObject the json object to parse
     */
    public ConvertedPaymentData(JsonObject jsonObject) throws JsonException {
        super(jsonObject);
    }

    /**
     *
     * @return the amount generated by the currency converter.
     */
    public CryptoAmount getConvertedAmount() {
        return convertedAmount;
    }

    /**
     *
     * @param convertedAmount the amount generated by the currency converter.
     */
    public void setConvertedAmount(CryptoAmount convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    /**
     * Method that should set the objects property to Json representation.
     *
     * @param jsonObjectBuilder the json object build to use to set key/values in json
     * @throws JsonException if problems occurred converting object to JSON.
     */
    @Override
    public void convertToJson(JsonObjectBuilder jsonObjectBuilder) throws JsonException {
        super.convertToJson(jsonObjectBuilder);
        add(jsonObjectBuilder,"convertedAmount",convertedAmount);
    }

    /**
     * Method to read all properties from a JsonObject into this value object.
     *
     * @param jsonObject the json object to read key and values from and set object properties.
     * @throws JsonException if problems occurred converting object from JSON.
     */
    @Override
    public void parseJson(JsonObject jsonObject) throws JsonException {
        super.parseJson(jsonObject);
        convertedAmount =  new CryptoAmount(getJsonObject(jsonObject,"convertedAmount",true));
    }
}
