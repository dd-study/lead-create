package com.agileboot.domain.weixin.channel.query;

import com.agileboot.common.core.page.AbstractPageQuery;
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
public class ChannelQuery extends AbstractPageQuery<Channel> {

    // todo ?
    private String id;
    private String name;

    @Override
    public QueryWrapper<Channel> addQueryCondition() {
        QueryWrapper<Channel> wrapper = new QueryWrapper<Channel>()
                .eq(!Misc.isEmpty(name), "name", name);

        this.setTimeRangeColumn("create_time");
        return wrapper;
    }
}
