package act_base_demo;

import engines.ProcessCoreEngine;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 流程定义管理
 */
public class b_ProcessDefinitionManager {

    private ProcessEngine processEngine = ProcessCoreEngine.getDefaultProcessEngine();

    //部署流程定义，资源来自bpmn格式
    @Test
    public void deploy(){
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("采购流程")
                .category("市场类别")//设置部署的类别
                .addClasspathResource("resource/diagrams/BuyBill.bpmn")
                .deploy();

        System.out.println("部署名称:"+deploy.getName());
        System.out.println("部署id:"+deploy.getId());
    }

/*
    //部署流程定义,资源来自zip格式
    @Test
    public void deployProcessDefiByZip(){
        InputStream in=getClass().getClassLoader().getResourceAsStream("BuyBill.zip");
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("采购流程")
                .addZipInputStream(new ZipInputStream(in))
                .deploy();

        System.out.println("部署名称:"+deploy.getName());
        System.out.println("部署id:"+deploy.getId());
    }
*/

    //查看流程定义
    @Test
    public void queryProcessDefination(){
        String processDefiKey = "buyBill";    //流程定义key   由bpmn文件 的 process 的  id属性决定
        String processDefiId = "buyBill:1:504"; //流程定义id    proDefikey:version:自动生成id

        //获取流程定义列表
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(processDefiKey)
//                .processDefinitionId(proDefiId)
                .latestVersion()    //最新版本
                .orderByProcessDefinitionVersion().desc()   //按版本的降序排序
//                .count()//统计结果
//                .listPage(arg0, arg1)//分页查询
                .list();

        //遍历结果
        if(list!=null&&list.size()>0){
            for(ProcessDefinition temp:list){
                System.out.print("流程定义的id: "+temp.getId());
                System.out.print("\n流程定义的key: "+temp.getKey());
                System.out.print("\n流程定义的版本: "+temp.getVersion());
                System.out.print("\n流程定义部署的id: "+temp.getDeploymentId());
                System.out.println("\n流程定义的名称: "+temp.getName());
            }
        }
    }

    //查看bpmn 资源图片
    @Test
    public void viewImage() throws Exception{
        String deploymentId = "501";
        String imageName = "imageName";
        //取得某个部署的资源的名称  deploymentId
        List<String> resourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        // buybill.bpmn  buybill.png
        if(resourceNames!=null&&resourceNames.size()>0){
            for(String temp :resourceNames){
                if(temp.indexOf(".png")>0){
                    imageName=temp;
                }
            }
        }

        /**
         * 读取资源
         * deploymentId:部署的id
         * resourceName：资源的文件名
         */
        InputStream resourceAsStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, imageName);

        //把文件输入流写入到文件中
        File file=new File("bpmnImages/"+imageName);
        FileUtils.copyInputStreamToFile(resourceAsStream, file);
    }

    //删除流程定义
    @Test
    public void deleteProcessDefi(){
        //通过部署id来删除流程定义
        String deploymentId="101";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }

}
