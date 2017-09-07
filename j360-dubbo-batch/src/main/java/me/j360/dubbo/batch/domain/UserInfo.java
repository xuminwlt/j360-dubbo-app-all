package me.j360.dubbo.batch.domain;

import lombok.Data;

/**
 * Package: cn.paomiantv.batch.domain
 * User: min_xu
 * Date: 2017/8/8 下午1:56
 * 说明：
 */

@Data
public class UserInfo {

    private Long uid;
    private Long belikeCount;
    private Long likeCount;
    private Long barrageCount;
    private Long bebarrageCount;

    private Long postCount;

    private Long followCount;
    private Long followerCount;

    private Long newAt;
    private Long newBarrage;
    private Long newLike;
    private Long newFollow;
    private Long newPlay;
    private Long newBroadcast;
}
