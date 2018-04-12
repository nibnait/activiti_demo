package engines;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 获取流程引擎，且自动创建Activiti涉及的数据库和表
 */
public class ProcessCoreEngine {

    public static ProcessEngine getDefaultProcessEngine(){
        /**1.通过代码形式创建
         *  - 取得ProcessEngineConfiguration对象
         *  - 设置数据库连接属性
         *  - 设置创建表的策略 （当没有表时，自动创建表）
         *  - 通过ProcessEngineConfiguration对象创建 ProcessEngine 对象

         //取得ProcessEngineConfiguration对象
         ProcessEngineConfiguration engineConfiguration=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
         //设置数据库连接属性
         engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
         engineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDB?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8");
         engineConfiguration.setJdbcUsername("root");
         engineConfiguration.setJdbcPassword("");
         // 设置创建表的策略 （当没有表时，自动创建表）
         //		  public static final java.lang.String DB_SCHEMA_UPDATE_FALSE = "false";//不会自动创建表，没有表，则抛异常
         //		  public static final java.lang.String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";//先删除，再创建表
         //		  public static final java.lang.String DB_SCHEMA_UPDATE_TRUE = "true";//假如没有表，则自动创建
         engineConfiguration.setDatabaseSchemaUpdate("true");
         //通过ProcessEngineConfiguration对象创建 ProcessEngine 对象
         ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
         System.out.println("流程引擎创建成功!");
         */


        /**
         * 2. 通过加载 activiti.cfg.xml 获取 流程引擎 和自动创建数据库及表
         */

        ProcessEngineConfiguration engineConfiguration= ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("/resource/activiti.cfg.xml");
        //从类加载路径中查找资源  activiti.cfg.xm文件名可以自定义
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
//        System.out.println("使用配置文件Activiti.cfg.xml获取流程引擎");


        /**
         * 3. 通过ProcessEngines 来获取默认的流程引擎

         //默认会加载类路径下的 activiti.cfg.xml
         ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
         System.out.println("通过ProcessEngines 来获取流程引擎");
         */

        return processEngine;
    }
}
