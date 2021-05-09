package io.github.wesleyone.spring.core.c1.c3;

/**
 * @author http://wesleyone.github.io/
 */
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
