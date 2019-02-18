package com.viking.ci.domain

import groovy.json.JsonOutput
import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.StringEntity

import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.Method.POST

class HttpClient {

    private HTTPBuilder client = new HTTPBuilder()

    private HttpClient() {
        client.encoder.'application/json' = { requestData ->
            def jsonData = JsonOutput.toJson(requestData)
            def entity = new StringEntity((String) jsonData)
            entity.setContentType("application/json")
            return entity
        }
    }

    static HttpClient getInstance() {
        return SingletonHolder.instance;
    }

    HTTPBuilder getBuilder() {
        return client;
    }

    void postRequest(String requestUrl, String requestPath, String requestBody, Map requestQuery, Closure successCallback, Closure failedCallback) {
        client.request(requestUrl,
            POST, TEXT) { req ->
            headers.'User-Agent' = "Gradle Java Exec Task/1.0.0"
            headers.'Content-Type' = 'application/json'
            headers.Accept = 'application/json'
            uri.path = requestPath
            uri.query = requestQuery
            body = requestBody
            response.success = successCallback
            response.failure = failedCallback
        }
    }

    private static final class SingletonHolder {
        private static HttpClient instance = new HttpClient()
    }
}