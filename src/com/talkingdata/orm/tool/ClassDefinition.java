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

import java.util.List;

public class ClassDefinition {
    private String tableName;
    private String packageName;
    private String className;
    private String classPackage;
    private String classInstanceName;
    private String classFullName;
    private String daoPackage;
    private String daoFullName;

    private List<ClassDefinitionColumn> columns;

    public ClassDefinition(String packageName, String tableName) {
        this.packageName = packageName;
        this.tableName = tableName;
        this.className = ClassUtils.capitalizeFully(this.tableName);
        this.classInstanceName = ClassUtils.lowerFirst(this.className);
        this.classPackage = packageName + ".domain";
        this.daoPackage = packageName + ".dao";
        this.classFullName = this.classPackage + "." + this.className;
        this.daoFullName = this.daoPackage + "." + this.className + "Dao";
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public String getClassInstanceName() {
        return classInstanceName;
    }

    public void setClassInstanceName(String classInstanceName) {
        this.classInstanceName = classInstanceName;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getDaoFullName() {
        return daoFullName;
    }

    public void setDaoFullName(String daoFullName) {
        this.daoFullName = daoFullName;
    }

    public List<ClassDefinitionColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ClassDefinitionColumn> columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
