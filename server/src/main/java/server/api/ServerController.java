package server.api;

import commons.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ServerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
this endpoint  handles requests related to servers so that
servers can be added, retrieved or removed from the list of
all available servers
 */
@RestController
@RequestMapping("/servers")
public class    ServerController {


    private final ServerRepository serverRepository;

    public ServerController(final ServerRepository serverRepository){
        this.serverRepository = serverRepository;
    }

   /*
    Mapping for getting only connected servers
     */
    @GetMapping(path = { "", "/" })
    public List<Server> getServers() {
        return serverRepository.findAll()
                .stream()
                .filter(server -> server.isConnected())
                        .collect(Collectors.toList());
        //we have to use .collect
        // since we turned our list into a stream
    }

    /*
    POST method for adding a server
     */
    @PostMapping("/add")
    public ResponseEntity<Server> addServer(@RequestBody final Server server){
        if (server==null|| isNullOrEmpty(server.getAddress())){
            return ResponseEntity.badRequest().build(); //400 bad request error
        }

        //this line stores the saved server in a variable for status msg and also saves in repo
        Server savedServer = serverRepository.save(server);

        return ResponseEntity.ok(savedServer); //200 OK
    }

    /*
    Post method for disconnecting a server (when disconnect button is pressed)
     */
    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnectServer(@RequestBody final Server server){
        if(server==null || isNullOrEmpty(server.getAddress())){
            return ResponseEntity.badRequest().body("Invalid server object");
        }

        //findBy<fieldName> in jpa returns an optional so I have to use isEmpty and get
        Optional<Server> existingServer = serverRepository.findByAddress(server.getAddress());
        if(existingServer.isEmpty()){
            return ResponseEntity.notFound().build(); //404 not found
        }

        Server disconnectServer = existingServer.get();
        disconnectServer.setConnected(false);
        serverRepository.save(disconnectServer);

        return ResponseEntity.ok("Server was disconnected");
    }

/*
helper method
 */
    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }
}
