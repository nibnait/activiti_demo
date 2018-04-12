package act_base_demo;

import engines.ProcessCoreEngine;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import java.util.List;

/**
 * 模拟Activiti 工作流框架 执行
 */
public class a_TestActiviti {

    //取得流程引擎，且自动创建Activiti涉及的数据库和表
    private ProcessEngine processEngine = ProcessCoreEngine.getDefaultProcessEngine();

    //部署流程定义
    @Test
    public void deploy(){
        //取得流程引擎对象
//		ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        //获取仓库服务 ：管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()//创建一个部署的构建器
                .addClasspathResource("resource/diagrams/LeaveBill.bpmn")//从类路径中添加资源,一次只能添加一个资源
//                .addClasspathResource("diagrams/LeaveBill.png")//从类路径中添加资源,一次只能添加一个资源
                .name("请求单流程")//设置部署的名称
                .category("办公类别")//设置部署的类别
                .deploy();

        System.out.println("部署的id"+deploy.getId());
        System.out.println("部署的名称"+deploy.getName());

    }

    //执行流程
    @Test
    public void startProcess(){
        String processDefiKey="leaveBill";
        //取运行时服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //取得流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefiKey);//通过流程定义的key 来执行流程
        System.out.println("流程实例id:"+pi.getId());//流程实例id
        System.out.println("流程定义id:"+pi.getProcessDefinitionId());//输出流程定义的id
    }

    //执行任务
    @Test
    public void compileTask(){
        String taskId="104";
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }

    //查询任务
    @Test
    public void queryTask(){
        //任务的办理人
        String assignee="张三";
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)//指定办理人
                .list();

        //遍历任务列表
        if(list!=null&&list.size()>0){
            for(Task task:list){
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务的id："+task.getId());
                System.out.println("任务的名称："+task.getName());

            }
        }

    }

}

