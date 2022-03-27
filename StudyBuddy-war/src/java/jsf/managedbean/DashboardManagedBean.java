
package jsf.managedbean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
/**
 *
 * @author wentinggy
 */

@Named(value = "dashboardManagedBean")
@SessionScoped

public class DashboardManagedBean implements Serializable {
	private DashboardModel model;

	public DashboardManagedBean() {
		// Initialize the dashboard model
		this.model = new DefaultDashboardModel();
		// Initialize the dashboard column #1
		DashboardColumn column1 = new DefaultDashboardColumn();
		// Initialize the dashboard column #2
		DashboardColumn column2 = new DefaultDashboardColumn();
		// Initialize the dashboard column #3
		DashboardColumn column3 = new DefaultDashboardColumn();

		// Add widgets into column1
		column1.addWidget("ImportantAnnouncements");
		column1.addWidget("ToDos");
		// Add widgets into column2
		column2.addWidget("Updates");
		column2.addWidget("Others");


		// Add columns into your model
		this.model.addColumn(column1);
		this.model.addColumn(column2);
		this.model.addColumn(column3);

	}

	public DashboardModel getModel() {
		return model;
	}

	public void setModel(DashboardModel model) {
		this.model = model;
	}

	public void handleReorder(DashboardReorderEvent event) {
		FacesMessage message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		message.setSummary("Reordered: " + event.getWidgetId());
//		message.setDetail("Item index: " + event.getItemIndex()+ ", Column index: " + event.getColumnIndex()
//				+ ", Sender index: " + event.getSenderColumnIndex());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
