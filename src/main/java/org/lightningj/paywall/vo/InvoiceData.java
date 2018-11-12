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

import org.lightningj.paywall.JSONParsable;
import org.lightningj.paywall.util.HexUtils;
import org.lightningj.paywall.vo.amount.CryptoAmount;

import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.Instant;

/**
 * Invoice data is generated by LightningHandler and contains information about
 * one Lightning Invoice.
 *
 * Created by Philip Vendil on 2018-11-11.
 */
public class InvoiceData extends JSONParsable{

    protected byte[] preImageHash;
    protected String bolt11Invoice;
    protected CryptoAmount invoiceAmount;
    protected NodeInfo nodeInfo;
    protected Instant expireDate;
    protected Instant invoiceDate;

    /**
     * Empty Constructor
     */
    public InvoiceData(){}

    /**
     * Default constructor
     *
     * @param preImageHash the generated preImageHash from PreImageData which acts as an unique id for the payment.
     * @param bolt11Invoice the bolt11 invoice to display for the requester.
     * @param invoiceAmount the amount in the invoice. (Optional)
     * @param nodeInfo information about the lightning handlers lightning node. (Optional)
     * @param expireDate the time the invoice will expire.
     * @param invoiceDate the time this invoice was created.
     */
    public InvoiceData(byte[] preImageHash, String bolt11Invoice, CryptoAmount invoiceAmount, NodeInfo nodeInfo, Instant expireDate, Instant invoiceDate) {
        this.preImageHash = preImageHash;
        this.bolt11Invoice = bolt11Invoice;
        this.invoiceAmount = invoiceAmount;
        this.nodeInfo = nodeInfo;
        this.expireDate = expireDate;
        this.invoiceDate = invoiceDate;
    }

    /**
     * JSON Parseable constructor
     *
     * @param jsonObject the json object to parse
     */
    public InvoiceData(JsonObject jsonObject) throws JsonException {
        super(jsonObject);
    }

    /**
     *
     * @return the generated preImageHash from PreImageData which acts as an unique id for the payment.
     */
    public byte[] getPreImageHash() {
        return preImageHash;
    }

    /**
     *
     * @param preImageHash the generated preImageHash from PreImageData which acts as an unique id for the payment.
     */
    public void setPreImageHash(byte[] preImageHash) {
        this.preImageHash = preImageHash;
    }

    /**
     *
     * @return the bolt11 invoice to display for the requester.
     */
    public String getBolt11Invoice() {
        return bolt11Invoice;
    }

    /**
     *
     * @param bolt11Invoice the bolt11 invoice to display for the requester.
     */
    public void setBolt11Invoice(String bolt11Invoice) {
        this.bolt11Invoice = bolt11Invoice;
    }

    /**
     *
     * @return the amount in the invoice. (Optional)
     */
    public CryptoAmount getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     *
     * @param invoiceAmount the amount in the invoice. (Optional)
     */
    public void setInvoiceAmount(CryptoAmount invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**
     *
     * @return information about the lightning handlers lightning node. (Optional)
     */
    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    /**
     *
     * @param nodeInfo information about the lightning handlers lightning node. (Optional)
     */
    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    /**
     *
     * @return the time the invoice will expire.
     */
    public Instant getExpireDate() {
        return expireDate;
    }

    /**
     *
     * @param expireDate the time the invoice will expire.
     */
    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    /**
     *
     * @return the time this invoice was created.
     */
    public Instant getInvoiceDate() {
        return invoiceDate;
    }

    /**
     *
     * @param invoiceDate the time this invoice was created.
     */
    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * Method that should set the objects property to Json representation.
     *
     * @param jsonObjectBuilder the json object build to use to set key/values in json
     * @throws JsonException if problems occurred converting object to JSON.
     */
    @Override
    public void convertToJson(JsonObjectBuilder jsonObjectBuilder) throws JsonException {
        add(jsonObjectBuilder,"preImageHash", HexUtils.encodeHexString(preImageHash));
        add(jsonObjectBuilder,"bolt11Invoice", bolt11Invoice);
        addNotRequired(jsonObjectBuilder,"invoiceAmount", invoiceAmount);
        addNotRequired(jsonObjectBuilder,"nodeInfo", nodeInfo);
        add(jsonObjectBuilder,"expireDate", expireDate);
        add(jsonObjectBuilder,"invoiceDate", invoiceDate);
    }

    /**
     * Method to read all properties from a JsonObject into this value object.
     *
     * @param jsonObject the json object to read key and values from and set object properties.
     * @throws JsonException if problems occurred converting object from JSON.
     */
    @Override
    public void parseJson(JsonObject jsonObject) throws JsonException {
        preImageHash = getByteArrayFromHex(jsonObject,"preImageHash",true);
        bolt11Invoice = getString(jsonObject,"bolt11Invoice", true);
        if(jsonObject.containsKey("invoiceAmount") && !jsonObject.isNull("invoiceAmount")) {
            invoiceAmount = new CryptoAmount(getJsonObject(jsonObject, "invoiceAmount", false));
        }
        if(jsonObject.containsKey("nodeInfo") && !jsonObject.isNull("nodeInfo")) {
            nodeInfo = new NodeInfo(getJsonObject(jsonObject,"nodeInfo", false));
        }
        expireDate = Instant.ofEpochMilli(getLong(jsonObject,"expireDate", true));
        invoiceDate = Instant.ofEpochMilli(getLong(jsonObject,"invoiceDate", true));
    }
}