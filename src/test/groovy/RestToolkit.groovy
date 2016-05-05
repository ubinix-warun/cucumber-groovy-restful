/**
 * Created by warun on 5/2/16.
 */

//import org.apache.log4j.Logger
import cucumber.runtime.*
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder;
import static groovyx.net.http.ContentType.*
//import static groovyx.net.http.Method.*
//import groovyx.net.http.RESTClient
import groovy.util.slurpersupport.GPathResult
import groovyx.net.http.URIBuilder
import org.apache.http.client.*

this.metaClass.mixin(cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(cucumber.runtime.groovy.EN)

def target = null
def mode = ""
def path = ""
def resp = null
def reqb = [:]
def status = 0

Given(~"I access the resource host \"([^\"]*)\"") { String host ->
    resp = null
    target = host
}

Given(~"I request \"([^\"]*)\" to \"([^\"]*)\"") { String method, String url ->
    mode = method
    path = url
}

Given(~"I provide parameter \"([^\"]*)\" as \"([^\"]*)\"") { String name, String value ->
    reqb[name] = value
}

When(~"I retrieve the results") { ->

    try {
        httpClient = new HTTPBuilder(target)
        httpClient.ignoreSSLIssues()
        // TODO if Method
        resp = httpClient.post([path: path,
                                requestContentType: JSON,
                                body: reqb])
    } catch (HttpResponseException ex) {
        status = ex.getStatusCode()
    }
    if (resp != null) {
        slurper = new JsonSlurper()
        parsed = slurper.parseText(resp.data)
    }
}

//Then(~"the status code should be \"(.*)\"") { String expectedStatusCode ->
//    assert status == expectedStatusCode
//}

Then(~"it should have the field \"(.*)\" containing the value \"(.*)\"") { String field, String value ->
    assert parsed."${field}".toString().equals(value)
}