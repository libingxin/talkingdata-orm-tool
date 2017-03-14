/*
 * The Talking Data ORM Tool Open Foundation licenses
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 *
 * @ author: bingxin.li@tendcloud.com
 */
package com.talkingdata.orm.tool;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ORMGenerateAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        ORMConfig config = ORMConfig.getInstance(project);

        // 1. validate input parameter
        if (!validateConfig(project, config)) {
            return;
        }

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s", config.getIp(), config.getPort(), config.getDatabase()));
        dataSource.setUsername(config.getUser());
        dataSource.setPassword(config.getPassword());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 2. validate database is connected
        try {
            jdbcTemplate.execute("SELECT 1");
        } catch (Exception exception) {
            Messages.showWarningDialog(project, "The database is not connected.", "Warning");
            return;
        }

        File resourceDirectory = new File(project.getBasePath(), "/src/main/resources/mybatis");
        if (!resourceDirectory.exists()) {
            resourceDirectory.mkdirs();
        }

        File packageDirectory = new File(project.getBasePath(), "/src/main/java/" + config.getPackageName().replaceAll("\\.", "/"));
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs();
        }

        Properties p = new Properties();
        p.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        Velocity.init(p);

        // 3. query all table
        SqlParser sqlParser = new SqlParser();

        try {
            for (ClassDefinition cls : sqlParser.getTables(jdbcTemplate, config.getDatabase(), config.getPackageName())) {
                Map<String, Object> map = new HashMap<>(1);
                map.put("cls", cls);

                File domainDirectory = new File(packageDirectory, "domain");
                if (!domainDirectory.exists()) {
                    domainDirectory.mkdirs();
                }
                File clsFile = new File(domainDirectory, cls.getClassName() + ".java");
                if (!clsFile.exists()) {
                    clsFile.createNewFile();
                }
                writeFile("com/talkingdata/orm/tool/vm/class.vm", map, new FileWriter(clsFile));

                File daoDirectory = new File(packageDirectory, "dao");
                if (!daoDirectory.exists()) {
                    daoDirectory.mkdirs();
                }
                File daoFile = new File(daoDirectory, cls.getClassName() + "Dao.java");
                if (!daoFile.exists()) {
                    daoFile.createNewFile();
                }
                writeFile("com/talkingdata/orm/tool/vm/dao.vm", map, new FileWriter(daoFile));

                File mapperFile = new File(resourceDirectory, cls.getClassInstanceName() + ".xml");
                if (!mapperFile.exists()) {
                    mapperFile.createNewFile();
                }
                writeFile("com/talkingdata/orm/tool/vm/mapper.vm", map, new FileWriter(mapperFile));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    private boolean validateConfig(Project project, ORMConfig config) {
        // 1. validate input parameter
        if (StringUtils.isBlank(config.getIp())) {
            Messages.showWarningDialog(project, "The database's ip is null.", "Warning");
            return false;
        }

        if (StringUtils.isBlank(config.getPort())) {
            Messages.showWarningDialog(project, "The database's port is null.", "Warning");
            return false;
        }

        try {
            Integer.parseInt(config.getPort());
        } catch (NumberFormatException e) {
            Messages.showWarningDialog(project, "The database's port is not number.", "Warning");
            return false;
        }

        if (StringUtils.isBlank(config.getDatabase())) {
            Messages.showWarningDialog(project, "The database's name is null.", "Warning");
            return false;
        }

        if (StringUtils.isBlank(config.getUser())) {
            Messages.showWarningDialog(project, "The database's user is null.", "Warning");
            return false;
        }

        if (StringUtils.isBlank(config.getPassword())) {
            Messages.showWarningDialog(project, "The database's password is null.", "Warning");
            return false;
        }

        if (StringUtils.isBlank(config.getPackageName())) {
            Messages.showWarningDialog(project, "The package's name is null.", "Warning");
            return false;
        }
        return true;
    }

    private void writeFile(String vm, Map<String, Object> map, FileWriter fileWriter) throws Exception {
        FileReader fileReader = null;
        try {
            RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
            InputStream is = ORMGenerateAction.class.getClassLoader().getResourceAsStream(vm);

            File file = File.createTempFile("td_orm_file", ".tmp");
            FileUtils.copyToFile(is, file);
            fileReader = new FileReader(file);

            SimpleNode node = runtimeServices.parse(fileReader, vm);
            Template template = new Template();
            template.setRuntimeServices(runtimeServices);
            template.setData(node);
            template.initDocument();
            template.merge(new VelocityContext(map), fileWriter);
            fileWriter.flush();
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }

            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }
}
