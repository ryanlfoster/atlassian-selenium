package com.atlassian.webdriver.testing.rule;

import com.atlassian.webdriver.debug.WebDriverDebug;
import com.google.common.base.Supplier;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A rule for taking screen-shots when a WebDriver test fails. It will also dump the html source of the page to
 * the target/webDriverTests directory.
 *
 * @since 2.1
 */
public class WebDriverScreenshotRule extends TestWatcher
{
    private static final Logger log = LoggerFactory.getLogger(WebDriverScreenshotRule.class);

    private final WebDriverDebug debug;
    private final File artifactDir;

    private static File defaultArtifactDir()
    {
        return new File("target/webdriverTests");
    }

    protected WebDriverScreenshotRule(@Nonnull WebDriverDebug webDriverDebug, @Nonnull File artifactDir)
    {
        this.debug = checkNotNull(webDriverDebug, "webDriverDebug");
        this.artifactDir = checkNotNull(artifactDir, "artifactDir");
    }

    protected WebDriverScreenshotRule(@Nonnull WebDriverSupport<? extends WebDriver> support, @Nonnull File artifactDir)
    {
        this(new WebDriverDebug(checkNotNull(support, "support").getDriver()), artifactDir);
    }

    /**
     * @deprecated  Will be removed in version 3.0; instead, use one of the other constructors to set the driver,
     *   then call {@link #artifactDir(File)} 
     */
    @Deprecated
    public WebDriverScreenshotRule(@Nonnull Supplier<? extends WebDriver> driverSupplier, @Nonnull File artifactDir)
    {
        this(WebDriverSupport.forSupplier(driverSupplier), artifactDir);
    }

    @SuppressWarnings("UnusedDeclaration")
	public WebDriverScreenshotRule(@Nonnull Supplier<? extends WebDriver> driverSupplier)
    {
        this(driverSupplier, defaultArtifactDir());
    }

	@SuppressWarnings("UnusedDeclaration")
	public WebDriverScreenshotRule(@Nonnull WebDriver webDriver)
    {
        this(WebDriverSupport.forInstance(webDriver), defaultArtifactDir());
    }

    @Inject
    public WebDriverScreenshotRule(@Nonnull WebDriverDebug webDriverDebug)
    {
        this(webDriverDebug, defaultArtifactDir());
    }

    public WebDriverScreenshotRule()
    {
        this(WebDriverSupport.fromAutoInstall(), defaultArtifactDir());
    }

    /**
     * Returns a copy of this rule, specifying a different artifact directory than the default.
     * @param artifactDir  the directory in which screenshots should be stored
     * @return  a new WebDriverScreenshotRule based on the current instance
     * @since 2.3
     */
    public WebDriverScreenshotRule artifactDir(File artifactDir)
    {
        return new WebDriverScreenshotRule(this.debug, artifactDir);
    }

    @Override
    protected void starting(@Nonnull final Description description)
    {
        File dir = getTargetDir(description);
        if (!dir.exists())
        {
            checkState(dir.mkdirs(), "Unable to create screenshot output directory " + dir.getAbsolutePath());
        }
    }

    @Override
    protected void failed(@Nonnull final Throwable e, @Nonnull final Description description)
    {
        final File dumpFile = getTargetFile(description, "html");
        final File screenShotFile = getTargetFile(description, "png");
        log.info("----- {} failed. ", description.getDisplayName());
        log.info("----- At page: " + debug.getCurrentUrl());
        log.info("----- Dumping page source to {} and screenshot to {}", dumpFile.getAbsolutePath(),
                screenShotFile.getAbsolutePath());
        debug.dumpSourceTo(dumpFile);
        debug.takeScreenshotTo(screenShotFile);
    }

    private File getTargetDir(Description description)
    {
        return new File(artifactDir, description.getClassName());
    }

    private File getTargetFile(Description description, String extension)
    {
		File file = new File(getTargetDir(description), description.getMethodName() + "." + extension);
		int fileNum = 1;
		while (file.exists()) {
			file = new File(getTargetDir(description), description.getMethodName() + "-" + (fileNum++) + "." + extension);
		}
		return file;
    }

}
