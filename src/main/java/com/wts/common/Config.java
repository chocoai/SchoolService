package com.wts.common;

import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.wts.controller.*;
import com.wts.controller.Desktop.MainDesktop;
import com.wts.controller.Desktop.Teacher.*;
import com.wts.controller.Mobile.MainMobile;
import com.wts.controller.Mobile.Teacher.CourseMobile;
import com.wts.controller.Mobile.Teacher.SemesterMobile;
import com.wts.entity.model._MappingKit;
import com.wts.weixin.callback.ParentCallback;
import com.wts.weixin.callback.TeacherCallback;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("a_little_config.txt");
        me.setViewType(ViewType.FREE_MARKER);
        me.setDevMode(false);
        me.setError404View("/dist/404.html");
        me.setError500View("/dist/500.html");
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
        me.add("/", MainController.class);

        me.add("/ParentCallback", ParentCallback.class);
        me.add("/TeacherCallback", TeacherCallback.class);

        me.add("/MainDesktop", MainDesktop.class);
        me.add("/CourseDesktop", CourseDesktop.class);
        me.add("/SemesterDesktop", SemesterDesktop.class);
        me.add("/RoomDesktop", RoomDesktop.class);
        me.add("/StudentDesktop", StudentDesktop.class);
        me.add("/TeacherDesktop", TeacherDesktop.class);
        me.add("/ParentDesktop", ParentDesktop.class);
        me.add("/StudentParentIdentityDesktop", StudentParentIdentityDesktop.class);
        me.add("/IdentityDesktop", IdentityDesktop.class);
        me.add("/RoomStudentDesktop", RoomStudentDesktop.class);
        me.add("/CourseRoomTeacherSemesterDesktop", CourseRoomTeacherSemesterDesktop.class);
        me.add("/StudentCourseSemesterDesktop", StudentCourseSemesterDesktop.class);

        me.add("/MainMobile", MainMobile.class);
        me.add("/SemesterMobile", SemesterMobile.class);
        me.add("/CourseMobile", CourseMobile.class);

//        me.add("/ForCourse", ForCourse.class);
//        me.add("/courseRoom", CourseRoomController.class);
//        me.add("/Teacher", TeacherController.class);
//        me.add("/teacherMessage", TeacherMessageController.class);
//        me.add("/teacherMessageRead", TeacherMessageReadController.class);
//        me.add("/Room", RoomController.class);
//        me.add("/Student", StudentController.class);
//        me.add("/Parent", ParentController.class);
//        me.add("/relation", RelationController.class);

//        me.add("/roomwork", RoomworkController.class);
//        me.add("/roomworkread", RoomworkReadController.class);
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
//        C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
//        me.add(c3p0Plugin);
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        me.add(druidPlugin);
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath());
        arp.addSqlTemplate("run.sql");
        _MappingKit.mapping(arp);
        me.add(arp);

        //配置任务调度插件
        Cron4jPlugin cp = new Cron4jPlugin();
        cp.addTask("*/1 * * * *", new MyTask());
        me.add(cp);
    }
    public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    }
    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new TxByMethods("save","update"));
    }

    /**
     * 配置模板
     */
    public void configEngine(Engine me) {
    }
    /**
     * 配置处理器
     * ${contextPath}/dist
     */
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("contextPath"));//设置上下文路径
    }
//    public void afterJFinalStart(){
//        WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
//        config.setCorpId("wx947e7da41f395af7");      // 设置微信企业号的appid
//        config.setCorpSecret("thcMcEeJdhuSDw7S_9314ILJ2hqPRCCxhupksDvPDQkUgfqfjDxVx16jPR_ztzzK");  // 设置微信企业号的app corpSecret
//        config.setAgentId(26);     // 设置微信企业号应用ID
//        config.setToken("weixin4j");       // 设置微信企业号应用的token
//        config.setAesKey("vUiM1GBGtPzce8Jh3UikdsHvn3NMghUT9l9Fr8UarrT");      // 设置微信企业号应用的EncodingAESKey
//
//        JFinal.me().getServletContext().setAttribute("xx",config);
//    }



    /**
     * 建议使用 JFinal 手册推荐的方式启动项目
     * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     * src/main/webapp 80 / 5
     * WebRoot
     */
    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/");
    }
}
