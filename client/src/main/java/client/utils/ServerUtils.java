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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;



import org.glassfish.jersey.client.ClientConfig;


import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;

import javax.inject.Inject;

public class ServerUtils {

    private static String serverAddress;

    @Inject
    public ServerUtils(final String serverAddress) {
        ServerUtils.serverAddress = serverAddress;
    }

//    public void getQuotesTheHardWay() throws IOException {
//        var url = new URL("http://localhost:8080/api/quotes");
//        var is = url.openConnection().getInputStream();
//        var br = new BufferedReader(new InputStreamReader(is));
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//    }


    public Boolean isTalioServer(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/talio") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Boolean>() {});
    }

    public void setServerAddress(final String serverAddress) {
        ServerUtils.serverAddress = "http://"+serverAddress+":8080";
    }

    public void disconnect() {
        serverAddress=null;
    }
}