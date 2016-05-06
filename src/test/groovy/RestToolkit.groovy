/**
 * Created by warun on 5/2/16.
 */

import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder;
import static groovyx.net.http.ContentType.*
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
        // TODO if Method GET/POST
        // TODO disable log file?
        resp = httpClient.post([path: path,
                                requestContentType: JSON,
                                body: reqb])
    } catch (HttpResponseException ex) {
        status = ex.getStatusCode()
    }
    if (resp != null) {
        if(resp.data != null) {
            slurper = new JsonSlurper()
            parsed = slurper.parseText(resp.data)
        }else {
            parsed = resp
        }
    }
}

//Then(~"the status code should be \"(.*)\"") { String expectedStatusCode ->
//    assert status == expectedStatusCode
//}

Then(~"show response") {  ->
    println parsed
}

Then(~"size of \"(.*)\" as \"(.*)\"") { String field, String size->
    assert parsed."${field}".size() == Integer.parseInt(size)
}

Then(~"it should have the field \"(.*)\" containing the value \"(.*)\"") { String field, String value ->
    assert parsed."${field}".toString().equals(value)
}
