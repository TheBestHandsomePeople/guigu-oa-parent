package com.atguigu.auth.activity;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-18-10:27
 */
@SpringBootTest
public class ProcessTestDemo1 {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void deployProcess002() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban03.bpmn20.xml").name("加班申请流程03").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //查询组任务
    @Test
    public void findGroupTaskList() {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("jerry01").list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }

    }

    //拾取组任务
    @Test
    public void  claimTask(){
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("jerry01").singleResult();
        if (task != null) {
            taskService.claim(task.getId(),"jerry01");
            System.out.println("任务拾取了");
        }
    }

    /**
     * 查询当前tom01个人待执行的任务
     */
    @Test
    public void findPendingTaskList() {
        //任务负责人
        String assignee = "jerry01";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
        for (Task task : list) {
            System.out.println("流程实例id："+task.getProcessDefinitionId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());

        }
    }
//办理个人业务
    @Test
    public void completGroupTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("jerry01")  //要查询的负责人
                .singleResult();//返回一条
        taskService.complete(task.getId());
    }


    @Test
    public void startProcessInstance02() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban02");
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void deployProcess001() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban01.bpmn20.xml").name("加班申请流程01").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    public void startProcessInstance01() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban03");
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }

    //***********************************************************************************************************************

    @Test
    public void deployProcess01() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //启动了流程实例
    @Test
    public void startProcessInstance() {
        Map<String, Object> map = new HashMap<>();
        //设置任务人
        map.put("assignee1", "lihua");
        map.put("assignee2", "kangkang");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban", map);

        System.out.println("流程定义id：" +processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }
}
