package winter.com.ideaaedi.classwinter;

import winter.com.ideaaedi.classwinter.author.JustryDeng;
import winter.com.ideaaedi.classwinter.util.BashUtil;
import winter.com.ideaaedi.classwinter.util.PathUtil;

/**
 * 全参数测试
 * P.S.
 * 注:本人提供的用于测试的my-project-with-encrypted-lib-have-pwd.jar包，貌似所有类都能正常加密，
 *    没出现class not find的情况，所以不需要指定supportFile。
 *    即: 本case略有不足之处， 没有把参数supportFile的作用体现出来，  再构造一个满足全部测试场景的jar/war太麻烦了，就这样吧。
 *    注: supportFile参数的作用见{@link SupportFile_Test}。
 *
 * @author {@link JustryDeng}
 * @since 2021/5/6 23:25:24
 */
public class AllTest {

    
    public static void main(String[] args) {
//      xmlTest();
        otherTest();
        
        // 杀下进程(结束拉起的jar程序)
        BashUtil.killProcessByPorts("8080");
    }
    
    /**
     * 直接加密测试
     */
    private static void encryptDirectly() {
        String projectRootDir = PathUtil.getProjectRootDir(AllTest.class);
        String originJarOrWar = projectRootDir + "my-project-with-encrypted-lib-have-pwd.jar";
        String includePrefix = "com.szlaozicl,com.aspire.ssm,org.springframework.aop.target.dynamic,com.fasterxml.jackson";
        String excludePrefix = "com.aspire.ssm.config,com.aspire.ssm.util.Person";
        String finalName = "jd";
        String includeLibs = "spring-aop-5.2.7.RELEASE.jar,jackson-core-2.10.4.jar";
        String tips = "方法已经被class-winter加密保护了，请不要直接使用java -jar xxx.jar启动项目，请使用java -javaagent:xxx.jar -jar xxx.jar启动项目.";
        String alreadyProtectedLibs = "encrypted-lib-have-pwd-1.0.0.jar:qwer123~";
        String debug = "true";
        
        Forward.main(new String[]{
                "originJarOrWar=" + originJarOrWar,
                "includePrefix=" + includePrefix,
                "excludePrefix=" + excludePrefix,
                "finalName=" + finalName,
                "includeLibs=" + includeLibs,
                "tips=" + tips,
                "alreadyProtectedLibs=" + alreadyProtectedLibs,
                "debug=" + debug
        });
    }
    
    /**
     * xml测试
     */
    private static void xmlTest() {
        Xml_Test.main(null);
    }
    
    /**
     * 其它测试
     */
    private static void otherTest() {
        // 杀下进程(以保证端口没有被占用)
        BashUtil.killProcessByPorts("8080");
        
        // 加密
        String projectRootDir = PathUtil.getProjectRootDir(AllTest.class);
        String originJarOrWar = projectRootDir + "my-project-with-encrypted-lib-have-pwd.jar";
        String includePrefix = "com.szlaozicl?cca=true&cfa=true,com.aspire.ssm,org.springframework.aop.target.dynamic,com.*.jackson";
        String excludePrefix = "com.aspire.ssm.config,com.aspire.ssm.util.Person";
        String finalName = "jd";
        String password = "pwd12345";
        String includeLibs = "spring-aop-5.2.7.RELEASE.jar,jackson-.*.jar";
        String tips = "方法已经被class-winter加密保护了，请不要直接使用java -jar xxx.jar启动项目，请使用java -javaagent:xxx.jar -jar xxx.jar启动项目.";
        String alreadyProtectedLibs = "encrypted-lib-have-pwd-1.0.0.jar:qwer123~";
        String jvmArgCheck = "-XX:+DisableAttachMechanism";
        String debug = "true";
        String startBat = "java -jar " + projectRootDir + "class-winter-core-2.9.7.jar"
                + " originJarOrWar=" + originJarOrWar
                + " includePrefix=" + includePrefix
                + " excludePrefix=" + excludePrefix
                + " finalName=" + finalName
                + " password=" + password
                + " includeLibs=" + includeLibs
                + " tips=" + (BashUtil.IS_WINDOWS ? "\"".concat(tips).concat("\"") : "'".concat(tips).concat("'"))
                + " alreadyProtectedLibs=" + alreadyProtectedLibs
                //+ "  supportFile=" +  supportFile
                + " jvmArgCheck=" +  jvmArgCheck
                + " debug=" + debug
                ;
        BashUtil.runBashAndPrint(startBat);
        
        String encryptedJar = projectRootDir + finalName + ".jar";
        // 解密（模拟启动加密后的jar包）
        // String javaagentArgs = "";
        String javaagentArgs = "=debug=true,password=" + password;
        BashUtil.runBashAndPrint(String.format("java -javaagent:%s%s -XX:+DisableAttachMechanism -jar %s", encryptedJar, javaagentArgs, encryptedJar));
    }
}
