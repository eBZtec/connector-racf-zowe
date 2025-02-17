/*
 * Copyright (c) 2010-2014 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.polygon.connector.racf.rest;

import com.evolveum.polygon.connector.racf.rest.api.GroupService;
import com.evolveum.polygon.connector.racf.rest.api.TestService;
import com.evolveum.polygon.connector.racf.rest.api.UserService;
import com.evolveum.polygon.connector.racf.rest.util.ConnectionHandler;
import org.identityconnectors.common.logging.Log;

public class RacfRestConnection extends ConnectionHandler {

    private static final Log LOG = Log.getLog(RacfRestConnection.class);

    private RacfRestConfiguration configuration;

    private static final Class USER_CLASS = UserService.class;
    private static final Class GROUP_CLASS = GroupService.class;
    private static final Class TEST_SERVICE = TestService.class;

    private UserService userService;
    private GroupService groupService;
    private TestService testService;

    public RacfRestConnection(RacfRestConfiguration configuration) {
        super(configuration);
    }

    public void setUpConnections() {
        userService = (UserService) setupClient(USER_CLASS);
        groupService = (GroupService) setupClient(GROUP_CLASS);
        testService = (TestService) setupClient(TEST_SERVICE);

        LOG.info("The connector services initialized successfully");
    }

    public UserService getUserService() { return userService; }

    public GroupService getGroupService() { return groupService; }

    public TestService getTestService() { return testService; }

    public void dispose() {

        setConfiguration(null);
        userService = null;
        groupService = null;
        testService = null;

        LOG.info("The configuration was disposed successfully");
    }
}