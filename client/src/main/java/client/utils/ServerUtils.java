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


import commons.Board;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.client.Entity;
import org.glassfish.jersey.client.ClientConfig;


import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;

import javax.inject.Inject;
import java.util.List;

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
//METHODS FOR GETTING ALL BOARDS FROM SERVER, ALL LISTS FROM BOARD AND ALL TASKS FROM LIST

    //get all boards from server
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }

    //get all lists from server
    //TODO create api lists endpoint
   /* public List<TaskList> getLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {});
    }
*/
//get all lists of a board from server
    public List<TaskList> getLists(final Long boardid) {
        return ClientBuilder.newClient(new ClientConfig()) //
                //endpoint still needs to be created
                .target(serverAddress).path("api/boards/"+boardid)
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {});
    }

    //get all tasks in a specific list of a specific board from server
    public List<Task> getTasks(final Long boardid,final Long listid) {
        return ClientBuilder.newClient(new ClientConfig()) //
                //endpoint still needs to be created
                .target(serverAddress).path("api/boards/"+ boardid +"/tasklists/"+listid+"/tasks")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Task>>() {});
    }

    //METHODS FOR CREATING BOARD, LIST AND TASK
    //create a board on the server
    public Board addBoard(final Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    //create a list on the server
    public TaskList addList(final TaskList list){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards"+list.getBoard().getId()+"/tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), TaskList.class);
    }
    //create a task on the server
    public Task addTask(final Task task){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards"
                        +task.getList().getBoard().getId()
                        +"/"+task.getList().getId()+"/task") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    //METHODS FOR UPDATING BOARD, LIST AND TASK

    //update a board on the server
    public void updateBoard(final long boardId, final String newName) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards/" + boardId) //
                .request(APPLICATION_JSON) //
                .put(Entity.entity(newName, APPLICATION_JSON), Void.class);
    }


    //update a list on the server
    public void updateList(final long boardId, final long listId, final String newName) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards/" + boardId + "/" + listId) //
                .request(APPLICATION_JSON) //
                .put(Entity.entity(newName, APPLICATION_JSON), Void.class);
    }

    //update a task on the server
    public void updateTask(final long boardId, final long listId,
                           final long taskId, final String newName) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards/"
                        + boardId + "/" + listId + "/" + taskId) //
                .request(APPLICATION_JSON) //
                .put(Entity.entity(newName, APPLICATION_JSON), Void.class);
    }
    //METHODS FOR DELETING BOARD, LIST AND TASK

    //delete a board on the server
    public void deleteBoard(final long boardId) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards/" + boardId) //
                .request(APPLICATION_JSON) //
                .delete();
    }
    //delete a list on the server
    public void deleteList(final long boardId, final long listId) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards/" + boardId + "/" + listId) //
                .request(APPLICATION_JSON) //
                .delete();
    }

    //delete a task on the server
    public void deleteTask(final long boardId, final long listId, final long taskId) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards/"
                        + boardId + "/" + listId + "/" + taskId) //
                .request(APPLICATION_JSON) //
                .delete();
    }

    //method for checking if a server is a Talio server
    public Boolean isTalioServer(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/talio") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Boolean>() {});
    }

    //method for setting the server address once the user has entered it
    public void setServerAddress(final String serverAddress) {
        ServerUtils.serverAddress = "http://"+serverAddress+":8080";
    }

    //method for disconnecting from the server
    public void disconnect() {
        serverAddress=null;
    }
}