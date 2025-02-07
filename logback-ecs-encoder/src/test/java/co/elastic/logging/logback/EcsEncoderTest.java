/*-
 * #%L
 * Java ECS logging
 * %%
 * Copyright (C) 2019 - 2020 Elastic and contributors
 * %%
 * Licensed to Elasticsearch B.V. under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch B.V. licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * #L%
 */
package co.elastic.logging.logback;

import ch.qos.logback.classic.LoggerContext;
import co.elastic.logging.AdditionalField;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class EcsEncoderTest extends AbstractEcsEncoderTest {

    private OutputStreamAppender appender;

    @BeforeEach
    void setUp() {
        LoggerContext context = new LoggerContext();
        logger = context.getLogger(getClass());
        appender = new OutputStreamAppender();
        appender.setContext(context);
        logger.addAppender(appender);
        EcsEncoder ecsEncoder = new EcsEncoder();
        ecsEncoder.setServiceName("test");
        ecsEncoder.setServiceNodeName("test-node");
        ecsEncoder.setIncludeMarkers(true);
        ecsEncoder.setIncludeOrigin(true);
        ecsEncoder.addAdditionalField(new AdditionalField("key1", "value1"));
        ecsEncoder.addAdditionalField(new AdditionalField("key2", "value2"));
        ecsEncoder.setEventDataset("testdataset");
        ecsEncoder.start();
        appender.setEncoder(ecsEncoder);
        appender.start();
    }

    @Override
    public JsonNode getLastLogLine() throws IOException {
        return objectMapper.readTree(appender.getBytes());
    }
}
