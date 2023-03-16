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
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;


import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class ServerUtils {

    private static String serverAddress;

    @Inject
    public ServerUtils(final String serverAddress) {
        ServerUtils.serverAddress = serverAddress;
    }

//METHODS FOR GETTING ALL BOARDS FROM SERVER, ALL LISTS FROM BOARD AND ALL TASKS FROM LIST

    /**
     * Get all boards from the server.
     *
     * @return A list of boards.
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {
                });
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
    /**
     * Get all lists from a specific board.
     *
     * @param boardid The id of the board.
     * @return A list of lists.
     */
    public List<TaskList> getLists(final Long boardid) {
        return ClientBuilder.newClient(new ClientConfig()) //
                //endpoint still needs to be created
                .target(serverAddress).path("api/boards/" + boardid)
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {
                });
    }

    /**
     * Get all tasks from a specific list.
     *
     * @param boardid The id of the board.
     * @param listid  The id of the list.
     * @return A list of tasks.
     */
    public List<Task> getTasks(final Long boardid, final Long listid) {
        return ClientBuilder.newClient(new ClientConfig()) //
                //endpoint still needs to be created
                .target(serverAddress)
                .path("api/boards/" + boardid + "/tasklists/" + listid + "/tasks")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Task>>() {
                });
    }

    //METHODS FOR CREATING BOARD, LIST AND TASK

    /**
     * Create a board on the server.
     *
     * @param board The board to create.
     * @return The created board.
     */
    public Board addBoard(final Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress)
                .path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Create a list on the server.
     *
     * @param list The list to create.
     * @return The created list.
     */
    public TaskList addList(final TaskList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress)
                .path("api/boards/" + list.getBoard().getId() + "/tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), TaskList.class);
    }

    /**
     * Create a task on the server.
     *
     * @param task The task to create.
     * @return The created task.
     */
    public Task addTask(final Task task) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress)
                .path("api/boards/"
                        + task.getList().getBoard().getId()
                        + "/" + task.getList().getId() + "/task") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    //METHODS FOR UPDATING BOARD, LIST AND TASK

    /**
     * Update a board on the server
     *
     * @param boardId the id of the board to update
     * @param newName the new name of the board
     * @return a response indicating whether the update was successful
     */
    public Response updateBoard(final long boardId, final String newName) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverAddress)
                    .path("api/boards/" + boardId) //
                    .request(APPLICATION_JSON) //
                    .put(Entity.entity(newName, APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return Response.status(Response.Status.OK)
                        .entity("Board name updated successfully").build();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while updating the board name").build();
            }
        } catch (Exception e) {
            // Log the error or perform other error-handling actions
            e.printStackTrace();
            // Return an appropriate HTTP status code and error message to the client
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while updating the board name").build();
        }

    }


    /**
     * Update a list on the server
     *
     * @param boardId the id of the board the list belongs to
     * @param listId  the id of the list to update
     * @param newName the new name of the list
     * @return a response indicating whether the list was updated successfully or not
     */
    public Response updateList(final long boardId, final long listId, final String newName) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverAddress)
                    .path("api/boards/" + boardId + "/" + listId) //
                    .request(APPLICATION_JSON) //
                    .put(Entity.entity(newName, APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return Response.status(Response.Status.OK)
                        .entity("List name updated successfully").build();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("List not found").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while updating the list name").build();
            }
        } catch (Exception e) {
            // Log the error or perform other error-handling actions
            e.printStackTrace();
            // Return an appropriate HTTP status code and error message to the client
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while updating the list name").build();
        }
    }


    /**
     * Update a task on the server
     *
     * @param boardId the id of the board the task belongs to
     * @param listId  the id of the list the task belongs to
     * @param taskId  the id of the task to be updated
     * @param newName the new name of the task
     * @return a response object containing the status code and message
     */
    public Response updateTask(final long boardId, final long listId,
                               final long taskId, final String newName) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverAddress)
                    .path("api/boards/"
                            + boardId + "/" + listId + "/" + taskId) //
                    .request(APPLICATION_JSON) //
                    .put(Entity.entity(newName, APPLICATION_JSON), Response.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return Response.status(Response.Status.OK)
                        .entity("Task name updated successfully").build();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task not found").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while updating the task name").build();
            }
        } catch (Exception e) {
            // Log the error or perform other error-handling actions
            e.printStackTrace();
            // Return an appropriate HTTP status code and error message to the client
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while updating the task name").build();
        }
    }
    //METHODS FOR DELETING BOARD, LIST AND TASK

    /**
     * Delete a board on the server
     *
     * @param boardId the id of the board to delete
     * @return a response indicating whether the board was deleted successfully or not
     */
    public Response deleteBoard(final long boardId) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverAddress)
                    .path("api/boards/" + boardId) //
                    .request(APPLICATION_JSON) //
                    .delete();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return Response.status(Response.Status.OK)
                        .entity("Board deleted succesfully!").build();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board not found!").build(); //this is how we will
                // handle errors when deleting something that
                //does not exist
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred while deleting the board").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred while deleting the board").build();
        }
    }

    /**
     * Delete a list on the server
     *
     * @param boardId the id of the board the list belongs to
     * @param listId  the id of the list to delete
     * @return a response indicating whether the list was deleted successfully or not
     */
    public Response deleteList(final long boardId, final long listId) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverAddress)
                    .path("api/boards/" + boardId + "/" + listId) //
                    .request(APPLICATION_JSON) //
                    .delete();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return Response.status(Response.Status.OK)
                        .entity("List deleted succesfully!").build();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("List not found!").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred while deleting the list").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred while deleting the list").build();
        }

    }

    /**
     * Delete a task on the server
     *
     * @param boardId the id of the board the task belongs to
     * @param listId  the id of the list the task belongs to
     * @param taskId  the id of the task to delete
     * @return a response indicating whether the task was deleted successfully or not
     */
    public Response deleteTask(final long boardId, final long listId, final long taskId) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverAddress)
                    .path("api/boards/" + boardId + "/" + listId + "/" + taskId) //
                    .request(APPLICATION_JSON) //
                    .delete();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return Response.status(Response.Status.OK)
                        .entity("Task deleted succesfully!").build();
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task not found!").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred while deleting the taskt").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred while deleting the task").build();
        }
    }

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
            System.out.println("1"+e.getMessage());
            return Optional.of("IOException");
        } catch (InterruptedException e) {
            //this is something related to threads
            System.out.println("2"+e.getMessage());
            return Optional.of("InterruptedException");
        } catch (Exception e) {
            //unsupportd uri
            System.out.println("3"+e.getMessage());
            return Optional.of("Exception");
        }
    }

    /**
     * Set the server address
     * @param serverAddress the server address
     */
    public void setServerAddress(final String serverAddress) {
        ServerUtils.serverAddress = "http://" + serverAddress + ":8080";
    }

    //method for disconnecting from the server
    public void disconnect() {
        serverAddress = null;
    }
}