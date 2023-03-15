/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zto.fire.hudi.util

import com.zto.fire.common.util.Logging
import com.zto.fire.spark.util.{SparkSingletonFactory, SparkUtils}
import com.zto.fire.{JHashMap, JMap}
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.common.model.HoodieTableType

/**
 * Hudi相关工具类
 *
 * @author ChengLong 2023-03-10 13:05:43
 * @since 2.3.5
 */
object HudiUtils extends Logging {


  /**
   * 用于根据指定配置构建sink hudi表的options配置
   *
   * @param deltaCommitNum
   * 流式写入场景下指定几个批次进行一次commit
   * @param parallelism
   * hoodie.upsert.shuffle.parallelism
   * @param tableType
   * COPY_ON_WRITE or MERGE_ON_READ
   * @return
   */
  def hudiOptions(deltaCommitNum: Int = 0, parallelism: Int = SparkUtils.executorNum * 3,
                  tableType: HoodieTableType = HoodieTableType.MERGE_ON_READ): JMap[String, String] = {

    val options = new JHashMap[String, String]()
    options.put("hoodie.upsert.shuffle.parallelism", s"$parallelism")
    options.put("hoodie.insert.shuffle.parallelism", s"$parallelism")
    options.put("hoodie.upsert.shuffle.parallelism", s"$parallelism")
    options.put("hoodie.bulkinsert.shuffle.parallelism", s"$parallelism")
    options.put("hoodie.delete.shuffle.parallelism", s"$parallelism")

    if (deltaCommitNum > 0) {
      options.put("hoodie.compact.inline", "true")
      options.put("hoodie.compact.inline.max.delta.commits", deltaCommitNum.toString)
      options.put("hoodie.fail.on.timeline.archiving", "false")
    }

    options
  }

}
