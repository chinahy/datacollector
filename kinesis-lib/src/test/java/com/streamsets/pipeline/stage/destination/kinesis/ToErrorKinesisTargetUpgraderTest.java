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
package com.streamsets.pipeline.stage.destination.kinesis;


import com.streamsets.pipeline.stage.lib.kinesis.KinesisCommonUpgraderTest;

public class ToErrorKinesisTargetUpgraderTest extends KinesisCommonUpgraderTest {

  @Override
  protected String getYamlResourceName() {
    return "ToErrorKinesisDTarget.yaml";
  }

  @Override
  protected int getConnectionIntroductionUpgradeVersion() {
    return 10;
  }

  @Override
  protected String getPrefix() {
    return "kinesisConfig.";
  }
}
