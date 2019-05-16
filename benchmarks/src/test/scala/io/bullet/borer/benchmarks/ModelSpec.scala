/*
 * Copyright (c) 2019 Mathias Doenitz
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.bullet.borer.benchmarks

import utest._

object ModelSpec extends TestSuite {

  val tests = Tests {
    val jsoniterScala = new JsoniterScalaModelBenchmark {
      load()
      setup()
    }
    val borer = new BorerModelBenchmark {
      load()
      setup()
    }

    "encode" - {
      val jsoniterScalaJson = new String(jsoniterScala.encodeModel)
      val borerJson         = new String(jsoniterScala.encodeModel)
      jsoniterScalaJson ==> borerJson
      val jsoniterScalaObj = jsoniterScala.decodeModel
      val borerObj         = jsoniterScala.decodeModel
      jsoniterScalaObj ==> borerObj
    }
  }
}
