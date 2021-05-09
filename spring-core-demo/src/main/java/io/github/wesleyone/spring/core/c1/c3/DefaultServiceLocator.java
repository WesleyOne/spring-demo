package io.github.wesleyone.spring.core.c1.c3;

/**
 * @author http://wesleyone.github.io/
 */
public class DefaultServiceLocator {

    private static ClientService clientService = ClientService.createInstance();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
