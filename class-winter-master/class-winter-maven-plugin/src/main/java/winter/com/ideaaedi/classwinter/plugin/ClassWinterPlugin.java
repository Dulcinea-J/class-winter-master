package winter.com.ideaaedi.classwinter.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import winter.com.ideaaedi.classwinter.executor.EncryptExecutor;
import winter.com.ideaaedi.classwinter.util.ExceptionUtil;
import winter.com.ideaaedi.classwinter.util.Logger;

import java.io.File;

/**
 * 加密jar/war文件的maven插件
 *
 * @author JustryDeng
 */
@SuppressWarnings("unused")
@Mojo(name = "class-winter", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class ClassWinterPlugin extends AbstractMojo {

    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    
    @Parameter
    private String originJarOrWar;
    
    @Parameter(required = true)
    private String includePrefix;
    
    
    @Parameter
    private String finalName;
    
    @Parameter
    private String password;
    
    @Parameter
    private String excludePrefix;
    
    @Parameter
    private String includeXmlPrefix;
    
    @Parameter
    private String excludeXmlPrefix;
    
    @Parameter
    private String toCleanXmlChildElementName;
    
    @Parameter
    private String includeLibs;
    
    @Parameter
    private String alreadyProtectedRootDir;
    
    @Parameter
    private String alreadyProtectedLibs;
    
    @Parameter
    private String supportFile;
    
    @Parameter
    private String jvmArgCheck;;
    
    @Parameter
    private String tips;
    
    @Parameter(defaultValue = "false")
    private Boolean debug;
    
    @Override
    @SuppressWarnings("RedundantThrows")
    public void execute() throws MojoExecutionException, MojoFailureException {
        Logger.simpleInfo("-----------------< class-winter-plugin start >-----------------");
        
        Logger.ENABLE_DEBUG.set(debug != null && debug);
        Logger.debug("You config arg originJarOrWar -> " + originJarOrWar);
        Logger.debug("You config arg includePrefix -> " + includePrefix);
        Logger.debug("You config arg excludePrefix -> " + excludePrefix);
        Logger.debug("You config arg includeXmlPrefix -> " + includeXmlPrefix);
        Logger.debug("You config arg excludeXmlPrefix -> " + excludeXmlPrefix);
        Logger.debug("You config arg toCleanXmlChildElementName -> " + toCleanXmlChildElementName);
        Logger.debug("You config arg finalName -> " + finalName);
        Logger.debug("You config arg password -> " + password);
        Logger.debug("You config arg includeLibs -> " + includeLibs);
        Logger.debug("You config arg alreadyProtectedRootDir -> " + alreadyProtectedRootDir);
        Logger.debug("You config arg alreadyProtectedLibs -> " + alreadyProtectedLibs);
        Logger.debug("You config arg supportFile -> " + supportFile);
        Logger.debug("You config arg jvmArgCheck -> " + jvmArgCheck);
        Logger.debug("You config arg tips -> " + tips);
        Logger.debug("You config arg debug -> " + debug);
        
        Build build = project.getBuild();
        // 要加密的jar/war文件的绝对路径
        if (StringUtils.isBlank(originJarOrWar)) {
            originJarOrWar = build.getDirectory() + File.separator + build.getFinalName() + "." + project.getPackaging();
            Logger.debug("Determine originJarOrWar -> " + originJarOrWar);
        }
        // 创建加密对象类
        EncryptExecutor encryptExecutor = EncryptExecutor.builder()
                .originJarOrWar(originJarOrWar)
                .finalName(finalName)
                .password(password)
                .includePrefix(includePrefix)
                .excludePrefix(excludePrefix)
                .includeXmlPrefix(includeXmlPrefix)
                .excludeXmlPrefix(excludeXmlPrefix)
                .toCleanXmlChildElementName(toCleanXmlChildElementName)
                .includeLibs(includeLibs)
                .alreadyProtectedRootDir(alreadyProtectedRootDir)
                .alreadyProtectedLibs(alreadyProtectedLibs)
                .supportFile(supportFile)
                .jvmArgCheck(jvmArgCheck)
                .debug(debug)
                .tips(tips)
                .build();
    
        Logger.debug("The encrypted executor generated based on your configuration is -> " + encryptExecutor);
        String encryptedJarOrWar;
        try {
            encryptExecutor.invokerIsPlugin = true;
            encryptedJarOrWar = encryptExecutor.process();
            Logger.simpleInfo("The absolute path of the obfuscated jar is [" + encryptedJarOrWar + "]");
        } catch (Exception e) {
            Logger.error(ClassWinterPlugin.class, ExceptionUtil.getStackTraceMessage(e));
            throw e;
        }
        Logger.simpleInfo("-----------------< class-winter-plugin  end  >-----------------");
    }

}
