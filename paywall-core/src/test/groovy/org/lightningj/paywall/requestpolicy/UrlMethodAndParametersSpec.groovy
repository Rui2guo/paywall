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
package org.lightningj.paywall.requestpolicy

import org.lightningj.paywall.util.BCUtils
import org.lightningj.paywall.vo.RequestData
import org.lightningj.paywall.web.CachableHttpServletRequest
import spock.lang.Specification

/**
 * Unit tests for UrlMethodAndParameters
 *
 * Created by Philip Vendil on 2018-10-27.
 */
class UrlMethodAndParametersSpec extends Specification {

    def policy = new UrlMethodAndParameters()
    def request = Mock(CachableHttpServletRequest)

    def setupSpec(){
        BCUtils.installBCProvider()
    }

    def "Verify that URL,Method and parameters are used for aggregation"(){
        when:
        RequestData result1 = policy.significantRequestDataDigest(request)
        then:
        result1.significantData.length == 32
        1 * request.getMethod() >> { return "POST"}
        1 * request.getRequestURL() >> { return new StringBuffer("http://somehost/test")}
        1 * request.getParameterMap() >> { return ["param1" : null, "param2" : ["val1","val2"] as String[], "param3" : ["val1"] as String[]]}
        when:
        RequestData result2 = policy.significantRequestDataDigest(request)
        then:
        result1 != result2
        result2.significantData.length == 32
        1 * request.getMethod() >> { return "POST"}
        1 * request.getRequestURL() >> { return new StringBuffer("http://somehost/test")}
        1 * request.getParameterMap() >> { return ["param1" : null, "param2" : ["val1","val3"] as String[], "param3" : ["val1"] as String[]]}
    }
}
