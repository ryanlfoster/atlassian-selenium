package com.atlassian.webdriver.jira.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.jira.component.project.ProjectSummary;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Document this class / interface here
 *
 */
public class ProjectsViewPage extends JiraAdminAbstractPage
{

    @FindBy (id = "add_project")
    private WebElement addProjectLink;

    private final List<ProjectSummary> projects;
    
    private final static String URI = "/secure/project/ViewProjects.jspa";

    public ProjectsViewPage()
    {
        projects = new ArrayList<ProjectSummary>();
    }

    public String getUrl()
    {
        return URI;
    }

    @Init
    public void loadProjects()
    {

        List<WebElement> rows = driver.findElements(By.cssSelector("table.grid > tbody > tr"));

        // Remove the th.
        rows.remove(0);

        if (rows.get(0).getText().equals("You do not have the permissions to administer any projects, or there are none created."))
        {
            return;
        }

        for(WebElement row : rows)
        {
            projects.add(pageBinder.bind(ProjectSummary.class, row));
        }

    }

    public Page addProject()
    {
        throw new UnsupportedOperationException("addProject for ProjectViewPage has not been implemented");
    }

    public List<ProjectSummary> getProjects()
    {
        return Collections.unmodifiableList(projects);
    }
}
