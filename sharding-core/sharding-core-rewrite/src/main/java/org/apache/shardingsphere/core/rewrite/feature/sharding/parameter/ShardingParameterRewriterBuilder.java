/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.rewrite.feature.sharding.parameter;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.core.metadata.table.TableMetas;
import org.apache.shardingsphere.core.rewrite.feature.sharding.parameter.impl.ShardingGeneratedKeyInsertValueParameterRewriter;
import org.apache.shardingsphere.core.rewrite.feature.sharding.parameter.impl.ShardingPaginationParameterRewriter;
import org.apache.shardingsphere.core.rewrite.feature.sharding.aware.SQLRouteResultAware;
import org.apache.shardingsphere.core.rewrite.feature.sharding.aware.ShardingRuleAware;
import org.apache.shardingsphere.core.rewrite.parameter.rewriter.ParameterRewriter;
import org.apache.shardingsphere.core.rewrite.parameter.rewriter.ParameterRewriterBuilder;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.aware.TableMetasAware;
import org.apache.shardingsphere.core.route.SQLRouteResult;
import org.apache.shardingsphere.core.rule.ShardingRule;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Parameter rewriter builder for sharding.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class ShardingParameterRewriterBuilder implements ParameterRewriterBuilder {
    
    private final ShardingRule shardingRule;
    
    private final SQLRouteResult sqlRouteResult;
    
    @Override
    public Collection<ParameterRewriter> getParameterRewriters(final TableMetas tableMetas) {
        Collection<ParameterRewriter> result = getParameterRewriters();
        for (ParameterRewriter each : result) {
            setUpParameterRewriters(each, tableMetas);
        }
        return result;
    }
    
    private static Collection<ParameterRewriter> getParameterRewriters() {
        Collection<ParameterRewriter> result = new LinkedList<>();
        result.add(new ShardingGeneratedKeyInsertValueParameterRewriter());
        result.add(new ShardingPaginationParameterRewriter());
        return result;
    }
    
    private void setUpParameterRewriters(final ParameterRewriter parameterRewriter, final TableMetas tableMetas) {
        if (parameterRewriter instanceof TableMetasAware) {
            ((TableMetasAware) parameterRewriter).setTableMetas(tableMetas);
        }
        if (parameterRewriter instanceof ShardingRuleAware) {
            ((ShardingRuleAware) parameterRewriter).setShardingRule(shardingRule);
        }
        if (parameterRewriter instanceof SQLRouteResultAware) {
            ((SQLRouteResultAware) parameterRewriter).setSqlRouteResult(sqlRouteResult);
        }
    }
}
