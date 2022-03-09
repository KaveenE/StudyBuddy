package jsf.managedbean;

import entities.ReportEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;



@Named(value = "viewProductManagedBean")
@ViewScoped

public class ViewReportManagedBean implements Serializable
{
    private ReportEntity reportEntityToView;
    
    
    
    public ViewReportManagedBean()
    {
        reportEntityToView = new ReportEntity();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {        
    }

    
    
    public ReportEntity getReportEntityToView() {
        return reportEntityToView;
    }

    public void setReportEntityToView(ReportEntity reportEntityToView) {
        this.reportEntityToView = reportEntityToView;
    }    
}
