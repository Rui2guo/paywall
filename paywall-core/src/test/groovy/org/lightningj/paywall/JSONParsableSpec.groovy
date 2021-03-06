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
package org.lightningj.paywall

import org.lightningj.paywall.vo.Invoice
import org.lightningj.paywall.vo.InvoiceSpec
import org.lightningj.paywall.btcpayserver.vo.Invoice as BtcPayInvoice
import spock.lang.Specification

import javax.json.Json
import javax.json.JsonException
import javax.json.JsonObject
import java.time.Instant

/**
 * Unit tests for JSONParsable
 *
 * Created by Philip Vendil on 2018-10-18.
 */
class JSONParsableSpec extends Specification {

    def "Verify toJsonAsString and toJson"(){
        setup:
        BtcPayInvoice i = new BtcPayInvoice()
        i.token = "abc"
        i.currentTime = 126L

        expect:
        i.toJsonAsString(false) == """{"token":"abc","currentTime":126}"""
        i.toJsonAsString( true) == """
{
    "token": "abc",
    "currentTime": 126
}"""

    }

    def "Verify addNotRequired and get<Type>IfSet works as expected"(){
        setup:
        def b = Json.createBuilderFactory().createObjectBuilder()
        Invoice i = new Invoice()
        when: // Verify that null value doesn't set key in object
        i.addNotRequired(b,"somestring",null)
        then:
        b.build().get("somestring") == null

        when: // Verify that all types sets keys in object
        b = Json.createBuilderFactory().createObjectBuilder()
        i.addNotRequired(b,"somestring","somevalue")
        i.addNotRequired(b,"someint",1)
        i.addNotRequired(b,"someboolean",true)
        i.addNotRequired(b,"somelong",1234L)
        i.addNotRequired(b,"somedouble",(double) 1.0)
        i.addNotRequired(b,"someintlist", [1,2,3])
        i.addNotRequired(b,"somelonglist", [123L])
        i.addNotRequired(b,"somestringlist", ["string1","string2"])
        i.addNotRequired(b,"somedoublelist", [(double) 1.2,(double)2.3,(double)2.5])
        i.addNotRequired(b,"somebooleanlist", [true,false])
        i.addNotRequired(b,"someinstantlist", [Instant.ofEpochMilli(4000)])
        i.addNotRequired(b,"someobjectlist", [InvoiceSpec.genFullInvoiceData(false)])

        JsonObject o  = b.build()
        then:
        o.getString("somestring") == "somevalue"
        o.getInt("someint") == 1
        o.getBoolean("someboolean")
        o.getJsonNumber("somelong").longValueExact() == 1234L
        o.getJsonNumber("somedouble").doubleValue() == 1.0

        i.getStringIfSet(o,"notexists") == null
        i.getStringIfSet(o,"somestring") == "somevalue"
        i.getIntIfSet(o,"notexists") == null
        i.getIntIfSet(o,"someint") == 1
        i.getBooleanIfSet(o,"notexists") == null
        i.getBooleanIfSet(o,"someboolean")
        i.getLongIfSet(o,"notexists") == null
        i.getLongIfSet(o,"somelong") == 1234L
        i.getDoubleIfSet(o,"notexists") == null
        i.getDoubleIfSet(o,"somedouble") == (double) 1.0
        i.getJsonArray(o,"someintlist",true).size() == 3
        i.getJsonArray(o,"someintlist",true).getInt(1) == 2
        i.getJsonArray(o,"somelonglist",true).size() == 1
        i.getJsonArray(o,"somelonglist",true).getJsonNumber(0).longValue() == 123L
        i.getJsonArray(o,"somestringlist",true).size() == 2
        i.getJsonArray(o,"somestringlist",true).getString(1) == "string2"
        i.getJsonArray(o,"somedoublelist",true).size() == 3
        i.getJsonArray(o,"somedoublelist",true).getJsonNumber(1).doubleValue() == 2.3
        i.getJsonArray(o,"someinstantlist",true).size() == 1
        i.getJsonArray(o,"someinstantlist",true).getJsonNumber(0).longValue() == 4000
        i.getJsonArray(o,"someobjectlist",true).size() == 1
        org.lightningj.paywall.vo.Invoice invoice = new org.lightningj.paywall.vo.Invoice(i.getJsonArray(o,"someobjectlist",true).getJsonObject(0))
        invoice != null
    }

    def "Verify that add() throws JsonException if required value is null"(){
        setup:
        def b = Json.createBuilderFactory().createObjectBuilder()
        Invoice i = new Invoice()
        when:
        i.add(b,"somestring",null)
        then:
        def e = thrown(JsonException)
        e.message == "Error building JSON object, required key somestring is null."
    }

    def "Verify that all get<Type> method throws JsonException if required but not set"(){
        setup:
        def b = Json.createBuilderFactory().createObjectBuilder()
        def o = b.build()
        Invoice i = new Invoice()
        when:
        i.getString(o,"notexists", true)
        then:
        def e = thrown(JsonException)
        e.message == "Error parsing JSON data, field key notexists is required."
        when:
        i.getInt(o,"notexists", true)
        then:
        e = thrown(JsonException)
        e.message == "Error parsing JSON data, field key notexists is required."
        when:
        i.getBoolean(o,"notexists", true)
        then:
        e = thrown(JsonException)
        e.message == "Error parsing JSON data, field key notexists is required."
        when:
        i.getLong(o,"notexists", true)
        then:
        e = thrown(JsonException)
        e.message == "Error parsing JSON data, field key notexists is required."
        when:
        i.getDouble(o,"notexists", true)
        then:
        e = thrown(JsonException)
        e.message == "Error parsing JSON data, field key notexists is required."
    }

    static JsonObject toJsonObject(String jsonData){
        return Json.createReader(new StringReader(jsonData)).readObject()
    }
}
