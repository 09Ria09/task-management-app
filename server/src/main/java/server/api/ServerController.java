package server.api;


import org.springframework.web.bind.annotation.*;


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
