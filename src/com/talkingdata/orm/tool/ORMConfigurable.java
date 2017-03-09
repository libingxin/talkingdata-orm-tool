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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.sun.istack.internal.NotNull;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ORMConfigurable implements SearchableConfigurable {
    private ORMSettingsForm contentPanel;

    @SuppressWarnings("FieldCanBeLocal")
    private final Project project;

    public ORMConfigurable(@NotNull Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Talking Data Mybatis generator.";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.TDConfigurable";
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.TDConfigurable";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        contentPanel = new ORMSettingsForm();
        contentPanel.createUI(project);
        return contentPanel.getContentPane();
    }

    @Override
    public boolean isModified() {
        return contentPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        contentPanel.apply();
    }

    @Override
    public void reset() {
        contentPanel.reset();
    }

    @Override
    public void disposeUIResources() {
        contentPanel = null;
    }
}
