/*
 * Copyright (c) 2019 Mathias Doenitz
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.bullet.borer

import java.io.ByteArrayOutputStream

import better.files._
import utest._

object JsonTestSuite extends TestSuite {

  val disabled: Set[String] = Set(
    "n_array_colon_instead_of_comma.json",
    "n_array_comma_after_close.json",
    "n_number_1eE2.json",
    "n_number_invalid+-.json",
    "n_object_comma_instead_of_colon.json",
    "n_structure_open_object_close_array.json",
  )

  val testFiles: Map[String, Array[Byte]] = {
    val zip = Resource.getAsStream("JSONTestSuite.zip").asZipInputStream
    zip
      .mapEntries { entry ⇒
        entry.getName → new ByteArrayOutputStream().autoClosed.map(zip.pipeTo(_).toByteArray).get()
      }
      .toMap
      .filterKeys(!disabled.contains(_))
  }

  val tests = Tests {

    "Accept" - {
      for {
        (name, bytes) ← testFiles
        if name startsWith "y"
      } {
        Json.decode(bytes).to[Dom.Element].valueEither match {
          case Left(e)  ⇒ throw new RuntimeException(s"Test `$name` did not parse as it should", e)
          case Right(_) ⇒ // ok
        }
      }
    }

    "Reject" - {
      for {
        (name, bytes) ← testFiles
        if name startsWith "n"
      } {
        Json.decode(bytes).to[Dom.Element].valueEither match {
          case Left(_)  ⇒ // ok
          case Right(x) ⇒ throw new RuntimeException(s"Test `$name` parsed even though it should have failed: $x")
        }
      }
    }

    "Not Crash" - {
      for {
        (name, bytes) ← testFiles
        if name startsWith "i"
      } {
        Json.decode(bytes).to[Dom.Element].valueEither match {
          case Left(e: Borer.Error.General[_]) ⇒ throw new RuntimeException(s"Test `$name` did fail unexpectedly", e)
          case _                               ⇒ // everything else is fine
        }
      }
    }
  }
}