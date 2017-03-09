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

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(name = "ORMConfig", storages = {@Storage("talkingdataOrmToolConfig.xml")})
public class ORMConfig implements PersistentStateComponent<ORMConfig> {
    private String ip;
    private String port;
    private String database;
    private String user;
    private String password;
    private String packageName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Nullable
    @Override
    public ORMConfig getState() {
        return this;
    }

    @Override
    public void loadState(ORMConfig mybatisEntity) {
        XmlSerializerUtil.copyBean(mybatisEntity, this);
    }

    @Nullable
    public static ORMConfig getInstance(Project project) {
        return ServiceManager.getService(project, ORMConfig.class);
    }
}
