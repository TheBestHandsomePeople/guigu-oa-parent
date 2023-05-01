package com.atguigu.auth.activity;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-18-12:37
 */
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        if (task.getName().equals("经理审批")) {
            //指定负责人
            task.setAssignee("张三");
        } else if (task.getName().equals("人事审批")) {
            task.setAssignee("王五");
        }

    }
}
