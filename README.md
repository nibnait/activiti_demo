# activiti工作流 demo使用

_原理剖析 [http:tianbin.org/]()_

bpmn插件：actiBPM  
运行时，若遇到file not found, 需手动配置working directory  
或配置Environment variables  
    使Working directory = %MODULE_WORKING_DIR%  
    MODULE_WORKING_DIR = /Users/nibnait/Downloads/github/activiti_demo/src/main/webapp

## 1. 流程的部署&&执行，任务的执行&&查询
以请假流程（leaveBill）为例：

1. a_TestActiviti.deploy()  
会在ACT_RE_DEPLOYMENT表中新建一条流程部署记录

2. 通过流程定义的key 来执行流程  
processEngine.getRuntimeService().startProcessInstanceByKey("leaveBill")

3. 执行任务  
processEngine.getTaskService().complete(taskId)

4. 查询任务  
processEngine.getTaskService().createTaskQuery()taskAssignee(assignee).list()   //指定办理人
                                                                

## 2. 流程定义管理
以采购流程（BuyBill）为例

1. 部署流程  
b_ProcessDefinitionManager.deploy()

2. 查看流程定义  
processEngine.getRepositoryService().createProcessDefinitionQuery().xxx

3. 查看bpmn资源图片  
processEngine.getRepositoryService().getResourceAsStream(deploymentId, imageName)

4. 删除流程定义
processEngine.getRepositoryService().deleteDeployment(deploymentId);


## 3. 流程实例与流程任务

#### c_ProcessInstanceAndTask


## 4. 流程变量细讲

    act_ru_variable：正在执行的流程变量表
    act_hi_varinst：流程变量历史表

流程变量在工作流中扮演着一个非常重要的角色。例如：请假流程中有请假天数、请假原因等一些参数都为流程变量的范围。流程变量的作用域范围是只对应一个流程实例。也就是说各个流程实例的流程变量是不相互影响的。流程实例结束完成以后流程变量还保存在数据库中（存放到流程变量的历史表中）。

![一个请假流程实例](/doc/leaveBillVariable.png)

1. 流程变量的作用域就是流程实例，所以只要设置就行了，不用管在哪个阶段设置
2. 基本类型设置流程变量，在taskService中使用任务ID，定义流程变量的名称，设置流程变量的值。

------------
======================================================================

## 数据库表结构说明

### ACT_GE_*: 普通数据，各种情况都使用的数据。
    ACT_GE_PROPERTY: 属性数据表。存储这个流程引擎级别的数据。
    ACT_GE_BYTEARRAY: 用来保存部署文件的大文本数据
    
### ACT_RE_*: ’RE’表示repository。带此前缀的表包含的是静态信息，如，流程定义，流程的资源（图片，规则等）。
    ACT_RE_DEPLOYMENT: 部署记录
    ACT_RE_PROCDEF: 流程定义
        注：此表和ACT_RE_DEPLOYMENT是多对一的关系，即，一个部署的bar包里可能包含多个流程定义文件，每个流程定义文件都会有一条记录在ACT_REPROCDEF表内，每个流程定义的数据，都会对于ACT_GE_BYTEARRAY表内的一个资源文件和PNG图片文件。和ACT_GE_BYTEARRAY的关联是通过程序用ACT_GE_BYTEARRAY.NAME与ACT_RE_PROCDEF.NAME_完成的，在数据库表结构中没有体现。
    
### ACT_ID_*: ’ID’表示identity。这些表包含标识的信息，如用户，用户组，等等。
    ACT_ID_GROUP: 用来存储用户组信息。
    ACT_ID_MEMBERSHIP: 用来保存用户的分组信息
    ACT_ID_USER: 保存用户的基本信息（姓名，邮箱，密码）
    
### ACT_RU_*: ’RU’表示runtime。这是运行时的表存储着流程变量，用户任务，变量，职责（job）等运行时的数据。Activiti只存储`执行期间的运行时数据，当流程实例结束时，将删除这些记录。这就保证了这些运行时的表小且快。
    ACT_RU_EXECUTION: 流程执行记录
    ACT_RU_TASK: 执行的任务记录
    ACT_RU_IDENTITYLINK: 任务参与者数据表。主要存储当前节点参与者的信息。
    ACT_RU_VARIABLE: 执行中的变量记录（正在执行的流程变量表）
    
### ACT_HI_*:’HI’表示history。就是这些表包含着历史的相关数据，如结束的流程实例，变量，任务，等等。
    ACT_HI_PROCINST: 历史流程实例表
    ACT_HI_ACTINST: 流程活动的实例
    ACT_HI_TASKINST: 任务实例
    ACT_HI_DETAIL: 启动流程或者在任务complete之后,记录历史流程变量
    ACT_HI_COMMENT: 处理意见表
    ACT_HI_VARINST：流程变量历史表
    