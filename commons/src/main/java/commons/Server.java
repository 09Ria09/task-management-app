package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //we will be using auto generated id's for the servers as well

    private String address; //this is essentially the IP of a
    // server through which we will wish to connect

    private boolean connected = true; //we will use this to
    // indicate all the servers currently connected to
    //the database (instead of deleting a server when logging out, we will keep its data)

    //required by JPA for object mappers
    public Server(){
    }

    //is used for creating new server instances with a known address
    public Server(final String address){
        this.address=address;
    }
    //in most setters we have to assign the parameters as
    //final so that they don't get reassigned within the method
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(final boolean connected) {
        this.connected = connected;
    }
}
