package com.atlassian.webdriver.rule.test;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;
import static org.mockito.internal.util.Checks.checkNotNull;

/**
 * @since 2.3
 */
public class TemporaryFolderPreservingOnFailure extends TestWatcher
{

    private final TemporaryFolder temporaryFolder;
    private final File targetDir;

    public TemporaryFolderPreservingOnFailure(@Nonnull File targetDir)
    {
        checkNotNull(targetDir, "targetDir");
        checkState(targetDir.isDirectory() || targetDir.getParentFile().isDirectory(),
                "target dir or its parent must be an existing directory: " + targetDir.getAbsolutePath());
        this.targetDir = targetDir;
        this.temporaryFolder = new TemporaryFolder();
    }

    public TemporaryFolder getFolder()
    {
        return temporaryFolder;
    }

    @Override
    protected void starting(Description description)
    {
        try
        {
            temporaryFolder.create();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void failed(Throwable e, Description description)
    {
        try
        {
            FileUtils.copyDirectory(temporaryFolder.getRoot(), new File(targetDir, temporaryFolder.getRoot().getName()));
        } catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    protected void finished(Description description)
    {
        temporaryFolder.delete();
    }
}
