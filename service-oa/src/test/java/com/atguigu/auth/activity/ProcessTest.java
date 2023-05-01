package com.atguigu.auth.activity;

import javafx.scene.media.VideoTrack;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-14-22:55
 */
@SpringBootTest
public class ProcessTest {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

//    单个流程实例挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "f162f7d3-dd84-11ed-9f3f-3a00258cf1e5";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            // 暂定,那就可以激活
            // 参数1:流程定义的id  参数2:是否激活    参数3:时间点
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程定义" + processInstanceId + "激活");

        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程定义" + processInstanceId + "挂起  ");
        }
    }

    /**
     * 启动流程实例，添加businessKey
     */
    @Test
    public void startUpProcessAddBusinessKey(){
        String businessKey = "1";
        // 启动流程实例，指定业务标识businessKey，也就是请假申请单id
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("qingjia",businessKey);
        // 输出
        System.out.println("业务id:"+processInstance.getBusinessKey());
        System.out.println("业务id:"+processInstance.getId());
    }

//    全部流程实例挂起
    @Test
    public void suspendProcessInstance() {
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery().processDefinitionKey("qingjia").singleResult();
        // 获取到当前流程定义是否为暂停状态 suspended方法为true是暂停的，suspended方法为false是运行的
        boolean suspended = qingjia.isSuspended();
        if (suspended) {
            // 暂定,那就可以激活
            // 参数1:流程定义的id  参数2:是否激活    参数3:时间点
            repositoryService.activateProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println("流程定义" + qingjia.getId() + "激活");

        } else {
            repositoryService.suspendProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println("流程定义" + qingjia.getId() + "挂起  ");
        }

    }

    /*查询已处理的业务*/
    @Test
    public void findProcessedTaskList() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {

            System.out.println("流程实例id："+historicTaskInstance.getProcessDefinitionId());
            System.out.println("任务id："+historicTaskInstance.getId());
            System.out.println("任务负责人："+historicTaskInstance.getAssignee());
            System.out.println("任务名称："+historicTaskInstance.getName());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        taskService.complete(task.getId());

    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void findPendingTaskList() {
        //任务负责人
        String assignee = "zhangsan";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
        for (Task task : list) {
            System.out.println("流程实例id："+task.getProcessDefinitionId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
            
        }
    }
    @Test
    public void deployProcess() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //启动流程实例
    @Test
    public void startUpProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }
}
