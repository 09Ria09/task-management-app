package server.api;

import commons.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
this endpoint  handles requests related to servers so that
servers can be added, retrieved or removed from the list of
all available servers
 */
@RestController
@RequestMapping("/api/talio")
public class ServerController {

    @GetMapping(path = { "", "/" })
    public Boolean isThisTalio() {
        return true;
    }

}
