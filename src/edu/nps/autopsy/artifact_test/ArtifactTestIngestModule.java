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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.Collection;
import java.util.Date;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.DataSourceIngestModule;
import org.sleuthkit.autopsy.ingest.DataSourceIngestModuleProgress;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.autopsy.ingest.IngestModuleReferenceCounter;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.Image;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Artifact Test ingest module.
 */
public class ArtifactTestIngestModule implements DataSourceIngestModule {

    private static final IngestModuleReferenceCounter refCounter = new IngestModuleReferenceCounter();
    private static final String moduleName = ArtifactTestIngestModuleFactory.getModuleName();

    // startUp
    private IngestJobContext context;
    private Logger logger;

    // blackboard artifact and attributes
    private static int artifactID = -1;
    private static int attributeID = -1;

    // process
    private DataSourceIngestModuleProgress progressBar;
    
    ArtifactTestIngestModule() {
    }

    @Override
    public void startUp(IngestJobContext context) throws IngestModuleException {
        this.context = context;
        this.logger = IngestServices.getInstance().getLogger(moduleName);
logger.log(Level.INFO, "startup.a");
        if (refCounter.incrementAndGet(context.getJobId()) != 1) {
            // do not run twice.  Can it?
            logger.log(Level.SEVERE, "ArtifactTestIngestModule.startUp count is bad");
            throw new IngestModuleException("ArtifactTestIngestModule.startUp count is bad");
        }
        
logger.log(Level.INFO, "startup.b");
        // establish the blackboard artifact and its attribute
//        setArtifactAndAttribute();
        setExistingArtifactAndAttribute();
logger.log(Level.INFO, "startup.c");
    }
    
    private synchronized void setExistingArtifactAndAttribute() throws IngestModuleException {
logger.log(Level.INFO, "setExistingArtifactAndAttribute.a");
        if (artifactID == -1) {
logger.log(Level.INFO, "setExistingArtifactAndAttribute.b");
            try {
                // set up static variables
                SleuthkitCase sleuthkitCase = Case.getCurrentCase().getSleuthkitCase();
                artifactID = sleuthkitCase.getArtifactTypeID("TSK_WEB_DOWNLOAD");

                if (artifactID == -1) {
logger.log(Level.INFO, "setExistingArtifactAndAttribute.c");
                    throw new IngestModuleException("ArtifactTestIngestModule artifact ID is bad");
                } else {
logger.log(Level.INFO, "setExistingArtifactAndAttribute.d");
                    // get attribute values
                    attributeID = sleuthkitCase.getAttrTypeID("TSK_PATH_SOURCE");
                    if (artifactID == -1) {
logger.log(Level.INFO, "setExistingArtifactAndAttribute.e");
                        throw new IngestModuleException("ArtifactTestIngestModule attribute ID is bad");
                    }
                }
logger.log(Level.INFO, "setExistingArtifactAndAttribute.f");
            } catch (TskCoreException ex) {
logger.log(Level.INFO, "setExistingArtifactAndAttribute.TskCoreException catch");
                IngestServices ingestServices = IngestServices.getInstance();
                logger.log(Level.SEVERE, "Failed to create blackboard artifact or attribute", ex);
                artifactID = -1;
                attributeID = -1;
                throw new IngestModuleException(ex.getLocalizedMessage());
            }
        }   
    }

    private synchronized void setArtifactAndAttribute() throws IngestModuleException {
logger.log(Level.INFO, "setArtifactAndAttribute.a");
        if (artifactID == -1) {
logger.log(Level.INFO, "setArtifactAndAttribute.b");
            try {
                // set up static variables
                SleuthkitCase sleuthkitCase = Case.getCurrentCase().getSleuthkitCase();
                artifactID = sleuthkitCase.getArtifactTypeID("ARTIFACT_TEST");

                if (artifactID == -1) {
logger.log(Level.INFO, "setArtifactAndAttribute.c");

                    // add artifact and attribute to Sleuthkit
                    artifactID = sleuthkitCase.addArtifactType("ARTIFACT_TEST", "Artifact for Artifact Test");
logger.log(Level.INFO, "setArtifactAndAttribute.d");
                    attributeID = sleuthkitCase.addAttrType("ATTRIBUTE_TEST", "Attribute for Artifact Test");
logger.log(Level.INFO, "setArtifactAndAttribute.e");
                } else {
logger.log(Level.INFO, "setArtifactAndAttribute.f");
                    // get attribute values
                    attributeID = sleuthkitCase.getAttrTypeID("ATTRIBUTE_TEST");
                }
logger.log(Level.INFO, "setArtifactAndAttribute.g");
            } catch (TskCoreException ex) {
logger.log(Level.INFO, "setArtifactAndAttribute.TskCoreException catch");
                IngestServices ingestServices = IngestServices.getInstance();
                logger.log(Level.SEVERE, "Failed to create blackboard artifact or attribute", ex);
                artifactID = -1;
                throw new IngestModuleException(ex.getLocalizedMessage());
            }
        }   
    }

    @Override
    public ProcessResult process(Content dataSource, DataSourceIngestModuleProgress progressBar) {
logger.log(Level.INFO, "process.a");
        this.progressBar = progressBar;

        // skip if startUp was called more than once
        if (refCounter.get(context.getJobId()) != 1) {
            // do not run twice.  Can it?
            logger.log(Level.SEVERE, "BlacklistDataSourceIngestModule.process count is bad");
            return ProcessResult.OK;
        }

        // skip if not processing sleuthkit.datamodel.Image
        if (!(dataSource instanceof Image)) {
            // not a disk image
            return ProcessResult.OK;
        }

        // write the artifact
        progressBar.switchToDeterminate(1);
        progressBar.progress(0);
        addTheArtifact(dataSource);
        progressBar.progress(1);

        return ProcessResult.OK;
    }

    // add the artifact
    private ProcessResult addTheArtifact(Content dataSource) {
logger.log(Level.INFO, "process.addTheArtifact.a");

        // create the attribute
        Collection<BlackboardAttribute> attributes = new ArrayList<BlackboardAttribute>();
        String dateAndTime = new SimpleDateFormat("MM-dd-yy-HH-mm-ss").format(new Date());

        attributes.add(new BlackboardAttribute(attributeID, moduleName, "hello attribute at " + dateAndTime));

        // create and add the artifact
        try {
logger.log(Level.INFO, "process.addTheArtifact.b");
            BlackboardArtifact blackboardArtifact = dataSource.newArtifact(artifactID);
logger.log(Level.INFO, "process.addTheArtifact.c");
            blackboardArtifact.addAttributes(attributes);
logger.log(Level.INFO, "process.addTheArtifact.d");

        } catch (TskCoreException ex) {
logger.log(Level.INFO, "process.addTheArtifact.e");
            logger.log(Level.SEVERE, "Failed to create blackboard artifact", ex);
            artifactID = -1;
            attributeID = -1;
            return ProcessResult.ERROR;
        }
        return ProcessResult.OK;
    }
}

