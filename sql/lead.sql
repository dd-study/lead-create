CREATE TABLE `lead_create`.`share_record`
(
    `id`          varchar(64) NOT NULL COMMENT 'id',
    `model_id`    varchar(64) NULL COMMENT '关联实体id',
    `type`        int(5) NULL COMMENT '分享类型',
    `creator_id`  int NULL COMMENT '创建者id',
    `create_time` datetime NULL COMMENT '创建时间',
    `updater_id`  int NULL COMMENT '更新者id',
    `update_time` datetime NULL COMMENT '更新时间',
    `deleted`     tinyint(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX         `key_type_modelId`(`type`, `model_id`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分享记录';


CREATE TABLE `lead_create`.`channel`
(
    `id`          varchar(64) NOT NULL COMMENT 'id',
    `name`        varchar(255) NULL COMMENT '渠道名',
    `remark`      varchar(255) NULL COMMENT '备注',
    `creator_id`  int NULL COMMENT '创建者id',
    `create_time` datetime NULL COMMENT '创建时间',
    `updater_id`  int NULL COMMENT '更新者id',
    `update_time` datetime NULL COMMENT '更新时间',
    `deleted`     tinyint(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='渠道';

CREATE TABLE `lead_create`.`qr_code_record`
(
    `id`          varchar(64) NOT NULL COMMENT 'id',
    `user_id`     varchar(64) NOT NULL COMMENT '关联员工id',
    `channel_id`  varchar(64) NOT NULL COMMENT '渠道id',
    `remark`      varchar(255) NULL COMMENT '备注',
    `creator_id`  int NULL COMMENT '创建者id',
    `create_time` datetime NULL COMMENT '创建时间',
    `updater_id`  int NULL COMMENT '更新者id',
    `update_time` datetime NULL COMMENT '更新时间',
    `deleted`     tinyint(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='二维码记录';

