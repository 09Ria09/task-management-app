package server.api;


import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.Random;

/*
*this endpoint is used to check if a server is running
* Talio, and in the future will also provide
* the server admin password
 */
@RestController
@RequestMapping("/api/talio")
public class ServerController {

    private String adminKey;

    /**
     * This endpoint is used to check if the server is running
     * @return true if the server is running
     */
    @GetMapping(path = { "", "/" })
    public Boolean isThisTalio() {
        return true;
    }

    @Autowired
    public void generateKey() {
        long key = 0;
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            key += random.nextInt(1, 10) * Math.pow(10, i);
        }
        adminKey = new String(Long.toString(key));
        System.out.println(adminKey);
    }


    @GetMapping(path = {"/admin" })
    public String getAdminKey() {
        return adminKey;
    }

}
