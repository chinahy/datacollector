/*
 * Copyright 2020 StreamSets Inc.
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

package com.streamsets.pipeline.stage.origin.jdbc;

import com.streamsets.pipeline.api.ConfigDefBean;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.HideConfigs;
import com.streamsets.pipeline.api.Source;
import com.streamsets.pipeline.api.StageDef;
import com.streamsets.pipeline.lib.event.NoMoreDataEvent;
import com.streamsets.pipeline.lib.jdbc.HikariPoolConfigBean;
import com.streamsets.pipeline.stage.config.SapHanaGroups;
import com.streamsets.pipeline.stage.config.SapHanaHikariPoolConfigBean;


@StageDef(version = 1,
    label = "SAP HANA Query Consumer",
    description = "Reads data from SAP HANA using a query",
    icon = "sapHanaIcon.png",
    execution = ExecutionMode.STANDALONE,
    upgraderDef = "upgrader/SapHanaDSource.yaml",
    recordsByRef = true,
    resetOffset = true,
    producesEvents = true,
    eventDefs = {NoMoreDataEvent.class},
    onlineHelpRefUrl = "index.html?contextID=task_xkt_kww_gmb")
@GenerateResourceBundle
@HideConfigs({
    "commonSourceConfigBean.allowLateTable",
    "commonSourceConfigBean.enableSchemaChanges",
    "commonSourceConfigBean.queriesPerSecond",
    "commonSourceConfigBean.txnWindow",
    "hikariConfigBean.connectionString",
    "hikariConfigBean.driverClassName",
    "hikariConfigBean.connectionTestQuery",
    "txnIdColumnName",
    "txnMaxSize"
})
@ConfigGroups(value = SapHanaGroups.class)
public class SapHanaDSource extends JdbcDSource {
  @ConfigDefBean
  public SapHanaHikariPoolConfigBean hikariConfigBean;

  @Override
  protected HikariPoolConfigBean getHikariConfigBean() {
    return hikariConfigBean;
  }

  @Override
  protected Source createSource() {
    return new SapHanaSource(
        isIncrementalMode,
        query,
        initialOffset,
        offsetColumn,
        disableValidation,
        txnMaxSize,
        jdbcRecordType,
        commonSourceConfigBean,
        createJDBCNsHeaders,
        jdbcNsHeaderPrefix,
        (SapHanaHikariPoolConfigBean) getHikariConfigBean(),
        unknownTypeAction,
        queryInterval
    );
  }
}
