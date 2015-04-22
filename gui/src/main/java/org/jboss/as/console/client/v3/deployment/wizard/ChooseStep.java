/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.console.client.v3.deployment.wizard;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.shared.util.IdHelper;
import org.jboss.as.console.client.v3.widgets.wizard.WizardStep;

/**
 * @author Harald Pehl
 */
public class ChooseStep extends
        WizardStep<AddDeploymentWizard.Context, AddDeploymentWizard.State> {

    private RadioButton deployNew;
    private RadioButton deployExisting;
    private RadioButton deployUnmanaged;

    public ChooseStep(final AddDeploymentWizard wizard) {super(wizard, "Please Choose");}

    @Override
    public Widget asWidget() {
        FlowPanel body = new FlowPanel();

        deployNew = new RadioButton("deployment_kind", "Upload and assign a new deployment");
        deployNew.setValue(true);
        deployNew.addStyleName("radio-block");
        IdHelper.setId(deployNew, id(), "deployNew");

        deployExisting = new RadioButton("deployment_kind", "Assign an existing deployment");
        deployExisting.addStyleName("radio-block");
        IdHelper.setId(deployExisting, id(), "deployExisting");

        deployUnmanaged = new RadioButton("deployment_kind", "Setup and assign an unmanaged deployment");
        deployUnmanaged.addStyleName("radio-block");
        IdHelper.setId(deployUnmanaged, id(), "deployUnmanaged");

        body.add(deployNew);
        body.add(deployExisting);
        body.add(deployUnmanaged);
        return body;
    }

    @Override
    protected boolean onNext(final AddDeploymentWizard.Context context) {
        context.deployNew = deployNew.getValue();
        context.deployExisting = deployExisting.getValue();
        context.deployUnmanaged = deployUnmanaged.getValue();
        return true;
    }
}
