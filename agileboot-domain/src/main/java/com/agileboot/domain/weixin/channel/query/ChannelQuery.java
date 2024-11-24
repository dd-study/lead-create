package com.agileboot.domain.weixin.channel.query;

import com.agileboot.common.core.page.AbstractQuery;
import com.agileboot.common.utils.Misc;
import com.agileboot.domain.weixin.channel.db.Channel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author bin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ChannelQuery extends AbstractQuery<Channel> {

    // todo ?
    private String id;
    private String name;


    @Override
    public QueryWrapper<Channel> addQueryCondition() {
        return new QueryWrapper<Channel>()
                .eq(!Misc.isEmpty(name), "name", name);
    }
}
