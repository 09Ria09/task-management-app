package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //we will be using auto generated id's for the servers as well

    private String address; //this is essentially the IP of a server through which we will wish to connect

    //required by JPA for object mappers
    public Server(){
    }

    //is used for creating new server instances with a known address
    public Server(String address){
        this.address=address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
