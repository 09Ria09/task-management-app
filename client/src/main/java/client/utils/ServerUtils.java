/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;


import jakarta.ws.rs.core.Response;

import objects.Servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;


public class ServerUtils {

    private RestUtils restUtils;
    private String serverAddress;

    private HttpClient httpClient;
    public ServerUtils() {
        this.restUtils = new RestUtils();
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    /**
        * Check if the server is a Talio server
        *
        * @return an empty optional if the server is a Talio server,
        * or an optional containing an error message
        */
    public Optional<String> isTalioServer() {
        try {
            HttpResponse<String> response = sendTalioServerRequest();
            return handleTalioServerResponse(response);
        } catch (IOException e) {
            //timeout
            return Optional.of("IOException");
        } catch (InterruptedException e) {
            //this is something related to threads
            return Optional.of("InterruptedException");
        } catch (Exception e) {
            //unsupported uri
            return Optional.of("Exception");
        }
    }

    private HttpResponse<String> sendTalioServerRequest() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/api/talio"))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Optional<String> handleTalioServerResponse(final HttpResponse<String> response) {
        if (response.statusCode() == 200) {
            if (Boolean.parseBoolean(response.body())) {
                if (!Servers.getInstance().getServers().containsKey(serverAddress)) {
                    Servers.getInstance().getServers().put(serverAddress, new HashSet<>());
                }
                return Optional.empty();
            } else {
                return Optional.of("Not a Talio server");
            }
        } else if (response.statusCode() == 404) {
            return Optional.of("Not a Talio server");
        } else {
            return Optional.of("Unexpected response status");
        }
    }
    /**
     * Set the server address
     * @param serverAddress the server address
     */
    public void setServerAddress(final String serverAddress) {
        this.serverAddress = "http://" + serverAddress + ":8080";
    }

    //method for disconnecting from the server
    public void disconnect() {
        serverAddress = null;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getWebsocketURL() {
        if (serverAddress == null || serverAddress.isEmpty())
            return "";
        return serverAddress.replace("http://", "ws://") + "/websocket";
    }

    public String getAdminKey() throws Exception {
        Response response = restUtils.sendRequest(serverAddress, "api/talio/admin",
                RestUtils.Methods.GET, null);
        return restUtils.handleResponse(response, String.class, "getAdminKey");
    }
    //made this in order to be able to test isTalioServer
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    public RestUtils getRestUtils() {
        return restUtils;
    }

    //for testing getAdminKey
    public void setRestUtils(final RestUtils restUtils) {
        this.restUtils = restUtils;
    }

}