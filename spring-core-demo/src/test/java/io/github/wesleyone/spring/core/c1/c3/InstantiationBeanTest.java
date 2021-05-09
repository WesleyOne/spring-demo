package io.github.wesleyone.spring.core.c1.c3;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author http://wesleyone.github.io/
 */
public class InstantiationBeanTest {

    @Test
    public void instantiationBeanTest() {
        // 创建和配置bean
        ApplicationContext context = new ClassPathXmlApplicationContext("1_3_2_实例化Bean.xml");

        ExampleBean exampleBean = context.getBean("exampleBean", ExampleBean.class);
        Assert.assertNotNull(exampleBean);

        ClientService clientService = context.getBean("clientService", ClientService.class);
        Assert.assertNotNull(clientService);

        ClientService clientService2 = context.getBean("clientService2", ClientService.class);
        Assert.assertNotNull(clientService2);

    }

    @Test
    public void getTypeTest() {
        // 创建和配置bean
        ApplicationContext context = new ClassPathXmlApplicationContext("1_3_2_实例化Bean.xml");

        ExampleBean exampleBean = context.getBean("exampleBean", ExampleBean.class);
        Assert.assertNotNull(exampleBean);
        Class<?> exampleBeanType = context.getType("exampleBean");
        Assert.assertEquals(exampleBeanType, ExampleBean.class);

        ClientService clientService = context.getBean("clientService", ClientService.class);
        Assert.assertNotNull(clientService);
        Class<?> clientServiceType = context.getType("clientService");
        Assert.assertEquals(clientServiceType, ClientService.class);

        ClientService clientService2 = context.getBean("clientService2", ClientService.class);
        Assert.assertNotNull(clientService2);
        Class<?> clientService2Type = context.getType("clientService2");
        Assert.assertEquals(clientService2Type, ClientService.class);

    }



}
