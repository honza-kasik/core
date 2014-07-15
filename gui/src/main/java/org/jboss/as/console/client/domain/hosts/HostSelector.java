package org.jboss.as.console.client.domain.hosts;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.domain.model.Host;
import org.jboss.as.console.client.v3.stores.domain.actions.HostSelection;
import org.jboss.as.console.client.widgets.popups.ComboPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heiko Braun
 * @date 11/2/11
 */
public class HostSelector {

    private ComboPicker hosts;

    public Widget asWidget() {

        VerticalPanel layout = new VerticalPanel();
        layout.getElement().setAttribute("title", "Please chose a host");
        layout.setStyleName("fill-layout-width");
        layout.addStyleName("lhs-selector");
        layout.getElement().setAttribute("style", "padding:4px;");

        hosts = new ComboPicker();
        hosts.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                if (!event.getValue().isEmpty()) {
                    Scheduler.get().scheduleDeferred(
                            new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    Console.getCircuit().dispatch(new HostSelection(hosts.getSelectedValue()));
                                }
                            }
                    );
                }
            }
        });

        Label hostLabel = new Label("Host:");
        hostLabel.setStyleName("header-label");
        layout.add(hostLabel);
        Widget hWidget = hosts.asWidget();
        hWidget.getElement().addClassName("table-picker");
        layout.add(hWidget);

        // combo box use all available space
        hWidget.getElement().getParentElement().setAttribute("width", "100%");

        return layout;
    }


    public void setHosts(String selectedHost, List<Host> hostModel) {
        List<String> hostNames = new ArrayList<String>();
        int selectedIndex = 0;
        int i = 0;
        for (Host h : hostModel) {
            hostNames.add(h.getName());
            if (h.getName().equals(selectedHost)) { selectedIndex = i; }
            i++;
        }

        hosts.clearSelection();
        hosts.setValues(hostNames);
        hosts.setItemSelected(selectedIndex, true, false);
    }
}
