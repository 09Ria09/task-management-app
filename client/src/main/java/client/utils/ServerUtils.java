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


import commons.TaskList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private String serverAddress;

   /**
        * Check if the server is a Talio server
        *
        * @return an empty optional if the server is a Talio server,
        * or an optional containing an error message
        */
    public Optional<String> isTalioServer() {
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverAddress + "/api/talio"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Boolean.parseBoolean(response.body())
                        ? Optional.empty()
                        : Optional.of("Not a Talio server");
            }
            else if (response.statusCode() == 404) {
                return Optional.of("Not a Talio server");
            }
            else {
                return Optional.of("Unexpected response status");
            }
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

    /**
     * Get all lists from a specific board.
     *
     * @param boardid The id of the board.
     * @return A list of lists.'
     * I KEPT THIS ONE HERE JUST SO THAT THE BUILD RUNS
     * THIS IS NOT THE FINAL VERSION OF THIS METHOD
     */
    public List<TaskList> getLists(final Long boardid) {
        return ClientBuilder.newClient(new ClientConfig()) //
                //endpoint still needs to be created
                .target(serverAddress).path("api/lists/" + boardid + "/tasklists")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {
                });
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
}