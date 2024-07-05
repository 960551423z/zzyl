package com.zzyl.service.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 阿庆
 */
@SpringBootTest
public class ActivitiDeploymentTest {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void testDeploymentByCheckIn(){

        String fileName = "bpmn/check_in.bpmn";
        //定义流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .name("checkIn")
                .deploy();
        //部署流程
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void testDeploymentByRetreat(){

        String fileName = "bpmn/retreat.bpmn";
        //定义流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .name("retreat")
                .deploy();
        //部署流程
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }
}
