package com.agileboot.domain.weixin.qrCode.query;

import com.agileboot.common.core.page.AbstractPageQuery;
import com.agileboot.common.utils.Misc;
import com.agileboot.domain.weixin.qrCode.db.QrCodeRecord;
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
public class QrCodeRecordQuery extends AbstractPageQuery<QrCodeRecord> {

    // todo ?
    private String id;
    private String userId;
    private String channelId;

    @Override
    public QueryWrapper<QrCodeRecord> addQueryCondition() {
        QueryWrapper<QrCodeRecord> wrapper = new QueryWrapper<QrCodeRecord>()
                .eq(!Misc.isEmpty(userId), "user_id", userId)
                .eq(!Misc.isEmpty(channelId), "channel_id", userId);

        this.setTimeRangeColumn("create_time");
        return wrapper;
    }
}
