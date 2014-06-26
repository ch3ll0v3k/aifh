/*
 * Artificial Intelligence for Humans
 * Volume 2: Nature Inspired Algorithms
 * Java Version
 * http://www.aifh.org
 * http://www.jeffheaton.com
 *
 * Code repository:
 * https://github.com/jeffheaton/aifh
 *
 * Copyright 2014 by Jeff Heaton
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
 *
 * For more information on Heaton Research copyrights, licenses
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package com.heatonresearch.aifh.aco

/**
 * Defines the cost structure between nodes in a graph.  Costs are assigned at the edges, between nodes.
 */
trait CostGraph {
  /**
   * Get the cost between two nodes.
   *
   * @param sourceNode The source node.
   * @param targetNode The target node.
   * @return The cost between two nodes.
   */
  def cost(sourceNode: Int, targetNode: Int): Double

  /**
   * @return The size of the graph, in nodes.
   */
  def graphSize: Int
}