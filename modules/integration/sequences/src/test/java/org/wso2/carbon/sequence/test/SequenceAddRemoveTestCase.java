/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.sequence.test;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.testng.annotations.Test;
import org.wso2.carbon.integration.core.utils.ArtifactReader;
import org.wso2.carbon.sequences.stub.types.SequenceAdminServiceStub;
import org.wso2.carbon.sequences.stub.types.SequenceEditorException;
import org.wso2.esb.integration.ESBIntegrationTestCase;

import java.rmi.RemoteException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SequenceAddRemoveTestCase extends ESBIntegrationTestCase {

    private static final String SEQUENCE_NAME =  "testFwSeq";

    private SequenceAdminServiceStub sequenceAdminServiceStub;

    public SequenceAddRemoveTestCase() {
        super("SequenceAdminService");
    }

    public void init() throws Exception {
        sequenceAdminServiceStub = new SequenceAdminServiceStub(getAdminServiceURL());
        authenticate(sequenceAdminServiceStub);
    }

    public void cleanup() {
        try {
            sequenceAdminServiceStub.cleanup();
        } catch (AxisFault ignored) {

        }
    }

    @Test(groups = {"wso2.esb"})
    public void testAddRemoveSequence() throws SequenceEditorException, RemoteException {

        int before = sequenceAdminServiceStub.getSequencesCount();
        ArtifactReader artifactReader = new ArtifactReader();

        OMElement omElement = artifactReader.getOMElement(SequenceAddRemoveTestCase.class.getResource("/synapse.xml").getPath());

        // add new sequence
        sequenceAdminServiceStub.addSequence(omElement);

        int after = sequenceAdminServiceStub.getSequencesCount();
        assertEquals(1, after - before);
        assertNotNull(sequenceAdminServiceStub.getSequence(SEQUENCE_NAME));
        assertEquals(SEQUENCE_NAME, sequenceAdminServiceStub.enableStatistics(SEQUENCE_NAME));
        assertEquals(SEQUENCE_NAME, sequenceAdminServiceStub.enableTracing(SEQUENCE_NAME));

        sequenceAdminServiceStub.deleteSequence(SEQUENCE_NAME);
        after = sequenceAdminServiceStub.getSequencesCount();

        assertEquals(before, after);
    }

}
