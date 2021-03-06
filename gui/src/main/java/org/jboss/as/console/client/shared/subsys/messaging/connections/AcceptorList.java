package org.jboss.as.console.client.shared.subsys.messaging.connections;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.properties.PropertyEditor;
import org.jboss.as.console.client.shared.properties.PropertyRecord;
import org.jboss.as.console.client.shared.subsys.messaging.forms.AcceptorForm;
import org.jboss.as.console.client.shared.subsys.messaging.model.Acceptor;
import org.jboss.as.console.client.shared.subsys.messaging.model.AcceptorType;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.ballroom.client.widgets.ContentHeaderLabel;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tables.DefaultPager;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.Feedback;

import java.util.List;
import java.util.Map;

/**
 * @author Heiko Braun
 * @date 4/2/12
 */
public class AcceptorList {

    private ContentHeaderLabel serverName;
    private DefaultCellTable<Acceptor> table;
    private ListDataProvider<Acceptor> provider;
    private MsgConnectionsPresenter presenter;
    private AcceptorForm acceptorForm;
    private AcceptorType type;
    private PropertyEditor properties;

    public AcceptorList(MsgConnectionsPresenter presenter,  AcceptorType type) {
        this.presenter = presenter;
        this.type = type;
    }

    Widget asWidget() {


        serverName = new ContentHeaderLabel();

        table = new DefaultCellTable<Acceptor>(10, new ProvidesKey<Acceptor>() {
            @Override
            public Object getKey(Acceptor acceptor) {
                return acceptor.getName();
            }
        });
        DefaultPager pager = new DefaultPager();
        pager.setDisplay(table);

        provider = new ListDataProvider<Acceptor>();
        provider.addDataDisplay(table);

        Column<Acceptor, String> name = new Column<Acceptor, String>(new TextCell()) {
            @Override
            public String getValue(Acceptor object) {
                return object.getName();
            }
        };

        table.addColumn(name, "Name");

        ToolStrip tools = new ToolStrip();
        tools.addToolButtonRight(
                new ToolButton(Console.CONSTANTS.common_label_add(), new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        presenter.launchNewAcceptorWizard(type);
                    }
                }));

        tools.addToolButtonRight(
                new ToolButton(Console.CONSTANTS.common_label_remove(), new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {

                        Feedback.confirm(
                                Console.MESSAGES.deleteTitle("Acceptor"),
                                Console.MESSAGES.deleteConfirm("Acceptor " + getSelectedEntity().getSocketBinding()),
                                new Feedback.ConfirmationHandler() {
                                    @Override
                                    public void onConfirmation(boolean isConfirmed) {
                                        if (isConfirmed) {
                                            presenter.onDeleteAcceptor(getSelectedEntity());
                                        }
                                    }
                                });

                    }

                }));

        // ----

        acceptorForm = new AcceptorForm(new FormToolStrip.FormCallback<Acceptor>()
        {
            @Override
            public void onSave(Map<String, Object> changeset) {
                presenter.onSaveAcceptor(getSelectedEntity(), changeset);
            }

            @Override
            public void onDelete(Acceptor entity) {

            }
        }, type);

        // ----

        properties = new PropertyEditor(presenter, true);

        VerticalPanel layout = new VerticalPanel();

        layout.add(tools);
        layout.add(table);
        layout.add(pager);

        acceptorForm.getForm().bind(table);


        TabPanel tabs = new TabPanel();
        tabs.setStyleName("default-tabpanel");
        tabs.addStyleName("master_detail-detail");
        tabs.getElement().setAttribute("style", "margin-top:15px;");

        tabs.add(acceptorForm.asWidget(), Console.CONSTANTS.common_label_details());
        tabs.add(properties.asWidget(), Console.CONSTANTS.common_label_properties());

        layout.add(tabs);
        tabs.selectTab(0);


        // ----

        table.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
                List<PropertyRecord> props = getSelectedEntity().getParameter();

                String tokens = getSelectedEntity().getType().getResource()+"_#_"+getSelectedEntity().getName();
                properties.setProperties(tokens, props);
            }
        });

        return layout;
    }

    public void setAcceptors(List<Acceptor> Acceptors) {
        properties.clearValues();
        provider.setList(Acceptors);
        serverName.setText("Acceptors: Provider "+presenter.getCurrentServer());

        table.selectDefaultEntity();


        // populate oracle
        presenter.loadSocketBindings(
                new AsyncCallback<List<String>>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(List<String> names) {
                        acceptorForm.setSocketBindings(names);
                    }
                });
    }

    public Acceptor getSelectedEntity() {
        SingleSelectionModel<Acceptor> selectionModel = (SingleSelectionModel<Acceptor>) table.getSelectionModel();
        return selectionModel.getSelectedObject();
    }


}
