package acr_ProcessVariable_demo;

import engines.ProcessCoreEngine;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程变量测试
 */
public class ProcessVariable {
    private ProcessEngine processEngine = ProcessCoreEngine.getDefaultProcessEngine();

    // 部署流程定义，资源来在bpmn格式
    @Test
    public void deployProcessDefi() {
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment().name("支付流程")
                .addClasspathResource("resource/diagrams/ApayBill.bpmn")
                .deploy();

        System.out.println("部署名称:" + deploy.getName());
        System.out.println("部署id:" + deploy.getId());
    }

    /*
    // 执行流程，开始跑流程
    @Test
    public void startProcess() {
        String processDefiKey = "PayBill";
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processDefiKey);

        System.out.println("流程执行对象的id：" + pi.getId());// Execution 对象
        System.out.println("流程实例的id：" + pi.getProcessInstanceId());// ProcessInstance
        System.out.println("流程定义的id：" + pi.getProcessDefinitionId());// 默认执行的是最新版本的流程定义
    }

    // 查询正在运行任务
    @Test
    public void queryTask() {
        // 取得任务服务
        TaskService taskService = processEngine.getTaskService();
        // 创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 办理人的任务列表
        List<Task> list = taskQuery.list();
        // 遍历任务列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("任务的id：" + task.getId());
                System.out.println("任务的名称：" + task.getName());
            }
        }

    }

    // 完成任务
    @Test
    public void compileTask() {
        String taskId = "1502";
        // taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }
*/

    // 测试动态设置任务的处理人
    // 执行流程，开始跑流程
    @Test
    public void startProcess() {
        String processDefiKey = "appayBill";// bpmn 的 process id属性
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userID", "王某某");
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefiKey, params);

        System.out.println("流程执行对象的id：" + pi.getId());// Execution 对象
        System.out.println("流程实例的id：" + pi.getProcessInstanceId());// ProcessInstance
        // 对象
        System.out.println("流程定义的id：" + pi.getProcessDefinitionId());// 默认执行的是最新版本的流程定义
    }

    // 查询正在运行任务
    @Test
    public void queryTask() {
        String assignee="王某某";//指定任务处理人
        // 取得任务服务
        TaskService taskService = processEngine.getTaskService();
        // 创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 办理人的任务列表
        List<Task> list = taskQuery
                .taskAssignee(assignee)
                .list();
        // 遍历任务列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("任务的id：" + task.getId());
                System.out.println("任务的名称：" + task.getName());
            }
        }

    }

    // 完成任务
    @Test
    public void compileTask() {
        String taskId = "3704";
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("visitor", 6);
        // taskId：任务id
        processEngine.getTaskService().complete(taskId, params);
//		processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }


    //设置流程变量值
    @Test
    public void setVariable() {
        String taskId="1305";//任务id
        //采用TaskService来设置流程变量

        //1. 第一次设置流程变量
//		TaskService taskService = processEngine.getTaskService();
//		taskService.setVariable(taskId, "cost", 1000);//设置单一的变量，作用域在整个流程实例
//		taskService.setVariable(taskId, "申请时间", new Date());
//		taskService.setVariableLocal(taskId, "申请人", "何某某");//该变量只有在本任务中是有效的


        //2. 在不同的任务中设置变量
//		TaskService taskService = processEngine.getTaskService();
//		taskService.setVariable(taskId, "cost", 5000);//设置单一的变量，作用域在整个流程实例
//		taskService.setVariable(taskId, "申请时间", new Date());
//		taskService.setVariableLocal(taskId, "申请人", "李某某");//该变量只有在本任务中是有效的

        /**
         * 3. 变量支持的类型
         * - 简单的类型 ：String 、boolean、Integer、double、date
         * - 自定义对象bean
         */
        TaskService taskService = processEngine.getTaskService();
        //传递的一个自定义bean对象
        PayBillBean payBillBean = new PayBillBean();
        payBillBean.setId(1);
        payBillBean.setCost(300);
        payBillBean.setDate(new Date());
        payBillBean.setPayPerson("何某某");
        taskService.setVariable(taskId, "payBillBean", payBillBean);

        System.out.println("设置成功！");
    }

    //查询流程变量
    @Test
    public void getVariable(){
        String taskId="1305";//任务id
//		TaskService taskService = processEngine.getTaskService();
//		Integer cost=(Integer) taskService.getVariable(taskId, "cost");//取变量
//		Date date=(Date) taskService.getVariable(taskId, "申请时间");//取本任务中的变量
////		Date date=(Date) taskService.getVariableLocal(taskId, "申请时间");//取本任务中的变量
//		String payPerson=(String) taskService.getVariableLocal(taskId, "申请人");//取本任务中的变量
////		String payPerson=(String) taskService.getVariable(taskId, "申请人");//取本任务中的变量
//
//		System.out.println("金额:"+cost);
//		System.out.println("申请时间:"+date);
//		System.out.println("申请人:"+payPerson);


        //读取实现序列化的对象变量数据
        TaskService taskService = processEngine.getTaskService();
        PayBillBean payBillBean =(PayBillBean) taskService.getVariable(taskId, "payBillBean");
        System.out.println(payBillBean.getCost());
        System.out.println(payBillBean.getPayPerson());

    }

    //模拟流程变量设置
    @Test
    public void  getAndSetProcessVariable(){
        //有两种服务可以设置流程变量
//		TaskService taskService = processEngine.getTaskService();
//		RuntimeService runtimeService = processEngine.getRuntimeService();

        /**1.通过 runtimeService 来设置流程变量
         * exxcutionId: 执行对象
         * variableName：变量名
         * values：变量值
         */
//		runtimeService.setVariable(exxcutionId, variableName, values);
//		runtimeService.setVariableLocal(executionId, variableName, values);
        //设置本执行对象的变量 ，该变量的作用域只在当前的execution对象
//		runtimeService.setVariables(exxcutionId, variables);
        //可以设置多个变量  放在 Map<key,value>  Map<String,Object>

        /**2. 通过TaskService来设置流程变量
         * taskId：任务id
         */
//		taskService.setVariable(taskId, variableName, values);
//		taskService.setVariableLocal(taskId, variableName, values);
////		设置本执行对象的变量 ，该变量的作用域只在当前的execution对象
//		taskService.setVariables(taskId, variables); //设置的是Map<key,values>

        /**3. 当流程开始执行的时候，设置变量参数
         * processDefiKey: 流程定义的key
         * variables： 设置多个变量  Map<key,values>
         */
//		processEngine.getRuntimeService()
//		.startProcessInstanceByKey(processDefiKey, variables)

        /**4. 当执行任务时候，可以设置流程变量
         * taskId:任务id
         * variables： 设置多个变量  Map<key,values>
         */
//		processEngine.getTaskService().complete(taskId, variables);


        /** 5. 通过RuntimeService取变量值
         * exxcutionId： 执行对象
         *
         */
//		runtimeService.getVariable(exxcutionId, variableName);//取变量
//		runtimeService.getVariableLocal(exxcutionId, variableName);//取本执行对象的某个变量
//		runtimeService.getVariables(variablesName);//取当前执行对象的所有变量
        /** 6. 通过TaskService取变量值
         * TaskId： 执行对象
         *
         */
//		taskService.getVariable(taskId, variableName);//取变量
//		taskService.getVariableLocal(taskId, variableName);//取本执行对象的某个变量
//		taskService.getVariables(taskId);//取当前执行对象的所有变量
    }

}