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

import com.intellij.openapi.project.Project;

import javax.swing.*;

public class ORMSettingsForm extends JDialog {
    private JPanel contentPane;
    private JComboBox comboBox1;
    private JPasswordField password;
    private JTextField ip;
    private JTextField packageName;
    private JTextField database;
    private JTextField port;
    private JTextField user;
    private ORMConfig config;

    public ORMSettingsForm() {
        setContentPane(contentPane);
    }

    public void createUI(Project project) {
        config = ORMConfig.getInstance(project);
        this.ip.setText(config.getIp());
        this.port.setText(config.getPort());
        this.database.setText(config.getDatabase());
        this.user.setText(config.getUser());
        this.password.setText(config.getPassword());
        this.packageName.setText(config.getPackageName());
    }

    public boolean isModified() {
        boolean modified = false;
        modified |= !this.ip.getText().equals(config.getIp());
        modified |= !this.port.getText().equals(config.getPort());
        modified |= !this.database.getText().equals(config.getDatabase());
        modified |= !this.user.getText().equals(config.getUser());
        modified |= !new String(this.password.getPassword()).equals(config.getPassword());
        modified |= !this.packageName.getText().equals(config.getPackageName());
        return modified;
    }

    public void apply() {
        config.setIp(this.ip.getText());
        config.setPort(this.port.getText());
        config.setDatabase(this.database.getText());
        config.setUser(this.user.getText());
        config.setPassword(new String(this.password.getPassword()));
        config.setPackageName(this.packageName.getText());
    }

    public void reset() {
        this.ip.setText(config.getIp());
        this.port.setText(config.getPort());
        this.database.setText(config.getDatabase());
        this.user.setText(config.getUser());
        this.password.setText(config.getPassword());
        this.packageName.setText(config.getPackageName());
    }

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }
}
