package server.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
*this endpoint is used to check if a server is running
* Talio, and in the future will also provide
* the server admin password
 */
@RestController
@RequestMapping("/api/talio")
public class ServerController {

    /**
     * This endpoint is used to check if the server is running
     * @return true if the server is running
     */
    @GetMapping(path = { "", "/" })
    public Boolean isThisTalio() {
        return true;
    }

}
