// The software provided here is released by the Naval Postgraduate
// School, an agency of the U.S. Department of Navy.  The software
// bears no warranty, either expressed or implied. NPS does not assume
// legal liability nor responsibility for a User's use of the software
// or the results of such use.
//
// Please note that within the United States, copyright protection,
// under Section 105 of the United States Code, Title 17, is not
// available for any work of the United States Government and/or for
// any works created by United States Government employees. User
// acknowledges that this software contains work which was created by
// NPS government employees and is therefore in the public domain and
// not subject to copyright.
//
// Released into the public domain on April 28, 2015 by Bruce Allen.

package edu.nps.autopsy.artifact_test;

import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.ingest.IngestModuleFactory;
import org.sleuthkit.autopsy.ingest.DataSourceIngestModule;
import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestModuleGlobalSettingsPanel;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;

/**
 * A factory for creating email parser file ingest module instances.
 */
@ServiceProvider(service = IngestModuleFactory.class)
public class ArtifactTestIngestModuleFactory implements IngestModuleFactory {

    private static final String VERSION_NUMBER = "1.0.0";

    static String getModuleName() {
        return "Artifact Test";
    }

    @Override
    public String getModuleDisplayName() {
        return getModuleName();
    }

    @Override
    public String getModuleDescription() {
        return "Test ability to generate a new artifact type"
    }

    @Override
    public String getModuleVersionNumber() {
        return VERSION_NUMBER;
    }

    @Override
    public boolean hasGlobalSettingsPanel() {
        return false;
    }

    @Override
    public IngestModuleGlobalSettingsPanel getGlobalSettingsPanel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IngestModuleIngestJobSettings getDefaultIngestJobSettings() {
        return new ArtifactTestIngestJobSettings();
    }

    @Override
    public boolean hasIngestJobSettingsPanel() {
        return false;
    }

    @Override
    public IngestModuleIngestJobSettingsPanel getIngestJobSettingsPanel() {
        throw new UnsupportedOperationException();
    }

    /**
     * Queries the factory to determine if it is capable of creating data source
     * ingest modules. If the module family does not include data source ingest
     * modules, the factory may extend IngestModuleFactoryAdapter to get an
     * implementation of this method that returns false.
     *
     * @return True if the factory can create data source ingest modules.
     */
    @Override
    public boolean isDataSourceIngestModuleFactory() {
        return true;
    }

    /**
     * Creates a data source ingest module instance.
     * <p>
     * Autopsy will generally use the factory to several instances of each type
     * of module for each ingest job it performs. Completing an ingest job
     * entails processing a single data source (e.g., a disk image) and all of
     * the files from the data source, including files extracted from archives
     * and any unallocated space (made to look like a series of files). The data
     * source is passed through one or more pipelines of data source ingest
     * modules. The files are passed through one or more pipelines of file
     * ingest modules.
     * <p>
     * The ingest framework may use multiple threads to complete an ingest job,
     * but it is guaranteed that there will be no more than one module instance
     * per thread. However, if the module instances must share resources, the
     * modules are responsible for synchronizing access to the shared resources
     * and doing reference counting as required to release those resources
     * correctly. Also, more than one ingest job may be in progress at any given
     * time. This must also be taken into consideration when sharing resources
     * between module instances. modules.
     * <p>
     * If the module family does not include data source ingest modules, the
     * factory may extend IngestModuleFactoryAdapter to get an implementation of
     * this method that throws an UnsupportedOperationException.
     *
     * @param settings The settings for the ingest job.
     * @return A data source ingest module instance.
     */
    @Override
    public ArtifactTestIngestModule createArtifactTestIngestModule() {
        return new ArtifactTestIngestModule();
    }

    @Override
    public boolean isFileIngestModuleFactory() {
        return false;
    }

    /**
     * Creates a file ingest module instance.
     * <p>
     * Autopsy will generally use the factory to several instances of each type
     * of module for each ingest job it performs. Completing an ingest job
     * entails processing a single data source (e.g., a disk image) and all of
     * the files from the data source, including files extracted from archives
     * and any unallocated space (made to look like a series of files). The data
     * source is passed through one or more pipelines of data source ingest
     * modules. The files are passed through one or more pipelines of file
     * ingest modules.
     * <p>
     * The ingest framework may use multiple threads to complete an ingest job,
     * but it is guaranteed that there will be no more than one module instance
     * per thread. However, if the module instances must share resources, the
     * modules are responsible for synchronizing access to the shared resources
     * and doing reference counting as required to release those resources
     * correctly. Also, more than one ingest job may be in progress at any given
     * time. This must also be taken into consideration when sharing resources
     * between module instances. modules.
     * <p>
     * If the module family does not include file ingest modules, the factory
     * may extend IngestModuleFactoryAdapter to get an implementation of this
     * method that throws an UnsupportedOperationException.
     *
     * @param settings The settings for the ingest job.
     * @return A file ingest module instance.
     */
    @Override
    public FileIngestModule createFileIngestModule(IngestModuleIngestJobSettings settings) {
        throw new UnsupportedOperationException();
//        if (!(settings instanceof BlacklistModuleIngestJobSettings)) {
//            throw new IllegalArgumentException("Expected settings argument to be instanceof BlacklistModuleIngestJobSettings");
//        }
//        return new BlacklistFileIngestModule();
    }
}

