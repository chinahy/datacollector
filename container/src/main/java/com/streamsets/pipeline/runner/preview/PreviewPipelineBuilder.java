/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.runner.preview;

import com.streamsets.pipeline.config.PipelineConfiguration;
import com.streamsets.pipeline.config.PipelineConfigurationValidator;
import com.streamsets.pipeline.config.StageConfiguration;
import com.streamsets.pipeline.runner.Pipeline;
import com.streamsets.pipeline.runner.PipelineRuntimeException;
import com.streamsets.pipeline.stagelibrary.StageLibrary;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PreviewPipelineBuilder {

  @SuppressWarnings("unchecked")
  private StageConfiguration createPlugStage(List<String> lanes) {
    return new StageConfiguration(PreviewStageLibrary.NAME + UUID.randomUUID().toString(), PreviewStageLibrary.LIBRARY,
                                  PreviewStageLibrary.NAME, PreviewStageLibrary.VERSION, Collections.EMPTY_LIST,
                                  Collections.EMPTY_MAP, lanes, Collections.EMPTY_LIST);
  }

  private final StageLibrary stageLib;
  private final PipelineConfiguration pipelineConf;

  public PreviewPipelineBuilder(StageLibrary stageLib, PipelineConfiguration pipelineConf) {
    this.stageLib = new PreviewStageLibrary(stageLib);
    this.pipelineConf = pipelineConf;
  }

  public PreviewPipeline build(PreviewPipelineRunner runner) throws PipelineRuntimeException {
    PipelineConfigurationValidator validator = new PipelineConfigurationValidator(stageLib, pipelineConf);
    if (validator.validate() || validator.canPreview()) {
      List<String> openLanes = validator.getOpenLanes();
      if (!openLanes.isEmpty()) {
        pipelineConf.getStages().add(createPlugStage(openLanes));
      }
    } else {
      throw new PipelineRuntimeException(PipelineRuntimeException.ERROR.CANNOT_PREVIEW, validator.getIssues());
    }
    Pipeline pipeline = new Pipeline.Builder(stageLib, pipelineConf).build(runner);
    return new PreviewPipeline(pipeline, validator.getIssues());
  }

}
